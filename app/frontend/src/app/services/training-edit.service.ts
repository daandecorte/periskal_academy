import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of, tap } from 'rxjs';
import { Training, LocalizedStrings } from '../services/training.service';

export interface TrainingUpdateDTO {
  title: { [key: string]: string };
  description: { [key: string]: string };
  isActive: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class TrainingEditService {
  private apiUrl = '/api/trainings';

  constructor(private http: HttpClient) {}

  // Update basic training information
  updateTrainingBasicInfo(id: number, data: TrainingUpdateDTO): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, data).pipe(
      tap(() => console.log('Training updated successfully')),
      catchError(error => {
        console.error('Error updating training:', error);
        throw error;
      })
    );
  }

  // Convert frontend localized strings to backend format
  convertToBackendFormat(localizedStrings: LocalizedStrings): { [key: string]: string } {
    const backendFormat: { [key: string]: string } = {};
    
    // Map frontend language codes to backend enum values
    const languageMap: { [key: string]: string } = {
      'EN': 'ENGLISH',
      'FR': 'FRENCH',
      'NL': 'DUTCH',
      'DE': 'GERMAN'
    };
    
    if (localizedStrings) {
      Object.entries(localizedStrings).forEach(([lang, value]) => {
        // Convert language code to backend enum format
        backendFormat[languageMap[lang] || lang] = value;
      });
    }
    
    return backendFormat;
  }
}