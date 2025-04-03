import { Component } from '@angular/core';
import {
  faGlobe,
  faInfoCircle,
  faAngleDown,
  faAngleUp,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-tips-and-tricks',
  imports: [FontAwesomeModule],
  templateUrl: './tips-and-tricks.component.html',
  styleUrl: './tips-and-tricks.component.css',
})
export class TipsAndTricksComponent {
  faGlobe = faGlobe;
  faInfo = faInfoCircle;
  faAngleUp = faAngleUp;
  faAngleDown = faAngleDown;

  sectionsOpenState: boolean[] = [false, false];
  ngOnInit() {}

  toggleSection(event: Event, index: number): void {
    const header = event.currentTarget as HTMLElement;
    header.classList.toggle('collapsed');

    const content = header.nextElementSibling as HTMLElement;
    if (content) {
      content.classList.toggle('hidden');
    }

    this.sectionsOpenState[index] = !this.sectionsOpenState[index];
  }
}
