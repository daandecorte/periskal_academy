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
      let result = await fetch(`/api/trainings`, {
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
}
