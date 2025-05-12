import {
  Component,
  ElementRef,
  ViewChild,
  ViewChildren,
  QueryList,
} from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { NewTrainingService } from '../../new-training.service';
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

  ContentType = ContentType;

  constructor(public trainingService: NewTrainingService) {
    this.trainingService.buttonSelectedLanguage$.subscribe((lang) => {
      this.selectedLanguage = lang;
    });
  }

  addTextField() {
    this.trainingService.newModule.content.push({
      contentType: ContentType.TEXT,
      reference: { ENGLISH: '', DUTCH: '', FRENCH: '', GERMAN: '' },
    });
    this.imagePreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
    this.videoPreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
  }

  addPictureField() {
    this.trainingService.newModule.content.push({
      contentType: ContentType.PICTURE,
      reference: { ENGLISH: '', DUTCH: '', FRENCH: '', GERMAN: '' },
    });
    this.imagePreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
    this.videoPreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
  }

  addVideoField() {
    this.trainingService.newModule.content.push({
      contentType: ContentType.VIDEO,
      reference: { ENGLISH: '', DUTCH: '', FRENCH: '', GERMAN: '' },
    });
    this.imagePreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
    this.videoPreviews.push({ ENGLISH: '', DUTCH: '', GERMAN: '', FRENCH: '' });
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
        this.imagePreviews[index][this.selectedLanguage] = event.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  removeImage(index: number) {
    this.trainingService.newModule.content[index].reference[
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
        this.videoPreviews[index][this.selectedLanguage] = event.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  removeVideo(index: number) {
    this.trainingService.newModule.content[index].reference[
      this.selectedLanguage
    ] = '';

    console.log('here');
  }

  deleteContent(index: number) {
    this.trainingService.newModule.content.splice(index, 1);
  }
}

interface ITranslated {
  ENGLISH: string | File;
  FRENCH: string | File;
  DUTCH: string | File;
  GERMAN: string | File;
}

enum ContentType {
  TEXT,
  PICTURE,
  VIDEO,
}
