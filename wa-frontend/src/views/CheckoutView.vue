<template>
  <main class="pt-32 pb-section-gap max-w-7xl mx-auto px-margin-mobile md:px-margin-desktop">
    <header class="mb-16 animate-fade-in-up">
      <nav class="flex items-center gap-2 text-on-surface-variant text-xs font-body mb-4">
        <RouterLink to="/winkelwagen" class="hover:text-primary">Winkelwagen</RouterLink>
        <span class="material-symbols-outlined text-xs">chevron_right</span>
        <span class="text-primary">Afrekenen</span>
      </nav>
      <h1 class="font-display text-5xl text-primary">Afrekenen</h1>
    </header>

    <p v-if="initError" class="mb-8 font-body text-sm text-error bg-error/5 border border-error/20 px-4 py-3">
      Er is een technisch probleem opgetreden. Probeer de pagina te vernieuwen of neem contact op.
    </p>

    <div class="grid grid-cols-1 lg:grid-cols-12 gap-16">
      <form @submit.prevent="submit" class="lg:col-span-7 space-y-10">

        <!-- Personal details -->
        <section>
          <h2 class="font-display text-2xl text-primary mb-6">Gegevens</h2>
          <div class="space-y-6">
            <div>
              <label class="block font-body text-xs text-on-surface-variant uppercase tracking-widest mb-2">Naam</label>
              <input
                v-model="form.customerName"
                type="text"
                required
                placeholder="Voor- en achternaam"
                class="w-full border-b border-outline bg-transparent font-body text-on-surface py-3 focus:outline-none focus:border-primary transition-colors placeholder:text-outline"
              />
            </div>
            <div>
              <label class="block font-body text-xs text-on-surface-variant uppercase tracking-widest mb-2">E-mailadres</label>
              <input
                v-model="form.customerEmail"
                type="email"
                required
                placeholder="jouw@email.nl"
                class="w-full border-b border-outline bg-transparent font-body text-on-surface py-3 focus:outline-none focus:border-primary transition-colors placeholder:text-outline"
              />
            </div>
          </div>
        </section>

        <!-- Delivery Address -->
        <section>
          <h2 class="font-display text-2xl text-primary mb-6">Bezorgadres</h2>
          <div class="space-y-6">

            <!-- Street name with autocompletion -->
            <div class="relative">
              <label class="block font-body text-xs text-on-surface-variant uppercase tracking-widest mb-2">Straatnaam</label>
              <input
                v-model="address.straatnaam"
                @input="onStraatnaamInput"
                @blur="hideSuggestionsDelayed"
                @keydown.escape="showSuggestions = false"
                @keydown.down.prevent="focusSuggestion(0)"
                type="text"
                required
                autocomplete="off"
                placeholder="Begin met typen..."
                class="w-full border-b border-outline bg-transparent font-body text-on-surface py-3 focus:outline-none focus:border-primary transition-colors placeholder:text-outline"
              />
              <Transition name="fade-in">
                <ul
                  v-if="showSuggestions && streetSuggestions.length"
                  class="absolute z-50 left-0 right-0 bg-surface border border-outline-variant shadow-sm mt-1 divide-y divide-outline-variant/20 max-h-60 overflow-y-auto"
                >
                  <li
                    v-for="(s, i) in streetSuggestions"
                    :key="s.id"
                    :ref="(el) => (suggestionRefs[i] = el as HTMLElement)"
                    @mousedown.prevent="selectSuggestion(s)"
                    @keydown.enter.prevent="selectSuggestion(s)"
                    @keydown.down.prevent="focusSuggestion(i + 1)"
                    @keydown.up.prevent="i === 0 ? undefined : focusSuggestion(i - 1)"
                    tabindex="-1"
                    class="px-4 py-3 font-body text-sm text-on-surface hover:bg-surface-container-low cursor-pointer transition-colors"
                  >
                    {{ s.display }}
                  </li>
                </ul>
              </Transition>
            </div>

            <!-- Huisnummer + toevoeging -->
            <div class="grid grid-cols-12 gap-4">
              <div class="col-span-4">
                <label class="block font-body text-xs text-on-surface-variant uppercase tracking-widest mb-2">Huisnr.</label>
                <input
                  v-model="address.huisnummer"
                  @blur="lookupByPostcodeHuisnummer"
                  type="text"
                  required
                  placeholder="12"
                  class="w-full border-b border-outline bg-transparent font-body text-on-surface py-3 focus:outline-none focus:border-primary transition-colors placeholder:text-outline"
                />
              </div>
              <div class="col-span-8">
                <label class="block font-body text-xs text-on-surface-variant uppercase tracking-widest mb-2">Toevoeging</label>
                <input
                  v-model="address.toevoeging"
                  type="text"
                  placeholder="A, ..."
                  class="w-full border-b border-outline bg-transparent font-body text-on-surface py-3 focus:outline-none focus:border-primary transition-colors placeholder:text-outline"
                />
              </div>
            </div>

            <!-- Postcode + plaatsnaam -->
            <div class="grid grid-cols-12 gap-4">
              <div class="col-span-4">
                <label class="block font-body text-xs text-on-surface-variant uppercase tracking-widest mb-2">Postcode</label>
                <input
                  v-model="address.postcode"
                  @input="onPostcodeInput"
                  @blur="lookupByPostcodeHuisnummer"
                  type="text"
                  required
                  maxlength="7"
                  placeholder="1234 AB"
                  class="w-full border-b border-outline bg-transparent font-body text-on-surface py-3 focus:outline-none focus:border-primary transition-colors placeholder:text-outline uppercase"
                />
              </div>
              <div class="col-span-8">
                <label class="block font-body text-xs text-on-surface-variant uppercase tracking-widest mb-2">Plaatsnaam</label>
                <input
                  v-model="address.plaatsnaam"
                  type="text"
                  required
                  placeholder="Amsterdam"
                  class="w-full border-b border-outline bg-transparent font-body text-on-surface py-3 focus:outline-none focus:border-primary transition-colors placeholder:text-outline"
                />
              </div>
            </div>

            <!-- Lookup feedback -->
            <div v-if="lookingUpAddress" class="flex items-center gap-2 font-body text-xs text-on-surface-variant -mt-2">
              <span class="material-symbols-outlined text-sm" style="animation: spin 1s linear infinite">refresh</span>
              Adres opzoeken...
            </div>
            <p v-if="addressLookupError" class="font-body text-xs text-error -mt-2">{{ addressLookupError }}</p>

            <!-- Land (readonly) -->
            <div>
              <label class="block font-body text-xs text-on-surface-variant uppercase tracking-widest mb-2">Land</label>
              <input
                value="Nederland"
                type="text"
                readonly
                class="w-full border-b border-outline-variant bg-transparent font-body text-on-surface-variant py-3 cursor-not-allowed"
              />
            </div>
          </div>
        </section>

        <!-- Betaling -->
        <section>
          <h2 class="font-display text-2xl text-primary mb-6">Betaling</h2>
          <div v-if="loadingStripe && !initError" class="animate-pulse space-y-3">
            <div class="h-12 bg-surface-container rounded"></div>
            <div class="h-12 bg-surface-container rounded"></div>
          </div>
          <div ref="paymentElementRef"></div>
          <p v-if="stripeError" class="mt-3 font-body text-sm text-error">{{ stripeError }}</p>
        </section>

        <button
          type="submit"
          :disabled="processing || loadingStripe || !!initError"
          class="w-full bg-primary text-white py-4 px-8 font-body text-sm uppercase tracking-widest transition-colors active:scale-[0.98] disabled:opacity-40 disabled:cursor-not-allowed overflow-hidden"
          :class="processing ? 'bg-primary-container' : 'hover:bg-primary-container'"
        >
          <Transition name="btn-text" mode="out-in">
            <span :key="processing ? 'processing' : 'idle'">
              {{ processing ? 'Verwerken...' : `Betaal €${grandTotal.toFixed(2).replace('.', ',')}` }}
            </span>
          </Transition>
        </button>
      </form>

      <!-- Besteloverzicht -->
      <aside class="lg:col-span-5 lg:sticky lg:top-32 self-start">
        <div class="bg-surface-container-low p-8 animate-fade-in-up" style="animation-delay: 150ms">
          <h2 class="font-display text-2xl text-primary mb-8">Jouw bestelling</h2>
          <ul class="space-y-4 mb-8">
            <li v-for="item in cartStore.items" :key="item.product.id" class="flex items-center gap-4">
              <div class="w-14 h-14 bg-surface-container overflow-hidden shrink-0">
                <img
                  :src="item.product.imageUrl ?? 'https://picsum.photos/seed/' + item.product.id + '/100/100'"
                  :alt="item.product.name"
                  class="w-full h-full object-cover"
                  width="56"
                  height="56"
                />
              </div>
              <div class="flex-1 min-w-0">
                <p class="font-body text-sm text-primary truncate">{{ item.product.name }}</p>
                <p class="font-body text-xs text-on-surface-variant">{{ item.quantity }}×</p>
              </div>
              <span class="font-body text-sm font-semibold text-primary shrink-0">
                €{{ (item.product.price * item.quantity).toFixed(2).replace('.', ',') }}
              </span>
            </li>
          </ul>
          <div class="border-t border-outline-variant/30 pt-6 space-y-2">
            <div class="flex justify-between font-body text-sm text-on-surface-variant">
              <span>Subtotaal</span>
              <span>€{{ cartStore.totalPrice.toFixed(2).replace('.', ',') }}</span>
            </div>
            <div class="flex justify-between font-body text-sm text-on-surface-variant">
              <span>Verzendkosten</span>
              <span>{{ shippingCost === 0 ? 'Gratis' : '€' + shippingCost.toFixed(2).replace('.', ',') }}</span>
            </div>
            <div class="flex justify-between font-body font-semibold text-primary text-lg pt-2 border-t border-outline-variant/20">
              <span>Totaal</span>
              <span>€{{ grandTotal.toFixed(2).replace('.', ',') }}</span>
            </div>
          </div>
        </div>
      </aside>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { loadStripe, type Stripe, type StripeElements } from '@stripe/stripe-js'
