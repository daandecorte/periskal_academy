import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.log("Checking authentication status...");

  if (authService.currentUserValue != null) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
