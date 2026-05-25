import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

import { AuthStateService } from '../services/auth-state.service';

export const authGuard: CanActivateFn = () => {
  const authStateService = inject(AuthStateService);
  const router = inject(Router);

  authStateService.restoreSession();

  if (authStateService.isAuthenticated()) {
    return true;
  }

  return router.parseUrl('/login');
};

export const guestOnlyGuard: CanActivateFn = () => {
  const authStateService = inject(AuthStateService);
  const router = inject(Router);

  authStateService.restoreSession();

  if (!authStateService.isAuthenticated()) {
    return true;
  }

  return router.parseUrl('/');
};
