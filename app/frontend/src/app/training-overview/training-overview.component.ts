import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TrainingService, Training, Module } from '../services/training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { 
  faPlayCircle, 
  faCheckCircle, 
  faBook, 
  faClock, 
  faQuestionCircle,
  faArrowLeft,
  faCertificate,
  faExclamationTriangle
} from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { LanguageService } from '../services/language.service';

interface ModuleSection {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  duration: any;
  questionCount: number;
}

@Component({
  selector: 'app-training-overview',
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule, TranslateModule],
  templateUrl: './training-overview.component.html',
  styleUrl: './training-overview.component.css'
})
export class TrainingOverviewComponent implements OnInit {
  trainingId: number = 0;
  training?: Training;
  moduleSections: ModuleSection[] = [];
  trainingsCompleted: number = 0;
  totalTrainings: number = 0;
  currentLanguage: string = 'EN'; // Default language
  
  // Font Awesome icons
  faPlayCircle = faPlayCircle;
  faCheckCircle = faCheckCircle;
  faBook = faBook;
  faClock = faClock;
  faQuestionCircle = faQuestionCircle;
  faArrowLeft = faArrowLeft;
  faCertificate = faCertificate;
  faExclamationTriangle = faExclamationTriangle;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService,
    private languageService: LanguageService
  ) {}

  ngOnInit(): void {
    // Subscribe to language changes
    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = this.mapLanguageCode(language);
      // Refresh content when language changes
      if (this.training) {
        this.loadModuleSections();
      }
    });

    this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.loadTrainingData();
    });
  }

  mapLanguageCode(language: any): string {
    const languageMappings: { [key: string]: string } = {
      'ENGLISH': 'EN',
      'FRENCH': 'FR',
      'DUTCH': 'NL',
      'GERMAN': 'DE'
    };

    // If it's already a code, return it
    if (['EN', 'FR', 'NL', 'DE'].includes(language)) {
      return language;
    }

    // Otherwise map the language name to code
    return languageMappings[language] || 'EN'; // Default to EN if mapping not found
  }

  loadTrainingData(): void {
    this.trainingService.getTrainingById(this.trainingId).subscribe(
      training => {
        if (training) {
          this.training = training;
          this.loadModuleSections();
          this.calculateProgress();
        } else {
          // Handle training not found
          this.router.navigate(['/trainings']);
        }
      },
      error => {
        console.error('Error loading training:', error);
        // Handle error
        this.router.navigate(['/trainings']);
      }
    );
  }

  loadModuleSections(): void {
    // Check if training has modules from the backend
    if (this.training?.modules && this.training.modules.length > 0) {
      // Map backend modules to our frontend model
      this.moduleSections = this.training.modules.map((module: Module) => {
        let video: number=0;
        let image: number=0;
        for(let content of module.content) {
          if(content.content_type.toString()=="PICTURE") {
            image++;
          }
          else if(content.content_type.toString()=="VIDEO") {
            video++;
          }
        }
        return {
          id: module.id,
          title: this.getLocalizedContent(module.title),
          description: this.getLocalizedContent(module.description),
          completed: false, // We need to fetch completion status separately or calculate it
          duration: [image, video],
          questionCount: module.questions ? module.questions.length : 0
        };
      });
    } else {
      // Fallback to demo data if no modules are available
      this.generateModuleSections();
    }
  }

  getLocalizedContent(contentMap: any): string {
    if (!contentMap) return '';
    if (typeof contentMap === 'string') return contentMap;
    
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

  generateModuleSections(): void {
    // Fallback to demo data
    this.moduleSections = [
      {
        id: 1,
        title: 'Basic Navigation Safety',
        description: 'Understanding fundamental navigation protocols',
        completed: true,
        duration: '15 min video',
        questionCount: 3
      },
      {
        id: 2,
        title: 'Emergency Procedures',
        description: 'Learn how to handle emergency situations',
        completed: true,
        duration: '11 min video',
        questionCount: 2
      },
      {
        id: 3,
        title: 'Communication Protocols',
        description: 'Maritime communication best practices',
        completed: false,
        duration: '10 min video',
        questionCount: 4
      }
    ];
  }

  calculateProgress(): void {
    // Calculate completed trainings
    this.trainingsCompleted = this.moduleSections.filter(section => section.completed).length;
    this.totalTrainings = this.moduleSections.length;
  }

  goToModule(sectionId: number): void {
    // Navigate to the specific module section
    this.router.navigate(['/trainings', this.trainingId, 'module', sectionId]);
  }

  goBack(): void {
    this.router.navigate(['/trainings']);
  }

  goToCertificate(): void {
    // Check if all trainings are completed
    if (this.trainingsCompleted === this.totalTrainings) {
      this.router.navigate(['/trainings', this.trainingId, 'certificate']);
    }
  }

  getActionText(completed: boolean): string {
    return completed ? 'Review' : 'Start';
  }

  getProgressPercentage(): number {
    if (this.totalTrainings === 0) return 0;
    return (this.trainingsCompleted / this.totalTrainings) * 100;
  }

  getLocalizedTitle(): string {
    if (!this.training) return '';
    
    if (this.training.titleLocalized && this.training.titleLocalized[this.currentLanguage]) {
      return this.training.titleLocalized[this.currentLanguage];
    }
    
    return this.training.title;
  }

  getLocalizedDescription(): string {
    if (!this.training) return '';
    
    if (this.training.descriptionLocalized && this.training.descriptionLocalized[this.currentLanguage]) {
      return this.training.descriptionLocalized[this.currentLanguage];
    }
    
    return this.training.description;
  }
}