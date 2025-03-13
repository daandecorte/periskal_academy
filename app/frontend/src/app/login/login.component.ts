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
import { UserInfoService } from '../user-info.service';
import { Router } from '@angular/router';
import { IUser } from '../types/user-info';

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

  constructor(private service: UserInfoService, private router: Router) {}

  ngOnInit() {
    let inputPassword = document.getElementById('password') as HTMLInputElement;
    let eye = document.getElementById('eyeIcon') as HTMLButtonElement;
    eye.addEventListener('click', () => {
      inputPassword.type === 'password'
        ? (inputPassword.type = 'text')
        : (inputPassword.type = 'password');
    });
  }

  async login() {
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
      this.service.user = userData;
      this.router.navigate(['/modules']);
    } else {
      let textIncorrect = document.getElementById('textIncorrect');
      if (textIncorrect)
        textIncorrect.innerText = 'Username or password incorrect.';
    }
  }
}
