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
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-exam-result',
  imports: [FontAwesomeModule, TranslatePipe, DatePipe],
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

  @Input() passed: boolean = true;
  @Input() score: number = 0;
  @Input() userCertificateId: number = 1;

  currentUser$: Observable<IUser | null>;
  currentUser!: IUser;
  userCertificate: UserCertificate | undefined;

  constructor(private authService: AuthService, private router: Router) {
    this.currentUser$ = this.authService.currentUser$;
    this.currentUser$.subscribe((user) => {
      if (user != null) this.currentUser = user;
    });
    this.getUserCertificate(this.userCertificateId);
  }
  async getUserCertificate(userCertificateId: number) {
    let userCertificateResponse = await fetch(`/api/user_certificates/${userCertificateId}`);
    this.userCertificate = await userCertificateResponse.json();
  }

  ngOnInit() {
    if (this.userCertificateId > 0) {
      this.getUserCertificate(this.userCertificateId);
    }
  }

  primaryClicked() {
    if (this.passed && this.userCertificateId > 1) {
      this.downloadPdf(this.userCertificateId);
    }
    else this.router.navigate(['/trainings']);
  }

  secondaryClicked() {
    this.router.navigate(['/trainings']);
  }
  async downloadPdf(userCertificateId: number) {
    const response = await fetch(`/api/pdf/generate/${userCertificateId}`);
  
    if (!response.ok) {
      throw new Error("Failed to fetch PDF");
    }
  
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
  
    const a = document.createElement("a");
    a.href = url;
    a.download = `certificate-${userCertificateId}.pdf`;
    document.body.appendChild(a);
    a.click();
    a.remove();
  
    window.URL.revokeObjectURL(url);
  }
}
interface UserCertificate {
  id: number,
  issue_date: Date,
  expiry_date: Date,
  status: String,
}