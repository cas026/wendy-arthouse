import { createRouter, createWebHistory } from 'vue-router'
import ProductsView from '@/views/ProductsView.vue'
import ProductDetailView from '@/views/ProductDetailView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/producten',
      name: 'products',
      component: ProductsView,
    },
    {
      path: '/producten/:id',
      name: 'product-detail',
      component: ProductDetailView,
    },
  ],
})

export default router
