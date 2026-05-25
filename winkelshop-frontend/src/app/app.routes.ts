import { Routes } from '@angular/router';

import { authGuard, guestOnlyGuard } from './auth/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    canActivate: [guestOnlyGuard],
    loadComponent: () =>
      import('./auth/pages/login-page/login-page').then(
        (module) => module.LoginPage
      )
  },
  {
    path: '',
    loadComponent: () =>
      import('./order/pages/winkelshop-page/winkelshop-page').then(
        (module) => module.WinkelshopPage
      )
  },
  {
    path: 'checkout',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./order/pages/checkout-page/checkout-page').then(
        (module) => module.CheckoutPage
      )
  },
  {
    path: '**',
    redirectTo: ''
  }
];
