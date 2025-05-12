import {
  Component,
  ElementRef,
  ViewChildren,
  QueryList,
  ChangeDetectorRef,
} from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { NewTrainingService } from '../../new-training.service';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-questions',
  imports: [TranslateModule, FormsModule, FontAwesomeModule],
  templateUrl: './questions.component.html',
  styleUrl: './questions.component.css',
})
export class QuestionsComponent {
  faTrash = faTrash;

  selectedLanguage: keyof ITranslated = 'ENGLISH';
  @ViewChildren('questionRef') questionRef!: QueryList<
    ElementRef<HTMLTextAreaElement>
  >;
  @ViewChildren('answerRef') answerRef!: QueryList<
    ElementRef<HTMLInputElement>
  >;

  constructor(
    public trainingService: NewTrainingService,
    private cdr: ChangeDetectorRef
  ) {
    this.trainingService.buttonSelectedLanguage$.subscribe((lang) => {
      this.selectedLanguage = lang;

      setTimeout(() => {
        this.updateViewValues();
      });
    });

    if (this.trainingService.newModule.questions.length == 0) {
      this.questionAdd();
    }
  }

  ngAfterViewInit() {
    this.updateViewValues();
  }

  updateViewValues() {
    const questionArray = this.questionRef.toArray();
    for (let i = 0; i < questionArray.length; i++) {
      if (i < this.trainingService.newModule.questions.length) {
        questionArray[i].nativeElement.value =
          (this.trainingService.newModule.questions[i].text[
            this.selectedLanguage
          ] as string) || '';
      }
    }

    const answerArray = this.answerRef.toArray();
    let answerCount = 0;
    for (let i = 0; i < this.trainingService.newModule.questions.length; i++) {
      for (
        let j = 0;
        j < this.trainingService.newModule.questions[i].questionOptions.length;
        j++
      ) {
        if (answerCount < answerArray.length) {
          answerArray[answerCount].nativeElement.value =
            (this.trainingService.newModule.questions[i].questionOptions[j]
              .text[this.selectedLanguage] as string) || '';
          answerCount++;
        }
      }
    }
  }

  questionAdd() {
    this.trainingService.addModuleQuestion();
    this.addQuestionOption(this.trainingService.newModule.questions.length - 1);

    setTimeout(() => {
      this.updateViewValues();
    });
  }

  addQuestionOption(index: number) {
    this.trainingService.addModuleQuestionOption(index);

    setTimeout(() => {
      this.updateViewValues();
    });
  }

  deleteQuestionOption(indexQ: number, indexO: number) {
    this.trainingService.deleteModuleQuestionOption(indexQ, indexO);

    setTimeout(() => {
      this.updateViewValues();
    });
  }

  answerChange(idAnswer: number, idQuestion: number) {
    let idAnswerRef: number = this.searchAnswerRef(idAnswer, idQuestion);

    if (idAnswerRef >= 0 && idAnswerRef < this.answerRef.length) {
      this.trainingService.newModule.questions[idQuestion].questionOptions[
        idAnswer
      ].text[this.selectedLanguage] =
        this.answerRef.toArray()[idAnswerRef].nativeElement.value;
    }
  }

  searchAnswerRef(idAnswer: number, idQuestion: number) {
    let answerCount: number = 0;
    for (let i = 0; i < this.trainingService.newModule.questions.length; i++) {
      if (i == idQuestion) {
        return answerCount + idAnswer;
      }

      answerCount +=
        this.trainingService.newModule.questions[i].questionOptions.length;
    }

    return -1;
  }

  questionChange(idQuestion: number) {
    if (
      idQuestion >= 0 &&
      idQuestion < this.trainingService.newModule.questions.length &&
      idQuestion < this.questionRef.length
    ) {
      this.trainingService.newModule.questions[idQuestion].text[
        this.selectedLanguage
      ] = this.questionRef.toArray()[idQuestion].nativeElement.value;
    }
  }

  deleteQuestion(idQuestion: number) {
    if (
      idQuestion >= 0 &&
      idQuestion < this.trainingService.newModule.questions.length
    ) {
      this.trainingService.newModule.questions.splice(idQuestion, 1);

      setTimeout(() => {
        this.updateViewValues();
        this.cdr.detectChanges();
      });
    }
  }

  answerChoose(indexQ: number, indexO: number) {
    const questionOptions =
      this.trainingService.newModule.questions[indexQ].questionOptions;
    questionOptions.forEach((option) => {
      option.is_correct = false;
    });
    questionOptions[indexO].is_correct = true;
  }
}

interface ITranslated {
  ENGLISH: string | File;
  FRENCH: string | File;
  DUTCH: string | File;
  GERMAN: string | File;
}
