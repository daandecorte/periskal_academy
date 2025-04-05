import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { faComments, faHeadset, faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { HttpStatusCode } from '@angular/common/http';

@Component({
  selector: 'app-trainee-chat',
  imports: [CommonModule, FontAwesomeModule,FormsModule],
  templateUrl: './trainee-chat.component.html',
  styleUrl: './trainee-chat.component.css'
})
export class TraineeChatComponent {
  isOpen = false;
  faComments=faComments;
  faHeadset=faHeadset;
  faPaperPlane=faPaperPlane;

  isTrainee: Boolean;
  message: string="";
  chatMemberId: number=0;
  chatId:number=0;

  constructor(private authService: AuthService) {
    this.isTrainee = authService.currentUserValue?.Role.toLocaleUpperCase()=="FLEETMANAGER" || authService.currentUserValue?.Role.toLocaleUpperCase()=="SKIPPER"|| authService.currentUserValue?.Role.toLocaleUpperCase()=="INSTALLER";
  }
  toggleChat() {
    this.isOpen = !this.isOpen;
  }
  async sendMessage() {
    if (!this.message.trim()) return;
    console.log('Sending message:', this.message);

    this.message = '';

    if(this.chatMemberId==0) {
      let member= await this.getChatMember();
      this.chatMemberId=member.id;
      console.log(member);
    }

  }
  async getChatMember() {
    let userResponse = await fetch(`/api/users/periskal_id/${this.authService.currentUserValue?.ID}`)

    if (!userResponse.ok) {
      console.error('Failed to fetch user');
      return null;
    }

    let userJson = await userResponse.json();

    let chatMemberResponse = await fetch(`/api/users/${userJson.id}/chat_member`);
    
    if(chatMemberResponse.status==HttpStatusCode.NotFound){
      let chatResponse = await fetch(`/api/chat`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({})
      })
      if (!chatResponse.ok) {
        console.error('Failed to create chat');
        return null;
      }
      const newChat = await chatResponse.json();
      const memberResponse = await fetch(`/api/chat/${newChat.id}/members`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          user_id: userJson.id,
        }),
      });
      if (!memberResponse.ok) {
        console.error('Failed to create chat member');
        return null;
      }
      const newMember = await memberResponse.json();
      console.log('New chat member created:', newMember);
      return newMember;
    }
    else if (!chatMemberResponse.ok) {
      console.error('Failed to fetch chat member');
      return null;
    }
    return await chatMemberResponse.json();

  }
}
