import { Component } from '@angular/core';
import {
  faGlobe,
  faInfoCircle,
  faAngleDown,
  faAngleUp,
  faSearch,
  faAdd,
  faPencil,
  faTrash,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthService } from '../services/auth.service';
import { IUser } from '../types/user-info';
import { Observable } from 'rxjs';

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
  faSearch = faSearch;
  faAdd = faAdd;
  faPencil = faPencil;
  faTrash = faTrash;

  sectionsOpenState: boolean[] = [false, false];

  currentUser$: Observable<IUser | null>;
  currentUserRole: string | null = null;
  editorUser: boolean = false;

  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
    this.authService.currentUser$.subscribe((user) => {
      this.currentUserRole = user ? user.Role : null;
    });

    if (
      this.currentUserRole?.toLowerCase() == 'support' ||
      this.currentUserRole?.toLowerCase() == 'administrator'
    ) {
      this.editorUser = true;
    }
  }

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
