import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
// Reusing LocalizedStrings from the Training Service, TODO: relocate things later
import { LocalizedStrings } from './training.service';

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
      return this.http.get<any[]>(`${this.apiUrl}/attempts/${examId}/user/${userId}`);    
  }
}