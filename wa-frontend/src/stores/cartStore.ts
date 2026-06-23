import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import type { Product } from '@/services/productService'

export interface CartItem {
  product: Product
  quantity: number
}

export const useCartStore = defineStore('cart', () => {
  const items = ref<CartItem[]>([])

  const storedCart = localStorage.getItem('wa-cart')
  if (storedCart) {
    try {
      items.value = JSON.parse(storedCart)
    } catch {
      // ignore corrupt data
    }
  }

  watch(items, (newItems) => localStorage.setItem('wa-cart', JSON.stringify(newItems)), { deep: true })

  const totalItems = computed(() => items.value.reduce((sum, item) => sum + item.quantity, 0))

  const totalPrice = computed(() =>
    items.value.reduce((sum, item) => sum + item.product.price * item.quantity, 0),
  )

  function addItem(product: Product, quantity: number = 1) {
    const existing = items.value.find((item) => item.product.id === product.id)
    if (existing) {
      existing.quantity += quantity
    } else {
      items.value.push({ product, quantity })
    }
  }

  function removeItem(productId: number) {
    items.value = items.value.filter((item) => item.product.id !== productId)
  }

  function updateQuantity(productId: number, quantity: number) {
    if (quantity <= 0) {
      removeItem(productId)
      return
    }
    const item = items.value.find((item) => item.product.id === productId)
    if (item) item.quantity = quantity
  }

  function clearCart() {
    items.value = []
  }

  return { items, totalItems, totalPrice, addItem, removeItem, updateQuantity, clearCart }
})
