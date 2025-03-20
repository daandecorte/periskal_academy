import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterLink, RouterLinkActive, RouterModule, RouterOutlet } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-add-module',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './add-module.component.html',
  styleUrl: './add-module.component.css'
})
export class AddModuleComponent {
  steps = ['basic-setup', 'trainings', 'exam', 'preview'];
  currentStep: string = this.steps[0]; 

  constructor(private router: Router) {}

  goToNextStep() {
    const currentPath = this.router.url.split('/').pop();
    const currentIndex = this.steps.indexOf(currentPath || '');
    if (currentIndex >= 0 && currentIndex < this.steps.length - 1) {
      this.router.navigate([`/add-module/${this.steps[currentIndex + 1]}`]);
      this.currentStep = this.steps[currentIndex + 1];
    }
  }

  goToPreviousStep() {
    const currentPath = this.router.url.split('/').pop();
    const currentIndex = this.steps.indexOf(currentPath || '');
    if (currentIndex > 0 && currentPath !== 'basic-setup') {
      this.router.navigate([`/add-module/${this.steps[currentIndex - 1]}`]);
      this.currentStep = this.steps[currentIndex - 1]; 
    }
  }

  publishModule() {
    this.router.navigate(['/modules']); // Navigeren naar de /modules route
  }
  
  /*steps = ['basic-setup', 'trainings', 'exam', 'preview'];
  currentStep : string = '';

  constructor(private router: Router) {}

  goToNextStep() {
    const currentPath = this.router.url.split('/').pop();
    const currentIndex = this.steps.indexOf(currentPath || '');
    if (currentIndex >= 0 && currentIndex < this.steps.length - 1) {
      this.router.navigate([`/add-module/${this.steps[currentIndex + 1]}`]);
    }
  }

  goToPreviousStep() {
    const currentPath = this.router.url.split('/').pop();
    const currentIndex = this.steps.indexOf(currentPath || '');
    if (currentIndex > 0) {
      this.router.navigate([`/add-module/${this.steps[currentIndex - 1]}`]);
    }
  }*/
}
