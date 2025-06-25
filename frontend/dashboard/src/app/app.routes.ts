import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { RegisterUserComponent } from './components/register-user/register-user.component';
import { UserEditComponent } from './components/user-edit/user-edit.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterUserComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'user-edit/:id', component: UserEditComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: 'dashboard' }
];