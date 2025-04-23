import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TrainingService, Training, LocalizedStrings } from '../../services/training.service';
import { TrainingEditService, TrainingUpdateDTO } from '../../services/training-edit.service';

@Component({
  selector: 'app-basic-info',
  standalone: true,
  imports: [RouterLink, TranslateModule, CommonModule, ReactiveFormsModule],
  templateUrl: './basic-info.component.html',
  styleUrl: './basic-info.component.css'
})
export class BasicInfoComponent implements OnInit {
  trainingForm!: FormGroup;
  training?: Training;
  trainingId!: number;
  loading = true;
  saving = false;
  error = false;
  saveError = false;
  currentLanguage = 'EN'; // Default language
  availableLanguages = ['EN', 'FR', 'NL', 'DE']; // Available languages in your app

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService,
    private trainingEditService: TrainingEditService,
    private translateService: TranslateService
  ) { }

  ngOnInit(): void {
    // Initialize the form
    this.initForm();
    
    // Get the current language from translation service
    this.currentLanguage = this.translateService.currentLang || 'EN';
    
    // Get parent route params (training ID)
    this.route.parent?.paramMap.subscribe(params => {
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

  initForm(): void {
    this.trainingForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      isActive: [true]
    });
  }

  loadTraining(): void {
    this.trainingService.getTrainingById(this.trainingId).subscribe({
      next: (training) => {
        if (training) {
          this.training = training;
          this.populateForm();
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

  populateForm(): void {
    if (!this.training) return;

    // Get localized values for the current language or fallback
    const title = this.getLocalizedValue(this.training.titleLocalized);
    const description = this.getLocalizedValue(this.training.descriptionLocalized);

    this.trainingForm.patchValue({
      title: title,
      description: description,
      isActive: this.training.isActive
    });
  }

  getLocalizedValue(localizedStrings?: LocalizedStrings): string {
    if (!localizedStrings) return '';
    
    // Try to get value in current language
    if (localizedStrings[this.currentLanguage]) {
      return localizedStrings[this.currentLanguage];
    }
    
    // Fallback to first available language
    const keys = Object.keys(localizedStrings);
    if (keys.length > 0) {
      return localizedStrings[keys[0]];
    }
    
    return '';
  }

  onSubmit(): void {
    if (this.trainingForm.invalid) {
      // Mark all fields as touched to trigger validation messages
      Object.keys(this.trainingForm.controls).forEach(key => {
        this.trainingForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.saving = true;
    
    // Prepare data for backend
    const updateData: TrainingUpdateDTO = {
      title: {},
      description: {},
      isActive: this.trainingForm.value.isActive
    };

    // Update data for current language only
    // For a real app, you might want to update all languages or provide UI for editing each language
    if (this.training?.titleLocalized) {
      updateData.title = this.trainingEditService.convertToBackendFormat(this.training.titleLocalized);
      updateData.title['ENGLISH'] = this.trainingForm.value.title; // Just update English for simplicity
    } else {
      updateData.title['ENGLISH'] = this.trainingForm.value.title;
    }

    if (this.training?.descriptionLocalized) {
      updateData.description = this.trainingEditService.convertToBackendFormat(this.training.descriptionLocalized);
      updateData.description['ENGLISH'] = this.trainingForm.value.description;
    } else {
      updateData.description['ENGLISH'] = this.trainingForm.value.description;
    }

    this.trainingEditService.updateTrainingBasicInfo(this.trainingId, updateData).subscribe({
      next: () => {
        this.saving = false;
        // Navigate back to admin trainings page
        this.router.navigate(['/admin/trainings']);
      },
      error: () => {
        this.saveError = true;
        this.saving = false;
      }
    });
  }

  changeLanguage(language: string): void {
    this.currentLanguage = language;
    // Save current form data
    const currentData = {
      title: this.trainingForm.value.title,
      description: this.trainingForm.value.description
    };
    
    // Update form with data for the selected language
    if (this.training?.titleLocalized && this.training?.titleLocalized[language]) {
      this.trainingForm.patchValue({ title: this.training.titleLocalized[language] });
    }
    
    if (this.training?.descriptionLocalized && this.training?.descriptionLocalized[language]) {
      this.trainingForm.patchValue({ description: this.training.descriptionLocalized[language] });
    }
    
    // Store current language data to the appropriate language property
    if (this.training && this.training.titleLocalized) {
      this.training.titleLocalized[this.currentLanguage] = currentData.title;
    }
    
    if (this.training && this.training.descriptionLocalized) {
      this.training.descriptionLocalized[this.currentLanguage] = currentData.description;
    }
  }
}