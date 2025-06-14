import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';

// Interface to represent the language-specific strings structure from backend
export interface LocalizedStrings {
  [language: string]: string;
}

export interface QuestionOption {
  id: number;
  text: any;
  correct: boolean;
}

export interface Question {
  id: number;
  text: any;
  question_options: QuestionOption[];
}
export interface Content {
  id: number;
  content_type: ContentType
  reference: any
}
export enum ContentType {
  TEXT="TEXT", VIDEO="VIDEO", PICTURE="PICTURE"
}
export interface Module {
  id: number;
  title: any;
  description: any;
  content: Content[]
  questions: Question[];
}

export interface Training {
  id: number;
  title: string;
  description: string;
  titleLocalized?: LocalizedStrings;
  descriptionLocalized?: LocalizedStrings;
  modules?: Module[];
  exam?: {
    id: number;
    passingScore: number;
    maxAttempts: number;
    time: number;
    questionAmount: number;
    questions?: Question[];
  };
  exams?: any[];
  tips?: any[];
  active: boolean;
  moduleCount: number;
  progress?: number;
  hasCertificate: boolean;
  assigned?: boolean;
  assignedDate?: string;
  status: 'not_started' | 'in_progress' | 'completed';
  languages?: string[];
  trainingType?: 'Navigation' | 'Safety' | 'Communication';
}

@Injectable({
  providedIn: 'root'
})
export class TrainingService {
  private apiUrl = '/api/trainings';

  constructor(private http: HttpClient) {}


  // Main method to get trainings based on current data source
  getTrainings(): Observable<Training[]> {
    return this.getBackendTrainings();
  }


   // Method to get trainings from backend
   private getBackendTrainings(): Observable<Training[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(backendTrainings => this.mapBackendTrainings(backendTrainings)),
      catchError(error => {
        console.error('Error fetching trainings from backend:', error);
        return throwError(() => error);
      })
    );
  }

  // Method to map backend data to Training interface
private mapBackendTrainings(backendTrainings: any[]): Training[] {
  return backendTrainings.map(backendTraining => {
    // Convert Language enum to string keys for titleLocalized and descriptionLocalized
    const titleLocalized: LocalizedStrings = {};
    const descriptionLocalized: LocalizedStrings = {};
    
    // Handle title mapping
    if (backendTraining.title) {
      Object.entries(backendTraining.title).forEach(([lang, value]) => {
        // Convert Language enum to string
        titleLocalized[this.convertLanguageToString(lang)] = value as string;
      });
    }
    
    // Handle description mapping
    if (backendTraining.description) {
      Object.entries(backendTraining.description).forEach(([lang, value]) => {
        // Convert Language enum to string
        descriptionLocalized[this.convertLanguageToString(lang)] = value as string;
      });
    }
    
    const titleKeys = Object.keys(titleLocalized);
    const defaultTitle = titleKeys.length > 0 ? titleLocalized[titleKeys[0]] : 'Untitled Training';
    
    const descriptionKeys = Object.keys(descriptionLocalized);
    const defaultDescription = descriptionKeys.length > 0 ? descriptionLocalized[descriptionKeys[0]] : 'No description available';
    
    // Handle exam mapping - supporting both singular exam and exam array structures
    let examData = null;
    let hasCertificateFlag = false;
    
    if (backendTraining.exam) {
      examData = backendTraining.exam;
      hasCertificateFlag = true;
    }
    
    // Create a Training object with required fields
    const training: Training = {
      id: backendTraining.id,
      title: defaultTitle, // This will be overridden by getLocalizedTitle in components
      description: defaultDescription, // This will be overridden by getLocalizedDescription in components
      titleLocalized: titleLocalized,
      descriptionLocalized: descriptionLocalized,
      moduleCount: backendTraining.modules ? backendTraining.modules.length : 0,
      exam: examData, // Add the exam data if available
      exams: backendTraining.exams || [], // Keep for backward compatibility
      hasCertificate: hasCertificateFlag || (backendTraining.exams && backendTraining.exams.length > 0),
      status: 'not_started', // Default status
      active: backendTraining.active !== undefined ? backendTraining.active : true,
      modules: this.processModules(backendTraining.modules),
      tips: backendTraining.tips
    };

    // Calculate available languages
    if (backendTraining.title) {
      training.languages = Object.keys(backendTraining.title).map(lang => 
        this.convertLanguageToString(lang)
      );
    }

    if (backendTraining.progress !== undefined) training.progress = backendTraining.progress;
    if (backendTraining.assigned !== undefined) training.assigned = backendTraining.assigned;
    if (backendTraining.assignedDate) training.assignedDate = backendTraining.assignedDate;
    if (backendTraining.trainingType) training.trainingType = backendTraining.trainingType;

    return training;
  });
}

  private processModules(modules: any[]): Module[] {
    if (!modules || modules.length === 0) return [];
    
    return modules.map(module => {
      // Process module titles
      if (module.title) {
        const titleLocalized: LocalizedStrings = {};
        Object.entries(module.title).forEach(([lang, value]) => {
          titleLocalized[this.convertLanguageToString(lang)] = value as string;
        });
        module.title = titleLocalized;
      }
      
      // Process module descriptions
      if (module.description) {
        const descriptionLocalized: LocalizedStrings = {};
        Object.entries(module.description).forEach(([lang, value]) => {
          descriptionLocalized[this.convertLanguageToString(lang)] = value as string;
        });
        module.description = descriptionLocalized;
      }
      
      // Process module video references
      if (module.videoReference) {
        const videoLocalized: LocalizedStrings = {};
        Object.entries(module.videoReference).forEach(([lang, value]) => {
          videoLocalized[this.convertLanguageToString(lang)] = value as string;
        });
        module.videoReference = videoLocalized;
      }
      
      // Process module questions
      if (module.questions && module.questions.length > 0) {
        module.questions = module.questions.map((question: any): Question => {
          if (question.text) {
            const textLocalized: LocalizedStrings = {};
            Object.entries(question.text).forEach(([lang, value]) => {
              textLocalized[this.convertLanguageToString(lang)] = value as string;
            });
            question.text = textLocalized;
          }
          
          if (question.questionOptions) {
            question.questionOptions = question.questionOptions.map((option: any): QuestionOption => {
              return {
                id: option.id,
                text: option.text,
                correct: option.isCorrect
              };
            });
          }
          
          return {
            id: question.id,
            text: question.text,
            question_options: question.questionOptions || []
          };
        });
      } else {
        module.questions = [];
      }
      
      return module as Module;
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

 // Method to get a single training by ID
 getTrainingById(id: number): Observable<Training | undefined> {
    return this.http.get<any>(`${this.apiUrl}/${id}`).pipe(
      map(backendTraining => {
        const trainings = this.mapBackendTrainings([backendTraining]);
        return trainings.length > 0 ? trainings[0] : undefined;
      }),
      catchError(error => {
        console.error(`Error fetching training with ID ${id}:`, error);
        return throwError(() => error);
      })
    );
  }
}