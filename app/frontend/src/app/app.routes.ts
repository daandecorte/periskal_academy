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
import { AdminEditTrainingComponent } from './admin-edit-training/admin-edit-training.component';
import { BasicInfoComponent } from './admin-edit-training/basic-info/basic-info.component';
import { EditModulesComponent } from './admin-edit-training/edit-modules/edit-modules.component';
import { UserdetailComponent } from './userdetail/userdetail.component';
import { TrainingOverviewComponent } from './training-overview/training-overview.component';
import { ModuleVideoViewComponent } from './module-video-view/module-video-view.component';
import { ModuleQuestionsComponent } from './module-questions/module-questions.component';
import { TrainingExamComponent } from './training-exam/training-exam.component';
import { SelectModuleComponent } from './certificates/select-module/select-module.component';
import { SendInfoComponent } from './certificates/send-info/send-info.component';
import { AcceptTermsComponent } from './certificates/accept-terms/accept-terms.component';
import { AssignSailorComponent } from './certificates/assign-sailor/assign-sailor.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'trainings',
    component: TrainingsComponent,
    //canActivate: [RoleGuard],
    data: {
      roles: [Role.SKIPPER, Role.INSTALLER],
    },
  },
  {
    path: 'admin/trainings',
    component: AdminTrainingsComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
  },
  {
    path: 'certificates',
    component: CertificatesComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.SKIPPER, Role.INSTALLER, Role.SUPPORT] },
    children: [
      { path: '', redirectTo: 'select-module', pathMatch: 'full' },
      { path: 'select-module', component: SelectModuleComponent },
      { path: 'assign-sailor', component: AssignSailorComponent },
      { path: 'accept-terms', component: AcceptTermsComponent },
      { path: 'send-info', component: SendInfoComponent },
    ],
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
    path: 'userdetail/:id',
    component: UserdetailComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN, Role.SUPPORT] },
  },
  {
    path: 'add-training',
    component: AddTrainingComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
    children: [
      { path: '', redirectTo: 'basic-setup', pathMatch: 'full' },
      { path: 'basic-setup', component: BasicSetupComponent },
      { path: 'modules', component: ModulesComponent },
      { path: 'exam', component: ExamComponent },
      { path: 'preview', component: PreviewComponent },
      {
        path: 'modules/new',
        component: ModuleFormComponent,
        children: [
          { path: 'content', component: ContentComponent },
          { path: 'questions', component: QuestionsComponent },
        ],
      },
    ],
  },
  {
    path: 'edit-training/:id',
    component: AdminEditTrainingComponent,
    //canActivate: [RoleGuard],
    data: { roles: [Role.ADMIN] },
    children: [
      { path: '', redirectTo: 'basic-info', pathMatch: 'full' },
      { path: 'basic-info', component: BasicInfoComponent },
      { path: 'modules', component: EditModulesComponent },
      { path: 'exam', component: ExamComponent },
    ]
  },
  { path: 'trainings/:id', component: TrainingOverviewComponent },
  { path: 'trainings/:id/module/:sectionId', component: ModuleVideoViewComponent },
  { path: 'trainings/:id/module/:sectionId/questions/:questionIndex', component: ModuleQuestionsComponent },
  { path: 'trainings/:id/module/:sectionId/questions', redirectTo: 'trainings/:id/module/:sectionId/questions/0' },
  { path: 'exams/:id/:questionIndex', component: TrainingExamComponent },
  { path: 'exams/:id', redirectTo: 'exams/:id/0' },
  { path: 'training/:id/certificate', redirectTo: 'trainings/:id' }, // Placeholder for future implementation
  { path: '', redirectTo: 'trainings', pathMatch: 'full' },
  { path: '**', redirectTo: 'trainings', pathMatch: 'full' },
];
