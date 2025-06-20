import { Component } from '@angular/core';
import { faCertificate, faSearch } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IUser } from '../types/user-info';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-skippers',
  imports: [FontAwesomeModule, FormsModule, RouterLink, TranslatePipe],
  templateUrl: './skippers.component.html',
  styleUrl: './skippers.component.css',
})
export class SkippersComponent {
  faCertificate = faCertificate;
  faSearch = faSearch;

  currentUser$: Observable<IUser | null>;
  skippersListPerId: string[] = []; // Per = Periskal

  skippersList: IUserFull[] = [];
  filteredSkippersList: IUserFull[] = [];

  searchTerm: string = '';
  selectedFilter: string = 'default';

  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
    this.authService.currentUser$.subscribe((user) => {
      if (user?.Role.toUpperCase() == 'FLEETMANAGER') {
        this.skippersListPerId = user.Skippers.map((skipper) => skipper.ID);
        this.getUsers();
      }
    });
  }

  async getUsers() {
    for (const id of this.skippersListPerId) {
      let result = await fetch(`api/users/periskal_id/${id}`);
      if(result.status==200) {
        let temp: IUsers = await result.json();
  
        let userTrainings = await this.getUserTrainings(Number(temp.id));
        let overallProgress=0;
        if(userTrainings) {
          let total=0;
          userTrainings?.forEach(u=>{
            if(u.training_progress.modules_completed!=0) {
              total+=((u.training_progress.modules_completed/u.training.modules.length)*100)
            }
          })
        overallProgress = total/userTrainings?.length;
      }
        let certificateCount = await this.getUserCertificates(Number(temp.id));
  
        let tempF: IUserFull = {
          id: temp.id,
          firstname: temp.firstname,
          lastname: temp.lastname,
          shipname: temp.shipname,
          products: `${userTrainings.length}`,
          certificates: `${certificateCount}`,
          module_completion: overallProgress
        };
  
        this.skippersList.push(tempF);
      }
    }

    this.filteredSkippersList = this.skippersList;
  }

  async getUserTrainings(id: number) {
    var res = await fetch(`api/users/${id}/trainings`);
    var modData: any[] = await res.json();
    return modData;
  }

  async getUserCertificates(id: number) {
    var res = await fetch(`api/users/${id}/certificates`);
    var certData: IData[] = await res.json();
    return certData.length;
  }

  filterSkippers() {
    this.filteredSkippersList = this.skippersList.filter((skipper) => {
      var matchesSearch;
      if (this.selectedFilter == 'default') {
        return this.filteredSkippersList;
      } else if (this.selectedFilter == 'name') {
        matchesSearch = this.searchTerm
          ? `${skipper.firstname} ${skipper.lastname}`
              .toLowerCase()
              .includes(this.searchTerm.toLowerCase())
          : true;
      } else if (this.selectedFilter == 'shipname') {
        matchesSearch = this.searchTerm
          ? `${skipper.shipname}`
              .toLowerCase()
              .includes(this.searchTerm.toLowerCase())
          : true;
      } else if (this.selectedFilter == 'products') {
        matchesSearch = this.searchTerm
          ? `${skipper.products}`
              .toLowerCase()
              .includes(this.searchTerm.toLowerCase())
          : true;
      } else if (this.selectedFilter == 'certificates') {
        matchesSearch = this.searchTerm
          ? `${skipper.certificates}`
              .toLowerCase()
              .includes(this.searchTerm.toLowerCase())
          : true;
      }

      return matchesSearch;
    });
  }
}

export interface IUsers {
  id: string;
  firstname: string;
  lastname: string;
  shipname: string;
}

interface IUserFull {
  id: string;
  firstname: string;
  lastname: string;
  shipname: string;
  products: string;
  certificates: string;
  module_completion: number;
}

interface IData {
  id: number;
}
