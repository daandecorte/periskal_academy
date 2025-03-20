import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faUser,
  faGlobe,
  faLock,
  faEye,
  faTimes,
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

  username: string = '';
  password: string = '';
  language: string = 'ENGLISH';

  constructor(
    private router: Router,
    private languageService: LanguageService
  ) {
    this.languageService.currentLanguage$.subscribe((lang) => {
      this.language = lang;
    });
  }

  ngOnInit() {
    let storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.router.navigate(['/modules']);
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

  async login() {
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

    let data = await result.json();
    if (!data.text) {
      const userData: IUser = {
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

      localStorage.setItem('currentUser', JSON.stringify(userData));
      this.router.navigate(['/modules']);
    } else {
      let textIncorrect = document.getElementById('textIncorrect');
      if (textIncorrect)
        textIncorrect.innerText = 'Username or password incorrect.';
    }
  }
}
