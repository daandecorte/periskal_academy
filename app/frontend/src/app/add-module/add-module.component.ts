import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterLink, RouterLinkActive, RouterModule, RouterOutlet } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { filter, Subscription } from 'rxjs';

@Component({
  selector: 'app-add-module',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule, TranslateModule],
  templateUrl: './add-module.component.html',
  styleUrl: './add-module.component.css'
})
export class AddModuleComponent {
  steps = ['basic-setup', 'trainings', 'exam', 'preview'];
  currentStep: string = this.steps[0]; 
  private routerSubscription!: Subscription;

  constructor(private router: Router, private route: ActivatedRoute) {}

  ngOnInit() {
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        const currentPath = this.route.snapshot.firstChild?.url[0]?.path || this.steps[0];
        if (this.steps.includes(currentPath)) {
          this.currentStep = currentPath;
        }
      });
  }

  goToNextStep() {
    const currentIndex = this.steps.indexOf(this.currentStep);
    if (currentIndex < this.steps.length - 1) {
      this.currentStep = this.steps[currentIndex + 1];
      this.router.navigate([`/add-module/${this.currentStep}`]);
    }
  }

  goToPreviousStep() {
    const currentIndex = this.steps.indexOf(this.currentStep);
    if (currentIndex > 0) {
      this.currentStep = this.steps[currentIndex - 1];
      this.router.navigate([`/add-module/${this.currentStep}`]);
    }
  }

  publishModule() {
    this.router.navigate(['/modules']);
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }
  
}
