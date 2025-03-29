import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-userdetail',
  imports: [CommonModule, TranslatePipe],
  templateUrl: './userdetail.component.html',
  styleUrl: './userdetail.component.css'
})
export class UserdetailComponent {
  userId: string | null = '';
  userdetails: UserDetail[] | undefined;

  constructor(private route: ActivatedRoute) {
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
interface UserExam {
  id: number,
  exam: Exam,
  exam_attempts: any[]
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
  user_exams: UserExam[]
}

interface UserDetailResponse {
  [key: number]: UserDetail;
  length: number;
}