import { useCartStore } from '@/stores/cartStore'
import { orderService } from '@/services/orderService'

const router = useRouter()
const cartStore = useCartStore()

const SHIPPING_THRESHOLD = 75
const SHIPPING_COST = 4.95

const shippingCost = computed(() =>
  cartStore.totalPrice >= SHIPPING_THRESHOLD ? 0 : SHIPPING_COST,
)
const grandTotal = computed(() => cartStore.totalPrice + shippingCost.value)

const form = reactive({ customerName: '', customerEmail: '' })

const address = reactive({
  straatnaam: '',
  huisnummer: '',
  toevoeging: '',
  postcode: '',
  plaatsnaam: '',
})

const shippingAddress = computed(() => {
  const toevoeging = address.toevoeging ? ` ${address.toevoeging}` : ''
  return `${address.straatnaam} ${address.huisnummer}${toevoeging}, ${address.postcode.toUpperCase()}, ${address.plaatsnaam}, Nederland`
})

// ─── Street name autocomplete (PDOK) ────────────────────────────────────────

interface Suggestion {
  id: string
  display: string
}

const streetSuggestions = ref<Suggestion[]>([])
const showSuggestions = ref(false)
const suggestionRefs = ref<HTMLElement[]>([])
let debounceTimer: ReturnType<typeof setTimeout> | null = null

