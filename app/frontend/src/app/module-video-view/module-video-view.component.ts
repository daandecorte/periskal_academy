import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TrainingService } from '../services/training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { LanguageService } from '../services/language.service';

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

  moduleTitle: string = '';
  moduleDescription: string = '';
  videoUrl: string = '';
  currentLanguage: string = 'EN'; // Default language
  
  // Font Awesome icons
  faArrowLeft = faArrowLeft;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService,
    private languageService: LanguageService
  ) {}

  ngOnInit(): void {
    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = this.mapLanguageCode(language);
      // Reload module data when language changes to get the right video
      if (this.trainingId && this.moduleId) {
        this.loadModuleData();
      }
    });

    this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.moduleId = +params['sectionId'];
      this.loadModuleData();
    });
  }

  mapLanguageCode(language: any): string {
    const languageMappings: { [key: string]: string } = {
      'ENGLISH': 'EN',
      'FRENCH': 'FR',
      'DUTCH': 'NL',
      'GERMAN': 'DE'
    };

    if (['EN', 'FR', 'NL', 'DE'].includes(language)) {
      return language;
    }

    return languageMappings[language] || 'EN';
  }

  loadModuleData(): void {
    this.trainingService.getTrainingById(this.trainingId).subscribe(
      training => {
        if (training && training.modules) {
          // Find the specific module in the training modules
          const module = training.modules.find((m: any) => m.id === this.moduleId);
          
          if (module) {
            this.setModuleData(module);
          } else {
            console.error(`Module with ID ${this.moduleId} not found in training ${this.trainingId}`);
            this.goBackToOverview();
          }
        } else {
          console.error(`Training with ID ${this.trainingId} not found or has no modules`);
          this.goBackToOverview();
        }
      },
      error => {
        console.error('Error loading module data:', error);
        this.goBackToOverview();
      }
    );
  }

  setModuleData(module: any): void {
    // Set title based on current language
    this.moduleTitle = this.getLocalizedContent(module.title);
    this.moduleDescription = this.getLocalizedContent(module.description);
    
    // Set video URL
    if (module.videoReference) {
      this.videoUrl = this.getLocalizedContent(module.videoReference);
    }
    
    // Set question count for total steps if available?? idk
    if (module.questions && module.questions.length > 0) {
      this.totalSteps = module.questions.length + 1; // +1 for video step
    }
  }

  getLocalizedContent(contentMap: any): string {
    if (!contentMap) return '';
    
    // Try current language first
    if (contentMap[this.currentLanguage]) {
      return contentMap[this.currentLanguage];
    }
    
    // Fallback to English
    if (contentMap['EN']) {
      return contentMap['EN'];
    }
    
    // If none of the above, take the first available
    const values = Object.values(contentMap);
    return values.length > 0 ? values[0] as string : '';
  }

  goBackToOverview(): void {
    this.router.navigate(['/trainings', this.trainingId]);
  }

  continueToQuestions(): void {
    // TODO: questions
    // this.router.navigate(['/trainings', this.trainingId, 'module', this.moduleId, 'questions']);
  }
}