import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterLink, RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-add-module',
  imports: [RouterOutlet],
  templateUrl: './add-module.component.html',
  styleUrl: './add-module.component.css'
})
export class AddModuleComponent {

  steps = ['basic-setup', 'trainings', 'exam', 'preview'];

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
  }
  
  /*constructor(private router: Router) {}

  goToNextStep() {
    const currentPath = this.router.url.split('/').pop();
    switch(currentPath) {
      case 'basic-setup':
        this.router.navigate(['/add-module/trainings']);
        break;
      case 'trainings':
        this.router.navigate(['/add-module/exam']);
        break;
      case 'exam':
        this.router.navigate(['/add-module/preview']);
        break;
      default:
        break;
    }
  }

  goToPreviousStep() {
    const currentPath = this.router.url.split('/').pop();
    switch(currentPath) {
      case 'trainings':
        this.router.navigate(['/add-module/basic-setup']);
        break;
      case 'exam':
        this.router.navigate(['/add-module/trainings']);
        break;
      case 'preview':
        this.router.navigate(['/add-module/exam']);
        break;
      default:
        break;
    }
  }*/
}
