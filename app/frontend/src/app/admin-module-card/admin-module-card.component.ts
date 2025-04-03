import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPencilAlt, faTrash } from '@fortawesome/free-solid-svg-icons';
import { Module } from '../services/module.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-module-card',
  imports: [
    CommonModule, 
    FontAwesomeModule,
    RouterLink
  ],
  templateUrl: './admin-module-card.component.html',
  styleUrl: './admin-module-card.component.css'
})
export class AdminModuleCardComponent {
  @Input() module!: Module;

  faPencilAlt = faPencilAlt;
  faTrash = faTrash;
}