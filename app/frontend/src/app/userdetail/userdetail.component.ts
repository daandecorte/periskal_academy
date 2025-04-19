import { CommonModule, DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { LanguageService } from '../services/language.service';
import { Subscription } from 'rxjs';
import { AuthService, Role } from '../services/auth.service';
import { IUser } from '../types/user-info';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-userdetail',
  imports: [CommonModule, TranslatePipe, FormsModule, DatePipe],
  templateUrl: './userdetail.component.html',
  styleUrl: './userdetail.component.css'
})
export class UserdetailComponent {
  Role=Role;
  userId: string | null = '';
  userdetails: UserDetail[] | undefined;
  filteredUserDetails: UserDetail[] = [];
  currentLanguage: keyof Translated = 'ENGLISH';
  selectedStatus: string = 'ALL';
  searchQuery: string = '';
  user: User | undefined;

  constructor(private route: ActivatedRoute, private languageService: LanguageService, public authService: AuthService) {
  }
  async getUserInfo() {
    let user=await fetch(`/api/users/${this.userId}`)
    this.user = await user.json();
    let trainings = await fetch(`/api/users/${this.userId}/modules`);
    this.userdetails = await trainings.json();
    if(this.userdetails) {
      await console.log(this.userdetails);
      this.filteredUserDetails = await this.userdetails;
    }
  }
  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id');
    this.getUserInfo()

    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = language as keyof Translated;
    });
  }
  filterTrainings() {
    if(this.userdetails) {
      this.filteredUserDetails = this.userdetails.filter(detail => {
        const matchesStatus = this.selectedStatus === '' || this.selectedStatus === 'ALL' || detail.module_progress?.status === this.selectedStatus;
        const matchesTitle = this.searchQuery === '' || detail.module.title?.[this.currentLanguage].toLowerCase().includes(this.searchQuery.toLowerCase());
  
        return matchesStatus && matchesTitle;
      });
    }
  }
}


interface Exam {
  id: number;
  max_attempts: number;
  passing_score: number;
  question_amount: number;
  questions: any[]; // Use a proper type if available
  time: number;
}

interface Module {
  id: number;
  active: boolean;
  description?: Translated;
  title?: Translated;
  exams: Exam[];
  tips: any[];
  trainings: any[];
}
interface ExamAttempt {
  id: number,
  start_date_time: string,
  end_date_time: string,
  exam_status_type: string,
  score: number
}
interface Translated {
  ENGLISH: string, 
  FRENCH: string, 
  DUTCH: string,
  GERMAN: string,
}

interface User {
  id: number;
  language: string;
  firstname: string;
  lastname: string;
  shipname: string;
  user_id: string;
}

interface UserDetail {
  id: number;
  module_progress: ModuleProgress
  module: Module;
  user: User;
  exam_attempts: ExamAttempt[]
}
interface ModuleProgress {
    id: number,
    start_date_time: string | null,
    last_time_accessed: string | null,
    status: string | null,
    training_progress: any
}

interface UserDetailResponse {
  [key: number]: UserDetail;
  length: number;
}