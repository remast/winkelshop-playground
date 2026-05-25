import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

import type {
  CartAddItemRequest,
  CartResponse,
  CartUpdateItemRequest
} from '../models/cart.models';

@Injectable({ providedIn: 'root' })
export class CartApiService {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBaseUrl = '/api';

  public addCartItem(payload: CartAddItemRequest) {
    return this.httpClient.post<CartResponse>(`${this.apiBaseUrl}/cart/items`, payload);
  }

  public getCart() {
    return this.httpClient.get<CartResponse>(`${this.apiBaseUrl}/cart`);
  }

  public updateCartItem(itemId: string, payload: CartUpdateItemRequest) {
    return this.httpClient.patch<CartResponse>(`${this.apiBaseUrl}/cart/items/${itemId}`, payload);
  }

  public removeCartItem(itemId: string) {
    return this.httpClient.delete<CartResponse>(`${this.apiBaseUrl}/cart/items/${itemId}`);
  }
}
