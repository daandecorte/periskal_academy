import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { LanguageService } from '../services/language.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-userdetail',
  imports: [CommonModule, TranslatePipe],
  templateUrl: './userdetail.component.html',
  styleUrl: './userdetail.component.css'
})
export class UserdetailComponent {
  userId: string | null = '';
  userdetails: UserDetail[] | undefined;
  currentLanguage: keyof Translated = 'ENGLISH';

  constructor(private route: ActivatedRoute, private languageService: LanguageService) {
  }
  async getUserInfo() {
    let modules = await fetch(`/api/users/${this.userId}/modules`);
    this.userdetails = await modules.json();
    if(this.userdetails) {
      await console.log(this.userdetails[0]);
    }
  }
  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id');
    this.getUserInfo()

    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = language as keyof Translated; // Type assertion
    });
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
  module_progress: any | null; // Use a proper type if available
  module: Module;
  user: User;
  exam_attempts: ExamAttempt[]
}

interface UserDetailResponse {
  [key: number]: UserDetail;
  length: number;
}