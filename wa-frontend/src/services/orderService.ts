import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api',
})

export interface CartItemDto {
  productId: number
  quantity: number
}

export interface CreateOrderRequest {
  customerName: string
  customerEmail: string
  shippingAddress: string
  items: CartItemDto[]
  stripePaymentIntentId: string
}

export interface OrderResponse {
  id: number
  status: string
  totalAmount: number
  customerEmail: string
}

export const orderService = {
  createPaymentIntent: (items: CartItemDto[]) =>
    api.post<{ clientSecret: string }>('/orders/payment-intent', { items }),

  createOrder: (request: CreateOrderRequest) =>
    api.post<OrderResponse>('/orders', request),
}
