import { Component } from '@angular/core';
import { SelectTrainingComponent } from './select-training/select-training.component';
import { Router } from '@angular/router';
import { AssignSailorComponent } from './assign-sailor/assign-sailor.component';
import { AcceptTermsComponent } from './accept-terms/accept-terms.component';
import { SendInfoComponent } from './send-info/send-info.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import emailjs from 'emailjs-com';
import {
  BillingInfo,
  CertificateService,
} from '../services/certificate.service';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-certificates',
  imports: [
    FormsModule,
    CommonModule,
    SelectTrainingComponent,
    AssignSailorComponent,
    AcceptTermsComponent,
    SendInfoComponent,
    TranslatePipe,
  ],
  templateUrl: './certificates.component.html',
  styleUrl: './certificates.component.css',
})
export class CertificatesComponent {
  paths = ['select-module', 'assign-sailor', 'accept-terms', 'send-info'];
  currentIndex: number = 0;
  termsAccepted = false;
  isSending: boolean = false;

  constructor(private router: Router, private service: CertificateService) {}

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
    this.updateUserCertificates();
    this.sendEmail();
  }
  async updateUserCertificates() {
    for (let certificate of this.service.selectedCertificates) {
      for (let user of this.service.selectedUsers) {
        try {
          let userTrainingGetResponse = await fetch(
            `/api/user_trainings/training/${certificate.training.id}/user/${user.id}`
          );
          if (userTrainingGetResponse.status == 404) {
            let userTrainingPostResponse = await fetch(`/api/user_trainings`, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
              },
              body: JSON.stringify({
                training_id: certificate.training.id,
                user_id: user.id,
                eligible_for_certificate: true,
              }),
            });
            if (userTrainingPostResponse.status != 201) {
              const errorText = await userTrainingPostResponse.text();
              console.error('failed to post usertraining ' + errorText);
            } else {
              const refreshedUserTrainingGet = await fetch(
                `/api/user_trainings/training/${certificate.training.id}/user/${user.id}`
              );
              const userTraining = await refreshedUserTrainingGet.json();

              let trainingProgressResponse = await fetch(
                `/api/training_progress`,
                {
                  method: 'POST',
                  headers: {
                    'Content-Type': 'application/json',
                    Accept: 'application/json',
                  },
                  body: JSON.stringify({
                    start_date_time: new Date().toISOString(),
                    status: 'NOT_STARTED',
                    user_training_id: userTraining.id,
                  }),
                }
              );
            }
          } else if (userTrainingGetResponse.status == 200) {
            let userTraining = await userTrainingGetResponse.json();
            let userTrainingPutResponse = await fetch(
              `/api/user_trainings/${userTraining.id}`,
              {
                method: 'PUT',
                headers: {
                  'Content-Type': 'application/json',
                  Accept: 'application/json',
                },
                body: JSON.stringify({
                  eligible_for_certificate: true,
                }),
              }
            );
            if (userTrainingPutResponse.status != 200) {
              const errorText = await userTrainingPutResponse.text();
              console.error('Error updating: ' + errorText);
            }
          } else {
            console.error(
              'an error occured requesting this usertraining ' +
                userTrainingGetResponse.status
            );
          }
        } catch (e) {}
      }
    }
  }
  sendEmail() {
    this.isSending = true;
    const templateParams: {
      orders: { name: string; price: number }[];
      users: { name: string; id: number }[];
      total_price: number;
      details: BillingInfo;
    } = {
      orders: [],
      users: [],
      total_price: this.service.totalPrice(),
      details: this.service.billingInfo,
    };
    for (let certificate of this.service.selectedCertificates) {
      templateParams.orders.push({
        name: certificate.training.title['ENGLISH'],
        price: certificate.price,
      });
    }
    for (let user of this.service.selectedUsers) {
      templateParams.users.push({
        name: user.firstname + ' ' + user.lastname,
        id: parseInt(user.id),
      });
    }
    emailjs
      .send(
        'service_apxfrbq',
        'template_qz2zssu',
        templateParams,
        'Iem59IEE2DjHe7Qtn'
      )
      .then(
        async (response) => {
          this.service.billingInfo = {
            company: '',
            vat: '',
            street: '',
            city: '',
            postal: '',
            country: '',
            state: '',
            name: '',
            phone: '',
            email: '',
          };
          this.service.selectedCertificates = [];
          this.service.selectedUsers = [];
          this.isSending = false;
          this.router.navigate(['/trainings']);
        },
        async (error) => {
          this.isSending = false;
          this.router.navigate(['/trainings']);
        }
      );
  }
  get disabled(): boolean {
    return (
      (this.termsAccepted == false && this.currentIndex == 2) ||
      (this.currentIndex == 0 &&
        this.service.selectedCertificates.length == 0) ||
      (this.currentIndex == 1 && this.service.selectedUsers.length == 0)
    );
  }
  get filledInForm(): boolean {
    for (const key in this.service.billingInfo) {
      if (key != 'state') {
        const value =
          this.service.billingInfo[
            key as keyof typeof this.service.billingInfo
          ];
        if (value.toString().trim() == '') {
          return true;
        }
      }
    }
    return false;
  }
}
