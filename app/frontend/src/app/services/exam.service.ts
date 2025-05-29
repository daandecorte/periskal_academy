import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

export interface LocalizedStrings {
  [key: string]: string;
}

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
  titleLocalized: LocalizedStrings;
  descriptionLocalized: LocalizedStrings;
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
  startTime?: Date;
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

  constructor(private http: HttpClient) { }

  // Get an exam by ID
  getExamById(examId: number): Observable<Exam> {
    return this.http.get<any>(`${this.apiUrl}/${examId}`).pipe(
      map(backendExam => this.mapBackendExam(backendExam)),
      catchError(error => {
        console.error(`Error fetching exam with ID ${examId}:`, error);
        return throwError(() => error);
      })
    );
  }
  
  // Get exams by training ID
  getExamsByTrainingId(trainingId: number): Observable<Exam[]> {
    return this.http.get<any[]>(`${this.apiUrl}/training/${trainingId}`).pipe(
      map(backendExams => backendExams.map(exam => this.mapBackendExam(exam))),
      catchError(error => {
        console.error(`Error fetching exams for training ID ${trainingId}:`, error);
        return throwError(() => error);
      })
    );
  }

  // Submit exam answers
  submitExam(submission: ExamSubmission): Observable<ExamResult> {
    return this.http.post<ExamResult>(`${this.apiUrl}/submit`, submission).pipe(
      catchError(error => {
        console.error('Error submitting exam:', error);
        return throwError(() => error);
      })
    );    
  }

  // Map backend exam data
  private mapBackendExam(backendExam: any): Exam {
    // Map localized title and description
    const titleLocalized: LocalizedStrings = {};
    const descriptionLocalized: LocalizedStrings = {};
    
    // Map language enum values to strings (EN, FR, etc.)
    if (backendExam.title) {
      Object.entries(backendExam.title).forEach(([lang, text]) => {
        titleLocalized[this.convertLanguageToString(lang)] = text as string;
      });
    }
    
    if (backendExam.description) {
      Object.entries(backendExam.description).forEach(([lang, text]) => {
        descriptionLocalized[this.convertLanguageToString(lang)] = text as string;
      });
    }
    
    // Default values if no localized content is available
    if (Object.keys(titleLocalized).length === 0) {
      titleLocalized['EN'] = backendExam.title || 'Exam';
    }
    
    if (Object.keys(descriptionLocalized).length === 0) {
      descriptionLocalized['EN'] = backendExam.description || 'Please complete all questions';
    }
    
    const exam: Exam = {
      id: backendExam.id,
      passingScore: backendExam.passingScore || backendExam.passing_score,
      maxAttempts: backendExam.maxAttempts || backendExam.max_attempts,
      time: backendExam.time,
      questionAmount: backendExam.questionAmount || backendExam.question_amount,
      trainingId: backendExam.training ? backendExam.training.id : 0,
      titleLocalized: titleLocalized,
      descriptionLocalized: descriptionLocalized,
      questions: this.mapBackendQuestions(backendExam.questions || [])
    };
    
    return exam;
  }

  // Map backend exam questions
  private mapBackendQuestions(backendQuestions: any[]): Question[] {
    return backendQuestions.map(q => {
      const textLocalized: LocalizedStrings = {};
      
      // Map the text entries from backend format
      if (q.text) {
        Object.entries(q.text).forEach(([lang, text]) => {
          textLocalized[this.convertLanguageToString(lang)] = text as string;
        });
      }
      
      // Default if no text is available
      if (Object.keys(textLocalized).length === 0) {
        textLocalized['EN'] = 'Question';
      }
      
      return {
        id: q.id,
        text: textLocalized,
        questionOptions: this.mapBackendOptions(q.questionOptions || q.question_options || [])
      };
    });
  }

  // Map backend options
  private mapBackendOptions(backendOptions: any[]): QuestionOption[] {
    return backendOptions.map(o => {
      const textLocalized: LocalizedStrings = {};
      
      // Map the text entries from backend format
      if (o.text) {
        Object.entries(o.text).forEach(([lang, text]) => {
          textLocalized[this.convertLanguageToString(lang)] = text as string;
        });
      }
      
      // Default if no text is available
      if (Object.keys(textLocalized).length === 0) {
        textLocalized['EN'] = 'Option';
      }
      
      return {
        id: o.id,
        text: textLocalized,
        isCorrect: o.isCorrect || o.correct || false
      };
    });
  }

  // Helper method to convert Language enum to string
  private convertLanguageToString(lang: string): string {
    const languageMap: { [key: string]: string } = {
      'ENGLISH': 'EN',
      'FRENCH': 'FR',
      'DUTCH': 'NL',
      'GERMAN': 'DE'
    };
    
    return languageMap[lang] || lang;
  }

  // Get user exam attempts
  getUserExamAttempts(userId: number, examId: number): Observable<any[]> {   
      return this.http.get<any[]>(`${this.apiUrl}/attempts/${examId}/user/${userId}`);    
  }

  // Method for starting an exam
  startExam(examId: number, userId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${examId}/start?userId=${userId}`).pipe(
      map(response => ({
        exam: this.mapBackendExam(response.exam),
        startTime: new Date(response.start_time)
      })),
      catchError(error => {
        console.error(`Error starting exam with ID ${examId}:`, error);
        return throwError(() => error);
      })
    );
  }

  // Method to get remaining time from backend
  getRemainingTime(examId: number, userId: number): Observable<number> {
    return this.http.get<{remainingSeconds: number}>(`${this.apiUrl}/${examId}/time?userId=${userId}`).pipe(
      map(response => response.remainingSeconds)
    );
  }
}