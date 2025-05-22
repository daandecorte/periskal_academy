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
import { AuthService } from '../services/auth.service';
import { IUser } from '../types/user-info';
import { firstValueFrom } from 'rxjs';

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
  
  userTraining: UserTraining | undefined;

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
    private languageService: LanguageService,
    private authService: AuthService
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
  async getUserTraining() {
    let currentUser = await firstValueFrom(this.authService.currentUser$);
    if(currentUser) {
      let userResponse = await fetch(`/api/users/periskal_id/${currentUser.ID}`);
      let user = await userResponse.json();
      let userTrainingResponse = await fetch(`/api/user_trainings/training/${this.trainingId}/user/${user.id}`);
      //check if userTraining exists
      if(userTrainingResponse.status==404) {
        let userTrainingPostResponse = await fetch(`/api/user_trainings`, 
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Accept: 'application/json',
            },
            body: JSON.stringify({
              training_id: this.trainingId,
              user_id: user.id,
              eligible_for_certificate: false
            })
          }
        )
        if(userTrainingPostResponse.status==201) {
          await this.getUserTraining();
        }
        else {
          console.error("failed to post user training!");
        }
      }
      else if(userTrainingResponse.status==200) {
        let userTrainingJson = await userTrainingResponse.json();
        this.userTraining = await userTrainingJson;
        if(this.userTraining?.training_progress==null) {
          let trainingProgressPostResponse = await fetch(`/api/training_progress`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Accept: 'application/json',
            },
            body: JSON.stringify({
              start_date_time: new Date().toISOString(),
              last_time_accessed: new Date().toISOString(),
              status: "IN_PROGRESS",
              user_training_id: this.userTraining?.id
            })
          })
          if(trainingProgressPostResponse.status==201) {
            this.getUserTraining();
          }
        }
        else {
          let trainingProgressPostResponse = await fetch(`/api/training_progress/${this.userTraining.training_progress.id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              Accept: 'application/json',
            },
            body: JSON.stringify({
              start_date_time: this.userTraining.training_progress.start_date_time,
              last_time_accessed: new Date().toISOString(),
              status: "IN_PROGRESS",
              modules_completed: this.userTraining.training_progress.modules_completed
            })
          })
        }
      }
    }
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
          this.getUserTraining();
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

  goToModule(sectionId: number): void {
    // Navigate to the specific module section
    this.router.navigate(['/trainings', this.trainingId, 'module', sectionId]);
  }

  goBack(): void {
    this.router.navigate(['/trainings']);
  }

  // TODO: if user has already passen exam, go to certificate download page
  goToCertificate(): void {
    // user is eligible for certificate
    if (this.userTraining?.eligible_for_certificate == true) {
      // Check if training has an exam - handle both possibilities from the model
      if (this.training) {
        let examId: number | undefined;
        
        // Check if exam is directly available as a single object
        if (this.training.exam && typeof this.training.exam === 'object' && this.training.exam.id) {
          examId = this.training.exam.id;
        } 
        // Check if exams is an array
        else if (this.training.exams && Array.isArray(this.training.exams) && this.training.exams.length > 0) {
          examId = this.training.exams[0].id;
        }
        
        if (examId) {
          // Navigate to the exam page with the exam ID
          this.router.navigate(['/exams', examId]);
        } else {
          console.error('No exam found for this training');
          // TODO: handle error
        }
      } else {
        console.error('No exams available for this training');
        // TODO: handle error
      }
    }
  }

  getActionText(completed: boolean): string {
    return completed ? 'Review' : 'Start';
  }

  getProgressPercentage(): number {
    if (this.totalTrainings === 0 || this.userTraining==undefined) return 0;
    return (this.userTraining?.training_progress.modules_completed / this.moduleSections.length) * 100;
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
interface UserTraining {
  id: number
  training_progress: {
    id: number
    start_date_time: Date
    last_time_accessed: Date
    status: String
    modules_completed: number
  }
  training: any
  exam_attempts: any,
  eligible_for_certificate: boolean
}