export interface CartAddItemRequest {
  productId: string;
  quantity: number;
}

export interface CartItem {
  itemId: string;
  productId: string;
  name: string;
  quantity: number;
  unitPrice: number;
}

export interface CartData {
  items: CartItem[];
  currency: string;
  totalPrice: number;
}

export interface CartResponse {
  data: CartData;
}

export interface CartUpdateItemRequest {
  quantity: number;
}
