export interface Category {
  id: string;
  name: string;
  description: string;
}

export interface CategoriesResponse {
  data: Category[];
  meta: {
    count: number;
  };
}

export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  currency: string;
  inStock: boolean;
  stock: number;
  categoryId: string;
  imageUrl: string;
}

export interface ProductPageResponse {
  content: Product[];
}
