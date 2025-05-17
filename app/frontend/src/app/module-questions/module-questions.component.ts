import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { faArrowLeft, faVideo } from '@fortawesome/free-solid-svg-icons';
import { Module, Question, TrainingService } from '../services/training.service';
import { LanguageService } from '../services/language.service';

@Component({
  selector: 'app-module-questions',
  imports: [CommonModule, RouterModule, FontAwesomeModule, TranslateModule],
  templateUrl: './module-questions.component.html',
  styleUrl: './module-questions.component.css'
})
export class ModuleQuestionsComponent implements OnInit {
  module: Module = {
    id: 0,
    title: {},
    description: {},
    content: [],
    questions: []
  }
  currentQuestionIndex:number=0;

  trainingId: number = 0;
  moduleId: number = 0;
  currentQuestion: Question | undefined;
  selectedOptionId: number | null = null;
  
  // Store user answers for each question
  userAnswers: Map<number, number> = new Map();
  userCorrectAnswers: Map<number, boolean> = new Map();
  
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


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService,
    public languageService: LanguageService,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.moduleId = +params['sectionId'];
      
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

  async loadQuestionData() {
    let response = await fetch(`/api/modules/${this.moduleId}`);
    let json = await response.json();
    this.module = await json;
    this.setCurrentQuestion();
    this.totalSteps = this.module.questions.length;
  }

  setCurrentQuestion(): void {
    this.currentQuestion = this.module.questions[this.currentQuestionIndex];
    this.isAnswerSubmitted = false;
    this.isAnswerCorrect = false;
  }

  // Calculate progress percentage based on current question index
  getProgressPercentage(): number {
    if (this.totalSteps === 0) return 0;
    return ((this.currentStep) / this.totalSteps * 100);
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
      
      // Save the user's answer
      if (this.currentQuestion) {
        this.userAnswers.set(this.currentQuestion.id, optionId);
      }
    }
  }

  submitAnswer(): void {
    if (this.selectedOptionId === null) return;
    this.isAnswerSubmitted = true;
    
    if(this.currentQuestion) {
      // Find selected option
      const selectedOption = this.currentQuestion.question_options.find(
        (option: any) => option.id === this.selectedOptionId
      );
      
      if (selectedOption) {
        this.isAnswerCorrect = selectedOption.correct;
        // Save whether the answer was correct
        this.userCorrectAnswers.set(this.currentQuestion.id, this.isAnswerCorrect);
      }
    }
  }

  nextQuestion(): void {
    if (this.isAnswerCorrect) {
      this.currentQuestionIndex++;
      this.currentStep++;
      this.setCurrentQuestion();
      if(this.currentQuestionIndex>=this.module.questions.length) {
        this.isModuleCompleted = true;
      }
    }
  }

  previousQuestion(): void {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
      this.currentStep--;
      this.setCurrentQuestion();
    }
  }

  getStepIndicatorText(): string {
    if (this.isModuleCompleted) {
      return this.translate.instant("QUESTIONS.COMPLETED");
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