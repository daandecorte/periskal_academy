import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faUser,
  faGlobe,
  faLock,
  faEye,
  faTimes,
  faKey,
} from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';
import { IUser } from '../types/user-info';
import { LanguageService } from '../services/language.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [FontAwesomeModule, FormsModule, TranslateModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  faUser = faUser;
  faGlobe = faGlobe;
  faLock = faLock;
  faEye = faEye;
  faTimes = faTimes;
  faKey = faKey;

  username: string = '';
  password: string = '';
  language: string = 'ENGLISH';
  dongleCode: string = '';
  showDongleDebug: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService,
    private languageService: LanguageService,
    private translate: TranslateService
  ) {
    this.languageService.currentLanguage$.subscribe((lang) => {
      this.language = lang;
    });
  }

  ngOnInit() {
    // Check for dongle code in URL query parameters
    const urlParams = new URLSearchParams(window.location.search);
    const loginParam = urlParams.get('login');

    if (loginParam) {
      // If dongle code is in URL, process it directly
      this.processDongleLogin(loginParam);
    } else {
      // Continue normal flow
      let storedUser = localStorage.getItem('currentUser');
      if (storedUser) {
        this.router.navigate(['/modules']);
      }
    }

    let inputPassword = document.getElementById('password') as HTMLInputElement;
    let eye = document.getElementById('eyeIcon') as HTMLButtonElement;
    eye.addEventListener('click', () => {
      inputPassword.type === 'password'
        ? (inputPassword.type = 'text')
        : (inputPassword.type = 'password');
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
    this.translate.use(this.mapLanguage(this.language));
  }

  toggleDongleDebug() {
    this.showDongleDebug = !this.showDongleDebug;
  }

  async processDongleLogin(dongleCode: string) {
    try {
      let result = await fetch(`/api/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          login: dongleCode,
          language: this.language,
        }),
      });

      await this.processLoginResponse(result);
    } catch (error) {
      console.error('Dongle login error:', error);
      this.showError('Failed to connect to authentication server. Please check your internet connection and try again.');
    }
  }

  async dongleLogin() {
    if (!this.dongleCode) {
      this.showError('Please enter a valid dongle code.');
      return;
    }

    // Add DEBUG: prefix for testing dongle code without prior encryption
    const formattedDongleCode = `DEBUG:${this.dongleCode}`;
    await this.processDongleLogin(formattedDongleCode);
  }

  async login() {
    if (!this.username) {
      this.showError('Please enter your username.');
      return;
    }

    if (!this.password) {
      this.showError('Please enter your password.');
      return;
    }

    try {
      let result = await fetch(`/api/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          username: this.username,
          password: this.password,
          language: this.language,
        }),
      });
      await this.processLoginResponse(result);
    } catch (error) {
      console.error('Login error:', error);
      this.showError('Failed to connect to authentication server. Please check your internet connection and try again.');
    }
  }

  private async processLoginResponse(result: Response) {
    let data = await result.json();

    if (!data.text) {
      let userData: IUser;

      if (data.Body.AuthenticateResponse) {
        userData = {
          ...data.Body.AuthenticateResponse.AuthenticateResult,
          Products:
            data.Body.AuthenticateResponse.AuthenticateResult.Products.string,
          Skippers:
            data.Body.AuthenticateResponse.AuthenticateResult.Skippers.Client.map(
              (skipper: any) => ({
                ...skipper,
                Products: skipper.Products.string,
              })
            ),
        };
      } else if (data.Body.Authenticate_DongleResponse) {
        userData = {
          ...data.Body.Authenticate_DongleResponse.Authenticate_DongleResult,
          Products: data.Body.Authenticate_DongleResponse.Authenticate_DongleResult.Products?.string || [],
          Skippers: data.Body.Authenticate_DongleResponse.Authenticate_DongleResult.Skippers?.Client?.map(
            (skipper: any) => ({
              ...skipper,
              Products: skipper.Products?.string || []
            })
          ) || []
        };
      } else {
        throw new Error('Unexpected response format from server');
      }

      this.authService.setCurrentUser(userData);
      this.languageService.setLanguage(this.language);
      this.router.navigate(['/modules']);
    } else {
      // Handle specific error cases
      let errorMessage = 'Authentication failed.';
      
      if (data.text.includes('Invalid username or password')) {
        errorMessage = 'The username or password you entered is incorrect. Please try again.';
      } else if (data.text.includes('Dongle authentication failed')) {
        errorMessage = 'The dongle code is invalid or expired. Please check the code and try again.';
      } else if (data.text.includes('Account locked')) {
        errorMessage = 'Your account has been temporarily locked due to multiple failed attempts. Please try again later or contact support.';
      } else if (data.text.includes('Server error')) {
        errorMessage = 'We encountered a server error while processing your request. Please try again later.';
      }
      
      this.showError(errorMessage);
    }
  }

  private showError(message: string) {
    let textIncorrect = document.getElementById('textIncorrect');
    if (textIncorrect) {
      textIncorrect.innerText = message;
    }
  }
}