import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import ProductsView from '@/views/ProductsView.vue'
import ProductDetailView from '@/views/ProductDetailView.vue'
import CartView from '@/views/CartView.vue'
import CheckoutView from '@/views/CheckoutView.vue'
import OrderConfirmationView from '@/views/OrderConfirmationView.vue'
import OverView from '@/views/OverView.vue'
import VerzendingView from '@/views/VerzendingView.vue'
import ContactView from '@/views/ContactView.vue'
import FaqView from '@/views/FaqView.vue'
import AdminView from '@/views/AdminView.vue'
import AdminLoginView from '@/views/AdminLoginView.vue'
import { useAuthStore } from '@/stores/authStore'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/producten', name: 'products', component: ProductsView },
    { path: '/producten/:id', name: 'product-detail', component: ProductDetailView },
    { path: '/winkelwagen', name: 'cart', component: CartView },
    { path: '/checkout', name: 'checkout', component: CheckoutView },
    { path: '/bevestiging', name: 'order-confirmation', component: OrderConfirmationView },
    { path: '/over', name: 'over', component: OverView },
    { path: '/verzending', name: 'verzending', component: VerzendingView },
    { path: '/contact', name: 'contact', component: ContactView },
    { path: '/faq', name: 'faq', component: FaqView },
    { path: '/admin/login', name: 'admin-login', component: AdminLoginView },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
      meta: { requiresAuth: true },
    },
  ],
})

router.beforeEach(async (to) => {
  if (!to.meta.requiresAuth) return

  const authStore = useAuthStore()
  const valid = await authStore.checkSession()
  if (!valid) return { name: 'admin-login' }
})

export default router
