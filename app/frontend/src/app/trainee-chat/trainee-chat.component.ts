import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { faComments, faHeadset, faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { HttpStatusCode } from '@angular/common/http';
import { ChatStatus } from '../support/support.component';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-trainee-chat',
  imports: [CommonModule, FontAwesomeModule,FormsModule, TranslatePipe],
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

  messageList: ChatMessage[]=[]
  private intervalId: any;
  private previousMessageCount=0;

  constructor(private authService: AuthService) {
    this.isTrainee = authService.currentUserValue?.Role.toLocaleUpperCase()=="FLEETMANAGER" || authService.currentUserValue?.Role.toLocaleUpperCase()=="SKIPPER"|| authService.currentUserValue?.Role.toLocaleUpperCase()=="INSTALLER";
    if(this.isTrainee) {
      this.init();
    }
  }
  async init() {
    if(this.chatMemberId==0) {
      let member= await this.getChatMember();
      if (!member) {
        console.error('Failed to initialize: chat member is null');
        return;
      }
      this.chatMemberId=member[0].id;
    }
    if(this.chatId==0) {
      let chatId = await this.getChatId();
      if (!chatId) {
        console.error('Failed to initialize: chat ID is null');
        return;
      }
      this.chatId=chatId;
    }
  }
  async toggleChat() {
    if(this.chatMemberId==0||this.chatId==0) {
      this.init();
    }
    this.isOpen = !this.isOpen;
    if(this.isOpen) {
      this.fetchMessages();
      this.startPolling();
      setTimeout(() => {
        this.scrollToBottom();
      }, 0);
    }
    else {
      clearInterval(this.intervalId);
    }
  }
  ngOnDestroy() {
    clearInterval(this.intervalId);
  }
  async sendMessage() {
    if (!this.message.trim()) return;
    this.checkChatStatusResolved();
    let message: ChatMessage = {
      chat_member_id: this.chatMemberId,
      date_time: new Date(),
      text_content: this.message
    }
    this.messageList.push(message)
    await fetch(`/api/messages`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        chat_member_id: this.chatMemberId,
        text_content: this.message
      })
    })
    this.message = '';

  }
  async checkChatStatusResolved() {
    const chatResponse = await fetch(`/api/chat/${this.chatId}`)
    if(!chatResponse.ok) {
      console.error("failed to fetch chat");
    }
    const chatData = await chatResponse.json();
    if(chatData.chat_status.toString()=="RESOLVED") {
      const updateChatResponse = await fetch(`/api/chat/${this.chatId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          chat_status: ChatStatus.NOT_STARTED,
        }),
      })
      if(!updateChatResponse.ok) {
        console.error("failed to update chat status");
      }
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
        body: JSON.stringify({
          chat_status: ChatStatus.RESOLVED
        })
      })
      if (!chatResponse.ok) {
        console.error('Failed to create chat');
        return null;
      }
      const newChat = await chatResponse.json();
      this.chatId=newChat.id;
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
      return newMember;
    }
    else if (!chatMemberResponse.ok) {
      console.error('Failed to fetch chat member');
      return null;
    }
    return await chatMemberResponse.json();

  }
  async getChatId() {
    if (!this.chatMemberId) {
      console.error('chatMemberId is not set before calling getChatId');
      return null;
    }
    let chatResponse = await fetch(`/api/members/${this.chatMemberId}/chat`);
    if (!chatResponse.ok) {
      console.error('Failed to fetch chat ID');
      return null;
    }
    let chatJson = await chatResponse.json();
    return await chatJson.id;
  }
  startPolling() {
    this.intervalId = setInterval(() => {
      this.fetchMessages();
    }, 10000);
  }
  async fetchMessages() {
    try {
      const response = await fetch(`/api/chat/${this.chatId}/messages`);
      if (response.ok) {
        const data = await response.json();
        await this.updateMessageList(data);
      }
    } catch (error) {
      console.error('Error fetching messages:', error);
    }
  }
  updateMessageList(data: any) {
    let messages: ChatMessage[]=[];
    for(let message of data) {
      let newMessage: ChatMessage = {
        chat_member_id: message.chat_member_id,
        date_time: message.date_time,
        text_content: message.text_content
      }
      messages.push(newMessage);
    }
    messages.sort(
      (a, b) => new Date(a.date_time).getTime() - new Date(b.date_time).getTime()
    );
    this.messageList=messages
  }

  @ViewChild('chatBody') chatBody!: ElementRef;

  ngAfterViewChecked() {
    if(this.messageList.length!=this.previousMessageCount) {
      this.scrollToBottom();
    }
    this.previousMessageCount=this.messageList.length;
  }

  scrollToBottom(): void {
    try {
      this.chatBody.nativeElement.scrollTop = this.chatBody.nativeElement.scrollHeight;
    } catch (err) {
      console.error('Scroll failed:', err);
    }
  }
}

export interface ChatMessage {
  chat_member_id: number;
  date_time: Date;
  text_content: string;
}