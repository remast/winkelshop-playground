import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

import type { CheckoutRequest, CheckoutResponse } from '../models/checkout.models';

@Injectable({ providedIn: 'root' })
export class CheckoutApiService {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBaseUrl = '/api';

  public checkout(payload: CheckoutRequest) {
    return this.httpClient.post<CheckoutResponse>(`${this.apiBaseUrl}/checkout`, payload);
  }
}
