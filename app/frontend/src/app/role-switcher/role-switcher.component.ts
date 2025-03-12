import { Component } from '@angular/core';
import { AuthService, Role } from '../services/auth.service';

// This component is for early debug purposes and should be deleted once not needed anymore!!
// This adds a way to create a mock user and switch roles for debug purposes

@Component({
  selector: 'app-role-switcher',
  template: `
    <div class="role-switcher">
      <h3>Development Testing: Switch Role</h3>
      <select (change)="switchRole($event)">
        <option value="">-- Select a role --</option>
        @for (role of availableRoles; track role) {
          <option [value]="role">{{ role }}</option>
        }
      </select>
      @if (currentRole) {
        <div class="current-role">
          Current role: <strong>{{ currentRole }}</strong>
        </div>
      }
    </div>
  `,
  styles: [`
    .role-switcher {
      position: fixed;
      bottom: 20px;
      right: 20px;
      padding: 15px;
      background-color: #f8f9fa;
      border: 1px solid #ddd;
      border-radius: 5px;
      z-index: 1000;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    }
    select {
      margin: 10px 0;
      padding: 5px;
      width: 100%;
    }
    .current-role {
      margin-top: 10px;
      font-size: 0.9em;
    }
  `]
})
export class RoleSwitcherComponent {
  availableRoles = Object.values(Role);
  currentRole: string | null = null;

  constructor(private authService: AuthService) {
    this.currentRole = this.authService.currentUserValue?.role || null;
  }

  switchRole(event: Event): void {
    const selectedRole = (event.target as HTMLSelectElement).value as Role;
    
    if (selectedRole) {
      // Create a mock user with the selected role
      const mockUser = {
        username: 'Test User',
        role: selectedRole,
      };
      
      // Update the auth service with the mock user
      this.authService.setCurrentUserForTesting(mockUser);
      this.currentRole = selectedRole;
    }
  }
}
