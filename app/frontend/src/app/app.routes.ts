import { Routes } from '@angular/router';
import { ModulesComponent } from './modules/modules.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { TipsAndTricksComponent } from './tips-and-tricks/tips-and-tricks.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { SkippersComponent } from './skippers/skippers.component';
import { SupportComponent } from './support/support.component';
import { LoginComponent } from './login/login.component';
import { Role } from './services/auth.service';
import { AddModuleComponent } from './add-module/add-module.component';
import { BasicSetupComponent } from './add-module/basic-setup/basic-setup.component';
import { TrainingsComponent } from './add-module/trainings/trainings.component';
import { ExamComponent } from './add-module/exam/exam.component';
import { PreviewComponent } from './add-module/preview/preview.component';
import { AdminModulesComponent } from './admin-modules/admin-modules.component';
import { RoleGuard } from './guards/role.guard';
import { TrainingFormComponent } from './add-module/training-form/training-form.component';
import { ContentComponent } from './add-module/training-form/content/content.component';
import { QuestionsComponent } from './add-module/training-form/questions/questions.component';
import { authGuard } from './auth.guard';
import { AdminEditModuleComponent } from './admin-edit-module/admin-edit-module.component';
import { BasicInfoComponent } from './admin-edit-module/basic-info/basic-info.component';
import { EditTrainingsComponent } from './admin-edit-module/edit-trainings/edit-trainings.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'modules',
    component: ModulesComponent,
    //canActivate: [RoleGuard],
    data: {
      roles: [Role.SKIPPER, Role.INSTALLER],
    },
  },
  {
    path: 'admin/modules',
    component: AdminModulesComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
  },
  {
    path: 'certificates',
    component: CertificatesComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT] },
  },
  {
    path: 'tips-and-tricks',
    component: TipsAndTricksComponent,
    //canActivate: [RoleGuard],
    data: {
      roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT, Role.FLEETMANAGER],
    },
  },
  {
    path: 'user-management',
    component: UserManagementComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
  },
  {
    path: 'skippers',
    component: SkippersComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.FLEETMANAGER] },
  },
  {
    path: 'support',
    component: SupportComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.SUPPORT] },
  },
  {
    path: 'add-module',
    component: AddModuleComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
    children: [
      { path: '', redirectTo: 'basic-setup', pathMatch: 'full' },
      { path: 'basic-setup', component: BasicSetupComponent },
      { path: 'trainings', component: TrainingsComponent },
      { path: 'exam', component: ExamComponent },
      { path: 'preview', component: PreviewComponent },
      {
        path: 'trainings/new',
        component: TrainingFormComponent,
        children: [
          { path: 'content', component: ContentComponent },
          { path: 'questions', component: QuestionsComponent },
        ],
      },
    ],
  },
  { path: 'edit-module',
    component: AdminEditModuleComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
    children: [
      { path: '', redirectTo: 'basic-info', pathMatch: 'full' },
      { path: 'basic-info', component: BasicInfoComponent },
      { path: 'trainings', component: EditTrainingsComponent },
      { path: 'exam', component: ExamComponent },
    ]
  },
  { path: '', redirectTo: 'modules', pathMatch: 'full' },
  { path: '**', redirectTo: 'modules', pathMatch: 'full' },
];
