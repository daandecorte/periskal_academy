import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';


export interface Module {
  id: number;
  title: string;
  description: string;
  trainingCount: number;
  progress?: number; // percentage of completion
  hasCertificate: boolean;
  assigned?: boolean;
  assignedDate?: string;
  status: 'not_started' | 'in_progress' | 'completed';
  isActive?: boolean;
  languages?: string[]; // ['NL', 'FR', 'DE', 'EN']
  moduleType?: 'Navigation' | 'Safety' | 'Communication';
}

@Injectable({
  providedIn: 'root'
})
export class ModuleService {
  private useDemoData = true; // Toggle for switching between hardcoded demo data and backend data
  private apiUrl = '/api/modules';

  private demoModules: Module[] = [
    {
      id: 1,
      title: 'Navigation Safety',
      description: 'Navigation safety training with multiple modules',
      trainingCount: 3,
      progress: 50,
      hasCertificate: true,
      assigned: true,
      assignedDate: '15-3-2024',
      status: 'in_progress',
      isActive: true,
      languages: ['NL', 'FR', 'DE', 'EN'],
      moduleType: 'Safety'
    },
    {
      id: 2,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      trainingCount: 2,
      hasCertificate: true,
      status: 'not_started',
      isActive: true,
      languages: ['NL', 'FR', 'DE', 'EN'],
      moduleType: 'Navigation'
    },
    {
      id: 3,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      trainingCount: 10,
      progress: 30,
      hasCertificate: true,
      assigned: true,
      status: 'in_progress',
      isActive: true,
      languages: ['NL', 'FR', 'DE', 'EN'],
      moduleType: 'Navigation'
    },
    {
      id: 4,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      trainingCount: 5,
      hasCertificate: true,
      status: 'not_started',
      isActive: true,
      languages: ['NL', 'FR', 'DE', 'EN'],
      moduleType: 'Navigation'
    },
    {
      id: 5,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      trainingCount: 2,
      hasCertificate: true,
      status: 'not_started',
      isActive: false,
      languages: ['NL', 'FR', 'DE', 'EN'],
      moduleType: 'Navigation'
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

  // Main method to get modules based on current data source
  getModules(): Observable<Module[]> {
    return this.useDemoData ? this.getDemoModules() : this.getBackendModules();
  }

  // Method to get hardcoded demo modules
  private getDemoModules(): Observable<Module[]> {
    return of(this.demoModules);
  }

   // Method to get modules from backend
   private getBackendModules(): Observable<Module[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(backendModules => this.mapBackendModules(backendModules)),
      catchError(error => {
        console.error('Error fetching modules from backend:', error);
        // Fallback to demo data if backend request fails
        return this.getDemoModules();
      })
    );
  }

  // Method to map backend data to Module interface
  private mapBackendModules(backendModules: any[]): Module[] {
    return backendModules.map(backendModule => {
      // Create a Module object with required fields
      const module: Module = {
        id: backendModule.id,
        title: backendModule.title || 'Untitled Module',
        description: backendModule.description || 'No description available',
        trainingCount: backendModule.trainingCount || 0,
        hasCertificate: backendModule.hasCertificate || false,
        status: backendModule.status || 'not_started'
      };

      // This is data that does not yet exist in the backend. 
      // The way these are implemented will probably change because it is mostly related to user progress.
      // But it is here just in case lol.
      if (backendModule.progress !== undefined) module.progress = backendModule.progress;
      if (backendModule.assigned !== undefined) module.assigned = backendModule.assigned;
      if (backendModule.assignedDate) module.assignedDate = backendModule.assignedDate;
      if (backendModule.isActive !== undefined) module.isActive = backendModule.isActive;
      if (backendModule.languages) module.languages = backendModule.languages;
      if (backendModule.moduleType) module.moduleType = backendModule.moduleType;

      return module;
    });
  }

 // Method to get a single module by ID
 getModuleById(id: number): Observable<Module | undefined> {
  if (this.useDemoData) {
    const module = this.demoModules.find(m => m.id === id);
    return of(module);
  } else {
    return this.http.get<any>(`${this.apiUrl}/${id}`).pipe(
      map(backendModule => {
        const modules = this.mapBackendModules([backendModule]);
        return modules.length > 0 ? modules[0] : undefined;
      }),
      catchError(error => {
        console.error(`Error fetching module with ID ${id}:`, error);
        // Fallback to demo data if backend request fails
        const module = this.demoModules.find(m => m.id === id);
        return of(module);
      })
    );
  }
}
}