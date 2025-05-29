import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { faArrowLeft, faArrowRight} from '@fortawesome/free-solid-svg-icons';
import { interval, Subscription } from 'rxjs';
import { ExamService, Exam, ExamSubmission, ExamQuestionAnswer, ExamResult, QuestionOption, Question } from '../services/exam.service';
import { AuthService } from '../services/auth.service';
import { ExamResultComponent } from '../exam-result/exam-result.component';
import { LanguageService } from '../services/language.service';

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
  examStartTime?: Date;
  
  // Question handling
  currentQuestionIndex: number = 0;
  questions: Question[] = [];
  currentQuestion: Question | null = null;
  selectedOptionId: number | null = null;
  shuffledQuestionsCache: Map<number, Question> = new Map();
  
  // Store user answers for each question
  userAnswers: Map<number, number> = new Map();
  
  // Status flags
  isLoading: boolean = true;
  isAnswerSubmitted: boolean = false;
  isExamCompleted: boolean = false;
  isSubmitting: boolean = false;
  isTimerStarted: boolean = false;
  
  // Progress tracking - will be set based on actual questions loaded
  totalQuestions: number = 0;
  answeredQuestions: number = 0;
  
  // Timer
  timeRemainingInSeconds: number = 0; // Will be set from exam.time
  timerSubscription?: Subscription;
  
  // Font Awesome icons
  faArrowLeft = faArrowLeft;
  faArrowRight = faArrowRight;
  
  // Current language - now properly integrated with LanguageService
  currentLanguage: string = 'ENGLISH';
  private languageSubscription?: Subscription;

  // Exam results
  examScore?: number;
  examPassed?: boolean;
  examCertificateId?: number;
  examSubmissionError: string = '';

  // User information
  currentUserId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private examService: ExamService,
    private authService: AuthService,
    private languageService: LanguageService // Add LanguageService
  ) {}

  ngOnInit(): void {
    // Subscribe to language changes
    this.languageSubscription = this.languageService.currentLanguage$.subscribe(
      (language: string) => {
        this.currentLanguage = language;
        // Update localized content when language changes
        if (this.exam) {
          this.updateLocalizedContent();
        }
      }
    );
    
    this.route.params.subscribe(async params => {
      const id = params['id'];
      if (id) {
        this.examId = +id; // Convert string to number
        
        // Get the question index from the URL
        if (params['questionIndex']) {
          this.currentQuestionIndex = +params['questionIndex'];
        } else {
          this.currentQuestionIndex = 0;
        }
        
        if (this.examId > 0) {
          // Get current user ID asynchronously
          this.currentUserId = await this.getCurrentUserIdAsync();
          
          // Check if user ID is valid before proceeding
          if (this.currentUserId <= 0) {
            this.examSubmissionError = 'User not authenticated. Please log in again.';
            this.isLoading = false;
            return;
          }
          
          // Load exam data if timer hasn't started yet
          // If timer is already running, just update the current question
          if (!this.isTimerStarted) {
            this.loadExamData();
          } else {
            this.setCurrentQuestion();
          }
        } else {
          this.examSubmissionError = 'Invalid exam ID';
          this.isLoading = false;
        }
      } else {
        this.examSubmissionError = 'No exam ID provided';
        this.isLoading = false;
      }
    });
  }

  ngOnDestroy(): void {
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
    if (this.languageSubscription) {
      this.languageSubscription.unsubscribe();
    }
  }

  getCurrentUserId(): number {
    const currentUser = this.authService.currentUserValue;
    if (!currentUser || !currentUser.ID) {
      return 0; // Return 0 to indicate no valid user
    }
    return 0; // This will be updated below
  }

  private async getCurrentUserIdAsync(): Promise<number> {
    const currentUser = this.authService.currentUserValue;
    if (!currentUser || !currentUser.ID) {
      return 0;
    }
    
    try {
      const response = await fetch(`/api/users/periskal_id/${currentUser.ID}`);
      if (response.status === 200) {
        const userData = await response.json();
        return userData.id || 0;
      }
      return 0;
    } catch (error) {
      console.error('Error fetching user ID:', error);
      return 0;
    }
  }

  updateLocalizedContent(): void {
    if (this.exam) {
      this.examTitle = this.getLocalizedContent(this.exam.titleLocalized);
      this.examDescription = this.getLocalizedContent(this.exam.descriptionLocalized);
    }
  }

  startTimer(): void {
    // Prevent multiple timer instances
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
    
    let syncCounter = 0;
    this.isTimerStarted = true;
    
    this.timerSubscription = interval(1000).subscribe(() => {
      if (this.timeRemainingInSeconds > 0) {
        this.timeRemainingInSeconds--;
        
        // Sync with backend every 30 seconds
        syncCounter++;
        if (syncCounter >= 30) {
          this.syncTimeWithBackend();
          syncCounter = 0;
        }
      } else {
        // If timer runs out, submit exam automatically
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
    
    this.examService.startExam(this.examId, this.currentUserId).subscribe({
      next: (response) => {
        this.exam = response.exam;
        this.examStartTime = response.startTime;
        
        // Set localized content using the update method
        this.updateLocalizedContent();
        
        // Get questions from exam
        if (response.exam.questions && response.exam.questions.length > 0) {
          this.questions = [...response.exam.questions];
          this.totalQuestions = this.questions.length;
        } else {
          this.examSubmissionError = 'This exam has no questions.';
          this.isLoading = false;
          return;
        }

        // Validate currentQuestionIndex is in range
        if (this.currentQuestionIndex >= this.totalQuestions) {
          this.currentQuestionIndex = 0;
        }
        
        // Get remaining time from backend
        this.syncTimeWithBackend();
        
        // Initialize question display
        this.setCurrentQuestion();
        
        // Start timer
        if (!this.isTimerStarted) {
          this.startTimer();
        }
        
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading exam:', err);
        this.isLoading = false;
        this.examSubmissionError = 'Failed to load exam. Please try again later.';
      }
    });
  }

  setCurrentQuestion(): void {
    // Reset answer state for new display
    this.isAnswerSubmitted = false;
    
    if (this.currentQuestionIndex < this.questions.length) {
      // Get the current question
      const baseQuestion = this.questions[this.currentQuestionIndex];

      if (!baseQuestion || !baseQuestion.questionOptions) {
        console.error('Invalid question data:', baseQuestion);
        this.examSubmissionError = 'Error loading question data.';
        return;
      }
      
      // Check if there is a cached version of this question with shuffled options
      if (this.shuffledQuestionsCache.has(this.currentQuestionIndex)) {
        this.currentQuestion = this.shuffledQuestionsCache.get(this.currentQuestionIndex)!;
      } else {
        // Create a shuffled version and cache it
        const shuffledQuestion = {
          ...baseQuestion,
          questionOptions: this.shuffleOptions([...baseQuestion.questionOptions])
        };
        this.shuffledQuestionsCache.set(this.currentQuestionIndex, shuffledQuestion);
        this.currentQuestion = shuffledQuestion;
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
  shuffleOptions(options: QuestionOption[]): QuestionOption[] {
    for (let i = options.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [options[i], options[j]] = [options[j], options[i]];
    }
    return options;
  }

  // Updated localization method to work with your language service
  getLocalizedContent(contentMap: any): string {
    if (!contentMap) return '';
    if (typeof contentMap === 'string') return contentMap;
    
    // Try current language first (using the enum-style language)
    if (contentMap[this.currentLanguage]) {
      return contentMap[this.currentLanguage];
    }
    
    // Convert language service format to string format for fallback
    const languageStringMap: { [key: string]: string } = {
      'ENGLISH': 'EN',
      'FRENCH': 'FR',
      'DUTCH': 'NL',
      'GERMAN': 'DE'
    };
    
    const stringLang = languageStringMap[this.currentLanguage];
    if (stringLang && contentMap[stringLang]) {
      return contentMap[stringLang];
    }
    
    // Fallback to English
    if (contentMap['ENGLISH']) {
      return contentMap['ENGLISH'];
    }
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
        this.updateAnsweredQuestionsCount();
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
    this.isTimerStarted = false;

    // Clear the question cache
    this.clearQuestionCache();
    
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
      startTime: this.examStartTime
    };
    
    // Submit to backend
    this.examService.submitExam(examSubmission).subscribe({
      next: (result: ExamResult) => {
        this.examScore = result.score;
        this.examPassed = result.passed;
        this.examCertificateId = result.certificate_id;
        this.isSubmitting = false;
      },
      error: (error) => {
        console.error('Error submitting exam:', error);
        this.examSubmissionError = 'Exam submission failed, please check your internet connection.';
        this.isSubmitting = false;
      }
    });
  }

  // Method for clearing cache when exam is completed:
  private clearQuestionCache(): void {
    this.shuffledQuestionsCache.clear();
  }

  // Method for syncing time with backend
  private syncTimeWithBackend(): void {
    this.examService.getRemainingTime(this.examId, this.currentUserId).subscribe({
      next: (remainingSeconds) => {
        this.timeRemainingInSeconds = remainingSeconds;
      },
      error: (err) => {
        console.error('Error syncing time with server:', err);
        // Fallback
        this.timeRemainingInSeconds = this.exam!.time * 60;
      }
    });
  }
}