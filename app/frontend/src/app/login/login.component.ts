import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUser, faGlobe, faLock } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  imports: [FontAwesomeModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  faUser = faUser;
  faGlobe = faGlobe;
  faLock = faLock;
}
