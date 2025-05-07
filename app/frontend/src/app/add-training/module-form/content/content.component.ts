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
import { faUpload, faTimes } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-content',
  imports: [TranslateModule, FontAwesomeModule],
  templateUrl: './content.component.html',
  styleUrl: './content.component.css',
})
export class ContentComponent {
  faUpload = faUpload;
  faTimes = faTimes;

  @ViewChild('titleRef') titleRef!: ElementRef<HTMLInputElement>;
  @ViewChild('descriptionRef') descriptionRef!: ElementRef<HTMLTextAreaElement>;
  @ViewChildren('contentRef') contentRef!: QueryList<ElementRef>;

  selectedImagePreview: ITranslated[] = [];
  selectedVideoPreview: ITranslated[] = [];

  selectedLanguage: keyof ITranslated = 'ENGLISH';

  ContentType = ContentType;

  constructor(public trainingService: NewTrainingService) {
    this.trainingService.buttonSelectedLanguage$.subscribe((lang) => {
      this.selectedLanguage = lang;

      this.ngAfterViewInit();
    });
  }

  ngAfterViewInit() {
    this.titleRef.nativeElement.value =
      this.trainingService.newModule.title[this.selectedLanguage];
    this.descriptionRef.nativeElement.value =
      this.trainingService.newModule.description[this.selectedLanguage];

    const contentArray = this.contentRef.toArray();

    for (let i = 0; i < contentArray.length; i++) {
      contentArray[i].nativeElement.value =
        this.trainingService.newModule.content[i].reference[
          this.selectedLanguage
        ];
    }
  }

  titleChange() {
    this.trainingService.newModule.title[this.selectedLanguage] =
      this.titleRef.nativeElement.value;
  }

  descriptionChange() {
    this.trainingService.newModule.description[this.selectedLanguage] =
      this.descriptionRef.nativeElement.value;
  }

  addTextField() {
    this.trainingService.newModule.content.push({
      contentType: ContentType.TEXT,
      reference: { ENGLISH: '', DUTCH: '', FRENCH: '', GERMAN: '' },
    });

    this.selectedImagePreview.push({
      ENGLISH: '',
      DUTCH: '',
      FRENCH: '',
      GERMAN: '',
    });

    this.selectedVideoPreview.push({
      ENGLISH: '',
      DUTCH: '',
      FRENCH: '',
      GERMAN: '',
    });
  }

  addPictureField() {
    this.trainingService.newModule.content.push({
      contentType: ContentType.PICTURE,
      reference: { ENGLISH: '', DUTCH: '', FRENCH: '', GERMAN: '' },
    });

    this.selectedImagePreview.push({
      ENGLISH: '',
      DUTCH: '',
      FRENCH: '',
      GERMAN: '',
    });

    this.selectedVideoPreview.push({
      ENGLISH: '',
      DUTCH: '',
      FRENCH: '',
      GERMAN: '',
    });
  }

  addVideoField() {
    this.trainingService.newModule.content.push({
      contentType: ContentType.VIDEO,
      reference: { ENGLISH: '', DUTCH: '', FRENCH: '', GERMAN: '' },
    });

    this.selectedImagePreview.push({
      ENGLISH: '',
      DUTCH: '',
      FRENCH: '',
      GERMAN: '',
    });

    this.selectedVideoPreview.push({
      ENGLISH: '',
      DUTCH: '',
      FRENCH: '',
      GERMAN: '',
    });
  }

  textChange(index: number) {
    const contentArray = this.contentRef.toArray();
    const el = contentArray[index].nativeElement as HTMLTextAreaElement;

    this.trainingService.newModule.content[index].reference[
      this.selectedLanguage
    ] = el.value;
  }

  imageChange(index: number, event: any) {
    const file = event.target.files[0];

    if (file) {
      const reader = new FileReader();
      reader.onload = (event: any) => {
        this.selectedImagePreview[index][this.selectedLanguage] =
          event.target.result;

        this.trainingService.newModule.content[index].reference[
          this.selectedLanguage
        ] = event.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  removeImage(index: number) {
    this.selectedImagePreview[index][this.selectedLanguage] = '';

    this.trainingService.newModule.content[index].reference[
      this.selectedLanguage
    ] = '';
  }

  videoChange(index: number, event: any) {
    const file = event.target.files[0];

    if (file) {
      const reader = new FileReader();
      reader.onload = (event: any) => {
        this.selectedVideoPreview[index][this.selectedLanguage] =
          event.target.result;

        this.trainingService.newModule.content[index].reference[
          this.selectedLanguage
        ] = event.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  removeVideo(index: number) {
    this.selectedVideoPreview[index][this.selectedLanguage] = '';

    this.trainingService.newModule.content[index].reference[
      this.selectedLanguage
    ] = '';
  }

  videoUrl: string = '';
  uploadVideo() {
    const fileInput = document.getElementById('videoInput') as HTMLInputElement;
    if (!fileInput || !fileInput.files || fileInput.files.length === 0) {
      alert('Please select a video file.');
      return;
    }
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    fetch('/api/upload', {
      method: 'POST',
      body: formData,
    })
      .then((response) => response.text())
      .then((message) => {
        this.videoUrl = message;
        alert('Video uploaded successfully');
        console.log(this.videoUrl);
      })
      .catch((error) => alert('Upload failed: ' + error));
  }
}

interface ITranslated {
  ENGLISH: string;
  FRENCH: string;
  DUTCH: string;
  GERMAN: string;
}

enum ContentType {
  TEXT,
  PICTURE,
  VIDEO,
}
