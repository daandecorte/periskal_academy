import { Component } from '@angular/core';
import { SelectModuleComponent } from "./select-module/select-module.component";
import { TraineeChatComponent } from '../trainee-chat/trainee-chat.component';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AssignSailorComponent } from "./assign-sailor/assign-sailor.component";
import { AcceptTermsComponent } from "./accept-terms/accept-terms.component";
import { SendInfoComponent } from "./send-info/send-info.component";
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-certificates',
  imports: [FormsModule, CommonModule, SelectModuleComponent, TraineeChatComponent, AssignSailorComponent, AcceptTermsComponent, SendInfoComponent],
  templateUrl: './certificates.component.html',
  styleUrl: './certificates.component.css'
})
export class CertificatesComponent {
  paths = ["select-module", "assign-sailor", "accept-terms", "send-info"];
  currentIndex:number=0;
  private routerSubscription!: Subscription;

  constructor(private router:Router, private route:ActivatedRoute) {

  }

  nextStep() {
    this.currentIndex++;
    this.router.navigate([`/certificates/${this.paths[this.currentIndex]}`]);
  }
  previousStep() {
    this.currentIndex--;
    this.router.navigate([`/certificates/${this.paths[this.currentIndex]}`]);
  }
}
