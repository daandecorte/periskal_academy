import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TrainingService } from '../services/training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-module-video-view',
  imports: [CommonModule, RouterModule, FontAwesomeModule, TranslateModule],
  templateUrl: './module-video-view.component.html',
  styleUrl: './module-video-view.component.css'
})
export class ModuleVideoViewComponent implements OnInit {
  trainingId: number = 0;
  moduleId: number = 0;
  currentStep: number = 1;
  totalSteps: number = 5;
  
  moduleTitle: string = 'Basic Navigation Safety';
  moduleDescription: string = 'Understanding fundamental navigation protocols';
  
  // Font Awesome icons
  faArrowLeft = faArrowLeft;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.moduleId = +params['sectionId'];
      this.loadModuleData();
    });
  }

  loadModuleData(): void {
    // TODO: actual data loading
  }

  goBackToOverview(): void {
    this.router.navigate(['/trainings', this.trainingId]);
  }

  continueToQuestions(): void {
    // TODO: questions
    // this.router.navigate(['/trainings', this.trainingId, 'module', this.moduleId, 'questions']);
  }
}