import { Routes } from '@angular/router';
import { ModulesComponent } from './modules/modules.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { TipsAndTricksComponent } from './tips-and-tricks/tips-and-tricks.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { SkippersComponent } from './skippers/skippers.component';
import { SupportComponent } from './support/support.component';


export const routes: Routes = [
    {path: "modules", component: ModulesComponent}, //When role is trainee, fleet-manager, helpdesk, admin
    {path: "certificates", component: CertificatesComponent}, //When role is trainee, fleet-manager
    {path: "tips-and-tricks", component: TipsAndTricksComponent}, //When role is trainee, fleet-manager, helpdesk
    {path: "user-management", component: UserManagementComponent}, //When role is admin
    {path: "skippers", component: SkippersComponent}, //When role is fleet-manager
    {path: "support", component: SupportComponent},
    {path: "", redirectTo: "modules", pathMatch: 'full'}, //When role is trainee
    {path: "**", redirectTo: "modules", pathMatch: 'full'} //When role is trainee
];
