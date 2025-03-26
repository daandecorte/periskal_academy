import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPencilAlt, faTrash } from '@fortawesome/free-solid-svg-icons';
import { Module } from '../services/module.service';

@Component({
  selector: 'app-admin-module-card',
  imports: [
    CommonModule, 
    FontAwesomeModule
  ],
  templateUrl: './admin-module-card.component.html',
  styleUrl: './admin-module-card.component.css'
})
export class AdminModuleCardComponent {
  @Input() module!: Module;

  faPencilAlt = faPencilAlt;
  faTrash = faTrash;
}