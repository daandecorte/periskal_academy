import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { faArrowLeft, faVideo } from '@fortawesome/free-solid-svg-icons';
import { Module, Question, Training, TrainingService } from '../services/training.service';
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

  userAnswers: Map<number, number> = new Map();
  userCorrectAnswers: Map<number, boolean> = new Map();
  
  isAnswerSubmitted: boolean = false;
  isAnswerCorrect: boolean = false;
  isModuleCompleted: boolean = false;
  
  currentStep: number = 2;
  totalSteps: number = 10;
  
  faArrowLeft = faArrowLeft;
  faVideo = faVideo;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public languageService: LanguageService,
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.moduleId = +params['sectionId'];
      
      this.loadQuestionData();
    });
  }

  async loadQuestionData() {
    let response = await fetch(`/api/modules/${this.moduleId}`);
    let json = await response.json();
    this.module = await json;
    this.setCurrentQuestion();
    this.totalSteps = this.module.questions.length + 1;
  }

  setCurrentQuestion(): void {
    this.currentQuestion = this.module.questions[this.currentQuestionIndex];
    this.isAnswerSubmitted = false;
    this.isAnswerCorrect = false;
  }

  getProgressPercentage(): number {
    if (this.totalSteps === 0) return 0;
    return ((this.currentStep) / this.totalSteps * 100);
  }

  selectOption(optionId: number): void {
    if (!this.isAnswerSubmitted) {
      this.selectedOptionId = optionId;
      
      if (this.currentQuestion) {
        this.userAnswers.set(this.currentQuestion.id, optionId);
      }
    }
  }

  submitAnswer(): void {
    if (this.selectedOptionId === null) return;
    this.isAnswerSubmitted = true;
    
    if(this.currentQuestion) {
      const selectedOption = this.currentQuestion.question_options.find(
        (option: any) => option.id === this.selectedOptionId
      );
      
      if (selectedOption) {
        this.isAnswerCorrect = selectedOption.correct;
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

  goBackToModule(): void {
    this.router.navigate(['/trainings', this.trainingId, 'module', this.moduleId]);
  }

  goBackToOverview(): void {
    this.router.navigate(['/trainings', this.trainingId]);
  }

  async goToNextModule(){
    let trainingResponse = await fetch(`/api/trainings/${this.trainingId}`);
    let training: Training = await trainingResponse.json();
    if(training.modules) {
      let nextModuleIndex = training.modules.findIndex(m=>m.id==this.moduleId) + 1;
      let nextModuleId = training.modules[nextModuleIndex]?.id;
      if(nextModuleId) {
        this.router.navigate(['/trainings', this.trainingId, 'module', nextModuleId]);
      }
      else {
        this.router.navigate(['/trainings', this.trainingId]);
      }
    }
    else {
      this.router.navigate(['/trainings', this.trainingId]);
    }
  }
}