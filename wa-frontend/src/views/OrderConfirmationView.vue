<template>
  <main class="pt-32 pb-section-gap max-w-7xl mx-auto px-margin-mobile md:px-margin-desktop">
    <div v-if="loading" class="flex justify-center py-32">
      <div class="animate-pulse space-y-4 text-center">
        <div class="h-8 bg-surface-container rounded w-64 mx-auto"></div>
        <div class="h-4 bg-surface-container rounded w-48 mx-auto"></div>
      </div>
    </div>

    <div v-else-if="error" class="text-center py-32">
      <p class="font-body text-error mb-4">{{ error }}</p>
      <RouterLink to="/winkelwagen" class="font-body text-sm text-primary underline">
        Terug naar winkelwagen
      </RouterLink>
    </div>

    <div v-else class="text-center max-w-lg mx-auto py-16 animate-fade-in-up">
      <span
        class="material-symbols-outlined text-muted-sage mb-6 block animate-pop"
        style="font-size: 64px"
      >
        check_circle
      </span>
      <h1 class="font-display text-4xl text-primary mb-4">Bestelling geplaatst!</h1>
      <p class="font-body text-on-surface-variant mb-2 leading-relaxed">
        Bedankt voor je bestelling. Je ontvangt een bevestiging op
        <strong class="text-primary">{{ email }}</strong>.
      </p>
      <p v-if="orderId" class="font-body text-xs text-on-surface-variant uppercase tracking-widest mb-12">
        Bestelnummer #{{ orderId }}
      </p>

      <RouterLink
        to="/producten"
        class="inline-block bg-primary text-white px-10 py-4 text-sm font-body font-semibold tracking-widest uppercase hover:bg-primary-container transition-colors"
      >
        Verder winkelen
      </RouterLink>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useCartStore } from '@/stores/cartStore'
import { orderService } from '@/services/orderService'

const cartStore = useCartStore()
const loading = ref(true)
const error = ref<string | null>(null)
const orderId = ref<number | null>(null)
const email = ref('')

onMounted(async () => {
  // Direct success: CheckoutView stored the result before navigating here
  const directSuccess = sessionStorage.getItem('wa-order-success')
  if (directSuccess) {
    const { id, email: customerEmail } = JSON.parse(directSuccess)
    orderId.value = id
    email.value = customerEmail
    sessionStorage.removeItem('wa-order-success')
    loading.value = false
    return
  }

  // 3D Secure redirect: Stripe redirected back here after authentication
  const params = new URLSearchParams(window.location.search)
  const paymentIntentId = params.get('payment_intent')
  const redirectStatus = params.get('redirect_status')

  if (paymentIntentId) {
    if (redirectStatus !== 'succeeded') {
      error.value = 'Je betaling is niet geslaagd. Ga terug en probeer het opnieuw.'
      loading.value = false
      return
    }

    const savedData = sessionStorage.getItem('wa-checkout-data')
    if (!savedData) {
      error.value = 'Bestelgegevens niet gevonden. Neem contact op als je betaling is afgeschreven.'
      loading.value = false
      return
    }
    try {
      const orderData = JSON.parse(savedData)
      const { data } = await orderService.createOrder({ ...orderData, stripePaymentIntentId: paymentIntentId })
      orderId.value = data.id
      email.value = data.customerEmail
      cartStore.clearCart()
      sessionStorage.removeItem('wa-checkout-data')
    } catch {
      error.value = 'Er ging iets mis bij het verwerken van je bestelling. Neem contact op.'
    }
  } else {
    error.value = 'Geen bestelling gevonden.'
  }

  loading.value = false
})
</script>
