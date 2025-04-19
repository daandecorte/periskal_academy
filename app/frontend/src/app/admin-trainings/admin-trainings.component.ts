import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Training, TrainingService } from '../services/training.service';
import { AdminTrainingCardComponent } from '../admin-training-card/admin-training-card.component';
import { RouterModule, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

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

  constructor(protected trainingService: TrainingService, private router: Router) {}

  ngOnInit(): void {
    this.loadTrainings();
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
    this.trainings = this.searchTerm
      ? this.allTrainings.filter(training => 
          training.title.toLowerCase().includes(this.searchTerm.toLowerCase())
        )
      : this.allTrainings;
  }

  onSearchChange(): void {
    this.filterTrainings();
  }

  navigateToAddTraining(): void {
    this.router.navigate(['/add-training']);
  }
}