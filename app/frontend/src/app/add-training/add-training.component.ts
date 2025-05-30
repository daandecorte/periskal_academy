import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  Router,
  RouterOutlet,
} from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { filter, Subscription } from 'rxjs';
import {
  NewTrainingService,
  ITranslated,
  ContentType,
} from './new-training.service';
import { BasicSetupComponent } from './basic-setup/basic-setup.component';
import { ModulesComponent } from './modules/modules.component';
import { ExamComponent } from './exam/exam.component';
import { PreviewComponent } from './preview/preview.component';

@Component({
  selector: 'app-add-training',
  imports: [
    RouterOutlet,
    CommonModule,
    TranslateModule,
    BasicSetupComponent,
    ModulesComponent,
    ExamComponent,
    PreviewComponent,
  ],
  templateUrl: './add-training.component.html',
  styleUrl: './add-training.component.css',
})
export class AddTrainingComponent {
  private routerSubscription!: Subscription;

  paths = ['basic-setup', 'modules', 'exam', 'preview'];

  ContentType = ContentType;

  constructor(
    private router: Router,
    public trainingService: NewTrainingService
  ) {}

  goToNextStep() {
    this.trainingService.currentIndex++;
  }

  goToPreviousStep() {
    this.trainingService.currentIndex--;
  }

  async publishTraining() {
    this.showLoadingModal();

    if (this.trainingService.newTraining.id == -1) this.publishNewTraining();
    else this.publishEditedTraining();

    this.hideLoadingModal();
    this.router.navigate(['/admin/trainings']);
  }

  async publishEditedTraining() {
    try {
      let trainingId = this.trainingService.newTraining.id;

      //PUT Training
      await fetch(`/api/trainings/${trainingId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          title: this.trainingService.newTraining.title,
          description: this.trainingService.newTraining.description,
          is_active: this.trainingService.newTraining.active,
        }),
      });

      //PUT Exam
      let resultExam = await fetch(
        `/api/exams/${this.trainingService.newTraining.exam.id}`,
        {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
          },
          body: JSON.stringify({
            passing_score: this.trainingService.newTraining.exam.passing_score,
            max_attempts: this.trainingService.newTraining.exam.max_attempts,
            time: this.trainingService.newTraining.exam.time,
            question_amount:
              this.trainingService.newTraining.exam.question_amount,
          }),
        }
      );

      let dataExam = await resultExam.json();
      let examId = dataExam.id;

      //DELETE Questions Exam
      await fetch(`/api/exams/${examId}/questions`, {
        method: 'DELETE',
      });

      //POST Questions Exam
      for (
        let i = 0;
        i < this.trainingService.newTraining.exam.questions.length;
        i++
      ) {
        let questionOptions: IQuestionOptionPost[] = [];
        for (
          let j = 0;
          j <
          this.trainingService.newTraining.exam.questions[i].question_options
            .length;
          j++
        ) {
          questionOptions.push({
            text: this.trainingService.newTraining.exam.questions[i]
              .question_options[j].text,
            correct:
              this.trainingService.newTraining.exam.questions[i]
                .question_options[j].correct,
          });
        }

        await fetch(`/api/exams/${examId}/questions`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
          },
          body: JSON.stringify({
            text: this.trainingService.newTraining.exam.questions[i].text,
            question_options: questionOptions,
          }),
        });
      }

      //PUT Certificate
      await fetch(
        `/api/certificates/${this.trainingService.newTraining.certificate.id}`,
        {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
          },
          body: JSON.stringify({
            validity_period:
              this.trainingService.newTraining.certificate.validity_period,
            price: this.trainingService.newTraining.certificate.price,
          }),
        }
      );

      //DELETE MODULES
      for (
        let i = 0;
        i < this.trainingService.removedModulesIndex.length;
        i++
      ) {
        await fetch(
          `/api/modules/${this.trainingService.removedModulesIndex[i]}`,
          { method: 'DELETE' }
        );
      }

      //PUT and POST Modules
      const modules = this.trainingService.newTraining.modules;
      for (let i = 0; i < modules.length; i++) {
        let resultModules;
        if (modules[i].id == -1) {
          resultModules = await fetch(`/api/modules`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Accept: 'application/json',
            },
            body: JSON.stringify({
              title: modules[i].title,
              description: modules[i].description,
              training_id: trainingId,
            }),
          });
        } else {
          resultModules = await fetch(`/api/modules/${modules[i].id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              Accept: 'application/json',
            },
            body: JSON.stringify({
              title: modules[i].title,
              description: modules[i].description,
              training_id: trainingId,
            }),
          });
        }

