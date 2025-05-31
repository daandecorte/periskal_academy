import { Routes } from '@angular/router';
import { TrainingsComponent } from './trainings/trainings.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { TipsAndTricksComponent } from './tips-and-tricks/tips-and-tricks.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { SkippersComponent } from './skippers/skippers.component';
import { SupportComponent } from './support/support.component';
import { LoginComponent } from './login/login.component';
import { Role } from './services/auth.service';
import { AddTrainingComponent } from './add-training/add-training.component';
import { BasicSetupComponent } from './add-training/basic-setup/basic-setup.component';
import { ModulesComponent } from './add-training/modules/modules.component';
import { ExamComponent } from './add-training/exam/exam.component';
import { PreviewComponent } from './add-training/preview/preview.component';
import { AdminTrainingsComponent } from './admin-trainings/admin-trainings.component';
import { RoleGuard } from './guards/role.guard';
import { ModuleFormComponent } from './add-training/module-form/module-form.component';
import { ContentComponent } from './add-training/module-form/content/content.component';
import { QuestionsComponent } from './add-training/module-form/questions/questions.component';
import { authGuard } from './auth.guard';
import { UserdetailComponent } from './userdetail/userdetail.component';
import { TrainingOverviewComponent } from './training-overview/training-overview.component';
import { ModuleVideoViewComponent } from './module-video-view/module-video-view.component';
import { ModuleQuestionsComponent } from './module-questions/module-questions.component';
import { TrainingExamComponent } from './training-exam/training-exam.component';
import { AdminCertificatesComponent } from './admin-certificates/admin-certificates.component';
import { ExamResultComponent } from './exam-result/exam-result.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'trainings',
    component: TrainingsComponent,
    canActivate: [RoleGuard],
    data: {
      roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT, Role.FLEETMANAGER],
    },
  },
  {
    path: 'admin/trainings',
    component: AdminTrainingsComponent,
    canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
  },
  {
    path: 'certificates',
    component: CertificatesComponent,
    canActivate: [RoleGuard],
    data: { roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT, Role.FLEETMANAGER] },
  },
  {
    path: 'admin/certificates/:id',
    component: AdminCertificatesComponent,
    canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN, Role.SUPPORT] },
  },
  {
    path: 'tips-and-tricks',
    component: TipsAndTricksComponent,
    canActivate: [RoleGuard],
    data: {
      roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT, Role.FLEETMANAGER],
    },
  },
  {
    path: 'user-management',
    component: UserManagementComponent,
    canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN, Role.SUPPORT] },
  },
  {
    path: 'skippers',
    component: SkippersComponent,
    canActivate: [RoleGuard],
    data: { roles: [Role.FLEETMANAGER] },
  },
  {
    path: 'support',
    component: SupportComponent,
    canActivate: [RoleGuard],
    data: { roles: [Role.SUPPORT] },
  },
  {
    path: 'userdetail/:id',
    component: UserdetailComponent,
    canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN, Role.SUPPORT, Role.FLEETMANAGER] },
  },
  {
    path: 'add-training',
    component: AddTrainingComponent,
    canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
  },
  {
    path: 'add-training/modules/new',
    component: ModuleFormComponent,
    canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
    children: [
      { path: 'content', 
        component: ContentComponent,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN] }, 
      },
      { path: 'questions', 
        component: QuestionsComponent,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN] },  
      },
    ],
  },
  { path: 'trainings/:id', 
    component: TrainingOverviewComponent,
    canActivate: [RoleGuard],
    data: {
      roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT],
    },
  },
  {
    path: 'trainings/:id/module/:sectionId',
    component: ModuleVideoViewComponent,
    canActivate: [RoleGuard],
    data: {
      roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT],
    },
  },
  {
    path: 'trainings/:id/module/:sectionId/questions',
    component: ModuleQuestionsComponent,
    canActivate: [RoleGuard],
    data: {
      roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT],
    },
  },
  { path: 'exams/:id/:questionIndex', 
    component: TrainingExamComponent,
    canActivate: [RoleGuard],
    data: {
      roles: [Role.SKIPPER, Role.INSTALLER],
    }, 
  },
  { path: 'exams/:id', redirectTo: 'exams/:id/0'},
  { path: '', redirectTo: 'trainings', pathMatch: 'full' },
  { path: '**', redirectTo: 'trainings', pathMatch: 'full' },
];
