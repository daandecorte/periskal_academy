import {
  Component,
  ElementRef,
  ViewChild,
  ViewChildren,
  QueryList,
  ChangeDetectorRef,
} from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { NewTrainingService } from '../new-training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-exam',
  imports: [TranslateModule, FontAwesomeModule, FormsModule],
  templateUrl: './exam.component.html',
  styleUrl: './exam.component.css',
})
export class ExamComponent {
  faTrash = faTrash;

  selectedButtonLanguage: keyof ITranslated = 'ENGLISH';

  @ViewChild('timeRef') timeRef!: ElementRef<HTMLInputElement>;
  @ViewChild('questionsPerExamRef')
  questionsPerExamRef!: ElementRef<HTMLInputElement>;
  @ViewChild('passingScoreRef') passingScoreRef!: ElementRef<HTMLInputElement>;
  @ViewChild('retryAttemptsRef')
  retryAttemptsRef!: ElementRef<HTMLInputElement>;

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
    if (this.trainingService.newTraining.exam.questions.length == 0) {
      this.questionAdd();
    }
  }

  ngAfterViewInit() {
    const buttonsLanguage =
      document.querySelectorAll<HTMLButtonElement>('.modal-button');

    buttonsLanguage.forEach((button) => {
      button.addEventListener('click', () => {
        buttonsLanguage.forEach((btn) => btn.classList.remove('selected'));

        button.classList.add('selected');

        this.changeInputLanguage();
      });
    });

    this.changeInputLanguage();

    this.timeRef.nativeElement.value = `${this.trainingService.newTraining.exam.time}`;
    this.questionsPerExamRef.nativeElement.value = `${this.trainingService.newTraining.exam.questionAmount}`;
    this.passingScoreRef.nativeElement.value = `${this.trainingService.newTraining.exam.passingScore}`;
    this.retryAttemptsRef.nativeElement.value = `${this.trainingService.newTraining.exam.maxAttempts}`;
  }

  changeInputLanguage() {
    const buttonLanguage =
      document.querySelector<HTMLButtonElement>('.selected');
    if (!buttonLanguage) return;

    this.selectedButtonLanguage = buttonLanguage.value as keyof ITranslated;

    this.updateViewValues();
  }

  updateViewValues() {
    const questionArray = this.questionRef.toArray();
    for (let i = 0; i < questionArray.length; i++) {
      if (i < this.trainingService.newTraining.exam.questions.length) {
        questionArray[i].nativeElement.value =
          (this.trainingService.newTraining.exam.questions[i].text[
            this.selectedButtonLanguage
          ] as string) || '';
      }
    }

    const answerArray = this.answerRef.toArray();
    let answerCount = 0;
    for (
      let i = 0;
      i < this.trainingService.newTraining.exam.questions.length;
      i++
    ) {
      for (
        let j = 0;
        j <
        this.trainingService.newTraining.exam.questions[i].questionOptions
          .length;
        j++
      ) {
        if (answerCount < answerArray.length) {
          answerArray[answerCount].nativeElement.value =
            (this.trainingService.newTraining.exam.questions[i].questionOptions[
              j
            ].text[this.selectedButtonLanguage] as string) || '';
          answerCount++;
        }
      }
    }
  }

  changeTimeLimit() {
    this.trainingService.newTraining.exam.time = Number(
      this.timeRef.nativeElement.value
    );
  }

  changeQuestionsPerExam() {
    this.trainingService.newTraining.exam.questionAmount = Number(
      this.questionsPerExamRef.nativeElement.value
    );
  }

  changePassingScore() {
    let passingScore = this.passingScoreRef.nativeElement.value;

    if (Number(passingScore) < 0) {
      passingScore = '0';
    } else if (Number(passingScore) > 100) {
      passingScore = '100';
    }

    this.trainingService.newTraining.exam.passingScore = Number(passingScore);
  }

  changeRetryAttempts() {
    this.trainingService.newTraining.exam.maxAttempts = Number(
      this.retryAttemptsRef.nativeElement.value
    );
  }

  questionAdd() {
    this.trainingService.addExamQuestion();
    this.addQuestionOption(
      this.trainingService.newTraining.exam.questions.length - 1
    );

    setTimeout(() => {
      this.updateViewValues();
    });
  }

  addQuestionOption(index: number) {
    this.trainingService.addExamQuestionOption(index);

    setTimeout(() => {
      this.updateViewValues();
    });
  }

  deleteQuestionOption(indexQ: number, indexO: number) {
    this.trainingService.deleteExamQuestionOption(indexQ, indexO);

    setTimeout(() => {
      this.updateViewValues();
    });
  }

  answerChange(idQuestion: number, idAnswer: number) {
    let idAnswerRef: number = this.searchAnswerRef(idAnswer, idQuestion);

    if (idAnswerRef >= 0 && idAnswerRef < this.answerRef.length) {
      this.trainingService.newTraining.exam.questions[
        idQuestion
      ].questionOptions[idAnswer].text[this.selectedButtonLanguage] =
        this.answerRef.toArray()[idAnswerRef].nativeElement.value;
    }
  }

  searchAnswerRef(idAnswer: number, idQuestion: number) {
    let answerCount: number = 0;
    for (
      let i = 0;
      i < this.trainingService.newTraining.exam.questions.length;
      i++
    ) {
      if (i == idQuestion) {
        return answerCount + idAnswer;
      }

      answerCount +=
        this.trainingService.newTraining.exam.questions[i].questionOptions
          .length;
    }

    return -1;
  }

  questionChange(idQuestion: number) {
    if (
      idQuestion >= 0 &&
      idQuestion < this.trainingService.newTraining.exam.questions.length &&
      idQuestion < this.questionRef.length
    ) {
      this.trainingService.newTraining.exam.questions[idQuestion].text[
        this.selectedButtonLanguage
      ] = this.questionRef.toArray()[idQuestion].nativeElement.value;
    }
  }

  deleteQuestion(idQuestion: number) {
    if (
      idQuestion >= 0 &&
      idQuestion < this.trainingService.newTraining.exam.questions.length
    ) {
      this.trainingService.newTraining.exam.questions.splice(idQuestion, 1);

      setTimeout(() => {
        this.updateViewValues();
        this.cdr.detectChanges();
      });
    }
  }

  answerChoose(indexQ: number, indexO: number) {
    const questionOptions =
      this.trainingService.newTraining.exam.questions[indexQ].questionOptions;
    questionOptions.forEach((option) => {
      option.is_correct = false;
    });
    questionOptions[indexO].is_correct = true;
  }
}

interface ITranslated {
  ENGLISH: string;
  FRENCH: string;
  DUTCH: string;
  GERMAN: string;
}
