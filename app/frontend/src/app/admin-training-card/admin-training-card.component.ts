import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { Training } from '../services/training.service';
import { Router } from '@angular/router';
import {
  ContentType,
  NewTrainingService,
} from '../add-training/new-training.service';

@Component({
  selector: 'app-admin-training-card',
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './admin-training-card.component.html',
  styleUrl: './admin-training-card.component.css',
})
export class AdminTrainingCardComponent {
  @Input() training!: Training;
  @Input() currentLanguage: string = 'EN';

  // FontAwesome icons used in the template
  faPencilAlt = faPencilAlt;

  constructor(
    private router: Router,
    private newTrainingService: NewTrainingService
  ) {}

  //Returns the localized title if available, otherwise returns the default title (English)
  getLocalizedTitle(): string {
    if (
      this.training.titleLocalized &&
      this.training.titleLocalized[this.currentLanguage]
    ) {
      return this.training.titleLocalized[this.currentLanguage];
    }
    return this.training.title || '';
  }

  //Returns the localized description if available, otherwise returns the default description (English)
  getLocalizedDescription(): string {
    if (
      this.training.descriptionLocalized &&
      this.training.descriptionLocalized[this.currentLanguage]
    ) {
      return this.training.descriptionLocalized[this.currentLanguage];
    }
    return this.training.description || '';
  }

  async editModule(id: number) {
    let dataTraining = await fetch(`/api/trainings/${id}`);
    this.newTrainingService.newTraining = await dataTraining.json();
    this.newTrainingService.editModuleIndex(id);

    let dataCertificate = await fetch(`/api/certificates/training/${id}`);
    this.newTrainingService.newTraining.certificate =
      await dataCertificate.json();

    // Process each module and set up previews
    const modules = this.newTrainingService.newTraining.modules;
    for (let i = 0; i < modules.length; i++) {
      // Initialize preview arrays for this module
      this.newTrainingService.addVideoPreviewModule();
      this.newTrainingService.addImagePreviewModule();

      // Set up content previews based on content type
      const moduleContent = modules[i].content;
      for (let j = 0; j < moduleContent.length; j++) {
        const content = moduleContent[j];
        if (content.content_type === ContentType.PICTURE) {
          this.newTrainingService.imagePreviews[i][j] = content.reference;
        } else if (content.content_type === ContentType.VIDEO) {
          this.newTrainingService.videoPreviews[i][j] = content.reference;
        }
      }
    }

    // Navigate to add training page
    this.router.navigate(['/add-training']);
  }

  // Checks if the training has content in the specified language
  // Used for the colorcoded language labels
  languageExists(lang: string): boolean {
    if (!this.training.titleLocalized) return false;
    return !!this.training.titleLocalized[lang];
  }
}
