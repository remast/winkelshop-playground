export interface CheckoutShippingAddress {
  fullName: string;
  street: string;
  city: string;
  postalCode: string;
  country: string;
}

export interface CheckoutRequest {
  paymentMethod: string;
  shippingAddress: CheckoutShippingAddress;
}

export interface CheckoutOrder {
  id: string;
  userId: string;
  status: string;
  paymentMethod: string;
  total: number;
  currency: string;
  shippingFullName: string;
  shippingStreet: string;
  shippingCity: string;
  shippingPostalCode: string;
  shippingCountry: string;
  createdAt: string;
}

export interface CheckoutOrderItem {
  id: string;
  orderId: string;
  productId: string;
  name: string;
  quantity: number;
  unitPrice: number;
}

export interface CheckoutResponse {
  data: {
    order: CheckoutOrder;
    items: CheckoutOrderItem[];
  };
}
