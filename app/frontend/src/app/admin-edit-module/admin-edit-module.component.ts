import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-admin-edit-module',
  imports: [RouterLink, RouterLinkActive, RouterOutlet, TranslateModule],
  templateUrl: './admin-edit-module.component.html',
  styleUrl: './admin-edit-module.component.css'
})
export class AdminEditModuleComponent {

}
