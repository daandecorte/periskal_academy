import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TrainingService, Training } from '../services/training.service';
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

interface ModuleSection {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  duration: string;
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
    private trainingService: TrainingService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.loadTrainingData();
    });
  }

  loadTrainingData(): void {
    this.trainingService.getTrainingById(this.trainingId).subscribe(
      training => {
        if (training) {
          this.training = training;
          this.generateModuleSections();
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

  generateModuleSections(): void {
    // For testing purposes
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
}