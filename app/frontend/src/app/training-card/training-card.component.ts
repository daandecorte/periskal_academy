import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Training } from '../services/training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faLayerGroup, faCertificate } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';

//Elke trainingkaart bevat een titel, beschrijving, aantal moduleen, voortgangsindicator en een speciaal icoon voor certificeringstrainings

@Component({
  selector: 'app-training-card',
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './training-card.component.html',
  styleUrl: './training-card.component.css'
})
export class TrainingCardComponent {
  @Input() training!: Training;
  @Input() currentLanguage: string = 'EN';

  // Font Awesome icons
  faLayerGroup = faLayerGroup;  
  faCertificate = faCertificate;

  constructor(private router: Router) {}

  getButtonText(): string {
    return this.training.status === 'not_started' ? 'Start Module' : 'Continue Module';
  }

  goToModuleOverview(): void {
    this.router.navigate(['/trainings', this.training.id]);
  }

  getLocalizedTitle(): string {
    if (this.training.titleLocalized && this.training.titleLocalized[this.currentLanguage]) {
      return this.training.titleLocalized[this.currentLanguage];
    }
    return this.training.title || '';
  }

  getLocalizedDescription(): string {
    if (this.training.descriptionLocalized && this.training.descriptionLocalized[this.currentLanguage]) {
      return this.training.descriptionLocalized[this.currentLanguage];
    }
    return this.training.description || '';
  }
}
