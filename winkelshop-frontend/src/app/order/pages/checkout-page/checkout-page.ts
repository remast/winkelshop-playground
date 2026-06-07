import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { catchError, of, switchMap, tap } from 'rxjs';

import { form, FormField, minLength, required, submit } from '@angular/forms/signals';

import { AuthStateService } from '../../../auth/services/auth-state.service';
import type { CartItem, CartResponse } from '../../models/cart.models';
import type { CheckoutResponse } from '../../models/checkout.models';
import { CartApiService } from '../../services/cart-api.service';
import { CheckoutApiService } from '../../services/checkout-api.service';

const EMPTY_CART_RESPONSE: CartResponse = {
  data: {
    items: [],
    currency: 'GALLEON',
    totalPrice: 0
  }
};

@Component({
  selector: 'app-checkout-page',
  imports: [FormField, RouterLink],
  templateUrl: './checkout-page.html',
  styleUrl: './checkout-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CheckoutPage {
  private readonly cartApiService = inject(CartApiService);
  private readonly checkoutApiService = inject(CheckoutApiService);
  private readonly authStateService = inject(AuthStateService);
  protected readonly currentUser = this.authStateService.user;

  private readonly numberFormatter = new Intl.NumberFormat('de-DE', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });

  private readonly cartReloadTick = signal(0);
  private readonly quantityDrafts = signal<Record<string, string>>({});

  protected readonly cartErrorMessage = signal<string | null>(null);
  protected readonly cartMessage = signal<string | null>(null);
  protected readonly checkoutErrorMessage = signal<string | null>(null);
  protected readonly checkoutResult = signal<CheckoutResponse['data'] | null>(null);
  protected readonly currentlyUpdatingItemId = signal<string | null>(null);
  protected readonly currentlyRemovingItemId = signal<string | null>(null);
  protected readonly isSubmittingCheckout = signal(false);

  protected readonly checkoutModel = signal({
    paymentMethod: 'credit-card',
    shippingAddress: {
      fullName: '',
      street: '',
      city: '',
      postalCode: '',
      country: ''
    }
  });

  protected readonly checkoutForm = form(this.checkoutModel, (s) => {
    required(s.paymentMethod, { message: 'Zahlungsart ist erforderlich.' });
    required(s.shippingAddress.fullName, { message: 'Vollstaendiger Name ist erforderlich.' });
    minLength(s.shippingAddress.fullName, 2, { message: 'Name muss mindestens 2 Zeichen lang sein.' });
    required(s.shippingAddress.street, { message: 'Strasse ist erforderlich.' });
    minLength(s.shippingAddress.street, 2, { message: 'Strasse muss mindestens 2 Zeichen lang sein.' });
    required(s.shippingAddress.city, { message: 'Stadt ist erforderlich.' });
    minLength(s.shippingAddress.city, 2, { message: 'Stadt muss mindestens 2 Zeichen lang sein.' });
    required(s.shippingAddress.postalCode, { message: 'PLZ ist erforderlich.' });
    minLength(s.shippingAddress.postalCode, 2, { message: 'PLZ muss mindestens 2 Zeichen lang sein.' });
    required(s.shippingAddress.country, { message: 'Land ist erforderlich.' });
    minLength(s.shippingAddress.country, 2, { message: 'Land muss mindestens 2 Zeichen lang sein.' });
  });

  constructor() {
    this.authStateService.restoreSession();
  }

  private readonly cartResource = toSignal(
    toObservable(this.cartReloadTick).pipe(
      switchMap(() => this.cartApiService.getCart()),
      tap((response) => {
        this.cartErrorMessage.set(null);
        this.pruneQuantityDrafts(response.data.items);
      }),
      catchError(() => {
        this.cartErrorMessage.set('Warenkorb konnte nicht geladen werden.');
        return of(EMPTY_CART_RESPONSE);
      })
    ),
    { initialValue: EMPTY_CART_RESPONSE }
  );

  protected readonly cart = computed(() => this.cartResource().data);
  protected readonly cartItems = computed(() => this.cart().items);
  protected readonly hasCartItems = computed(() => this.cartItems().length > 0);

  protected updateItemQuantity(item: CartItem): void {
    const draftQuantity = this.quantityDrafts()[item.itemId];
    if (draftQuantity === undefined) {
      this.cartMessage.set('Bitte gib eine neue Menge ein.');
      return;
    }

    const parsedQuantity = this.parseQuantity(draftQuantity);
    if (parsedQuantity === null) {
      this.cartMessage.set('Die Menge muss eine ganze Zahl groesser als 0 sein.');
      return;
    }

    if (parsedQuantity === item.quantity) {
      this.cartMessage.set('Menge bleibt unveraendert.');
      return;
    }

    this.currentlyUpdatingItemId.set(item.itemId);
    this.cartMessage.set(null);
    this.checkoutResult.set(null);

    this.cartApiService.updateCartItem(item.itemId, { quantity: parsedQuantity }).subscribe({
      next: () => {
        this.currentlyUpdatingItemId.set(null);
        this.refreshCart();
        this.quantityDrafts.update((current) => {
          const { [item.itemId]: _removed, ...remaining } = current;
          return remaining;
        });
        this.cartMessage.set('Menge wurde aktualisiert.');
      },
      error: () => {
        this.currentlyUpdatingItemId.set(null);
        this.cartMessage.set('Menge konnte nicht aktualisiert werden.');
      }
    });
  }

  protected removeItem(itemId: string): void {
    this.currentlyRemovingItemId.set(itemId);
    this.cartMessage.set(null);
    this.checkoutResult.set(null);

    this.cartApiService.removeCartItem(itemId).subscribe({
      next: () => {
        this.currentlyRemovingItemId.set(null);
        this.refreshCart();
        this.quantityDrafts.update((current) => {
          const { [itemId]: _removed, ...remaining } = current;
          return remaining;
        });
        this.cartMessage.set('Artikel wurde entfernt.');
      },
      error: () => {
        this.currentlyRemovingItemId.set(null);
        this.cartMessage.set('Artikel konnte nicht entfernt werden.');
      }
    });
  }

  protected onQuantityInput(itemId: string, event: Event): void {
    const target = event.target;
    if (!(target instanceof HTMLInputElement)) {
      return;
    }

    this.quantityDrafts.update((current) => ({
      ...current,
      [itemId]: target.value
    }));
  }

  protected quantityInputValue(itemId: string, currentQuantity: number): string {
    return this.quantityDrafts()[itemId] ?? String(currentQuantity);
  }

  protected submitCheckout(): void {
    if (!this.hasCartItems()) {
      this.checkoutErrorMessage.set('Dein Warenkorb ist leer.');
      return;
    }

    submit(this.checkoutForm, async () => {
      this.isSubmittingCheckout.set(true);
      this.checkoutErrorMessage.set(null);
      this.cartMessage.set(null);
      this.checkoutResult.set(null);

      const { paymentMethod, shippingAddress } = this.checkoutModel();

      await new Promise<void>((resolve, reject) => {
        this.checkoutApiService
          .checkout({ paymentMethod, shippingAddress })
          .subscribe({
            next: (response) => {
              this.isSubmittingCheckout.set(false);
              this.checkoutResult.set(response.data);
              this.refreshCart();
              resolve();
            },
            error: () => {
              this.isSubmittingCheckout.set(false);
              this.checkoutErrorMessage.set('Checkout ist fehlgeschlagen. Bitte pruefe deine Angaben.');
              reject();
            }
          });
      });
    });
  }

  protected logout(): void {
    this.authStateService.logout().subscribe();
  }

  protected itemSubtotal(item: CartItem): number {
    return item.quantity * item.unitPrice;
  }

  protected formatPrice(price: number, currency: string): string {
    return `${this.numberFormatter.format(price)} ${this.currencyLabel(currency)}`;
  }

  protected trackByCartItemId(_index: number, item: CartItem): string {
    return item.itemId;
  }

  private parseQuantity(value: string): number | null {
    if (!/^\d+$/.test(value)) {
      return null;
    }

    const parsed = Number.parseInt(value, 10);
    if (!Number.isInteger(parsed) || parsed < 1) {
      return null;
    }

    return parsed;
  }

  private currencyLabel(currency: string): string {
    if (currency === 'GALLEON') {
      return '🪙 GALLEON';
    }

    return currency;
  }

  private refreshCart(): void {
    this.cartReloadTick.update((value) => value + 1);
  }

  private pruneQuantityDrafts(items: CartItem[]): void {
    const validItemIds = new Set(items.map((item) => item.itemId));

    this.quantityDrafts.update((current) => {
      const filtered = Object.entries(current).filter(([itemId]) => validItemIds.has(itemId));
      return Object.fromEntries(filtered);
    });
  }
}
