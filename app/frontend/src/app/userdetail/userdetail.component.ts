import { CommonModule, DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { LanguageService } from '../services/language.service';
import { Observable, Subscription } from 'rxjs';
import { AuthService, Role } from '../services/auth.service';
import { IUser } from '../types/user-info';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-userdetail',
  imports: [CommonModule, TranslatePipe, FormsModule, DatePipe, RouterLink],
  templateUrl: './userdetail.component.html',
  styleUrl: './userdetail.component.css',
})
export class UserdetailComponent {
  Role = Role;
  userId: string | null = '';
  userdetails: UserDetail[] | undefined;
  filteredUserDetails: UserDetail[] = [];
  currentLanguage: keyof Translated = 'ENGLISH';
  selectedStatus: string = 'ALL';
  searchQuery: string = '';
  user: User | undefined;
  currentUser: Observable<IUser | null>;
  role: String = '';
  userCertificateMap: Map<number, UserCertificate | null> = new Map<
    number,
    UserCertificate | null
  >();
  overallProgress = 0;

  constructor(
    private route: ActivatedRoute,
    private languageService: LanguageService,
    public authService: AuthService
  ) {
    this.currentUser = this.authService.currentUser$;
    this.currentUser.subscribe((user) => {
      if (user) {
        this.role = user.Role;
      } else {
        this.role = '';
      }
    });
  }
  async getUserInfo() {
    let user = await fetch(`/api/users/${this.userId}`);
    this.user = await user.json();
    let modules = await fetch(`/api/users/${this.userId}/trainings`);
    this.userdetails = await modules.json();
    if (this.userdetails) {
      this.filteredUserDetails = await this.userdetails;
      await this.getOverallProgress();
      for (const u of this.userdetails) {
        const cert = await this.getUserCertificate(u.training.id);
        this.userCertificateMap.set(u.training.id, cert);
      }
    }
  }
  getOverallProgress() {
    if (this.userdetails) {
      let total = 0;
      this.userdetails?.forEach((u) => {
        if (u.training_progress.modules_completed != 0) {
          total +=
            (u.training_progress.modules_completed /
              u.training.modules.length) *
            100;
        }
      });
      this.overallProgress = total / this.userdetails?.length;
    }
  }
  async getUserCertificate(trainingId: number) {
    let userCertificateResponse = await fetch(
      `/api/user_certificates/training/${trainingId}/user/${this.user?.id}`
    );
    if (userCertificateResponse.status == 200) {
      let userCertificate = await userCertificateResponse.json();
      if (userCertificate) {
        return userCertificate;
      }
    }
    return null;
  }
  async downloadPdf(userCertificateId: number) {
    const response = await fetch(`/api/pdf/generate/${userCertificateId}`);

    if (!response.ok) {
      throw new Error('Failed to fetch PDF');
    }

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = url;
    a.download = `certificate-${userCertificateId}.pdf`;
    document.body.appendChild(a);
    a.click();
    a.remove();

    window.URL.revokeObjectURL(url);
  }
  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id');
    this.getUserInfo();

    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = language as keyof Translated;
    });
  }
  filterTrainings() {
    if (this.userdetails) {
      this.filteredUserDetails = this.userdetails.filter((detail) => {
        const matchesStatus =
          this.selectedStatus === '' ||
          this.selectedStatus === 'ALL' ||
          detail.training_progress?.status === this.selectedStatus;
        const matchesTitle =
          this.searchQuery === '' ||
          detail.training.title?.[this.currentLanguage]
            .toLowerCase()
            .includes(this.searchQuery.toLowerCase());

        return matchesStatus && matchesTitle;
      });
    }
  }
  mapStatusToTranslationKey(status: string | null | undefined): string {
    switch (status) {
      case 'IN_PROGRESS':
        return 'USER_DETAIL.IN_PROGRESS';
      case 'NOT_STARTED':
        return 'USER_DETAIL.NOT_STARTED';
      case 'COMPLETED':
        return 'USER_DETAIL.COMPLETED';
      default:
        return 'USER_DETAIL.NO_STATUS';
    }
  }
}

interface Exam {
  id: number;
  max_attempts: number;
  passing_score: number;
  question_amount: number;
  questions: any[];
  time: number;
}

interface Training {
  id: number;
  active: boolean;
  description?: Translated;
  title?: Translated;
  exams: Exam[];
  tips: any[];
  modules: any[];
}
interface ExamAttempt {
  id: number;
  start_date_time: string;
  end_date_time: string;
  exam_status_type: string;
  score: number;
}
interface Translated {
  ENGLISH: string;
  FRENCH: string;
  DUTCH: string;
  GERMAN: string;
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
  training_progress: TrainingProgress;
  training: Training;
  user: User;
  exam_attempts: ExamAttempt[];
}
interface TrainingProgress {
  id: number;
  start_date_time: string | null;
  last_time_accessed: string | null;
  status: string | null;
  modules_completed: number;
}

interface UserDetailResponse {
  [key: number]: UserDetail;
  length: number;
}

interface UserCertificate {
  id: number;
  issue_date: Date;
  expiry_date: Date;
  status: string;
}
