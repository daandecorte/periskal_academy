import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TrainingService } from '../services/training.service';
import { Training } from '../services/training.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-edit-training',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterOutlet, TranslateModule, CommonModule],
  templateUrl: './admin-edit-training.component.html',
  styleUrl: './admin-edit-training.component.css'
})
export class AdminEditTrainingComponent implements OnInit {
  trainingId!: number;
  training?: Training;
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private trainingService: TrainingService
  ) {}

  ngOnInit(): void {
    // Get the training ID from URL parameters
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.trainingId = +id;
        this.loadTraining();
      } else {
        this.error = true;
        this.loading = false;
      }
    });
  }

  loadTraining(): void {
    this.trainingService.getTrainingById(this.trainingId).subscribe({
      next: (training) => {
        if (training) {
          this.training = training;
        } else {
          this.error = true;
        }
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }
}