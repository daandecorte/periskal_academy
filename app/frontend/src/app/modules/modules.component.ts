import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Module, ModuleService } from '../services/module.service';
import { ModuleCardComponent } from '../module-card/module-card.component';
import { AssignedModuleCardComponent } from '../assigned-module-card/assigned-module-card.component';

@Component({
  selector: 'app-modules',
  standalone: true,
  imports: [CommonModule, ModuleCardComponent, AssignedModuleCardComponent],
  templateUrl: './modules.component.html',
  styleUrl: './modules.component.css'
})
export class ModulesComponent {
  modules: Module[] = [];
  assignedModules: Module[] = [];

  constructor(private moduleService: ModuleService) {}

  ngOnInit(): void {
    this.moduleService.getModules().subscribe(modules => {
      this.modules = modules;
      this.assignedModules = modules.filter(module => module.assigned);
    });
  }
}