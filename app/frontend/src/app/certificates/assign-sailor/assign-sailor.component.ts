import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-assign-sailor',
  imports: [FontAwesomeModule],
  templateUrl: './assign-sailor.component.html',
  styleUrl: './assign-sailor.component.css'
})
export class AssignSailorComponent {
  faUser=faUser;
}
