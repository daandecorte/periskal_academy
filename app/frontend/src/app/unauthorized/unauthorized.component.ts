import { Component } from '@angular/core';

@Component({
  selector: 'app-unauthorized',
  template: `
  <div class="unauthorized-container">
    <h2>Unauthorized Access</h2>
    <p>You don't have permission to access this page.</p>
    <button (click)="goBack()" class="back-button">Go Back</button>
  </div>
`,
styles: [`
  .unauthorized-container {
    text-align: center;
    padding: 2rem;
    max-width: 600px;
    margin: 2rem auto;
  }
  
  .back-button {
    background-color: #f44336;
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 4px;
    cursor: pointer;
  }
`]
})
export class UnauthorizedComponent {
  goBack(): void {
    window.history.back();
  }
}
