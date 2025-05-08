import { Component } from '@angular/core';
import { init } from 'emailjs-com';
import { Certificate } from '../certificates/select-training/select-training.component';
import { LanguageService } from '../services/language.service';
import { CommonModule } from '@angular/common';
import { TitleStrategy } from '@angular/router';

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
  constructor(public languageService: LanguageService) {
    this.init();
  }
  async init() {
    let res = await fetch("/api/certificates");
    let certificates = await res.json();
    this.certificates = certificates;
    this.filteredCertificates = this.certificates;
  }
  select(id: number) {
    const index = this.filteredCertificates.findIndex(c=>c.id==id);
    if(index!=-1) {
      const index = this.selectedCertificates.findIndex(c=>c.id==this.filteredCertificates[index].id)
      this.selectedCertificates.splice(index, 1);
      console.log(index);
    }
    this.selectedCertificates.push(this.filteredCertificates[index]);
  }

  public isSelected(id:number): boolean {
    return this.selectedCertificates.some(c=> c.id==id);
  }

}
