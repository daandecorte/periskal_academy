import { Component } from '@angular/core';
import { init } from 'emailjs-com';
import { Certificate } from '../certificates/select-training/select-training.component';
import { LanguageService } from '../services/language.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Route, Router, TitleStrategy } from '@angular/router';

@Component({
  selector: 'app-admin-certificates',
  imports: [CommonModule],
  templateUrl: './admin-certificates.component.html',
  styleUrl: './admin-certificates.component.css'
})
export class AdminCertificatesComponent {
  certificates: Certificate[] = [];
  filteredCertificates:Certificate[] = [];
  selectedCertificates:Certificate[] = [];
  userId: number;
  constructor(public languageService: LanguageService, private router: Router, private route: ActivatedRoute) {
    this.userId = parseInt(this.route.snapshot.paramMap.get('id') ?? "");
    this.init();
  }
  async init() {
    let res = await fetch("/api/certificates");
    let certificates = await res.json();
    this.certificates = certificates;
    this.filteredCertificates = this.certificates;
  }
  select(id: number) {
    const index = this.selectedCertificates.findIndex(c => c.id === id);
    if (index !== -1) {
      const idx = this.selectedCertificates.findIndex(c => c.id === this.filteredCertificates[index].id);
      this.selectedCertificates.splice(idx, 1);
    } else {
      const cert = this.filteredCertificates.find(c => c.id === id);
      if (cert) {
        this.selectedCertificates.push(cert);
      }
    }
  }

  public isSelected(id:number): boolean {
    return this.selectedCertificates.some(c=> c.id==id);
  }
  async postSelection() {
    this.selectedCertificates.forEach( async c=> {
      let response = await fetch("/api/user-certificates", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          user_id: this.userId,
          certificate_id: c.id,
          issue_date: new Date().toISOString(),
          expiry_date: new Date(new Date().setFullYear(new Date().getFullYear() + c.validity_period)).toISOString(),
          status: "VALID"
        })
      });
    })
    this.router.navigate([`/userdetail/`+this.userId]);
  }

}
