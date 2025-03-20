import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Module, ModuleService } from '../services/module.service';

@Component({
  selector: 'app-admin-modules',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-modules.component.html',
  styleUrl: './admin-modules.component.css'
})
export class AdminModulesComponent {
  modules: Module[] = [];
  searchTerm: string = '';

  constructor(private moduleService: ModuleService) {}

  ngOnInit(): void {
    this.moduleService.getModules().subscribe(modules => {
      this.modules = modules;
      this.filterModules();
    });
  }

  filterModules(): void {
    this.modules = this.searchTerm
      ? this.modules.filter(module => 
          module.title.toLowerCase().includes(this.searchTerm.toLowerCase())
        )
      : this.modules;
  }

  onSearchChange(): void {
    this.filterModules();
  }
}


