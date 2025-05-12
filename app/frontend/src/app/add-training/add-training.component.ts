import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  Router,
  RouterLink,
  RouterLinkActive,
  RouterModule,
  RouterOutlet,
} from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { filter, Subscription } from 'rxjs';
import { NewTrainingService } from './new-training.service';

@Component({
  selector: 'app-add-training',
  imports: [RouterOutlet, CommonModule, TranslateModule],
  templateUrl: './add-training.component.html',
  styleUrl: './add-training.component.css',
})
export class AddTrainingComponent {
  steps = ['basic-setup', 'modules', 'exam', 'preview'];
  currentStep: string = this.steps[0];
  private routerSubscription!: Subscription;

  ContentType = ContentType;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private trainingService: NewTrainingService
  ) {}

  ngOnInit() {
    this.routerSubscription = this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        const currentPath =
          this.route.snapshot.firstChild?.url[0]?.path || this.steps[0];
        if (this.steps.includes(currentPath)) {
          this.currentStep = currentPath;
        }
      });
  }

  goToNextStep() {
    const currentIndex = this.steps.indexOf(this.currentStep);
    if (currentIndex < this.steps.length - 1) {
      this.currentStep = this.steps[currentIndex + 1];
      this.router.navigate([`/add-training/${this.currentStep}`]);
    }
  }

  goToPreviousStep() {
    const currentIndex = this.steps.indexOf(this.currentStep);
    if (currentIndex > 0) {
      this.currentStep = this.steps[currentIndex - 1];
      this.router.navigate([`/add-training/${this.currentStep}`]);
    }
  }

  async publishTraining() {
    //Logic for uploading to database
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
          isActive: this.trainingService.newTraining.isActive,
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
          passing_score: this.trainingService.newTraining.exam.passingScore,
          maxAttempts: this.trainingService.newTraining.exam.maxAttempts,
          time: this.trainingService.newTraining.exam.time,
          questionAmount: this.trainingService.newTraining.exam.questionAmount,
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
        await fetch(`/api/exams/${examId}/questions`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
          },
          body: JSON.stringify({
            text: this.trainingService.newTraining.exam.questions[i].text,
            question_options:
              this.trainingService.newTraining.exam.questions[i]
                .questionOptions,
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
            this.trainingService.newTraining.certificate.validityPeriod,
          price: this.trainingService.newTraining.certificate.price,
        }),
      });

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
          if (modules[i].content[j].contentType != ContentType.TEXT) {
            await this.uploadVideo(modules[i].content[j].reference, i, j);
          }

          await fetch(`/api/modules/${moduleId}/content`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Accept: 'application/json',
            },
            body: JSON.stringify({
              content_type: modules[i].content[j].contentType,
              reference: modules[i].content[j].reference,
            }),
          });
        }
      }

      this.resetTraining();
    } catch (error) {
      console.error(error);
    }

    this.router.navigate(['/trainings']);
  }

  resetTraining() {
    this.trainingService.newTraining = {
      title: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      description: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      isActive: false,
      modules: [],
      exam: {
        passingScore: 0,
        maxAttempts: 0,
        time: 0,
        questionAmount: 0,
        questions: [],
      },
      certificate: {
        validityPeriod: 1,
        price: 0,
      },
    };
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  // Fix for the uploadVideo method in ContentComponent
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
