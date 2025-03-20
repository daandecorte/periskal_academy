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

@Component({
  selector: 'app-login',
  imports: [FontAwesomeModule, FormsModule],
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
    private languageService: LanguageService
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

  onLanguageChange() {
    this.languageService.setLanguage(this.language);
  }

  // toggle dongle debug
  toggleDongleDebug() {
    this.showDongleDebug = !this.showDongleDebug;
  }

  async processDongleLogin(dongleCode: string) {
    try {
      let result = await fetch(
        `/api/login?login=${encodeURIComponent(dongleCode)}&language=${
          this.language
        }`,
        {
          method: 'GET',
          headers: {
            Accept: 'application/json',
          },
        }
      );

      await this.processLoginResponse(result);
    } catch (error) {
      console.error('Dongle login error:', error);
      let textIncorrect = document.getElementById('textIncorrect');
      if (textIncorrect)
        textIncorrect.innerText = 'Dongle authentication failed.';
    }
  }

  async dongleLogin() {
    if (!this.dongleCode) {
      let textIncorrect = document.getElementById('textIncorrect');
      if (textIncorrect)
        textIncorrect.innerText = 'Please enter a dongle code.';
      return;
    }

    // Add DEBUG: prefix for testing dongle code without prior encryption
    // If the DEBUG: prefix is present, the backend knows it still needs to encrypt the dongle code
    const formattedDongleCode = `DEBUG:${this.dongleCode}`;
    await this.processDongleLogin(formattedDongleCode);
  }

  async login() {
    try {
      let result = await fetch(
        `/api/login?username=${encodeURIComponent(
          this.username
        )}&password=${encodeURIComponent(this.password)}&language=${
          this.language
        }`,
        {
          method: 'GET',
          headers: {
            Accept: 'application/json',
          },
        }
      );

      await this.processLoginResponse(result);
    } catch (error) {
      console.error('Login error:', error);
      let textIncorrect = document.getElementById('textIncorrect');
      if (textIncorrect) textIncorrect.innerText = 'Authentication error.';
    }
  }

  private async processLoginResponse(result: Response) {
    let data = await result.json();

    // Common response handler for both authentication methods
    if (!data.text) {
      // Determine the proper path to the authentication result
      let userData: IUser;

      if (data.Body.AuthenticateResponse) {
        // Username/password auth response
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
        // Dongle auth response
        userData = {
          ...data.Body.Authenticate_DongleResponse.Authenticate_DongleResult,
          Products:
            data.Body.Authenticate_DongleResponse.Authenticate_DongleResult
              .Products.string,
          Skippers:
            data.Body.Authenticate_DongleResponse.Authenticate_DongleResult.Skippers.Client.map(
              (skipper: any) => ({
                ...skipper,
                Products: skipper.Products.string,
              })
            ),
        };
      } else {
        throw new Error('Unexpected response format');
      }

      localStorage.setItem('currentUser', JSON.stringify(userData));
      this.router.navigate(['/modules']);
    } else {
      let textIncorrect = document.getElementById('textIncorrect');
      if (textIncorrect)
        textIncorrect.innerText = data.text || 'Authentication failed.';
    }
  }
}
