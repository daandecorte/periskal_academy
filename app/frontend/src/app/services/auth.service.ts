import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { IUser } from '../types/user-info';

export enum Role {
  SKIPPER = 'SKIPPER',
  INSTALLER = 'INSTALLER',
  ADMIN = 'ADMINISTRATOR',
  SUPPORT = 'SUPPORT',
  FLEETMANAGER = 'FLEETMANAGER',
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<IUser | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  public get currentUserValue(): IUser | null {
    return this.currentUserSubject.value;
  }

  // Actual authentication logic here

  setCurrentUser(user: IUser): void {
    localStorage.setItem('currentUser', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  hasRole(role: Role): boolean {
    const user = this.currentUserValue;
    return user !== null && user.Role.toUpperCase() === role;
  }

  hasAnyRole(roles: Role[]): boolean {
    const user = this.currentUserValue;
    return user !== null && roles.includes(user.Role.toUpperCase() as Role);
  }
  isAdmin() {
    const user = this.currentUserValue;
    if(user?.Role.toLocaleUpperCase()=="ADMINISTRATOR") {
      return true;
    }
    else return false;
  }

  logout(): void {
    localStorage.removeItem('currentUser');

    this.currentUserSubject.next(null);

    this.router.navigate(['/login']);
  }
}
