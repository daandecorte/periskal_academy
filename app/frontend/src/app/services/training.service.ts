import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';

// Interface to represent the language-specific strings structure from backend
export interface LocalizedStrings {
  [language: string]: string;
}

export interface Training {
  id: number;
  // Keep the original title as string for backward compatibility for now, fix this later
  title: string;
  // Keep the original description as string for backward compatibility for now, fix this later
  description: string;
  // Add the multilingual versions as separate properties
  titleLocalized?: LocalizedStrings;
  descriptionLocalized?: LocalizedStrings;
  modules?: any[];
  exams?: any[];
  tips?: any[];
  isActive: boolean;
  
  // Fields not in backend but used in frontend
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
  private useDemoData = true; // Toggle for switching between hardcoded demo data and backend data
  private apiUrl = '/api/trainings';

  private demoTrainings: Training[] = [
    {
      id: 1,
      title: 'Navigation Safety',
      description: 'Navigation safety module with multiple trainings',
      titleLocalized: { 'EN': 'Navigation Safety' },
      descriptionLocalized: { 'EN': 'Navigation safety module with multiple trainings' },
      moduleCount: 3,
      progress: 50,
      hasCertificate: true,
      assigned: true,
      assignedDate: '15-3-2024',
      status: 'in_progress',
      isActive: true,
      languages: ['NL', 'FR', 'DE', 'EN'],
      trainingType: 'Safety'
    },
    {
      id: 2,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      titleLocalized: { 'EN': 'Advanced Charts' },
      descriptionLocalized: { 'EN': 'Working with maritime charts and routes' },
      moduleCount: 2,
      hasCertificate: true,
      status: 'not_started',
      isActive: true,
      languages: ['NL', 'FR', 'DE', 'EN'],
      trainingType: 'Navigation'
    },
    {
      id: 3,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      titleLocalized: { 'EN': 'Advanced Charts' },
      descriptionLocalized: { 'EN': 'Working with maritime charts and routes' },
      moduleCount: 10,
      progress: 30,
      hasCertificate: true,
      assigned: true,
      status: 'in_progress',
      isActive: true,
      languages: ['NL', 'FR', 'DE', 'EN'],
      trainingType: 'Navigation'
    },
    {
      id: 4,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      titleLocalized: { 'EN': 'Advanced Charts' },
      descriptionLocalized: { 'EN': 'Working with maritime charts and routes' },
      moduleCount: 5,
      hasCertificate: true,
      status: 'not_started',
      isActive: true,
      languages: ['NL', 'FR', 'DE', 'EN'],
      trainingType: 'Navigation'
    },
    {
      id: 5,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      titleLocalized: { 'EN': 'Advanced Charts' },
      descriptionLocalized: { 'EN': 'Working with maritime charts and routes' },
      moduleCount: 2,
      hasCertificate: true,
      status: 'not_started',
      isActive: false,
      languages: ['NL', 'FR', 'DE', 'EN'],
      trainingType: 'Navigation'
    }
  ];

  constructor(private http: HttpClient) {}

  // Set data source
  setUseDemoData(useDemo: boolean): void {
    this.useDemoData = useDemo;
  }

  // Get current data source
  getUseDemoData(): boolean {
    return this.useDemoData;
  }

  // Main method to get trainings based on current data source
  getTrainings(): Observable<Training[]> {
    return this.useDemoData ? this.getDemoTrainings() : this.getBackendTrainings();
  }

  // Method to get hardcoded demo trainings
  private getDemoTrainings(): Observable<Training[]> {
    return of(this.demoTrainings);
  }

   // Method to get trainings from backend
   private getBackendTrainings(): Observable<Training[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(backendTrainings => this.mapBackendTrainings(backendTrainings)),
      catchError(error => {
        console.error('Error fetching trainings from backend:', error);
        // Fallback to demo data if backend request fails
        return this.getDemoTrainings();
      })
    );
  }

  // Method to map backend data to Training interface
  private mapBackendTrainings(backendTrainings: any[]): Training[] {
    return backendTrainings.map(backendTraining => {
      // Store the localized data
      const titleLocalized = backendTraining.title || { 'EN': 'Untitled Training' };
      const descriptionLocalized = backendTraining.description || { 'EN': 'No description available' };
      
      // Get default language or first available language
      const defaultLanguage = 'EN';
      const titleText = titleLocalized[defaultLanguage] || Object.values(titleLocalized)[0] || 'Untitled Training';
      const descriptionText = descriptionLocalized[defaultLanguage] || Object.values(descriptionLocalized)[0] || 'No description available';
      
      // Create a Training object with required fields
      const training: Training = {
        id: backendTraining.id,
        title: titleText, // String for backward compatibility
        description: descriptionText, // String for backward compatibility
        titleLocalized: titleLocalized, // Store the full localized data
        descriptionLocalized: descriptionLocalized, // Store the full localized data
        moduleCount: backendTraining.modules ? backendTraining.modules.length : 0,
        hasCertificate: backendTraining.exams && backendTraining.exams.length > 0,
        status: 'not_started',
        isActive: backendTraining.isActive !== undefined ? backendTraining.isActive : false,
        modules: backendTraining.modules,
        exams: backendTraining.exams,
        tips: backendTraining.tips
      };

      // This still needs to be properly implemented using the new progress controller!!
      if (backendTraining.progress !== undefined) training.progress = backendTraining.progress;
      if (backendTraining.assigned !== undefined) training.assigned = backendTraining.assigned;
      if (backendTraining.assignedDate) training.assignedDate = backendTraining.assignedDate;
      if (backendTraining.languages) training.languages = backendTraining.languages;
      if (backendTraining.trainingType) training.trainingType = backendTraining.trainingType;

      return training;
    });
  }

 // Method to get a single training by ID
 getTrainingById(id: number): Observable<Training | undefined> {
  if (this.useDemoData) {
    const training = this.demoTrainings.find(m => m.id === id);
    return of(training);
  } else {
    return this.http.get<any>(`${this.apiUrl}/${id}`).pipe(
      map(backendTraining => {
        const trainings = this.mapBackendTrainings([backendTraining]);
        return trainings.length > 0 ? trainings[0] : undefined;
      }),
      catchError(error => {
        console.error(`Error fetching training with ID ${id}:`, error);
        // Fallback to demo data if backend request fails
        const training = this.demoTrainings.find(m => m.id === id);
        return of(training);
      })
    );
  }
}
}