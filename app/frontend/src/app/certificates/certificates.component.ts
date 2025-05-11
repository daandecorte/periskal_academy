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
import { BillingInfo, CertificateService } from '../services/certificate.service';
import { Serializer } from '@angular/compiler';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-certificates',
  imports: [FormsModule, CommonModule, SelectTrainingComponent, TraineeChatComponent, AssignSailorComponent, AcceptTermsComponent, SendInfoComponent, TranslatePipe],
  templateUrl: './certificates.component.html',
  styleUrl: './certificates.component.css'
})
export class CertificatesComponent {
  paths = ["select-module", "assign-sailor", "accept-terms", "send-info"];
  currentIndex:number=0;
  private routerSubscription!: Subscription;
  termsAccepted = false;

  constructor(private router:Router, private route:ActivatedRoute, private service: CertificateService, private translate: TranslateService) {}

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
    const templateParams: {
      orders: { name: string; price: number }[];
      users: {name: string, id: number}[];
      total_price: number;
      details: BillingInfo;
    } = {
      orders: [],
      users: [],
      total_price: this.service.totalPrice(),
      details: this.service.billingInfo
    }
    for(let certificate of this.service.selectedCertificates) {
      templateParams.orders.push({name: certificate.training.title['ENGLISH'], price: certificate.price})
    }
    for(let user of this.service.selectedUsers) { 
      templateParams.users.push({name: user.firstname+" "+user.lastname, id: parseInt(user.id)})
    }
    emailjs.send(
      'service_apxfrbq',
      'template_qz2zssu',
      templateParams,
      'Iem59IEE2DjHe7Qtn'
    ).then(
      async (response) => {
        this.service.billingInfo = {
          company: "",
          vat: "",
          street: "",
          city: "",
          postal: "",
          country: "",
          state: "",
          name: "",
          phone: "",
          email: "",
        };
        this.service.selectedCertificates=[];
        this.service.selectedUsers=[];
        const msg = await this.translate.get('CERTIFICATES.INFO_SENT_SUCCESS').toPromise();
        alert(msg);
      },
      async (error) => {
        const msg = await this.translate.get('CERTIFICATES.INFO_SENT_FAILED').toPromise();
        alert(msg);
      }
    );
  }
}
