import { Component } from '@angular/core';
import { RouterLink, Router, RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import {
  NewTrainingService,
  ITranslated,
  IModule,
  ContentType,
} from '../new-training.service';
import { LanguageService } from '../../services/language.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faTrash, faEdit } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-modules',
  imports: [RouterLink, RouterModule, TranslateModule, FontAwesomeModule],
  templateUrl: './modules.component.html',
  styleUrl: './modules.component.css',
})
export class ModulesComponent {
  faTrash = faTrash;
  faEdit = faEdit;

  modules: IModule[] = [];
  contentCount: IContentCount[] = [];
  currentLanguage: keyof ITranslated = 'ENGLISH';

  modalOverlayDelete!: HTMLDivElement;
  deleteId: number = -1;

  ContentType = ContentType;

  constructor(
    private trainingService: NewTrainingService,
    private languageService: LanguageService,
    private router: Router
  ) {
    this.modules = this.trainingService.newTraining.modules;

    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = language as keyof ITranslated;
    });

    this.modules.forEach((module) => {
      let textsCount = 0;
      let picturesCount = 0;
      let videosCount = 0;

      module.content.forEach((el) => {
        if (el.content_type == ContentType.TEXT) textsCount++;
        if (el.content_type == ContentType.PICTURE) picturesCount++;
        if (el.content_type == ContentType.VIDEO) videosCount++;
      });

      this.contentCount.push({
        texts: textsCount,
        pictures: picturesCount,
        videos: videosCount,
      });
    });
  }

  ngAfterViewInit() {
    this.modalOverlayDelete = document.getElementById(
      'modal-overlay-delete'
    ) as HTMLDivElement;
  }

  deleteModal(index: number) {
    this.deleteId = index;

    this.modalOverlayDelete.classList.remove('hidden');
  }

  cancelDelete() {
    this.modalOverlayDelete.classList.add('hidden');
  }

  deleteModule() {
    this.trainingService.newTraining.modules.splice(this.deleteId, 1);
    this.modalOverlayDelete.classList.add('hidden');
  }

  editModule(index: number) {
    this.trainingService.newModule =
      this.trainingService.newTraining.modules[index];
    this.trainingService.editModuleIndex(index);
    this.router.navigate(['/add-training/modules/new/content']);
  }

  addNewModule() {
    this.trainingService.addVideoPreviewModule();
    this.trainingService.addImagePreviewModule();
    this.router.navigate(['/add-training/modules/new/content']);
  }
}

interface IContentCount {
  texts: number;
  pictures: number;
  videos: number;
}
