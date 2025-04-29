import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../services/auth.service';
import { IUser } from '../../types/user-info';
import { Observable } from 'rxjs';
import { IUsers } from '../../skippers/skippers.component';
import { CertificateService } from '../../services/certificate.service';

@Component({
  selector: 'app-assign-sailor',
  imports: [FontAwesomeModule],
  templateUrl: './assign-sailor.component.html',
  styleUrl: './assign-sailor.component.css'
})
export class AssignSailorComponent {
  faUser=faUser;
  currentUser: Observable<IUser | null>;
  userListPerId: string[] = [];
  userList: IUsers[] = [];
  constructor(private authService: AuthService, public service: CertificateService) {
    this.currentUser = this.authService.currentUser$;
    this.authService.currentUser$.subscribe((user) => {
      if (user?.Role.toUpperCase() == 'FLEETMANAGER') {
        this.userListPerId = user.Skippers.map((skipper) => skipper.ID);
        this.getUsers();
      }
      else if(user?.Role.toUpperCase() == 'SKIPPER'|| user?.Role.toUpperCase() == 'INSTALLER') {
        this.userListPerId.push(user.ID);
        this.getUsers();
      }
      else {
        this.getAllUsers();
      }
    });
  }
  toggleUserSelect(index: number) {

  }
  isSelected(id: number) {

  }

  async getUsers() {
    for (const id of this.userListPerId) {
      let result = await fetch(`api/users/periskal_id/${id}`);
      let data: IUsers = await result.json();

      let user: IUsers = {
        id: data.id,
        firstname: data.firstname,
        lastname: data.lastname,
        shipname: data.shipname,
      };

      this.userList.push(user);
    }
  }
  async getAllUsers() {
    let res = await fetch("api/users");
    let data: IUsers[] = await res.json();
    for(let user of data) {
      this.userList.push(user);
    }
  }
}