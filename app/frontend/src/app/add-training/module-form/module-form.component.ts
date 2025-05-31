import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { NewTrainingService, ITranslated } from '../new-training.service';

@Component({
  selector: 'app-module-form',
  imports: [RouterLink, RouterLinkActive, RouterOutlet, TranslatePipe],
  templateUrl: './module-form.component.html',
  styleUrl: './module-form.component.css',
})
export class ModuleFormComponent {
  constructor(
    private trainingService: NewTrainingService,
    private router: Router
  ) {}

  ngAfterViewInit() {
    const buttonsLanguage =
      document.querySelectorAll<HTMLButtonElement>('.modal-button');

    buttonsLanguage.forEach((button) => {
      button.addEventListener('click', () => {
        buttonsLanguage.forEach((btn) => btn.classList.remove('selected'));

        button.classList.add('selected');

        this.trainingService.setButtonSelectedLanguage(
          button.value as keyof ITranslated
        );
      });
    });
  }

  saveModule() {
    this.trainingService.saveModule();
    this.router.navigate(['/add-training']);
    this.trainingService.currentIndex=1;
  }
  
  cancelModule() {
    this.trainingService.resetModule();
    this.router.navigate(['/add-training']);
    this.trainingService.currentIndex=1;
  }
}
