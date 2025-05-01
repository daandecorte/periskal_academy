import { Component } from '@angular/core';
import { LanguageService } from '../../services/language.service';
import { CertificateService } from '../../services/certificate.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
@Component({
  selector: 'app-select-training',
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './select-training.component.html',
  styleUrl: './select-training.component.css'
})
export class SelectTrainingComponent {
  Language=Language
  certificates: Certificate[]=[];
  filteredCertificates: Certificate[]=[];
  searchQuery='';
  constructor(public service: CertificateService, public languageService: LanguageService) {
    this.init();
  }
  async init() {
    let certificateData = await fetch("/api/certificates")
    this.certificates= await certificateData.json();
    this.filteredCertificates = await this.certificates;
  }
  toggleCertificate(index: number) {
    let certificateIndex = this.service.selectedCertificates.findIndex(c=>c.id==this.certificates[index].id)
    if(certificateIndex!=-1) {
      this.service.selectedCertificates.splice(certificateIndex, 1);
    }
    else this.service.selectedCertificates.push(this.certificates[index])
  }
  isSelected(id: number) {
    return this.service.selectedCertificates.some(c => c.id == id);
  }
  filterCertificates() {
    if (this.certificates) {
      const language = this.languageService.getLanguage();
  
      this.filteredCertificates = this.certificates.filter(cert => {
        const title = cert.training.title?.[language];
        const matchesTitle =
          this.searchQuery === '' ||
          (title?.toLowerCase() ?? '').includes(this.searchQuery.toLowerCase());
        return matchesTitle;
      });
    }
  }
}
export interface Certificate {
  id: number,
  training: {
    id: number,
    title: any,
    description: any, 
    modules: [],
    exams: [],
    active: boolean
  },
  validity_period: number,
  price: number
}
export enum Language {
  ENGLISH, FRENCH, DUTCH, GERMAN
}
