import { Component } from '@angular/core';
import { faCertificate, faSearch } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IUser, ISkipper } from '../types/user-info';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-skippers',
  imports: [FontAwesomeModule, FormsModule],
  templateUrl: './skippers.component.html',
  styleUrl: './skippers.component.css',
})
export class SkippersComponent {
  faCertificate = faCertificate;
  faSearch = faSearch;

  currentUser$: Observable<IUser | null>;
  skippersList: ISkipper[] = [];
  filteredSkippersList: ISkipper[] = [];

  searchTerm: string = '';
  selectedStatus: string = 'all';

  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
    this.authService.currentUser$.subscribe((user) => {
      this.skippersList = user?.Skippers || [];
      this.filteredSkippersList = this.skippersList;
    });
  }

  filterSkippers() {
    this.filteredSkippersList = this.skippersList.filter((skipper) => {
      const matchesSearch = this.searchTerm
        ? `${skipper.Firstname} ${skipper.Lastname}`
            .toLowerCase()
            .includes(this.searchTerm.toLowerCase())
        : true;

      //const matchesStatus = this.selectedStatus
      //? skipper.Status.toLowerCase() === this.selectedStatus
      //: true;

      return matchesSearch;
    });
  }
}
