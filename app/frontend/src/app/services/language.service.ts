import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AuthService } from './auth.service';

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

  async setLanguage(lang: string) {
    if(SUPPORTED_LANGUAGES.includes(lang)) {
      this.currentLanguage.next(lang);
      localStorage.setItem('language', lang);

      let user= localStorage.getItem("currentUser");
      if(user) {
        let periskalId=JSON.parse(user).ID;

        let result = await fetch('/api/users', {
          method:'PUT',
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
          },
          body: JSON.stringify({
            periskal_id: periskalId,
            language: lang
          })
        });
        console.log(result);
      }
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
