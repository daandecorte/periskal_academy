import { Component, OnInit, ViewChild, ElementRef, OnDestroy, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Module, TrainingService } from '../services/training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { LanguageService } from '../services/language.service';
import { Subscription } from 'rxjs';
import { ContentType } from '../services/training.service';

@Component({
  selector: 'app-module-video-view',
  imports: [CommonModule, RouterModule, FontAwesomeModule, TranslateModule],
  templateUrl: './module-video-view.component.html',
  styleUrl: './module-video-view.component.css'
})
export class ModuleVideoViewComponent {
  trainingId: number = 0;
  moduleId: number = 0;
  currentStep: number = 1;
  totalSteps: number = 1; 

  zoomImageId = -1;
  isZoomed: boolean= false;
  
  public module: Module = {
    id: 0,
    title: {},
    description: {},
    questions: [],
    content: []
  };
  
  faArrowLeft = faArrowLeft;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService,
    public languageService: LanguageService
  ) {}

  ngOnInit(): void {
    const routeSub = this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.moduleId = +params['sectionId'];
      this.loadModuleData();
    });
  }
  zoomImage(index: number) {
    this.zoomImageId = index;
    this.isZoomed=false;
  }
  toggleZoom(event: MouseEvent) {
    event.stopPropagation();
    this.isZoomed = !this.isZoomed;
  }
  async loadModuleData() {
    let response = await fetch(`/api/modules/${this.moduleId}`);
    if(response.status!=200) {
      let errorText = await response.text
      console.log("error fetching module" + errorText);
    }
    else {
      let json = await response.json();
      this.module = await json;
      this.totalSteps += this.module.questions.length;
    }
  }
  
  getProgressPercentage(): number {
    if (this.totalSteps === 0) return 0;
    return ((this.currentStep) / this.totalSteps * 100);
  }

  getStepIndicatorText(): string {
    return `${this.currentStep} of ${this.totalSteps}`;
  }

  goBackToOverview(): void {
    this.router.navigate(['/trainings', this.trainingId]);
  }

  continueToQuestions(): void {
    this.router.navigate(['/trainings', this.trainingId, 'module', this.moduleId, 'questions']);
  }
  disableRightClick(event: MouseEvent): void {
  event.preventDefault();
}
}