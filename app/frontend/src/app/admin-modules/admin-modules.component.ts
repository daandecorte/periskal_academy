import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Module, ModuleService } from '../services/module.service';
import { AdminModuleCardComponent } from '../admin-module-card/admin-module-card.component';
import { RouterModule, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';


@Component({
  selector: 'app-admin-modules',
  imports: [CommonModule, FormsModule, AdminModuleCardComponent, RouterModule, TranslateModule],
  templateUrl: './admin-modules.component.html',
  styleUrl: './admin-modules.component.css'
})
export class AdminModulesComponent {
  modules: Module[] = [];
  allModules: Module[] = [];
  searchTerm: string = '';

  constructor(private moduleService: ModuleService, private router: Router) {}

  ngOnInit(): void {
    this.moduleService.getModules().subscribe(modules => {
      this.allModules = modules;
      this.filterModules();
    });
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


