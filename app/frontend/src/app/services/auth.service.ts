import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

export enum Role {
  SKIPPER = 'SKIPPER',
  INSTALLER = 'INSTALLER',
  ADMIN = 'ADMIN',
  SUPPORT = 'SUPPORT',
  FLEETMANAGER = 'FLEETMANAGER'
}

export interface User {
  username: string;
  role: Role;
  // Add other user properties
}

// This Authentication service is a placeholder for testing and demonstrating roles implementation and should be expanded on with actual authentication logic

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    // Check if user is stored in localStorage on service initialization
    // I don't know if this is supposed to be here but I copied it of the internet haha
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  // Actual authentication logic here
  

  hasRole(role: Role): boolean {
    const user = this.currentUserValue;
    return user !== null && user.role === role;
  }

  hasAnyRole(roles: Role[]): boolean {
    const user = this.currentUserValue;
    return user !== null && roles.includes(user.role);
  }

  // Method for testing - DO NOT USE IN PRODUCTION
  setCurrentUserForTesting(user: User): void {
    localStorage.setItem('currentUser', JSON.stringify(user));
    this.currentUserSubject.next(user);
    console.log('Switched to role:', user.role);
  }
}