import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { shareReplay } from 'rxjs';

import type { CategoriesResponse, ProductPageResponse } from '../models/shop.models';

@Injectable({ providedIn: 'root' })
export class ShopApiService {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBaseUrl = '/api';

  public getCategoriesResponse() {
    return this.httpClient
      .get<CategoriesResponse>(`${this.apiBaseUrl}/categories`)
      .pipe(shareReplay({ bufferSize: 1, refCount: true }));
  }

  public getProductsResponse(categoryId: string | null) {
    const params = categoryId
      ? new HttpParams().set('category', categoryId)
      : undefined;

    return this.httpClient
      .get<ProductPageResponse>(`${this.apiBaseUrl}/products`, { params })
      .pipe(shareReplay({ bufferSize: 1, refCount: true }));
  }
}
