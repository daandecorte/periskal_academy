import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-edit-trainings',
  imports: [RouterLink, TranslateModule],
  templateUrl: './edit-trainings.component.html',
  styleUrl: './edit-trainings.component.css'
})
export class EditTrainingsComponent {

}
