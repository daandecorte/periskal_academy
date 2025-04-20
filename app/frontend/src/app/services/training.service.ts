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
  private useDemoData = false; // Toggle for switching between hardcoded demo data and backend data
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
    
    // Don't use a hardcoded default language - we'll retrieve this dynamically in components
    const titleKeys = Object.keys(titleLocalized);
    const defaultTitle = titleKeys.length > 0 ? titleLocalized[titleKeys[0]] : 'Untitled Training';
    
    const descriptionKeys = Object.keys(descriptionLocalized);
    const defaultDescription = descriptionKeys.length > 0 ? descriptionLocalized[descriptionKeys[0]] : 'No description available';
    
    // Create a Training object with required fields
    const training: Training = {
      id: backendTraining.id,
      title: defaultTitle, // This will be overridden by getLocalizedTitle in components
      description: defaultDescription, // This will be overridden by getLocalizedDescription in components
      titleLocalized: titleLocalized,
      descriptionLocalized: descriptionLocalized,
      moduleCount: backendTraining.modules ? backendTraining.modules.length : 0,
      hasCertificate: backendTraining.exams && backendTraining.exams.length > 0,
      status: 'not_started', // Default status
      isActive: backendTraining.isActive !== undefined ? backendTraining.isActive : false,
      modules: this.processModules(backendTraining.modules),
      exams: backendTraining.exams,
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

private processModules(modules: any[]): any[] {
  if (!modules || modules.length === 0) return [];
  
  return modules.map(module => {
    // Process module data but don't pre-select a language
    // This ensures we keep all language options available
    return {
      ...module,
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