import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService, Role } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const requiredRoles = route.data['roles'] as Role[];

    if (!requiredRoles || requiredRoles.length === 0) {
      return true; // No role restrictions
    }

    if (this.authService.hasAnyRole(requiredRoles)) {
      return true;
    }

    // Redirect to an unauthorized page or home
    return this.router.createUrlTree(['/unauthorized']);
  }
}
