import { Component, ElementRef, ViewChild } from '@angular/core';
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
  currentChatIndex: number=0;
  private intervalId: any;
  private previousMessageCount=0;
  currentUserId:number=0;
  currentChatMemberId:number=0;
  message: string='';
  statusFilter: ChatStatus|null = ChatStatus.IN_PROGRESS;
  searchTerm: string='';

  statusClassMap: Record<string, string> = {
    NOT_STARTED: 'status-new',
    IN_PROGRESS: 'status-active',
    RESOLVED: 'status-resolved'
  };

  constructor(private authService: AuthService) {
    this.getChats();
    this.getUserId();
  }

  async getChats() {
    let allChatsResponse = await fetch("/api/chat");
    if (allChatsResponse.ok) {
      const data = await allChatsResponse.json();
      await this.updateChatList(data);
    }
    
  }
  async getUserId() {
    let userResponse = await fetch(`/api/users/periskal_id/${this.authService.currentUserValue?.ID}`);
    if (userResponse.ok) {
      const data = await userResponse.json();
      this.currentUserId = await data.id;
    }
    
  }
  updateChatList(data: any) {
    console.log(data)
    let chats:ChatOverview[]=[];
    for(let chat of data) {
      let chatOverview: ChatOverview|null=null;
      let chatMemberId=0;
      for(let chatMember of chat.chat_members) {
        if(this.currentUserId==chatMember.user.id) {
          chatMemberId=chatMember.id;
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
      if(chatMemberId==0) {
        if(chatOverview && chatOverview.status==ChatStatus.NOT_STARTED) {
          chats.push(chatOverview);
        }
      }
      else if(chatOverview) {
        chats.push(chatOverview);
      }
    }
    this.chats=chats.sort((a,b) => a.status-b.status);
  }
  async changeChat(index: number) {
    clearInterval(this.intervalId);
    this.currentChatIndex=index;
    let currentChatResponse = await fetch(`/api/chat/${this.chats[index].id}`)
    if(currentChatResponse.ok) {
      let currentChatData = await currentChatResponse.json();
      this.currentChat.id= await currentChatData.id;
      this.currentChat.firstname=await this.chats[index].firstname
      this.currentChat.lastname=await this.chats[index].lastname
      this.currentChat.shipname=await this.chats[index].shipname
      this.currentChat.status= await currentChatData.chat_status
      if(this.currentChat.status.toString()!='NOT_STARTED') {
        for(let chatMember of currentChatData.chat_members) {
            if(chatMember.user.id==this.currentUserId) {
              this.currentChatMemberId=chatMember.id
            }
        }
      }
    }
    this.fetchMessages();
    this.startPolling();
    setTimeout(() => {
      this.scrollToBottom();
    }, 0);
  }
  startPolling() {
    this.intervalId = setInterval(() => {
      this.fetchMessages();
    }, 10000);
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
  async claimSupportTicket() {
    const updateChatResponse = await fetch(`/api/chat/${this.currentChat.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        chat_status: ChatStatus.IN_PROGRESS,
      }),
    })
    if(!updateChatResponse.ok) {
      console.error("failed to update chat status");
    }
    console.log(updateChatResponse);
    const memberResponse = await fetch(`/api/chat/${this.currentChat.id}/members`,  {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        user_id: this.currentUserId,
      }),
    })
    if (!memberResponse.ok) {
      console.error('Failed to create chat member');
    }
    const newMember = await memberResponse.json();
    this.currentChatMemberId=newMember.id;
    this.getChats();
    this.changeChat(this.currentChatIndex);
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
  get filteredChatIndices(): number[] {
    return this.chats
      .map((chat, index) => ({ chat, index }))
      .filter(({ chat }) => {
        console.log(chat);
        const fullName = `${chat.firstname} ${chat.lastname}`.toLowerCase();
        const matchesName = this.searchTerm
          ? fullName.includes(this.searchTerm.toLowerCase())
          : true;
        const matchesStatus = this.statusFilter == null
          ? true
          : chat.status.toString() == ChatStatus[this.statusFilter];
        return matchesName && matchesStatus;
      })
      .map(({ index }) => index);
  }
}
export interface ChatOverview {
  id: number,
  status: ChatStatus,
  firstname: string, 
  lastname: string,
  shipname: string
}
