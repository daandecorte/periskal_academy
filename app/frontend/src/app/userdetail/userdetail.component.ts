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

  constructor(private route: ActivatedRoute) {
  }
  async getUserInfo() {
    let modules = await fetch(`/api/users/${this.userId}/modules`);
    let modulesdata = await modules.json();
    await console.log(modulesdata);
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
  description?: string;
  title?: string;
  exams: Exam[];
  tips: any[]; // Use a proper type if available
  trainings: any[]; // Use a proper type if available
}

interface User {
  id: number;
  language: string;
  user_id: string;
}

interface UserDetail {
  id: number;
  module_progress: any | null; // Use a proper type if available
  module: Module;
  user: User;
}

interface UserDetailResponse {
  [key: number]: UserDetail;
  length: number;
}