import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Module, ModuleService } from '../services/module.service';
import { AdminModuleCardComponent } from '../admin-module-card/admin-module-card.component';
import { RouterModule, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-admin-modules',
  standalone: true,
  imports: [CommonModule, FormsModule, AdminModuleCardComponent, RouterModule, TranslateModule],
  templateUrl: './admin-modules.component.html',
  styleUrl: './admin-modules.component.css'
})
export class AdminModulesComponent implements OnInit {
  modules: Module[] = [];
  allModules: Module[] = [];
  searchTerm: string = '';
  loading: boolean = true;
  error: string | null = null;

  constructor(protected moduleService: ModuleService, private router: Router) {}

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
    this.modules = this.searchTerm
      ? this.allModules.filter(module => 
          module.title.toLowerCase().includes(this.searchTerm.toLowerCase())
        )
      : this.allModules;
  }

  onSearchChange(): void {
    this.filterModules();
  }

  navigateToAddModule(): void {
    this.router.navigate(['/add-module']);
  }
}