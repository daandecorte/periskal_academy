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
  faGlobe
} from '@fortawesome/free-solid-svg-icons';
import { NgClass } from '@angular/common';
import { AuthService, Role, User } from '../services/auth.service';
import { Observable } from 'rxjs';

interface NavLink {
  path: string;
  icon: any;
  label: string;
  allowedRoles: Role[];
}

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, FontAwesomeModule, NgClass],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent{
  faLayerGroup = faLayerGroup;
  faCertificate = faCertificate;
  faLightbulb = faLightbulb;
  faUsers = faUsers;
  faHeadset = faHeadset;
  faGlobe = faGlobe;
  faSignOutAlt = faSignOutAlt;
  faUser = faUser;

  currentUser$: Observable<User | null>;

  navLinks: NavLink[] = [
    {
      path: 'modules',
      icon: this.faLayerGroup,
      label: 'Modules',
      allowedRoles: [Role.SKIPPER, Role.INSTALLER, Role.ADMIN, Role.SUPPORT, Role.FLEETMANAGER]
    },
    {
      path: 'certificates',
      icon: this.faCertificate,
      label: 'Certificates',
      allowedRoles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT]
    },
    {
      path: 'tips-and-tricks',
      icon: this.faLightbulb,
      label: 'Tips and tricks',
      allowedRoles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT, Role.FLEETMANAGER]
    },
    {
      path: 'user-management',
      icon: this.faUsers,
      label: 'User Management',
      allowedRoles: [Role.ADMIN]
    },
    {
      path: 'skippers',
      icon: this.faUsers,
      label: 'Skippers',
      allowedRoles: [Role.FLEETMANAGER]
    },
    {
      path: 'support',
      icon: this.faHeadset,
      label: 'Support',
      allowedRoles: [Role.SUPPORT]
    }
  ];

  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
  }

  isLinkVisible(link: NavLink): boolean {
    const user = this.authService.currentUserValue;
    if (!user) return false;
    return link.allowedRoles.includes(user.role);
  }

  /* logout(): void {
    this.authService.logout();
  } */
}
