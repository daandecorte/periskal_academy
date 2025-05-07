import { Component } from '@angular/core';
import { RouterLink, Router, RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { NewTrainingService } from '../new-training.service';
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
  currentLanguage: keyof ITranslated = 'ENGLISH';

  modalOverlayDelete!: HTMLDivElement;
  deleteId: number = -1;

  constructor(
    private trainingService: NewTrainingService,
    private languageService: LanguageService,
    private router: Router
  ) {
    this.modules = this.trainingService.newTraining.modules;

    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = language as keyof ITranslated;
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
}

interface ITranslated {
  ENGLISH: string;
  FRENCH: string;
  DUTCH: string;
  GERMAN: string;
}

interface IModule {
  title: ITranslated;
  description: ITranslated;
  content: IContent[];
  questions: IQuestion[];
}

interface IContent {
  contentType: ContentType;
  reference: ITranslated;
}

enum ContentType {
  TEXT,
  PICTURE,
  VIDEO,
}

interface IQuestion {
  text: ITranslated;
  questionOptions: IQuestionOption[];
}

interface IQuestionOption {
  text: ITranslated;
  isCorrect: boolean;
}
