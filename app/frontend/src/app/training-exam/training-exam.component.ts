import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { faArrowLeft, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-exam',
  imports: [CommonModule, RouterModule, FontAwesomeModule, TranslateModule],
  templateUrl: './training-exam.component.html',
  styleUrl: './training-exam.component.css'
})
export class TrainingExamComponent implements OnInit, OnDestroy {
  // Exam information
  examId: number = 0;
  examTitle: string = 'Navigation Safety Certification Exam';
  examDescription: string = 'Complete all questions to receive your certificate';
  
  // Question handling
  currentQuestionIndex: number = 0;
  questions: any[] = [];
  currentQuestion: any = null;
  selectedOptionId: number | null = null;
  
  // Store user answers for each question
  userAnswers: Map<number, number> = new Map();
  
  // Status flags
  isAnswerSubmitted: boolean = false;
  isExamCompleted: boolean = false;
  
  // Progress tracking - will be set based on actual questions loaded
  totalQuestions: number = 0;
  
  // Timer
  timeRemainingInSeconds: number = 60 * 60; // 60 minutes
  timerSubscription?: Subscription;
  
  // Font Awesome icons
  faArrowLeft = faArrowLeft;
  faArrowRight = faArrowRight;
  
  // Current language
  currentLanguage: string = 'EN';

  // Exam results
  examScore?: number;
  examPassed?: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.examId = +params['id'] || 0;
      
      // Get the question index from the URL
      if (params['questionIndex']) {
        this.currentQuestionIndex = +params['questionIndex'];
      } else {
        this.currentQuestionIndex = 0; // Default to first question
      }
      
      this.loadExamData();
      this.startTimer();
    });
  }

  ngOnDestroy(): void {
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
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
    // Hardcoded data for now
    this.questions = this.getHardcodedQuestions();
    
    // Update total questions based on the actual number of questions loaded
    this.totalQuestions = this.questions.length;
    
    this.setCurrentQuestion();
  }

  getHardcodedQuestions(): any[] {
    // Sample exam questions
    return [
      {
        id: 1,
        text: {
          'EN': 'How often should safety equipment be inspected?',
          'FR': 'À quelle fréquence l\'équipement de sécurité doit-il être inspecté?',
          'NL': 'Hoe vaak moet veiligheidsuitrusting worden geïnspecteerd?',
          'DE': 'Wie oft sollten Sicherheitsausrüstungen überprüft werden?',
        },
        questionOptions: [
          {
            id: 101,
            text: {
              'EN': 'Once a year',
              'FR': 'Une fois par an',
              'NL': 'Een keer per jaar',
              'DE': 'Einmal im Jahr',
            },
            isCorrect: false
          },
          {
            id: 102,
            text: {
              'EN': 'Only when malfunctioning',
              'FR': 'Seulement en cas de dysfonctionnement',
              'NL': 'Alleen bij storing',
              'DE': 'Nur bei Fehlfunktion',
            },
            isCorrect: false
          },
          {
            id: 103,
            text: {
              'EN': 'Monthly',
              'FR': 'Mensuel',
              'NL': 'Maandelijks',
              'DE': 'Monatlich',
            },
            isCorrect: false
          },
          {
            id: 104,
            text: {
              'EN': 'Daily before operation',
              'FR': 'Quotidiennement avant l\'opération',
              'NL': 'Dagelijks voor gebruik',
              'DE': 'Täglich vor dem Betrieb',
            },
            isCorrect: true
          }
        ]
      },
      {
        id: 2,
        text: {
          'EN': 'What is the minimum safe distance to maintain from other vessels?',
          'FR': 'Quelle est la distance minimale de sécurité à maintenir par rapport aux autres navires?',
          'NL': 'Wat is de minimale veilige afstand die moet worden aangehouden van andere vaartuigen?',
          'DE': 'Was ist der Mindestsicherheitsabstand zu anderen Schiffen?',
        },
        questionOptions: [
          {
            id: 201,
            text: {
              'EN': '50 meters',
              'FR': '50 mètres',
              'NL': '50 meter',
              'DE': '50 Meter',
            },
            isCorrect: false
          },
          {
            id: 202,
            text: {
              'EN': '100 meters',
              'FR': '100 mètres',
              'NL': '100 meter',
              'DE': '100 Meter',
            },
            isCorrect: true
          },
          {
            id: 203,
            text: {
              'EN': '150 meters',
              'FR': '150 mètres',
              'NL': '150 meter',
              'DE': '150 Meter',
            },
            isCorrect: false
          },
          {
            id: 204,
            text: {
              'EN': '200 meters',
              'FR': '200 mètres',
              'NL': '200 meter',
              'DE': '200 Meter',
            },
            isCorrect: false
          }
        ]
      },
      // Add more questions to match your total
      {
        id: 3,
        text: {
          'EN': 'What should you do in case of a man overboard situation?',
          'FR': 'Que devez-vous faire en cas de situation d\'homme à la mer?',
          'NL': 'Wat moet u doen in geval van man overboord?',
          'DE': 'Was sollten Sie im Falle einer Mann-über-Bord-Situation tun?',
        },
        questionOptions: [
          {
            id: 301,
            text: {
              'EN': 'Continue your course and call for help',
              'FR': 'Continuez votre route et appelez à l\'aide',
              'NL': 'Vervolg je koers en roep om hulp',
              'DE': 'Setzen Sie Ihren Kurs fort und rufen Sie um Hilfe',
            },
            isCorrect: false
          },
          {
            id: 302,
            text: {
              'EN': 'Stop the vessel and wait for rescue services',
              'FR': 'Arrêtez le navire et attendez les services de secours',
              'NL': 'Stop het vaartuig en wacht op reddingsdiensten',
              'DE': 'Stoppen Sie das Schiff und warten Sie auf Rettungsdienste',
            },
            isCorrect: false
          },
          {
            id: 303,
            text: {
              'EN': 'Shout "Man overboard", throw a life buoy, keep the person in sight, and maneuver to rescue',
              'FR': 'Criez "Homme à la mer", lancez une bouée de sauvetage, gardez la personne en vue et manœuvrez pour secourir',
              'NL': 'Roep "Man overboord", gooi een reddingsboei, houd de persoon in zicht en manoeuvreer om te redden',
              'DE': 'Rufen Sie "Mann über Bord", werfen Sie einen Rettungsring, behalten Sie die Person im Auge und manövrieren Sie zur Rettung',
            },
            isCorrect: true
          },
          {
            id: 304,
            text: {
              'EN': 'Accelerate to get help as quickly as possible',
              'FR': 'Accélérez pour obtenir de l\'aide le plus rapidement possible',
              'NL': 'Versnel om zo snel mogelijk hulp te krijgen',
              'DE': 'Beschleunigen Sie, um so schnell wie möglich Hilfe zu holen',
            },
            isCorrect: false
          }
        ]
      },
      // Add more questions as needed
    ];
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
    this.router.navigate(['/exams']);
  }

  submitExam(): void {
    this.isExamCompleted = true;

    //Send to backend to validate  
    
    /* Example of what would happen
    const examSubmission = {
      examId: this.examId,
      userId: getCurrentUserId(), // Get from your auth service
      answers: Array.from(this.userAnswers.entries()).map(([questionId, answerId]) => ({
        questionId,
        answerId
      })),
      timeSpent: (60 * 60) - this.timeRemainingInSeconds // Time in seconds
    };
    
    this.examService.submitExam(examSubmission).subscribe(
      result => {
        this.examScore = result.score;
        this.examPassed = result.passed;
      },
      error => {
        // Handle error
      }
    );
    */
  }
}