async function onStraatnaamInput() {
  if (debounceTimer) clearTimeout(debounceTimer)
  if (address.straatnaam.length < 3) {
    streetSuggestions.value = []
    showSuggestions.value = false
    return
  }
  debounceTimer = setTimeout(async () => {
    try {
      const query = encodeURIComponent(`${address.straatnaam} ${address.postcode}`.trim())
      const url = `https://api.pdok.nl/bzk/locatieserver/search/v3_1/suggest?q=${query}&fq=type:adres&rows=6`
      const res = await fetch(url)
      const data = await res.json()
      streetSuggestions.value = (data.response?.docs ?? []).map((doc: Record<string, string>) => ({
        id: doc.id,
        display: doc.weergavenaam,
      }))
      showSuggestions.value = streetSuggestions.value.length > 0
    } catch {
      // silently ignore suggest failures
    }
  }, 280)
}

async function selectSuggestion(s: Suggestion) {
  showSuggestions.value = false
  streetSuggestions.value = []
  try {
    const url = `https://api.pdok.nl/bzk/locatieserver/search/v3_1/lookup?id=${s.id}`
    const res = await fetch(url)
    const data = await res.json()
    const doc = data.response?.docs?.[0]
    if (doc) {
      address.straatnaam = doc.straatnaam ?? address.straatnaam
      address.huisnummer = doc.huisnummer != null ? String(doc.huisnummer) : address.huisnummer
      address.postcode = doc.postcode ?? address.postcode
      address.plaatsnaam = doc.woonplaatsnaam ?? address.plaatsnaam
    }
  } catch {
    // silently ignore lookup failures
  }
}

function focusSuggestion(index: number) {
  const el = suggestionRefs.value[index]
  if (el) el.focus()
}

function hideSuggestionsDelayed() {
  setTimeout(() => (showSuggestions.value = false), 150)
}

// ─── Postcode + house number lookup (PDOK) ───────────────────────────────────

