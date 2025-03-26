import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LanguageService {
  private currentLanguage = new BehaviorSubject<string>('ENGLISH');
  public currentLanguage$ = this.currentLanguage.asObservable();

  constructor() {
    const savedLanguage = localStorage.getItem('language');
    if (savedLanguage && SUPPORTED_LANGUAGES.includes(savedLanguage)) {
      this.currentLanguage.next(savedLanguage);
    }
  }

  setLanguage(lang: string) {
    if(SUPPORTED_LANGUAGES.includes(lang)) {
      this.currentLanguage.next(lang);
      localStorage.setItem('language', lang);
    }
    else {
      console.warn(`Language ${lang} is not supported`)
    }
  }

  getLanguage(): string {
    return this.currentLanguage.value;
  }
}
export const SUPPORTED_LANGUAGES = ['ENGLISH', 'FRENCH', 'GERMAN', 'DUTCH'];
