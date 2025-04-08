import { Component } from '@angular/core';
import { faPaperPlane, faUser } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { CommonModule, DatePipe } from '@angular/common';
import { ChatMessage } from '../trainee-chat/trainee-chat.component';
import { AuthService } from '../services/auth.service';
import { TitleStrategy } from '@angular/router';
import { FormsModule } from '@angular/forms';

export enum ChatStatus {
  NOT_STARTED,IN_PROGRESS,RESOLVED
}

@Component({
  selector: 'app-support',
  imports: [FontAwesomeModule, CommonModule, FormsModule, DatePipe],
  templateUrl: './support.component.html',
  styleUrl: './support.component.css'
})
export class SupportComponent {
  faPaperPlane=faPaperPlane;
  faUser=faUser;
  ChatStatus=ChatStatus

  chats: ChatOverview[] = []
  messageList: ChatMessage[] = []
  currentChat: ChatOverview = {id:0, status: ChatStatus.NOT_STARTED, firstname: "", lastname: "", shipname: ""};
  private intervalId: any;
  private previousMessageCount=0;
  currentChatMemberId:number=0;
  message: string='';

  statusClassMap: Record<string, string> = {
    NOT_STARTED: 'status-new',
    IN_PROGRESS: 'status-active',
    RESOLVED: 'status-resolved'
  };

  constructor(private authService: AuthService) {
    this.getChats();
  }

  async getChats() {
    let allChatsResponse = await fetch("/api/chat");
    if (allChatsResponse.ok) {
      const data = await allChatsResponse.json();
      await this.updateChatList(data);
    }
    
  }
  updateChatList(data: any) {
    console.log(data)
    let chats:ChatOverview[]=[];
    for(let chat of data) {
      let chatOverview: ChatOverview|null=null;
      for(let chatMember of chat.chat_members) {
        console.log(this.authService.currentUserValue?.ID, chatMember.user.periskal_id)
        if(this.authService.currentUserValue?.ID==chatMember.user.periskal_id) {
          this.currentChatMemberId=chatMember.id;
        }
        else {
          chatOverview={
            id: chat.id,
            status: chat.chat_status,
            firstname: chatMember.user.firstname,
            lastname: chatMember.user.lastname,
            shipname: chatMember.user.shipname
          }
        }
      }
      if(chatOverview)
        chats.push(chatOverview);
    }
    this.chats=chats;
  }
  async changeChat(index: number) {
    let currentChatResponse = await fetch(`/api/chat/${this.chats[index].id}`)
    if(currentChatResponse.ok) {
      let currentChatData = await currentChatResponse.json();
      this.currentChat.id= await currentChatData.id;
      this.currentChat.firstname=await this.chats[index].firstname
      this.currentChat.lastname=await this.chats[index].lastname
      this.currentChat.shipname=await this.chats[index].shipname
      this.currentChat.status= await currentChatData.chat_status
    }
    this.fetchMessages();
  }
  async fetchMessages() {
    try {
      const response = await fetch(`/api/chat/${this.currentChat.id}/messages`);
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
  async sendMessage() {
    if (!this.message.trim()) return;
    let message: ChatMessage = {
      chat_member_id: this.currentChatMemberId,
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
        chat_member_id: this.currentChatMemberId,
        text_content: this.message
      })
    })
    this.message = '';

  }
}
export interface ChatOverview {
  id: number,
  status: ChatStatus,
  firstname: string, 
  lastname: string,
  shipname: string
}
