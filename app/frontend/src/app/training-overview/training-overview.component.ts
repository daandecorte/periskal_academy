import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ModuleService, Module } from '../services/module.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPlayCircle, faCheckCircle } from '@fortawesome/free-solid-svg-icons';

interface TrainingSection {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  duration: string;
  questionCount: number;
}

@Component({
  selector: 'app-training-overview',
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule],
  templateUrl: './training-overview.component.html',
  styleUrl: './training-overview.component.css'
})
export class TrainingOverviewComponent implements OnInit {
  moduleId: number = 0;
  module?: Module;
  trainingSections: TrainingSection[] = [];
  modulesCompleted: number = 0;
  totalModules: number = 0;
  
  // Font Awesome icons
  faPlayCircle = faPlayCircle;
  faCheckCircle = faCheckCircle;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private moduleService: ModuleService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.moduleId = +params['id'];
      this.loadModuleData();
    });
  }

  loadModuleData(): void {
    this.moduleService.getModuleById(this.moduleId).subscribe(
      module => {
        if (module) {
          this.module = module;
          this.generateTrainingSections();
          this.calculateProgress();
        } else {
          // Handle module not found
          this.router.navigate(['/modules']);
        }
      },
      error => {
        console.error('Error loading module:', error);
        // Handle error
        this.router.navigate(['/modules']);
      }
    );
  }

  generateTrainingSections(): void {
    // For testing purposes
    this.trainingSections = [
      {
        id: 1,
        title: 'Basic Navigation Safety',
        description: 'Understanding fundamental navigation protocols',
        completed: true,
        duration: '15 min video',
        questionCount: 3
      },
      {
        id: 2,
        title: 'Emergency Procedures',
        description: 'Learn how to handle emergency situations',
        completed: false,
        duration: '11 min video',
        questionCount: 2
      },
      {
        id: 3,
        title: 'Communication Protocols',
        description: 'Maritime communication best practices',
        completed: false,
        duration: '10 min video',
        questionCount: 4
      }
    ];
  }

  calculateProgress(): void {
    // Calculate completed modules
    this.modulesCompleted = this.trainingSections.filter(section => section.completed).length;
    this.totalModules = this.trainingSections.length;
  }

  goToTraining(sectionId: number): void {
    // Navigate to the specific training section
    this.router.navigate(['/modules', this.moduleId, 'training', sectionId]);
  }

  goBack(): void {
    this.router.navigate(['/modules']);
  }

  goToCertificate(): void {
    // Check if all modules are completed
    if (this.modulesCompleted === this.totalModules) {
      this.router.navigate(['/modules', this.moduleId, 'certificate']);
    }
  }

  getActionText(completed: boolean): string {
    return completed ? 'Review' : 'Start';
  }

  getProgressPercentage(): number {
    if (this.totalModules === 0) return 0;
    return (this.modulesCompleted / this.totalModules) * 100;
  }
}