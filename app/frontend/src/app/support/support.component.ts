import { Component } from '@angular/core';
import { faPaperPlane, faUser } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

export enum ChatStatus {
  NOT_STARTED,IN_PROGRESS,RESOLVED
}

@Component({
  selector: 'app-support',
  imports: [FontAwesomeModule],
  templateUrl: './support.component.html',
  styleUrl: './support.component.css'
})
export class SupportComponent {
  faPaperPlane=faPaperPlane;
  faUser=faUser;

  constructor() {

  }
}
