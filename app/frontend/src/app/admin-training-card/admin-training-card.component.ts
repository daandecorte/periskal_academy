import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPencilAlt, faTrash } from '@fortawesome/free-solid-svg-icons';
import { Training } from '../services/training.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-training-card',
  imports: [
    CommonModule, 
    FontAwesomeModule,
    RouterLink
  ],
  templateUrl: './admin-training-card.component.html',
  styleUrl: './admin-training-card.component.css'
})
export class AdminTrainingCardComponent {
  @Input() training!: Training;
  @Input() currentLanguage: string = 'EN';

  faPencilAlt = faPencilAlt;
  faTrash = faTrash;

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