const lookingUpAddress = ref(false)
const addressLookupError = ref<string | null>(null)

async function lookupByPostcodeHuisnummer() {
  const postcodeClean = address.postcode.replace(/\s/g, '').toUpperCase()
  if (!/^\d{4}[A-Z]{2}$/.test(postcodeClean) || !address.huisnummer) return

  lookingUpAddress.value = true
  addressLookupError.value = null
  try {
    const url = `https://api.pdok.nl/bzk/locatieserver/search/v3_1/free?q=${postcodeClean}+${address.huisnummer}&fq=type:adres&rows=1`
    const res = await fetch(url)
    const data = await res.json()
    const doc = data.response?.docs?.[0]
    if (doc) {
      address.straatnaam = doc.straatnaam ?? address.straatnaam
      address.plaatsnaam = doc.woonplaatsnaam ?? address.plaatsnaam
      if (doc.postcode) address.postcode = doc.postcode
    } else {
      addressLookupError.value = 'Adres niet gevonden. Vul straatnaam en plaatsnaam handmatig in.'
    }
  } catch {
    addressLookupError.value = 'Kon adres niet opzoeken. Vul handmatig in.'
  } finally {
    lookingUpAddress.value = false
  }
}

function onPostcodeInput(event: Event) {
  const raw = (event.target as HTMLInputElement).value.replace(/\s/g, '').toUpperCase()
  address.postcode = raw.length > 4 ? raw.slice(0, 4) + ' ' + raw.slice(4, 6) : raw
}

// ─── Stripe ──────────────────────────────────────────────────────────────────

const paymentElementRef = ref<HTMLElement | null>(null)
const stripe = ref<Stripe | null>(null)
const elements = ref<StripeElements | null>(null)
const loadingStripe = ref(true)
const processing = ref(false)
const stripeError = ref<string | null>(null)
const initError = ref<string | null>(null)

onMounted(async () => {
  if (cartStore.items.length === 0) {
    router.replace('/winkelwagen')
    return
  }
  try {
    stripe.value = await loadStripe(import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY)
    if (!stripe.value) throw new Error('Stripe kon niet worden geladen.')

    const { data } = await orderService.createPaymentIntent(
      cartStore.items.map((item) => ({ productId: item.product.id, quantity: item.quantity })),
    )

    elements.value = stripe.value.elements({
      clientSecret: data.clientSecret,
      appearance: {
        theme: 'flat',
        variables: {
          colorPrimary: '#34150c',
          colorBackground: '#fbf9f2',
          colorText: '#1b1c18',
          colorDanger: '#ba1a1a',
          fontFamily: '"Hanken Grotesk", Helvetica, sans-serif',
          borderRadius: '4px',
        },
        rules: {
          '.Input': { border: '0', borderBottom: '1px solid #837470', borderRadius: '0' },
          '.Input:focus': { borderBottom: '1px solid #34150c', boxShadow: 'none' },
        },
      },
    })

    elements.value.create('payment').mount(paymentElementRef.value!)
  } catch (e) {
    console.error('[Checkout] initialization error:', e)
    initError.value = 'technisch probleem'
  } finally {
    loadingStripe.value = false
  }
})

// ─── Submit ───────────────────────────────────────────────────────────────────

async function submit() {
  if (!stripe.value || !elements.value || processing.value) return
  processing.value = true
  stripeError.value = null

  const orderData = {
    customerName: form.customerName,
    customerEmail: form.customerEmail,
    shippingAddress: shippingAddress.value,
    items: cartStore.items.map((i) => ({ productId: i.product.id, quantity: i.quantity })),
  }

  sessionStorage.setItem('wa-checkout-data', JSON.stringify(orderData))

  const { error, paymentIntent } = await stripe.value.confirmPayment({
    elements: elements.value,
    confirmParams: { return_url: `${window.location.origin}/bevestiging` },
    redirect: 'if_required',
  })

  if (error) {
    stripeError.value = error.message ?? 'Betaling mislukt. Probeer het opnieuw.'
    processing.value = false
    return
  }

  if (paymentIntent?.status === 'succeeded') {
    const { data } = await orderService.createOrder({
      ...orderData,
      stripePaymentIntentId: paymentIntent.id,
    })
    cartStore.clearCart()
    sessionStorage.removeItem('wa-checkout-data')
    sessionStorage.setItem('wa-order-success', JSON.stringify({ id: data.id, email: data.customerEmail }))
    router.push('/bevestiging')
  }

  processing.value = false
}
</script>
