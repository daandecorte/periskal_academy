import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { faArrowLeft, faArrowRight} from '@fortawesome/free-solid-svg-icons';
import { interval, Subscription } from 'rxjs';
import { ExamService, Exam, ExamSubmission, ExamQuestionAnswer, ExamResult } from '../services/exam.service';
import { AuthService } from '../services/auth.service';
import { ExamResultComponent } from '../exam-result/exam-result.component';

@Component({
  selector: 'app-exam',
  imports: [CommonModule, RouterModule, FontAwesomeModule, TranslateModule, ExamResultComponent],
  templateUrl: './training-exam.component.html',
  styleUrl: './training-exam.component.css'
})
export class TrainingExamComponent implements OnInit, OnDestroy {
  // Exam information
  examId: number = 0;
  exam?: Exam;
  examTitle: string = "";
  examDescription: string = "";
  
  // Question handling
  currentQuestionIndex: number = 0;
  questions: any[] = [];
  currentQuestion: any = null;
  selectedOptionId: number | null = null;
  
  // Store user answers for each question
  userAnswers: Map<number, number> = new Map();
  
  // Status flags
  isLoading: boolean = true;
  isAnswerSubmitted: boolean = false;
  isExamCompleted: boolean = false;
  isSubmitting: boolean = false;
  
  // Progress tracking - will be set based on actual questions loaded
  totalQuestions: number = 0;
  answeredQuestions: number = 0;
  
  // Timer
  timeRemainingInSeconds: number = 0; // Will be set from exam.time
  timerSubscription?: Subscription;
  
  // Font Awesome icons
  faArrowLeft = faArrowLeft;
  faArrowRight = faArrowRight;
  
  // Current language
  currentLanguage: string = 'EN';

  // Exam results
  examScore?: number;
  examPassed?: boolean;
  examSubmissionError: string = '';

  // User information
  currentUserId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private examService: ExamService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Get current user ID
    this.currentUserId = this.getCurrentUserId();
    
    // Get current language preference
    this.getCurrentLanguage();
    
    this.route.params.subscribe(params => {
      this.examId = +params['id'] || 0;
      
      // Get the question index from the URL
      if (params['questionIndex']) {
        this.currentQuestionIndex = +params['questionIndex'];
      } else {
        this.currentQuestionIndex = 0; // Default to first question
      }
      
      this.loadExamData();
    });
  }

  ngOnDestroy(): void {
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
  }

  getCurrentUserId(): number {
    // Implement based on auth system
    //return this.authService.getCurrentUserId()
    return 1; // Placeholder for demo
  }

  getCurrentLanguage(): void {
    this.currentLanguage = 'EN'; // Default to English
  }

  startTimer(): void {
    this.timerSubscription = interval(1000).subscribe(() => {
      if (this.timeRemainingInSeconds > 0) {
        this.timeRemainingInSeconds--;
      } else {
        // Time's up - submit exam automatically
        this.submitExam();
      }
    });
  }

  formatTimeRemaining(): string {
    const minutes = Math.floor(this.timeRemainingInSeconds / 60);
    const seconds = this.timeRemainingInSeconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  }

  loadExamData(): void {
    this.isLoading = true;
    
    this.examService.getExamById(this.examId).subscribe({
      next: (exam) => {
        this.exam = exam;
        this.examTitle = this.getLocalizedContent(exam.titleLocalized);
        this.examDescription = this.getLocalizedContent(exam.descriptionLocalized);
        this.questions = [...exam.questions];
        this.totalQuestions = this.questions.length;
        
        // Set timer based on exam time (convert minutes to seconds)
        this.timeRemainingInSeconds = exam.time * 60;
        
        // Initialize question display
        this.setCurrentQuestion();
        
        // Start timer
        this.startTimer();
        
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading exam:', err);
        this.isLoading = false;
        // Handle error
        this.examSubmissionError = 'Failed to load exam. Please try again later.';
      }
    });
  }

  setCurrentQuestion(): void {
    // Reset answer state for new display
    this.isAnswerSubmitted = false;
    
    if (this.currentQuestionIndex < this.questions.length) {
      this.currentQuestion = this.questions[this.currentQuestionIndex];
      
      // Create a copy of the question options and shuffle them
      if (this.currentQuestion && this.currentQuestion.questionOptions) {
        this.currentQuestion = {
          ...this.currentQuestion,
          questionOptions: this.shuffleOptions([...this.currentQuestion.questionOptions])
        };
      }
      
      // Restore previously selected answer if exists
      if (this.userAnswers.has(this.currentQuestion.id)) {
        this.selectedOptionId = this.userAnswers.get(this.currentQuestion.id) || null;
      } else {
        this.selectedOptionId = null;
      }
    } else {
      this.isExamCompleted = true;
    }
  }

  // Calculate progress percentage based on current question index
  getProgressPercentage(): number {
    if (this.totalQuestions === 0) return 0;
    return ((this.currentQuestionIndex + 1) / this.totalQuestions * 100);
  }

   // Update the number of answered questions
   updateAnsweredQuestionsCount(): void {
    this.answeredQuestions = this.userAnswers.size;
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
    
    // Immediately proceed to the next question without showing if answer is correct
    this.nextQuestion();
  }

  nextQuestion(): void {
    if (this.currentQuestionIndex < this.questions.length - 1) {
      this.currentQuestionIndex++;
      this.setCurrentQuestion();
      
      // Update URL
      this.router.navigate(
        ['/exams', this.examId, this.currentQuestionIndex],
        { replaceUrl: true }
      );
    } else {
      // If this was the last question, complete the exam
      this.submitExam();
    }
  }

  previousQuestion(): void {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
      this.setCurrentQuestion();
      
      // Update URL
      this.router.navigate(
        ['/exams', this.examId, this.currentQuestionIndex],
        { replaceUrl: true }
      );
    }
  }

  goBackToOverview(): void {
    this.router.navigate(['/trainings']);
  }

  submitExam(): void {
    // Prevent multiple submissions
    if (this.isSubmitting) return;
    
    this.isSubmitting = true;
    this.isExamCompleted = true;

    // Stop the timer
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
    
    // Prepare answers for submission
    const answers: ExamQuestionAnswer[] = [];
    this.userAnswers.forEach((optionId, questionId) => {
      answers.push({
        questionId: questionId,
        optionId: optionId
      });
    });
    
    // Check if all questions were answered
    if (answers.length < this.totalQuestions) {
      console.warn(`Not all questions were answered. Expected: ${this.totalQuestions}, Got: ${answers.length}`);
    }
    
    // Create submission object
    const examSubmission: ExamSubmission = {
      examId: this.examId,
      userId: this.currentUserId,
      answers: answers,
    };
    
    // Submit to backend
    this.examService.submitExam(examSubmission).subscribe({
      next: (result: ExamResult) => {
        this.examScore = result.score;
        this.examPassed = result.passed;
        this.isSubmitting = false;
      },
      error: (error) => {
        console.error('Error submitting exam:', error);
        // TODO: properly handle error
        this.examSubmissionError = 'DEBUG: submitting fails when using demo data (exam id 0 is used as a fallback but is not found in database)';
        this.isSubmitting = false;
      }
    });
  }
}