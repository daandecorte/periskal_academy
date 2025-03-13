import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

// This service contains hardcoded modules, modules should later be pulled from te backend

export interface Module {
  id: number;
  title: string;
  description: string;
  trainingCount: number;
  progress?: number; // percentage of completion
  hasCertificate: boolean;
  assigned?: boolean;
  status: 'not_started' | 'in_progress' | 'completed';
}

@Injectable({
  providedIn: 'root'
})
export class ModuleService {
  private modules: Module[] = [
    {
      id: 1,
      title: 'Navigation Safety',
      description: 'Navigation safety training with multiple modules',
      trainingCount: 3,
      progress: 50,
      hasCertificate: true,
      assigned: true,
      status: 'in_progress'
    },
    {
      id: 2,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      trainingCount: 2,
      hasCertificate: true,
      status: 'not_started'
    },
    {
      id: 3,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      trainingCount: 10,
      progress: 30,
      hasCertificate: true,
      assigned: true,
      status: 'in_progress'
    },
    {
      id: 4,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      trainingCount: 5,
      hasCertificate: true,
      status: 'not_started'
    },
    {
      id: 5,
      title: 'Advanced Charts',
      description: 'Working with maritime charts and routes',
      trainingCount: 2,
      hasCertificate: true,
      status: 'not_started'
    }
  ];

  getModules(): Observable<Module[]> {
    return of(this.modules);
  }
}
