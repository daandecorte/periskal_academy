import { Component } from '@angular/core';
import { SelectTrainingComponent } from './select-training/select-training.component';
import { TraineeChatComponent } from '../trainee-chat/trainee-chat.component';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AssignSailorComponent } from "./assign-sailor/assign-sailor.component";
import { AcceptTermsComponent } from "./accept-terms/accept-terms.component";
import { SendInfoComponent } from "./send-info/send-info.component";
import { FormsModule } from '@angular/forms';
import { CommonModule, CurrencyPipe } from '@angular/common';
import emailjs from 'emailjs-com';

@Component({
  selector: 'app-certificates',
  imports: [FormsModule, CommonModule, SelectTrainingComponent, TraineeChatComponent, AssignSailorComponent, AcceptTermsComponent, SendInfoComponent],
  templateUrl: './certificates.component.html',
  styleUrl: './certificates.component.css'
})
export class CertificatesComponent {
  paths = ["select-module", "assign-sailor", "accept-terms", "send-info"];
  currentIndex:number=0;
  private routerSubscription!: Subscription;
  termsAccepted = false;

  constructor(private router:Router, private route:ActivatedRoute) {}

  nextStep() {
    this.currentIndex++;
  }
  previousStep() {
    this.currentIndex--;
  }

  onUpdateAcceptTerms(accepted: boolean) {
    this.termsAccepted = accepted;
  }
  complete() {
    this.sendEmail();
    this.router.navigate(["/trainings"])
  }
  sendEmail() {
    const templateParams = {
      from_name: 'Periskal Certificates',
      to_name: 'Periskal Billing',
      message: 'test',
      price: '3 euro'
    };
    emailjs.send(
      'service_apxfrbq',
      'template_qz2zssu',
      templateParams,
      'Iem59IEE2DjHe7Qtn'
    ).then(
      (response) => {
        console.log('SUCCESS!', response.status, response.text);
        alert('Email sent successfully!');
      },
      (error) => {
        console.log('FAILED...', error);
        alert('Failed to send email.');
      }
    );
  }
}
