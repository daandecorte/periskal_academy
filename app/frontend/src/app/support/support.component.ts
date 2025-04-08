import { Component } from '@angular/core';
import { faPaperPlane, faUser } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { CommonModule } from '@angular/common';
import { ChatMessage } from '../trainee-chat/trainee-chat.component';

export enum ChatStatus {
  NOT_STARTED,IN_PROGRESS,RESOLVED
}

@Component({
  selector: 'app-support',
  imports: [FontAwesomeModule, CommonModule],
  templateUrl: './support.component.html',
  styleUrl: './support.component.css'
})
export class SupportComponent {
  faPaperPlane=faPaperPlane;
  faUser=faUser;
  ChatStatus=ChatStatus

  chats: ChatOverview[] = []
  messageList: ChatMessage[] = []
  currentChat: Chat = {id:0, status: ChatStatus.NOT_STARTED};
  private intervalId: any;
  private previousMessageCount=0;

  statusClassMap: Record<string, string> = {
    NOT_STARTED: 'status-new',
    IN_PROGRESS: 'status-active',
    RESOLVED: 'status-resolved'
  };

  constructor() {
    this.getChats();
  }

  async getChats() {
    let allChatsResponse = await fetch("/api/chat");
    if (allChatsResponse.ok) {
      const data = await allChatsResponse.json();
      await this.updateChatList(data);
      this.currentChat={
        id: this.chats[0].id,
        status: this.chats[0].id
      }
    }
    
  }
  updateChatList(data: any) {
    console.log(data)
    let chats:ChatOverview[]=[];
    for(let chat of data) {
      let chatOverview: ChatOverview={
        id: chat.id,
        status: chat.chat_status,
        firstname: chat.chat_members[0].user.firstname,
        lastname: chat.chat_members[0].user.lastname,
        shipname: chat.chat_members[0].user.shipname
      }
      chats.push(chatOverview);
    }
    this.chats=chats;
  }
  async changeChat(id: number) {
    let currentChatResponse = await fetch(`/api/chat/${id}`)
    if(currentChatResponse.ok) {
      let currentChatData = await currentChatResponse.json();
      this.currentChat.id= await currentChatData.id;
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
}
export interface ChatOverview {
  id: number,
  status: ChatStatus,
  firstname: string, 
  lastname: string,
  shipname: string
}
export interface Chat {
  id: number,
  status: ChatStatus
}
