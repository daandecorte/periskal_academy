import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faCheck,
  faClose,
  faCalendar,
  faClock,
  faDownload,
  faHome,
  faBookOpen,
} from '@fortawesome/free-solid-svg-icons';
import { TranslatePipe } from '@ngx-translate/core';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { IUser } from '../types/user-info';
import { Router } from '@angular/router';

@Component({
  selector: 'app-exam-result',
  imports: [FontAwesomeModule, TranslatePipe],
  templateUrl: './exam-result.component.html',
  styleUrl: './exam-result.component.css',
})
export class ExamResultComponent {
  faCheck = faCheck;
  faClose = faClose;
  faCalendar = faCalendar;
  faClock = faClock;
  faDownload = faDownload;
  faHome = faHome;
  faBookOpen = faBookOpen;

  @Input() passed: boolean = false;

  currentUser$: Observable<IUser | null>;
  currentUser!: IUser;
  score!: number;

  constructor(private authService: AuthService, private router: Router) {
    this.currentUser$ = this.authService.currentUser$;
    this.currentUser$.subscribe((user) => {
      if (user != null) this.currentUser = user;
    });
  }

  async getScore() {
    let result = await fetch(`/api/users/${this.currentUser.ID}/exam_attempts`);
    let data: IExamAttempt[] = await result.json();

    const now = new Date();

    let closestAttempt: IExamAttempt | null = null;
    let closestDiff = Infinity;

    for (let attempt of data) {
      const endDate = new Date(attempt.endDateTime);
      const diff = Math.abs(endDate.getTime() - now.getTime());

      if (diff < closestDiff) {
        closestDiff = diff;
        closestAttempt = attempt;
      }
    }

    if (
      closestAttempt &&
      (closestAttempt.examStatusType == 'PASSED' ||
        closestAttempt.examStatusType == 'FAILED')
    ) {
      this.score = closestAttempt.score;
    }
  }

  primaryClicked() {
    if (this.passed) this.router.navigate(['/trainings']);
    else this.router.navigate(['/trainings']);
  }

  secondaryClicked() {
    this.router.navigate(['/trainings']);
  }
}

interface IExamAttempt {
  id: number;
  startDateTime: string;
  endDateTime: string;
  examStatusType: string;
  score: number;
}
