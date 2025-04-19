import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-edit-modules',
  imports: [RouterLink, TranslateModule],
  templateUrl: './edit-modules.component.html',
  styleUrl: './edit-modules.component.css'
})
export class EditModulesComponent {

}
