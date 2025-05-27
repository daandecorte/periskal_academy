import { Component } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import { NewTrainingService, ITranslated } from '../new-training.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-preview',
  imports: [TranslateModule, FontAwesomeModule, FormsModule],
  templateUrl: './preview.component.html',
  styleUrl: './preview.component.css',
})
export class PreviewComponent {
  faLock = faLock;

  basicTrainingReady: boolean = false;
  modulesReady: boolean = false;
  examReady: boolean = false;

  sliderActive: boolean= true;

  constructor(public trainingService: NewTrainingService) {
    this.checkBasicTraining();
    this.checkModules();
    this.checkExam();
  }
  ngOnInit() {
    this.isActive();
  }

  checkBasicTraining() {
    const training = this.trainingService.newTraining;

    this.basicTrainingReady =
      this.isTranslatedFilled(training.title) &&
      this.isTranslatedFilled(training.description);
  }

  checkModules() {
    const modules = this.trainingService.newTraining.modules;

    if (modules.length == 0) return;

    for (let i = 0; i < modules.length; i++) {
      if (!this.isTranslatedFilled(modules[i].title)) return;
      if (!this.isTranslatedFilled(modules[i].description)) return;
      //Logic for checking module content

      const questions = modules[i].questions;
      for (let j = 0; j < questions.length; j++) {
        if (!this.isTranslatedFilled(questions[j].text)) return;

        const questionOptions = questions[j].question_options;
        for (let k = 0; k < questionOptions.length; k++) {
          if (!this.isTranslatedFilled(questionOptions[k].text)) return;
        }
      }
    }

    this.modulesReady = true;
  }

  checkExam() {
    const exam = this.trainingService.newTraining.exam;

    if (exam.time == 0) return;
    if (exam.question_amount > exam.questions.length) return;

    const questions = exam.questions;
    for (let j = 0; j < questions.length; j++) {
      if (!this.isTranslatedFilled(questions[j].text)) return;

      const questionOptions = questions[j].question_options;
      for (let k = 0; k < questionOptions.length; k++) {
        if (!this.isTranslatedFilled(questionOptions[k].text)) return;
      }
    }

    this.examReady = true;
  }

  private isTranslatedFilled(translated: ITranslated): boolean {
    return Object.values(translated).every((value) => value.trim() !== '');
  }

  //isActive: boolean = false;

  get allReady(): boolean {
    return this.basicTrainingReady && this.modulesReady && this.examReady;
  }
  isActive() {
    if(!this.allReady) {
      this.trainingService.newTraining.active=false;
    }
  }
}
