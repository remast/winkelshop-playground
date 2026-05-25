import { computed, inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, map, of, tap } from 'rxjs';

import { AuthApiService } from './auth-api.service';
import type { AuthUser, LoginRequest } from '../models/auth.models';

const AUTH_SESSION_STORAGE_KEY = 'winkelshop.auth';

interface AuthSessionSnapshot {
  accessToken: string;
  tokenType: string;
  user: AuthUser;
}

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private readonly authApiService = inject(AuthApiService);
  private readonly router = inject(Router);

  private readonly sessionState = signal<AuthSessionSnapshot | null>(null);
  private readonly hasRestoredSession = signal(false);

  public readonly token = computed(() => this.sessionState()?.accessToken ?? null);
  public readonly tokenType = computed(() => this.sessionState()?.tokenType ?? 'Bearer');
  public readonly user = computed(() => this.sessionState()?.user ?? null);
  public readonly isAuthenticated = computed(() => this.token() !== null);

  public restoreSession(): void {
    if (this.hasRestoredSession()) {
      return;
    }

    this.hasRestoredSession.set(true);

    const raw = sessionStorage.getItem(AUTH_SESSION_STORAGE_KEY);
    if (!raw) {
      return;
    }

    try {
      const parsed: unknown = JSON.parse(raw);
      if (!this.isAuthSessionSnapshot(parsed)) {
        sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
        return;
      }

      this.sessionState.set(parsed);
    } catch {
      sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
    }
  }

  public login(credentials: LoginRequest) {
    return this.authApiService.login(credentials).pipe(
      tap((response) => {
        const session: AuthSessionSnapshot = {
          accessToken: response.data.accessToken,
          tokenType: response.data.tokenType,
          user: response.data.user
        };

        this.sessionState.set(session);
        sessionStorage.setItem(AUTH_SESSION_STORAGE_KEY, JSON.stringify(session));
      }),
      map(() => true),
      catchError(() => of(false))
    );
  }

  public logout() {
    return this.authApiService.logout().pipe(
      catchError(() => of(undefined)),
      tap(() => {
        this.clearSession();
      }),
      tap(() => {
        void this.router.navigate(['/login']);
      })
    );
  }

  public clearSession(): void {
    this.sessionState.set(null);
    sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
  }

  private isAuthSessionSnapshot(value: unknown): value is AuthSessionSnapshot {
    if (!value || typeof value !== 'object') {
      return false;
    }

    const candidate = value as Partial<AuthSessionSnapshot>;
    return (
      typeof candidate.accessToken === 'string' &&
      typeof candidate.tokenType === 'string' &&
      !!candidate.user &&
      typeof candidate.user.id === 'string' &&
      typeof candidate.user.name === 'string' &&
      typeof candidate.user.email === 'string' &&
      typeof candidate.user.role === 'string' &&
      typeof candidate.user.createdAt === 'string'
    );
  }
}
