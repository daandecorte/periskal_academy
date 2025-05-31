import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CertificateService } from '../../services/certificate.service';
import { TranslatePipe } from '@ngx-translate/core';
@Component({
  selector: 'app-send-info',
  imports: [FormsModule, TranslatePipe],
  templateUrl: './send-info.component.html',
  styleUrl: './send-info.component.css',
})
export class SendInfoComponent {
  constructor(public service: CertificateService) {}
}
