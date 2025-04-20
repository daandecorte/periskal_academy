import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Training } from '../services/training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faLayerGroup } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-assigned-training-card',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, TranslateModule],
  templateUrl: './assigned-training-card.component.html',
  styleUrl: './assigned-training-card.component.css'
})
export class AssignedTrainingCardComponent {
  @Input() training!: Training;
  @Input() currentLanguage: string = 'EN';
  faLayerGroup = faLayerGroup;
  
  constructor(private router: Router) {}
  
  getLocalizedTitle(): string {
    if (this.training.titleLocalized && this.training.titleLocalized[this.currentLanguage]) {
      return this.training.titleLocalized[this.currentLanguage];
    }
    return this.training.title || '';
  }
  
  goToModuleOverview(): void {
    this.router.navigate(['/trainings', this.training.id]);
  }
}