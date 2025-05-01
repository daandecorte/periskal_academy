import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CertificatesComponent } from '../certificates.component';
import { CertificateService } from '../../services/certificate.service';
import { LanguageService } from '../../services/language.service';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-accept-terms',
  templateUrl: './accept-terms.component.html',
  styleUrls: ['./accept-terms.component.css'],
  imports: [CurrencyPipe], 
  standalone: true
})
export class AcceptTermsComponent {
  @Input() public accepted = false;
  @Output() accept = new EventEmitter<boolean>();

  acceptTerms() {
    this.accept.emit(!this.accepted);
  }
  constructor(public service: CertificateService, public languageService: LanguageService) {}
}
