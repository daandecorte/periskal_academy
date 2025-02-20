import { Routes } from '@angular/router';
import { ModulesComponent } from './modules/modules.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { TipsAndTricksComponent } from './tips-and-tricks/tips-and-tricks.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { SkippersComponent } from './skippers/skippers.component';


export const routes: Routes = [
    {path: "dashboard", component: DashboardComponent},
    {path: "modules", component: ModulesComponent},
    {path: "certificates", component: CertificatesComponent},
    {path: "tips-and-tricks", component: TipsAndTricksComponent},
    {path: "user-management", component: UserManagementComponent},
    {path: "skippers", component: SkippersComponent},
    {path: "", redirectTo: "dashboard", pathMatch: 'full'},
    {path: "**", redirectTo: "dashboard", pathMatch: 'full'}
];
