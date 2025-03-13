import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Module, ModuleService } from '../services/module.service';
import { ModuleCardComponent } from '../module-card/module-card.component';

@Component({
  selector: 'app-modules',
  imports: [CommonModule, ModuleCardComponent],
  templateUrl: './modules.component.html',
  styleUrl: './modules.component.css'
})
export class ModulesComponent {
  modules: Module[] = [];

  constructor(private moduleService: ModuleService) {}

  ngOnInit(): void {
    this.moduleService.getModules().subscribe(modules => {
      this.modules = modules;
    });
  }
}
