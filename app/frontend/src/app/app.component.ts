import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { NavbarComponent } from './navbar/navbar.component';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { AuthService } from './services/auth.service';
import { LanguageService } from './services/language.service';
import { map } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    NavbarComponent,
    TranslateModule,
    HttpClientModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  constructor(private translate: TranslateService, private languageService: LanguageService) {
    this.translate.setDefaultLang('en');
    const userLang = this.languageService.getLanguage();
    translate.use(this.mapLanguage(userLang));
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
}