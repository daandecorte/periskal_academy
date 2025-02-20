import { Routes } from '@angular/router';
import { ModulesComponent } from './modules/modules.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { TipsAndTricksComponent } from './tips-and-tricks/tips-and-tricks.component';
import { DashboardComponent } from './dashboard/dashboard.component';


export const routes: Routes = [
    {path: "dashboard", component: DashboardComponent},
    {path: "modules", component: ModulesComponent},
    {path: "certificates", component: CertificatesComponent},
    {path: "tips-and-tricks", component: TipsAndTricksComponent},
    {path: "", redirectTo: "dashboard", pathMatch: 'full'},
    {path: "**", redirectTo: "dashboard", pathMatch: 'full'}
];
