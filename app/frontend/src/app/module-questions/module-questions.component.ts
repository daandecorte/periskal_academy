import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { faArrowLeft, faVideo } from '@fortawesome/free-solid-svg-icons';
import { TrainingService } from '../services/training.service';
import { LanguageService } from '../services/language.service';

// The backend for the questions does not yet work and this code will always fall back on demo data for now
// I have literally tried everything but as soon as I add questions to the database, completely unrelated things start breaking and I don't know why
// Maybe it's because training and module are being confused in the backend? Maybe something goes wrong in the training service? Maybe it's something else. I don't know.
// And so basically I gave up actually sorry

@Component({
  selector: 'app-module-questions',
  imports: [CommonModule, RouterModule, FontAwesomeModule, TranslateModule],
  templateUrl: './module-questions.component.html',
  styleUrl: './module-questions.component.css'
})
export class ModuleQuestionsComponent implements OnInit {
  trainingId: number = 0;
  moduleId: number = 0;
  currentQuestionIndex: number = 0;
  questions: any[] = [];
  currentQuestion: any = null;
  selectedOptionId: number | null = null;
  
  // Status flags
  isAnswerSubmitted: boolean = false;
  isAnswerCorrect: boolean = false;
  isModuleCompleted: boolean = false;
  
  // Progress tracking
  currentStep: number = 1;
  totalSteps: number = 1;
  
  // Module information
  moduleTitle: string = '';
  
  // Font Awesome icons
  faArrowLeft = faArrowLeft;
  faVideo = faVideo;
  
  currentLanguage: string = 'EN';
  
  // Flag to track if we're using hardcoded data
  isUsingHardcodedData: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService,
    private languageService: LanguageService
  ) {}

  ngOnInit(): void {
    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = this.mapLanguageCode(language);
      // Reload question data when language changes
      if (this.trainingId && this.moduleId) {
        this.loadQuestionData();
      }
    });

    this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.moduleId = +params['sectionId'];
      
      // Get the question index from the URL
      if (params['questionIndex']) {
        this.currentQuestionIndex = +params['questionIndex'];
      } else {
        this.currentQuestionIndex = 0; // Default to first question
      }
      
      this.loadQuestionData();
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

  loadQuestionData(): void {
    this.trainingService.getTrainingById(this.trainingId).subscribe(
      training => {
        if (training && training.modules) {
          const module = training.modules.find((m: any) => m.id === this.moduleId);
          
          if (module) {
            this.moduleTitle = this.getLocalizedContent(module.title);
            
            // Check if questions data is available or use hardcoded data
            if (module.questions && module.questions.length > 0) {
              this.isUsingHardcodedData = false;
              this.questions = module.questions;
            } else {
              console.warn(`Module with ID ${this.moduleId} has no questions. Using hardcoded data.`);
              this.isUsingHardcodedData = true;
              this.questions = this.getHardcodedQuestions();
            }
            
            this.totalSteps = this.questions.length + 1; // +1 for video step
            this.currentStep = this.currentQuestionIndex + 2; // +2 because video is step 1 and questions are 1-indexed
            
            // Set current question
            this.setCurrentQuestion();
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
        console.warn('Using hardcoded data due to error loading module data.');
        this.isUsingHardcodedData = true;
        this.moduleTitle = "Demo Module"; // Fallback module title
        this.questions = this.getHardcodedQuestions();
        this.totalSteps = this.questions.length + 1;
        this.currentStep = this.currentQuestionIndex + 2;
        this.setCurrentQuestion();
      }
    );
  }

  getHardcodedQuestions(): any[] {
    // Create hardcoded questions with localized content structure
    return [
      {
        id: 1,
        text: {
          'EN': 'Question?',
          'FR': 'Question?',
          'NL': 'Question?',
          'DE': 'Question?',
        },
        questionOptions: [
          {
            id: 101,
            text: {
              'EN': 'Answer 1',
              'FR': 'Answer 1',
              'NL': 'Answer 1',
              'DE': 'Answer 1',
            },
            isCorrect: false
          },
          {
            id: 102,
            text: {
              'EN': 'Answer 2',
              'FR': 'Answer 2',
              'NL': 'Answer 2',
              'DE': 'Answer 2',
            },
            isCorrect: true
          },
          {
            id: 103,
            text: {
              'EN': 'Answer 3',
              'FR': 'Answer 3',
              'NL': 'Answer 3',
              'DE': 'Answer 3',
            },
            isCorrect: false
          }
        ]
      },
      {
        id: 2,
        text: {
          'EN': 'Another question?',
          'FR': 'Quel est l\'objectif des services Angular?',
          'NL': 'Wat is het doel van Angular-services?',
          'DE': 'Was ist der Zweck von Angular-Diensten?'
        },
        questionOptions: [
          {
            id: 201,
            text: {
              'EN': 'Answer 1',
              'FR': 'Answer 1',
              'NL': 'Answer 1',
              'DE': 'Answer 1',
            },
            isCorrect: false
          },
          {
            id: 202,
            text: {
              'EN': 'Answer 2',
              'FR': 'Answer 2',
              'NL': 'Answer 2',
              'DE': 'Answer 2',
            },
            isCorrect: false
          },
          {
            id: 203,
            text: {
              'EN': 'Answer 3',
              'FR': 'Answer 3',
              'NL': 'Answer 3',
              'DE': 'Answer 3',
            },
            isCorrect: true
          }
        ]
      }
    ];
  }

  setCurrentQuestion(): void {
    // Reset answer state
    this.selectedOptionId = null;
    this.isAnswerSubmitted = false;
    this.isAnswerCorrect = false;
    
    if (this.currentQuestionIndex < this.questions.length) {
      this.currentQuestion = this.questions[this.currentQuestionIndex];
    } else {
      this.isModuleCompleted = true;
    }
  }

   // Shuffle the answer options
   shuffleOptions(options: any[]): any[] {
    for (let i = options.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [options[i], options[j]] = [options[j], options[i]];
    }
    return options;
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

  selectOption(optionId: number): void {
    if (!this.isAnswerSubmitted) {
      this.selectedOptionId = optionId;
    }
  }

  submitAnswer(): void {
    if (this.selectedOptionId === null) return;
    
    this.isAnswerSubmitted = true;
    
    // Find selected option
    const selectedOption = this.currentQuestion.questionOptions.find(
      (option: any) => option.id === this.selectedOptionId
    );
    
    if (selectedOption) {
      this.isAnswerCorrect = selectedOption.isCorrect;
    }
  }

  nextQuestion(): void {
    if (this.isAnswerCorrect) {
      this.currentQuestionIndex++;
      this.currentStep++;
      this.setCurrentQuestion();
      
      // Update URL
      this.router.navigate(
        ['/trainings', this.trainingId, 'module', this.moduleId, 'questions', this.currentQuestionIndex],
        { replaceUrl: true }
      );
    }
  }

  getStepIndicatorText(): string {
    if (this.isModuleCompleted) {
      return "completed";
    }
    return `${this.currentStep} ${this.getLocalizedContent({ 'EN': 'of', 'FR': 'de', 'NL': 'van', 'DE': 'von' })} ${this.totalSteps}`;
  }


  goBackToVideo(): void {
    this.router.navigate(['/trainings', this.trainingId, 'module', this.moduleId]);
  }

  goBackToOverview(): void {
    this.router.navigate(['/trainings', this.trainingId]);
  }

  goToNextModule(): void {
    // TODO: determine the next module ID
    // For now, just go back to training overview
    this.goBackToOverview();
  }
}