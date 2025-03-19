import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Module, ModuleService } from '../services/module.service';
import { ModuleCardComponent } from '../module-card/module-card.component';
import { AssignedModuleCardComponent } from '../assigned-module-card/assigned-module-card.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-modules',
  standalone: true,
  imports: [CommonModule, ModuleCardComponent, AssignedModuleCardComponent, FormsModule],
  templateUrl: './modules.component.html',
  styleUrl: './modules.component.css'
})
export class ModulesComponent {
  modules: Module[] = [];
  assignedModules: Module[] = [];
  allModules: Module[] = [];
  searchTerm: string = '';

  constructor(private moduleService: ModuleService) {}

  ngOnInit(): void {
    this.moduleService.getModules().subscribe(modules => {
      this.allModules = modules;
      this.filterModules();
    });
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