import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api',
})

export interface Product {
  id: number
  name: string
  description: string | null
  price: number
  imageUrl: string | null
  category: string | null
  stock: number
}

export const productService = {
  getAll: () => api.get<Product[]>('/products'),
  getById: (id: number) => api.get<Product>(`/products/${id}`),
  getByCategory: (category: string) => api.get<Product[]>(`/products?category=${category}`),
}
