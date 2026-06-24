import axios from 'axios'
import type { Product } from './productService'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api',
  withCredentials: true,
})

export interface ProductRequest {
  name: string
  description?: string
  price: number
  imageUrl?: string
  category?: string
  stock: number
}

export interface AdminOrderItem {
  productName: string
  quantity: number
  priceAtPurchase: number
}

export type OrderStatus =
  | 'PENDING_PAYMENT'
  | 'PAID'
  | 'PROCESSING'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELLED'

export interface AdminOrder {
  id: number
  customerName: string
  customerEmail: string
  shippingAddress: string
  status: OrderStatus
  totalAmount: number
  createdAt: string
  items: AdminOrderItem[]
  trackingCode?: string | null
}

export const adminService = {
  createProduct: (req: ProductRequest) =>
    api.post<Product>('/admin/products', req),
  updateProduct: (id: number, req: ProductRequest) =>
    api.put<Product>(`/admin/products/${id}`, req),
  deleteProduct: (id: number) =>
    api.delete(`/admin/products/${id}`),
  uploadImage: (id: number, formData: FormData) =>
    api.post<Product>(`/admin/products/${id}/image`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    }),

  getOrders: () =>
    api.get<AdminOrder[]>('/admin/orders'),
  updateOrderStatus: (id: number, status: OrderStatus, trackingCode?: string) =>
    api.patch<AdminOrder>(`/admin/orders/${id}/status`, { status, trackingCode: trackingCode || undefined }),
}
