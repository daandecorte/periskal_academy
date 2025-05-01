import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CertificateService } from '../../services/certificate.service';
import emailjs from 'emailjs-com';
@Component({
  selector: 'app-send-info',
  imports: [FormsModule],
  templateUrl: './send-info.component.html',
  styleUrl: './send-info.component.css'
})
export class SendInfoComponent {
  constructor(public service: CertificateService) {

  }
}
