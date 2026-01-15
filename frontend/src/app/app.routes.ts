import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home.component').then((m) => m.HomeComponent),
    canActivate: [authGuard],
  },
  {
    path: 'weddings/:id',
    loadComponent: () =>
      import('./pages/wedding-detail/wedding-detail.component').then(
        (m) => m.WeddingDetailComponent
      ),
    canActivate: [authGuard],
  },
  {
    path: 'weddings/:id/upload',
    loadComponent: () =>
      import('./pages/wedding-upload/wedding-upload.component').then(
        (m) => m.WeddingUploadComponent
      ),
    canActivate: [authGuard],
  },
  {
    path: 'auth/sign-in',
    loadComponent: () =>
      import('./pages/auth/sign-in/sign-in.component').then((m) => m.SignInComponent),
  },
  {
    path: 'auth/sign-up',
    loadComponent: () =>
      import('./pages/auth/sign-up/sign-up.component').then((m) => m.SignUpComponent),
  },
  {
    path: '**',
    redirectTo: '',
  },
];
