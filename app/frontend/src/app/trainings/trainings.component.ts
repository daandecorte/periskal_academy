import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Training, TrainingService } from '../services/training.service';
import { TrainingCardComponent } from '../training-card/training-card.component';
import { AssignedTrainingCardComponent } from '../assigned-training-card/assigned-training-card.component';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TraineeChatComponent } from '../trainee-chat/trainee-chat.component';
import { LanguageService } from '../services/language.service';

@Component({
  selector: 'app-trainings',
  standalone: true,
  imports: [CommonModule, TrainingCardComponent, AssignedTrainingCardComponent, FormsModule, TranslateModule, TraineeChatComponent],
  templateUrl: './trainings.component.html',
  styleUrl: './trainings.component.css'
})
export class TrainingsComponent implements OnInit {
  trainings: Training[] = [];
  assignedTrainings: Training[] = [];
  allTrainings: Training[] = [];
  searchTerm: string = '';
  loading: boolean = true;
  error: string | null = null;
  currentLanguage: string = 'EN'; // Default language

  constructor(
    protected trainingService: TrainingService,
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

  toggleDataSource(): void {
    const currentSetting = this.trainingService.getUseDemoData();
    this.trainingService.setUseDemoData(!currentSetting);
    this.loadTrainings();
  }

  filterTrainings(): void {
    // Filter trainings using localized titles when available
    const filteredTrainings = this.searchTerm
      ? this.allTrainings.filter(training => {
          let searchText = training.title.toLowerCase(); // Default to standard title
          
          // Use localized title if available
          if (training.titleLocalized && training.titleLocalized[this.currentLanguage]) {
            searchText = training.titleLocalized[this.currentLanguage].toLowerCase();
          }
          
          return searchText.includes(this.searchTerm.toLowerCase());
        })
      : this.allTrainings;

    this.trainings = filteredTrainings;
    this.assignedTrainings = filteredTrainings.filter(training => training.assigned);
  }

  onSearchChange(): void {
    this.filterTrainings();
  }
}