import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Training, TrainingService } from '../services/training.service';
import { AdminTrainingCardComponent } from '../admin-training-card/admin-training-card.component';
import { RouterModule, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { LanguageService } from '../services/language.service';

@Component({
  selector: 'app-admin-trainings',
  standalone: true,
  imports: [CommonModule, FormsModule, AdminTrainingCardComponent, RouterModule, TranslateModule],
  templateUrl: './admin-trainings.component.html',
  styleUrl: './admin-trainings.component.css'
})
export class AdminTrainingsComponent implements OnInit {
  trainings: Training[] = [];
  allTrainings: Training[] = [];
  searchTerm: string = '';
  loading: boolean = true;
  error: string | null = null;
  currentLanguage: string = 'EN'; // Default language

  constructor(
    protected trainingService: TrainingService, 
    private router: Router,
    private languageService: LanguageService
  ) {}

  ngOnInit(): void {
    // Subscribe to language changes
    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = this.mapLanguageCode(language);
      // Re-filter trainings to update the UI with the new language
      if (this.allTrainings.length > 0) {
        this.filterTrainings();
      }
    });
    
    this.loadTrainings();
  }

  mapLanguageCode(language: any): string {
    const languageMappings: { [key: string]: string } = {
      'ENGLISH': 'EN',
      'FRENCH': 'FR',
      'DUTCH': 'NL',
      'GERMAN': 'DE'
    };
    
    // If it's already a code, return it
    if (['EN', 'FR', 'NL', 'DE'].includes(language)) {
      return language;
    }
    
    // Otherwise map the language name to code
    return languageMappings[language] || 'EN'; // Default to EN if mapping not found
  }

  loadTrainings(): void {
    this.loading = true;
    this.error = null;
    
    this.trainingService.getTrainings().subscribe({
      next: (trainings) => {
        this.allTrainings = trainings;
        this.filterTrainings();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading trainings:', err);
        this.error = 'Failed to load trainings. Please try again later.';
        this.loading = false;
      }
    });
  }

  filterTrainings(): void {
    // Filter trainings using localized titles when available
    this.trainings = this.searchTerm
      ? this.allTrainings.filter(training => {
          let searchText = training.title.toLowerCase(); // Default to standard title
          
          // Use localized title if available
          if (training.titleLocalized && training.titleLocalized[this.currentLanguage]) {
            searchText = training.titleLocalized[this.currentLanguage].toLowerCase();
          }
          
          return searchText.includes(this.searchTerm.toLowerCase());
        })
      : this.allTrainings;
  }

  onSearchChange(): void {
    this.filterTrainings();
  }

  navigateToAddTraining(): void {
    this.router.navigate(['/add-training']);
  }
}