import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CertificatesComponent } from '../certificates.component';

@Component({
  selector: 'app-accept-terms',
  templateUrl: './accept-terms.component.html',
  styleUrls: ['./accept-terms.component.css'],
})
export class AcceptTermsComponent {
  @Input() public accepted = false;
  @Output() accept = new EventEmitter<boolean>();

  acceptTerms() {
    this.accept.emit(!this.accepted);
  }
}
