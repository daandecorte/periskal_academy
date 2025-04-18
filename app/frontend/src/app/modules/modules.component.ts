import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Module, ModuleService } from '../services/module.service';
import { ModuleCardComponent } from '../module-card/module-card.component';
import { AssignedModuleCardComponent } from '../assigned-module-card/assigned-module-card.component';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TraineeChatComponent } from '../trainee-chat/trainee-chat.component';
import { LanguageService } from '../services/language.service';

@Component({
  selector: 'app-modules',
  standalone: true,
  imports: [CommonModule, ModuleCardComponent, AssignedModuleCardComponent, FormsModule, TranslateModule, TraineeChatComponent],
  templateUrl: './modules.component.html',
  styleUrl: './modules.component.css'
})
export class ModulesComponent implements OnInit {
  modules: Module[] = [];
  assignedModules: Module[] = [];
  allModules: Module[] = [];
  searchTerm: string = '';
  loading: boolean = true;
  error: string | null = null;
  currentLanguage: string = 'EN'; // Default language

  constructor(
    protected moduleService: ModuleService,
    private languageService: LanguageService
  ) {}

  ngOnInit(): void {
    // Subscribe to language changes
    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = this.mapLanguageCode(language);
      // Re-filter modules to update the UI with the new language
      if (this.allModules.length > 0) {
        this.filterModules();
      }
    });
    
    this.loadModules();
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

  loadModules(): void {
    this.loading = true;
    this.error = null;
    
    this.moduleService.getModules().subscribe({
      next: (modules) => {
        this.allModules = modules;
        this.filterModules();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading modules:', err);
        this.error = 'Failed to load modules. Please try again later.';
        this.loading = false;
      }
    });
  }

  toggleDataSource(): void {
    const currentSetting = this.moduleService.getUseDemoData();
    this.moduleService.setUseDemoData(!currentSetting);
    this.loadModules();
  }

  filterModules(): void {
    // Filter modules using localized titles when available
    const filteredModules = this.searchTerm
      ? this.allModules.filter(module => {
          let searchText = module.title.toLowerCase(); // Default to standard title
          
          // Use localized title if available
          if (module.titleLocalized && module.titleLocalized[this.currentLanguage]) {
            searchText = module.titleLocalized[this.currentLanguage].toLowerCase();
          }
          
          return searchText.includes(this.searchTerm.toLowerCase());
        })
      : this.allModules;

    this.modules = filteredModules;
    this.assignedModules = filteredModules.filter(module => module.assigned);
  }

  onSearchChange(): void {
    this.filterModules();
  }
}