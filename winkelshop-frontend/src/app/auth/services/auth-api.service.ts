import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import type { LoginRequest, LoginResponse } from '../models/auth.models';

@Injectable({ providedIn: 'root' })
export class AuthApiService {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBaseUrl = '/api';

  public login(payload: LoginRequest) {
    return this.httpClient.post<LoginResponse>(`${this.apiBaseUrl}/auth/login`, payload);
  }

  public logout() {
    return this.httpClient.post<void>(`${this.apiBaseUrl}/auth/logout`, {});
  }
}
