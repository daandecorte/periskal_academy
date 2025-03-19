import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Module } from '../services/module.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faLayerGroup, faCertificate } from '@fortawesome/free-solid-svg-icons';

//Elke modulekaart bevat een titel, beschrijving, aantal trainingen, voortgangsindicator en een speciaal icoon voor certificeringsmodules

@Component({
  selector: 'app-module-card',
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './module-card.component.html',
  styleUrl: './module-card.component.css'
})
export class ModuleCardComponent {
  @Input() module!: Module;

  // Font Awesome icons
  faLayerGroup = faLayerGroup;  
  faCertificate = faCertificate;

  getButtonText(): string {
    return this.module.status === 'not_started' ? 'Start Training' : 'Continue Training';
  }
}
