import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faLayerGroup,
  faCertificate,
  faLightbulb,
  faUsers,
  faHeadset,
  faSignOutAlt,
  faUser,
  faGlobe,
} from '@fortawesome/free-solid-svg-icons';
import { NgClass } from '@angular/common';
import { AuthService, Role } from '../services/auth.service';
import { IUser } from '../types/user-info';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { LanguageService } from '../services/language.service';
import { FormsModule } from '@angular/forms';
import {
  TranslateModule,
  TranslateService,
  TranslatePipe,
} from '@ngx-translate/core';

interface NavLink {
  path: string;
  icon: any;
  label: string;
  allowedRoles: Role[];
}

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLink,
    RouterLinkActive,
    FontAwesomeModule,
    NgClass,
    CommonModule,
    FormsModule,
    TranslateModule,
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  faLayerGroup = faLayerGroup;
  faCertificate = faCertificate;
  faLightbulb = faLightbulb;
  faUsers = faUsers;
  faHeadset = faHeadset;
  faGlobe = faGlobe;
  faSignOutAlt = faSignOutAlt;
  faUser = faUser;

  currentUser$: Observable<IUser | null>;
  currentUserRole: string | null = null;

  language: string = 'ENGLISH';

  navLinks: NavLink[] = [
    {
      path: 'modules',
      icon: this.faLayerGroup,
      label: 'Modules',
      allowedRoles: [
        Role.SKIPPER,
        Role.INSTALLER,
        Role.ADMIN,
        Role.SUPPORT,
        Role.FLEETMANAGER,
      ],
    },
    {
      path: 'certificates',
      icon: this.faCertificate,
      label: 'Certificates',
      allowedRoles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT],
    },
    {
      path: 'tips-and-tricks',
      icon: this.faLightbulb,
      label: 'Tips and tricks',
      allowedRoles: [
        Role.SKIPPER,
        Role.INSTALLER,
        Role.SUPPORT,
        Role.FLEETMANAGER,
      ],
    },
    {
      path: 'user-management',
      icon: this.faUsers,
      label: 'User Management',
      allowedRoles: [Role.ADMIN],
    },
    {
      path: 'skippers',
      icon: this.faUsers,
      label: 'Skippers',
      allowedRoles: [Role.FLEETMANAGER],
    },
    {
      path: 'support',
      icon: this.faHeadset,
      label: 'Support',
      allowedRoles: [Role.SUPPORT],
    },
  ];

  constructor(
    private authService: AuthService,
    private languageService: LanguageService,
    private translate: TranslateService
  ) {
    this.currentUser$ = this.authService.currentUser$;
    this.authService.currentUser$.subscribe((user) => {
      this.currentUserRole = user ? user.Role : null;
    });
    this.languageService.currentLanguage$.subscribe((lang) => {
      this.language = lang;
    });
  }

  mapLanguage(lang: string): string {
    switch (lang) {
      case 'ENGLISH':
        return 'en';
      case 'FRENCH':
        return 'fr';
      case 'GERMAN':
        return 'de';
      case 'DUTCH':
        return 'nl';
      default:
        return 'en';
    }
  }

  onLanguageChange() {
    this.languageService.setLanguage(this.language);
    const langCode = this.mapLanguage(this.language);
    this.translate.use(langCode);
    console.log('lang: ' + langCode);
  }

  isLinkVisible(link: NavLink): boolean {
    const user = this.authService.currentUserValue;
    if (!user) return false;
    return link.allowedRoles.includes(user.Role.toUpperCase() as Role);
  }

  getRoleClass(): string {
    return this.currentUserRole
      ? `${this.currentUserRole.toLowerCase()}-role`
      : '';
  }

  logout(): void {
    this.authService.logout();
  }
}
