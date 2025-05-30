import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { faArrowLeft, faVideo } from '@fortawesome/free-solid-svg-icons';
import { Module, Question, Training, TrainingService } from '../services/training.service';
import { LanguageService } from '../services/language.service';
import { AuthService } from '../services/auth.service';

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

  training: Training |undefined;
  
  faArrowLeft = faArrowLeft;
  faVideo = faVideo;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public languageService: LanguageService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.moduleId = +params['sectionId'];
      
      this.loadQuestionData();
      this.getTraining();
    });
  }
  async getTraining() {
    let trainingResponse = await fetch(`/api/trainings/${this.trainingId}`);
    this.training = await trainingResponse.json();
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
        this.addModuleCompleted();
        this.isModuleCompleted = true;
      }
    }
  }

  async addModuleCompleted() {
    try {
      const userPerId = this.authService.currentUserValue?.ID;
      if (!userPerId) {
        console.error("User Periskal ID not found.");
        return;
      }

      const userIdResponse = await fetch(`/api/users/periskal_id/${userPerId}`);
      if (!userIdResponse.ok) {
        throw new Error(`Failed to fetch user by Periskal ID: ${userIdResponse.statusText}`);
      }
      const user = await userIdResponse.json();

      const userId = user?.id;
      if (!userId) {
        throw new Error("User ID not found in response.");
      }

      const userTrainingResponse = await fetch(`/api/user_trainings/training/${this.trainingId}/user/${userId}`);
      if (!userTrainingResponse.ok) {
        throw new Error(`Failed to fetch user training: ${userTrainingResponse.statusText}`);
      }
      const userTraining = await userTrainingResponse.json();

      if(this.training!=undefined) {
        let moduleIndex = this.training.modules?.findIndex(m=>m.id==this.moduleId);
        if(moduleIndex! < userTraining.training_progress.modules_completed) {
          return
        }
      }

      const trainingProgressId = userTraining?.training_progress?.id;
      if (!trainingProgressId) {
        throw new Error("Training progress ID not found.");
      }

      const addModuleCompletedResponse = await fetch(`/api/training_progress/${trainingProgressId}/complete_module`);
      if (!addModuleCompletedResponse.ok) {
        throw new Error(`Failed to complete module: ${addModuleCompletedResponse.statusText}`);
      }
      const response = await addModuleCompletedResponse.json();
    } catch (error) {
      console.error("Error in addModuleCompleted:", error);
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
    if(this.training==undefined) return;
    if(this.training.modules) {
      let nextModuleIndex = this.training.modules.findIndex(m=>m.id==this.moduleId) + 1;
      let nextModuleId = this.training.modules[nextModuleIndex]?.id;
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