import { defineStore } from 'pinia'
import { ref } from 'vue'
import { productService, type Product } from '@/services/productService'

export const useProductStore = defineStore('product', () => {
  const products = ref<Product[]>([])
  const currentProduct = ref<Product | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchProducts(category?: string) {
    loading.value = true
    error.value = null
    try {
      const response = category
        ? await productService.getByCategory(category)
        : await productService.getAll()
      products.value = response.data
    } catch (e) {
      error.value = 'Producten konden niet worden geladen.'
    } finally {
      loading.value = false
    }
  }

  async function fetchProductById(id: number) {
    loading.value = true
    error.value = null
    try {
      const response = await productService.getById(id)
      currentProduct.value = response.data
    } catch (e) {
      error.value = 'Product niet gevonden.'
    } finally {
      loading.value = false
    }
  }

  return { products, currentProduct, loading, error, fetchProducts, fetchProductById }
})
