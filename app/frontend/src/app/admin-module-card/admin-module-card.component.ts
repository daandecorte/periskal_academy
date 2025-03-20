import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Module } from '../services/module.service';

@Component({
  selector: 'app-admin-module-card',
  imports: [CommonModule],
  templateUrl: './admin-module-card.component.html',
  styleUrl: './admin-module-card.component.css'
})
export class AdminModuleCardComponent {
  @Input() module!: Module;
}
