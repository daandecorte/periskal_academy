import {
  Component,
  ElementRef,
  ViewChild,
  ViewChildren,
  QueryList,
} from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import {
  NewTrainingService,
  ITranslated,
  ContentType,
} from '../../new-training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUpload, faTimes, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-content',
  imports: [TranslateModule, FontAwesomeModule, FormsModule],
  templateUrl: './content.component.html',
  styleUrl: './content.component.css',
})
export class ContentComponent {
  faUpload = faUpload;
  faTimes = faTimes;
  faTrash = faTrash;

  @ViewChild('titleRef') titleRef!: ElementRef<HTMLInputElement>;
  @ViewChild('descriptionRef') descriptionRef!: ElementRef<HTMLTextAreaElement>;
  @ViewChildren('contentRef') contentRef!: QueryList<ElementRef>;

  selectedLanguage: keyof ITranslated = 'ENGLISH';
  indexModule: number = -1;

  ContentType = ContentType;

  constructor(public trainingService: NewTrainingService) {
    this.trainingService.buttonSelectedLanguage$.subscribe((lang) => {
      this.selectedLanguage = lang;
    });

    this.indexModule = this.trainingService.editModuleIndexGet;
    if (this.indexModule == -1)
      this.indexModule = this.trainingService.newTraining.modules.length;
  }

  addTextField() {
    this.trainingService.newModule.content.push({
      id: -1,
      content_type: ContentType.TEXT,
      reference: { ENGLISH: '', DUTCH: '', FRENCH: '', GERMAN: '' },
    });
    this.imagePreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
    this.videoPreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
    this.trainingService.addVideoPreview();
    this.trainingService.addImagePreview();
  }

  addPictureField() {
    this.trainingService.newModule.content.push({
      id: -1,
      content_type: ContentType.PICTURE,
      reference: { ENGLISH: '', DUTCH: '', FRENCH: '', GERMAN: '' },
    });
    this.imagePreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
    this.videoPreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
    this.trainingService.addVideoPreview();
    this.trainingService.addImagePreview();
  }

  addVideoField() {
    this.trainingService.newModule.content.push({
      id: -1,
      content_type: ContentType.VIDEO,
      reference: { ENGLISH: '', DUTCH: '', FRENCH: '', GERMAN: '' },
    });
    this.imagePreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
    this.videoPreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
    this.trainingService.addVideoPreview();
    this.trainingService.addImagePreview();
  }

  imagePreviews: ITranslated[] = [];

  imageChange(index: number, event: any) {
    const file = event.target.files[0];

    if (file) {
      this.trainingService.newModule.content[index].reference[
        this.selectedLanguage
      ] = file;

      const reader = new FileReader();
      reader.onload = (event: any) => {
        if (this.indexModule != -1) {
          this.trainingService.imagePreviews[this.indexModule][index][
            this.selectedLanguage
          ] = event.target.result;
        } else {
          this.trainingService.imagePreviews[
            this.trainingService.newTraining.modules.length
          ][index][this.selectedLanguage] = event.target.result;
        }
      };
      reader.readAsDataURL(file);
    }
  }

  removeImage(index: number) {
    this.trainingService.newModule.content[index].reference[
      this.selectedLanguage
    ] = '';

    this.trainingService.imagePreviews[this.indexModule][index][
      this.selectedLanguage
    ] = '';
  }

  videoPreviews: ITranslated[] = [];

  videoChange(index: number, event: any) {
    const file = event.target.files[0];

    if (file) {
      this.trainingService.newModule.content[index].reference[
        this.selectedLanguage
      ] = file;

      const reader = new FileReader();
      reader.onload = (event: any) => {
        if (this.indexModule != -1) {
          this.trainingService.videoPreviews[this.indexModule][index][
            this.selectedLanguage
          ] = event.target.result;
        } else {
          this.trainingService.videoPreviews[
            this.trainingService.newTraining.modules.length
          ][index][this.selectedLanguage] = event.target.result;
        }
      };
      reader.readAsDataURL(file);
    }
  }

  removeVideo(index: number) {
    this.trainingService.newModule.content[index].reference[
      this.selectedLanguage
    ] = '';

    this.trainingService.videoPreviews[this.indexModule][index][
      this.selectedLanguage
    ] = '';
  }

  deleteContent(index: number) {
    this.trainingService.newModule.content.splice(index, 1);

    this.trainingService.videoPreviews[this.indexModule].splice(index, 1);
    this.trainingService.imagePreviews[this.indexModule].splice(index, 1);
  }
}
