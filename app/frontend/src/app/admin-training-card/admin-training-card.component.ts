import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPencilAlt, faTrash } from '@fortawesome/free-solid-svg-icons';
import { Training } from '../services/training.service';
import { RouterLink, Router } from '@angular/router';
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

  faPencilAlt = faPencilAlt;
  faTrash = faTrash;

  constructor(
    private router: Router,
    private newTrainingService: NewTrainingService
  ) {}

  getLocalizedTitle(): string {
    if (
      this.training.titleLocalized &&
      this.training.titleLocalized[this.currentLanguage]
    ) {
      return this.training.titleLocalized[this.currentLanguage];
    }
    return this.training.title || '';
  }

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

    for (
      let i = 0;
      i < this.newTrainingService.newTraining.modules.length;
      i++
    ) {
      this.newTrainingService.addVideoPreviewModule();
      this.newTrainingService.addImagePreviewModule();

      for (
        let j = 0;
        j < this.newTrainingService.newTraining.modules[i].content.length;
        j++
      ) {
        if (
          this.newTrainingService.newTraining.modules[i].content[j]
            .content_type == ContentType.PICTURE
        ) {
          this.newTrainingService.imagePreviews[i][j] =
            this.newTrainingService.newTraining.modules[i].content[j].reference;
        } else if (
          this.newTrainingService.newTraining.modules[i].content[j]
            .content_type == ContentType.VIDEO
        ) {
          this.newTrainingService.videoPreviews[i][j] =
            this.newTrainingService.newTraining.modules[i].content[j].reference;
        }
      }
    }

    this.router.navigate(['/add-training']);
  }
}
