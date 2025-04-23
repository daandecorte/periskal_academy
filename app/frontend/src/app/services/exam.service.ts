import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
// Reusing LocalizedStrings from the Training Service, TODO: relocate things later
import { LocalizedStrings } from './training.service';

// This service will still need a few changes to the backend to properly work but it works with demo data for now
// Languages for the demo data somehow got broken along the way and I can't find how to fix it, my guess is a problem in the training service
// But I thought it really didn't matter cause it's demo data anyway

export interface QuestionOption {
  id: number;
  text: LocalizedStrings;
  isCorrect: boolean;
}

export interface Question {
  id: number;
  text: LocalizedStrings;
  questionOptions: QuestionOption[];
}

export interface Exam {
  id: number;
  title?: string;
  titleLocalized?: LocalizedStrings;
  description?: string;
  descriptionLocalized?: LocalizedStrings;
  passingScore: number;
  maxAttempts: number;
  time: number; // Time in minutes
  questionAmount: number;
  questions: Question[];
  trainingId: number;
}

export interface ExamAttempt {
  id?: number;
  examId: number;
  userId: number;
  startTime: Date;
  endTime?: Date;
  score?: number;
  passed?: boolean;
  answers: ExamQuestionAnswer[];
}

export interface ExamQuestionAnswer {
  questionId: number;
  optionId: number;
}

export interface ExamSubmission {
  examId: number;
  userId: number;
  answers: ExamQuestionAnswer[];
}

export interface ExamResult {
  score: number;
  passed: boolean;
  certificateId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ExamService {
  private apiUrl = '/api/exams';
  private useDemoData = false; // Toggle for switching between hardcoded demo data and backend

  constructor(private http: HttpClient) { }

   // Set data source
   setUseDemoData(useDemo: boolean): void {
    this.useDemoData = useDemo;
  }

  // Get current data source
  getUseDemoData(): boolean {
    return this.useDemoData;
  }

  // Get an exam by ID
  getExamById(examId: number): Observable<Exam> {
    if (this.useDemoData) {
      // Return demo exam if ID matches
      if (examId === this.demoExam.id) {
        return of(this.demoExam);
      }
      return throwError(() => new Error(`Exam with ID ${examId} not found in demo data`));
    } else {
      return this.http.get<any>(`${this.apiUrl}/${examId}`).pipe(
        map(backendExam => this.mapBackendExam(backendExam)),
        catchError(error => {
          console.error(`Error fetching exam with ID ${examId}:`, error);
          // Fallback to demo data if backend request fails
          if (examId === this.demoExam.id) {
            return of(this.demoExam);
          }
          return throwError(() => error);
        })
      );
    }
  }

  // Get exams by training ID
  getExamsByTrainingId(trainingId: number): Observable<Exam[]> {
    if (this.useDemoData) {
      // Filter demo exams by training ID
      const exams = this.demoExam.trainingId === trainingId ? [this.demoExam] : [];
      return of(exams);
    } else {
      return this.http.get<any[]>(`${this.apiUrl}/training/${trainingId}`).pipe(
        map(backendExams => backendExams.map(exam => this.mapBackendExam(exam))),
        catchError(error => {
          console.error(`Error fetching exams for training ID ${trainingId}:`, error);
          // Fallback to demo data if backend request fails
          const exams = this.demoExam.trainingId === trainingId ? [this.demoExam] : [];
          return of(exams);
        })
      );
    }
  }

  // Submit exam answers
  submitExam(submission: ExamSubmission): Observable<ExamResult> {
    if (this.useDemoData) {
      // Simulate exam evaluation
      // This might need changes depending on how the backend will work
      const result: ExamResult = {
        score: 75, // Example
        passed: true
      };
      return of(result);
    } else {
      return this.http.post<ExamResult>(`${this.apiUrl}/submit`, submission).pipe(
        catchError(error => {
          console.error('Error submitting exam:', error);
          return throwError(() => error);
        })
      );
    }
  }

  // Map backend exam data
  private mapBackendExam(backendExam: any): Exam {
    // Create localized title and description objects
    const titleLocalized: { [key: string]: string } = {};
    const descriptionLocalized: { [key: string]: string } = {};
    
    // Default values
    titleLocalized['EN'] = 'Exam';
    descriptionLocalized['EN'] = 'Please complete all questions';
    
    const exam: Exam = {
      id: backendExam.id,
      passingScore: backendExam.passingScore,
      maxAttempts: backendExam.maxAttempts,
      time: backendExam.time,
      questionAmount: backendExam.questionAmount,
      trainingId: backendExam.training ? backendExam.training.id : 0,
      titleLocalized: titleLocalized,
      descriptionLocalized: descriptionLocalized,
      questions: this.mapBackendQuestions(backendExam.questions || [])
    };
    
    return exam;
  }

  // Map backend exam questions
  private mapBackendQuestions(backendQuestions: any[]): any[] {
    return backendQuestions.map(q => {
      // Create localized text for the question
      const textLocalized: { [key: string]: string } = {};
      textLocalized['EN'] = q.text || 'Question';
      
      return {
        id: q.id,
        text: textLocalized,
        questionOptions: this.mapBackendOptions(q.options || [])
      };
    });
  }

  // Map backend options
  private mapBackendOptions(backendOptions: any[]): any[] {
    return backendOptions.map(o => {
      // Create localized text for the option
      const textLocalized: { [key: string]: string } = {};
      textLocalized['EN'] = o.text || 'Option';
      
      return {
        id: o.id,
        text: textLocalized,
        isCorrect: o.isCorrect || false
      };
    });
  }

  // Get user exam attempts
  getUserExamAttempts(userId: number, examId: number): Observable<any[]> {
    if (this.useDemoData) {
      // Return empty array for demo mode
      return of([]);
    } else {
      return this.http.get<any[]>(`${this.apiUrl}/attempts/${examId}/user/${userId}`);
    }
  }

    // Demo data
    private demoExam: Exam = {
      id: 1,
      titleLocalized: {
        'EN': 'Navigation Safety Certification Exam',
        'FR': 'Examen de certification de sécurité de navigation',
        'NL': 'Certificeringsexamen voor navigatieveiligheid',
        'DE': 'Zertifizierungsprüfung für Navigationssicherheit'
      },
      descriptionLocalized: {
        'EN': 'Complete all questions to receive your certificate',
        'FR': 'Répondez à toutes les questions pour recevoir votre certificat',
        'NL': 'Beantwoord alle vragen om uw certificaat te ontvangen',
        'DE': 'Beantworten Sie alle Fragen, um Ihr Zertifikat zu erhalten'
      },
      passingScore: 70,
      maxAttempts: 3,
      time: 60, // 60 minutes
      questionAmount: 3,
      trainingId: 1,
      questions: [
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
        }
      ]
    };
}
