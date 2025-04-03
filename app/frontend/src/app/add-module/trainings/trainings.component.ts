import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-trainings',
  imports: [RouterLink, TranslateModule],
  templateUrl: './trainings.component.html',
  styleUrl: './trainings.component.css'
})
export class TrainingsComponent {
}
