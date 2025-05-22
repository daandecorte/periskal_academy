import { Component } from '@angular/core';
import { faCertificate, faSearch } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-user-management',
  imports: [FontAwesomeModule, FormsModule, RouterLink, TranslatePipe],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css',
})
export class UserManagementComponent {
  faCertificate = faCertificate;
  faSearch = faSearch;

  userListRaw: IUsers[] = [];
  userList: IUserFull[] = [];
  filteredUserList: IUserFull[] = [];

  searchTerm: string = '';
  selectedFilter: string = 'default';

  constructor(private authService: AuthService) {
    this.getUsers();
  }

  async getUsers() {
    let result = await fetch('api/users');
    this.userListRaw = await result.json();
    for (const el of this.userListRaw) {
      let trainingCount = await this.getUserTrainings(Number(el.id));
      let certificateCount = await this.getUserCertificates(Number(el.id));

      var temp: IUserFull = {
        id: el.id || '-1',
        firstname: el.firstname || 'invalid',
        lastname: el.lastname || 'invalid',
        shipname: el.shipname || 'invalid',
        products: `${trainingCount}`,
        certificates: `${certificateCount}`,
      };

      console.log(temp);
      this.userList.push(temp);
    }

    this.filteredUserList = this.userList;
  }

  async getUserTrainings(id: number) {
    var res = await fetch(`api/users/${id}/trainings`);
    var modData: IData[] = await res.json();
    return modData.length;
  }

  async getUserCertificates(id: number) {
    var res = await fetch(`api/users/${id}/certificates`);
    var certData: IData[] = await res.json();
    return certData.length;
  }

  filterUsers() {
    this.filteredUserList = this.userList.filter((user) => {
      var matchesSearch;
      if (this.selectedFilter == 'default') {
        return this.filteredUserList;
      } else if (this.selectedFilter == 'name') {
        matchesSearch = this.searchTerm
          ? `${user.firstname} ${user.lastname}`
              .toLowerCase()
              .includes(this.searchTerm.toLowerCase())
          : true;
      } else if (this.selectedFilter == 'shipname') {
        matchesSearch = this.searchTerm
          ? `${user.shipname}`
              .toLowerCase()
              .includes(this.searchTerm.toLowerCase())
          : true;
      } else if (this.selectedFilter == 'products') {
        matchesSearch = this.searchTerm
          ? `${user.products}`
              .toLowerCase()
              .includes(this.searchTerm.toLowerCase())
          : true;
      } else if (this.selectedFilter == 'certificates') {
        matchesSearch = this.searchTerm
          ? `${user.certificates}`
              .toLowerCase()
              .includes(this.searchTerm.toLowerCase())
          : true;
      }

      return matchesSearch;
    });
  }
}

interface IUsers {
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
}

interface IData {
  id: number;
}
