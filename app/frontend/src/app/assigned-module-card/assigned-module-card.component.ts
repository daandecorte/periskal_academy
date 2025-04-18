import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Module } from '../services/module.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faLayerGroup } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-assigned-module-card',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, TranslateModule],
  templateUrl: './assigned-module-card.component.html',
  styleUrl: './assigned-module-card.component.css'
})
export class AssignedModuleCardComponent {
  @Input() module!: Module;
  @Input() currentLanguage: string = 'EN';
  faLayerGroup = faLayerGroup;
  
  getLocalizedTitle(): string {
    if (this.module.titleLocalized && this.module.titleLocalized[this.currentLanguage]) {
      return this.module.titleLocalized[this.currentLanguage];
    }
    return this.module.title || '';
  }
}