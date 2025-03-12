import { Routes } from '@angular/router';
import { ModulesComponent } from './modules/modules.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { TipsAndTricksComponent } from './tips-and-tricks/tips-and-tricks.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { SkippersComponent } from './skippers/skippers.component';
import { SupportComponent } from './support/support.component';
import { LoginComponent } from './login/login.component';
import { Role } from './services/auth.service';


export const routes: Routes = [
  { path: 'login', component: LoginComponent }, //To get a role
  { path: 'modules', component: ModulesComponent , data: { roles: [Role.SKIPPER, Role.INSTALLER, Role.ADMIN, Role.SUPPORT, Role.FLEETMANAGER] }}, //When role is trainee, fleet-manager, helpdesk, admin
  { path: 'certificates', component: CertificatesComponent , data: { roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT] }}, //When role is trainee, fleet-manager
  { path: 'tips-and-tricks', component: TipsAndTricksComponent , data: { roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT, Role.FLEETMANAGER] }}, //When role is trainee, fleet-manager, helpdesk
  { path: 'user-management', component: UserManagementComponent , data: { roles: [Role.ADMIN] }}, //When role is admin
  { path: 'skippers', component: SkippersComponent , data: { roles: [Role.FLEETMANAGER] }}, //When role is fleet-manager
  { path: 'support', component: SupportComponent , data: { roles: [Role.SUPPORT] }}, //When role is helpdesk
  { path: '', redirectTo: 'modules', pathMatch: 'full' }, 
  { path: '**', redirectTo: 'modules', pathMatch: 'full' },
];
