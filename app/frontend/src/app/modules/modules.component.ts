import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Module, ModuleService } from '../services/module.service';
import { ModuleCardComponent } from '../module-card/module-card.component';
import { AssignedModuleCardComponent } from '../assigned-module-card/assigned-module-card.component';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-modules',
  standalone: true,
  imports: [CommonModule, ModuleCardComponent, AssignedModuleCardComponent, FormsModule, TranslateModule],
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

  constructor(protected moduleService: ModuleService) {}

  ngOnInit(): void {
    this.loadModules();
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
    const filteredModules = this.searchTerm
      ? this.allModules.filter(module => 
          module.title.toLowerCase().includes(this.searchTerm.toLowerCase())
        )
      : this.allModules;

    this.modules = filteredModules;
    this.assignedModules = filteredModules.filter(module => module.assigned);
  }

  onSearchChange(): void {
    this.filterModules();
  }
}