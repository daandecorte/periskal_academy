import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { faComments, faHeadset, faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-trainee-chat',
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './trainee-chat.component.html',
  styleUrl: './trainee-chat.component.css'
})
export class TraineeChatComponent {
  isOpen = false;
  faComments=faComments;
  faHeadset=faHeadset;
  faPaperPlane=faPaperPlane;

  toggleChat() {
    this.isOpen = !this.isOpen;
  }
}
