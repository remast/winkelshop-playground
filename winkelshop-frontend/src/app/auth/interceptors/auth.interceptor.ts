import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { AuthStateService } from '../services/auth-state.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authStateService = inject(AuthStateService);
  const token = authStateService.token();

  if (!token || !request.url.startsWith('/api')) {
    return next(request);
  }

  const tokenType = authStateService.tokenType();
  const authRequest = request.clone({
    setHeaders: {
      Authorization: `${tokenType} ${token}`
    }
  });

  return next(authRequest);
};
