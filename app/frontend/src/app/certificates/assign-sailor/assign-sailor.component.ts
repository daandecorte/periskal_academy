import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../services/auth.service';
import { IUser } from '../../types/user-info';
import { Observable } from 'rxjs';
import { IUsers } from '../../skippers/skippers.component';
import { CertificateService } from '../../services/certificate.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-assign-sailor',
  imports: [FontAwesomeModule, CommonModule, FormsModule],
  templateUrl: './assign-sailor.component.html',
  styleUrl: './assign-sailor.component.css'
})
export class AssignSailorComponent {
  faUser=faUser;
  currentUser: Observable<IUser | null>;
  userListPerId: string[] = [];
  userList: IUsers[] = [];
  filteredUsers: IUsers[] = [];
  searchQuery='';
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
    let userIndex=this.service.selectedUsers.findIndex(u=> u.id==this.userList[index].id);
    console.log(userIndex + " " + this.userList[index].id)
    if(userIndex!=-1) {
      this.service.selectedUsers.splice(userIndex, 1);
    }
    else this.service.selectedUsers.push(this.userList[index]);
  }
  isSelected(id: string) {
    return this.service.selectedUsers.some(u=>u.id == id);
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
    this.filteredUsers=this.userList;
  }
  async getAllUsers() {
    let res = await fetch("api/users");
    let data: IUsers[] = await res.json();
    for(let user of data) {
      this.userList.push(user);
    }
  }
  filterUsers() {
    if(this.userList) {
        this.filteredUsers = this.userList.filter(user => { const matchesTitle = this.searchQuery === '' || `${user.firstname} ${user.lastname}`.toLowerCase().includes(this.searchQuery.toLowerCase());
        return matchesTitle;
      });
    }
  }
}