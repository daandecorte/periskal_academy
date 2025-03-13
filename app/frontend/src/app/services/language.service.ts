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
    if (savedLanguage) {
      this.currentLanguage.next(savedLanguage);
    }
  }

  setLanguage(lang: string) {
    this.currentLanguage.next(lang);
    localStorage.setItem('language', lang);
  }

  getLanguage(): string {
    return this.currentLanguage.value;
  }
}