        let dataModules = await resultModules.json();
        let moduleId = dataModules.id;

        for (
          let j = 0;
          j < this.trainingService.removedContentsIndex.length;
          j++
        ) {
          await fetch(
            `/api/modules/${moduleId}/content/${this.trainingService.removedContentsIndex[j]}`,
            { method: 'DELETE' }
          );
        }

        for (let j = 0; j < modules[i].content.length; j++) {
          if (modules[i].content[j].content_type != ContentType.TEXT) {
            await this.uploadVideo(modules[i].content[j].reference, i, j);
          }

          if (modules[i].content[j].id == -1) {
            await fetch(`/api/modules/${moduleId}/content`, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
              },
              body: JSON.stringify({
                content_type: modules[i].content[j].content_type,
                reference: modules[i].content[j].reference,
              }),
            });
          } else {
            await fetch(
              `/api/modules/${moduleId}/content/${modules[i].content[j].id}`,
              {
                method: 'PUT',
                headers: {
                  'Content-Type': 'application/json',
                  Accept: 'application/json',
                },
                body: JSON.stringify({
                  content_type: modules[i].content[j].content_type,
                  reference: modules[i].content[j].reference,
                }),
              }
            );
          }
        }

        //DELETE Questions Module
        await fetch(`/api/modules/${moduleId}/questions`, {
          method: 'DELETE',
        });

        for (let j = 0; j < modules[i].questions.length; j++) {
          let questionOptions: IQuestionOptionPost[] = [];
          for (
            let k = 0;
            k < modules[i].questions[j].question_options.length;
            k++
          )
            questionOptions.push({
              text: modules[i].questions[j].question_options[k].text,
              correct: modules[i].questions[j].question_options[k].correct,
            });

          await fetch(`/api/modules/${moduleId}/questions`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Accept: 'application/json',
            },
            body: JSON.stringify({
              text: modules[i].questions[j].text,
              question_options: questionOptions,
            }),
          });
        }
      }

      //POST Certificate
      await fetch(
        `/api/certificates/${this.trainingService.newTraining.certificate.id}`,
        {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
          },
          body: JSON.stringify({
            training_id: trainingId,
            validity_period:
              this.trainingService.newTraining.certificate.validity_period,
            price: this.trainingService.newTraining.certificate.price,
          }),
        }
      );

      this.resetTraining();
    } catch (error) {
      console.error(error);
    }
  }

  async publishNewTraining() {
    try {
      //POST Training
      let resultTraining = await fetch(`/api/trainings`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          title: this.trainingService.newTraining.title,
          description: this.trainingService.newTraining.description,
          is_active: this.trainingService.newTraining.active,
        }),
      });

      let dataTraining = await resultTraining.json();
      let trainingId = dataTraining.id;

      //POST Exam
      let resultExam = await fetch(`/api/exams`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          passing_score: this.trainingService.newTraining.exam.passing_score,
          max_attempts: this.trainingService.newTraining.exam.max_attempts,
          time: this.trainingService.newTraining.exam.time,
          question_amount:
            this.trainingService.newTraining.exam.question_amount,
          training_id: trainingId,
        }),
      });

      let dataExam = await resultExam.json();
      let examId = dataExam.id;

      //POST Questions Exam
      for (
        let i = 0;
        i < this.trainingService.newTraining.exam.questions.length;
        i++
      ) {
        let questionOptions: IQuestionOptionPost[] = [];
        for (
          let j = 0;
          j <
          this.trainingService.newTraining.exam.questions[i].question_options
            .length;
          j++
        ) {
          questionOptions.push({
            text: this.trainingService.newTraining.exam.questions[i]
              .question_options[j].text,
            correct:
              this.trainingService.newTraining.exam.questions[i]
                .question_options[j].correct,
          });
        }

        await fetch(`/api/exams/${examId}/questions`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
          },
          body: JSON.stringify({
            text: this.trainingService.newTraining.exam.questions[i].text,
            question_options: questionOptions,
          }),
        });
      }

      //POST Certificate
      await fetch(`/api/certificates`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          training_id: trainingId,
          validity_period:
            this.trainingService.newTraining.certificate.validity_period,
          price: this.trainingService.newTraining.certificate.price,
        }),
      });

      //POST Modules
      const modules = this.trainingService.newTraining.modules;
      for (let i = 0; i < modules.length; i++) {
        let resultModules = await fetch(`/api/modules`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
          },
          body: JSON.stringify({
            title: modules[i].title,
            description: modules[i].description,
            training_id: trainingId,
          }),
        });

        let dataModules = await resultModules.json();
        let moduleId = dataModules.id;

        for (let j = 0; j < modules[i].content.length; j++) {
          if (modules[i].content[j].content_type != ContentType.TEXT) {
            await this.uploadVideo(modules[i].content[j].reference, i, j);
          }

          await fetch(`/api/modules/${moduleId}/content`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Accept: 'application/json',
            },
            body: JSON.stringify({
              content_type: modules[i].content[j].content_type,
              reference: modules[i].content[j].reference,
            }),
          });
        }

        for (let j = 0; j < modules[i].questions.length; j++) {
          let questionOptions: IQuestionOptionPost[] = [];
          for (
            let k = 0;
            k < modules[i].questions[j].question_options.length;
            k++
          )
            questionOptions.push({
              text: modules[i].questions[j].question_options[k].text,
              correct: modules[i].questions[j].question_options[k].correct,
            });

          await fetch(`/api/modules/${moduleId}/questions`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Accept: 'application/json',
            },
            body: JSON.stringify({
              text: modules[i].questions[j].text,
              question_options: questionOptions,
            }),
          });
        }
      }

      this.resetTraining();
    } catch (error) {
      console.error(error);
    }
  }

  resetTraining() {
    this.trainingService.newTraining = {
      id: -1,
      title: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      description: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      active: false,
      modules: [],
      exam: {
        id: -1,
        passing_score: 0,
        max_attempts: 0,
        time: 0,
        question_amount: 0,
        questions: [],
      },
      certificate: {
        id: -1,
        validity_period: 1,
        price: 0,
      },
    };
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    this.trainingService.currentIndex=0;
  }

  async uploadVideo(files: ITranslated, indexM: number, indexC: number) {
    const languages: (keyof ITranslated)[] = [
      'ENGLISH',
      'FRENCH',
      'DUTCH',
      'GERMAN',
    ];

    for (const lang of languages) {
      const file = files[lang];
      if (!(file instanceof File)) continue;

      const formData = new FormData();
      formData.append('file', file);

      try {
        console.log(`Uploading ${lang} file:`, {
          name: file.name,
          type: file.type,
        });

        const response = await fetch('/api/upload', {
          method: 'POST',
          body: formData,
        });

        if (!response.ok) {
          throw new Error(
            `Server responded with ${response.status}: ${response.statusText}`
          );
        }

        const message = await response.text();

        if (!message.startsWith('https://')) {
          console.error(`Invalid response for ${lang}:`, message);
          throw new Error(`Upload failed: ${message}`);
        }

        this.trainingService.newTraining.modules[indexM].content[
          indexC
        ].reference[lang] = message;
        console.log(`Uploaded ${lang}:`, message);
      } catch (error) {
        console.error(`Error uploading ${lang} file:`, error);
        alert(`Upload failed for ${lang}: ${error}`);
      }
    }
  }

  showLoadingModal() {
    document.getElementById('loadingModal')!.classList.remove('hidden');
  }

  hideLoadingModal() {
    document.getElementById('loadingModal')!.classList.add('hidden');
  }
}

interface IQuestionOptionPost {
  text: ITranslated;
  correct: boolean;
}
