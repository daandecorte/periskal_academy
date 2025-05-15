import { Component, ElementRef, ViewChild } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';
import { LanguageService } from '../../services/language.service';
import {
  INewTraining,
  NewTrainingService,
  ITranslated,
} from '../new-training.service';

@Component({
  selector: 'app-basic-setup',
  imports: [TranslateModule, FormsModule],
  templateUrl: './basic-setup.component.html',
  styleUrl: './basic-setup.component.css',
})
export class BasicSetupComponent {
  @ViewChild('titleRef') titleRef!: ElementRef<HTMLInputElement>;
  @ViewChild('descriptionRef') descriptionRef!: ElementRef<HTMLTextAreaElement>;
  @ViewChild('periodRef') periodRef!: ElementRef<HTMLInputElement>;
  @ViewChild('priceRef') priceRef!: ElementRef<HTMLInputElement>;

  currentLanguage: keyof ITranslated = 'ENGLISH';
  selectedButtonLanguage: keyof ITranslated = 'ENGLISH';

  constructor(
    private languageService: LanguageService,
    private trainingService: NewTrainingService
  ) {
    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = language as keyof ITranslated;
    });
  }

  public get NewTraining() {
    return this.trainingService.newTraining;
  }

  public set NewTraining(value: INewTraining) {
    this.trainingService.newTraining = value;
  }

  ngAfterViewInit() {
    const buttonsLanguage =
      document.querySelectorAll<HTMLButtonElement>('.modal-button');

    buttonsLanguage.forEach((button) => {
      button.addEventListener('click', () => {
        buttonsLanguage.forEach((btn) => btn.classList.remove('selected'));

        button.classList.add('selected');

        this.changeInputLanguage();
      });
    });

    this.titleRef.nativeElement.value =
      (this.trainingService.newTraining.title[
        this.selectedButtonLanguage
      ] as string) || '';
    this.descriptionRef.nativeElement.value =
      (this.trainingService.newTraining.description[
        this.selectedButtonLanguage
      ] as string) || '';
    this.periodRef.nativeElement.value = `${this.trainingService.newTraining.certificate.validityPeriod}`;
    this.priceRef.nativeElement.value = `${this.trainingService.newTraining.certificate.price}`;
    console.log(this.trainingService.newTraining);
  }

  changeInputLanguage() {
    const buttonLanguage =
      document.querySelector<HTMLButtonElement>('.selected');
    if (!buttonLanguage) return;

    this.selectedButtonLanguage = buttonLanguage.value as keyof ITranslated;

    this.titleRef.nativeElement.value =
      (this.trainingService.newTraining.title[
        this.selectedButtonLanguage
      ] as string) || '';
    this.descriptionRef.nativeElement.value =
      (this.trainingService.newTraining.description[
        this.selectedButtonLanguage
      ] as string) || '';
  }

  titleChange() {
    this.trainingService.newTraining.title[this.selectedButtonLanguage] =
      this.titleRef.nativeElement.value;
  }

  descriptionChange() {
    this.trainingService.newTraining.description[this.selectedButtonLanguage] =
      this.descriptionRef.nativeElement.value;
  }

  periodChange() {
    this.trainingService.newTraining.certificate.validityPeriod = Number(
      this.periodRef.nativeElement.value
    );
  }

  priceChange() {
    this.trainingService.newTraining.certificate.price = Number(
      this.priceRef.nativeElement.value
    );
  }
}
