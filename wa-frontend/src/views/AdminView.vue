<template>
  <main class="pt-32 pb-section-gap max-w-7xl mx-auto px-margin-mobile md:px-margin-desktop">
    <header class="mb-12 flex items-end justify-between flex-wrap gap-4">
      <div>
        <p class="font-body text-xs text-on-surface-variant uppercase tracking-widest mb-1">Intern</p>
        <h1 class="font-display text-5xl text-primary">Admin</h1>
      </div>
      <div class="flex items-center gap-6">
        <nav class="flex gap-1 border border-outline-variant/40 p-1">
          <RouterLink
            v-for="tab in tabs"
            :key="tab.key"
            :to="{ path: '/admin', query: { tab: tab.key } }"
            class="px-6 py-2 font-body text-sm uppercase tracking-widest transition-colors"
            :class="activeTab === tab.key
              ? 'bg-primary text-white'
              : 'text-on-surface-variant hover:text-primary'"
          >
            {{ tab.label }}
          </RouterLink>
        </nav>
        <button
          @click="handleLogout"
          class="font-body text-sm text-on-surface-variant hover:text-error uppercase tracking-widest transition-colors flex items-center gap-1"
        >
          <span class="material-symbols-outlined text-sm">logout</span>
          Uitloggen
        </button>
      </div>
    </header>

    <!-- ─── Products tab ─────────────────────────────────────────────────────── -->
    <section v-if="activeTab === 'products'">
      <div class="flex justify-between items-center mb-6">
        <h2 class="font-display text-2xl text-primary">Producten</h2>
        <button
          @click="openCreateModal"
          class="flex items-center gap-2 bg-primary text-white px-6 py-3 font-body text-sm uppercase tracking-widest hover:bg-primary-container transition-colors"
        >
          <span class="material-symbols-outlined text-sm">add</span>
          Product toevoegen
        </button>
      </div>

      <div v-if="productsLoading" class="py-16 text-center font-body text-on-surface-variant">Laden…</div>
      <div v-else-if="products.length === 0" class="py-16 text-center font-body text-on-surface-variant">Nog geen producten.</div>

      <div v-else class="overflow-x-auto">
        <table class="w-full font-body text-sm">
          <thead>
            <tr class="border-b border-outline-variant/40 text-left">
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs w-12">#</th>
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs w-16"></th>
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs">Naam</th>
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs">Categorie</th>
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs text-right">Prijs</th>
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs text-right">Stock</th>
              <th class="pb-3 font-semibold text-on-surface-variant uppercase tracking-widest text-xs w-20"></th>
            </tr>
          </thead>
          <tbody class="divide-y divide-outline-variant/20">
            <tr v-for="product in products" :key="product.id" class="hover:bg-surface-container-low/50 transition-colors">
              <td class="py-3 pr-4 text-on-surface-variant">{{ product.id }}</td>
              <td class="py-3 pr-4">
                <img
                  :src="product.imageUrl ?? FALLBACK_IMAGE"
                  :alt="product.name"
                  class="w-10 h-10 object-cover bg-surface-container-low"
                />
              </td>
              <td class="py-3 pr-4 text-primary font-medium">{{ product.name }}</td>
              <td class="py-3 pr-4 text-on-surface-variant">{{ product.category ?? '—' }}</td>
              <td class="py-3 pr-4 text-right">€{{ Number(product.price).toFixed(2).replace('.', ',') }}</td>
              <td class="py-3 pr-4 text-right" :class="product.stock === 0 ? 'text-error' : 'text-on-surface-variant'">
                {{ product.stock }}
              </td>
              <td class="py-3">
                <div class="flex items-center gap-2 justify-end">
                  <button @click="openEditModal(product)" class="text-on-surface-variant hover:text-primary transition-colors" title="Bewerken">
                    <span class="material-symbols-outlined text-sm">edit</span>
                  </button>
                  <button @click="confirmDelete(product)" class="text-on-surface-variant hover:text-error transition-colors" title="Verwijderen">
                    <span class="material-symbols-outlined text-sm">delete</span>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <!-- ─── Orders tab ───────────────────────────────────────────────────────── -->
    <section v-else-if="activeTab === 'orders'">
      <h2 class="font-display text-2xl text-primary mb-6">Bestellingen</h2>

      <div v-if="ordersLoading" class="py-16 text-center font-body text-on-surface-variant">Laden…</div>
      <div v-else-if="orders.length === 0" class="py-16 text-center font-body text-on-surface-variant">Nog geen bestellingen.</div>

      <div v-else class="overflow-x-auto">
        <table class="w-full font-body text-sm">
          <thead>
            <tr class="border-b border-outline-variant/40 text-left">
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs w-12">#</th>
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs">Klant</th>
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs">E-mail</th>
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs text-right">Totaal</th>
              <th class="pb-3 pr-4 font-semibold text-on-surface-variant uppercase tracking-widest text-xs">Status</th>
              <th class="pb-3 font-semibold text-on-surface-variant uppercase tracking-widest text-xs">Datum</th>
            </tr>
          </thead>
          <tbody>
            <!-- <template v-for> allows two sibling <tr> rows per order -->
            <template v-for="order in orders" :key="order.id">
              <tr
                class="border-b border-outline-variant/20 hover:bg-surface-container-low/50 transition-colors cursor-pointer"
                @click="toggleOrderDetail(order.id)"
              >
                <td class="py-4 pr-4 text-on-surface-variant">{{ order.id }}</td>
                <td class="py-4 pr-4 text-primary font-medium">{{ order.customerName }}</td>
                <td class="py-4 pr-4 text-on-surface-variant">{{ order.customerEmail }}</td>
                <td class="py-4 pr-4 text-right font-semibold">
                  €{{ Number(order.totalAmount).toFixed(2).replace('.', ',') }}
                </td>
                <td class="py-4 pr-4" @click.stop>
                  <select
                    :value="order.status"
                    @change="onStatusChange(order.id, ($event.target as HTMLSelectElement).value)"
                    class="font-body text-xs uppercase tracking-widest border-0 bg-transparent cursor-pointer focus:outline-none"
                    :class="statusColor(order.status)"
                  >
                    <option v-for="s in orderStatuses" :key="s.value" :value="s.value">{{ s.label }}</option>
                  </select>
                </td>
                <td class="py-4 text-on-surface-variant text-xs">{{ formatDate(order.createdAt) }}</td>
              </tr>
              <tr v-if="expandedOrderId === order.id" class="bg-surface-container-low/40 border-b border-outline-variant/20">
                <td colspan="6" class="px-4 py-4">
                  <p class="font-body text-xs text-on-surface-variant uppercase tracking-widest mb-3">Artikelen</p>
                  <ul class="space-y-1 mb-3">
                    <li
                      v-for="item in order.items"
                      :key="item.productName"
                      class="flex justify-between font-body text-sm text-on-surface-variant"
                    >
                      <span>{{ item.productName }} × {{ item.quantity }}</span>
                      <span>€{{ (Number(item.priceAtPurchase) * item.quantity).toFixed(2).replace('.', ',') }}</span>
                    </li>
                  </ul>
                  <p class="font-body text-xs text-on-surface-variant">
                    <span class="font-semibold text-primary">Verzendadres:</span> {{ order.shippingAddress }}
                  </p>
                  <p v-if="order.trackingCode" class="font-body text-xs text-on-surface-variant mt-1">
                    <span class="font-semibold text-primary">Trackingnummer:</span> {{ order.trackingCode }}
                  </p>
                </td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
    </section>

    <!-- ─── Product modal ─────────────────────────────────────────────────────── -->
    <Teleport to="body">
    <Transition name="modal">
      <div v-if="modalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4" @click.self="closeModal">
        <div class="absolute inset-0 bg-black/40"></div>
        <div class="relative bg-white max-w-lg w-full p-8 shadow-xl animate-fade-in-up overflow-y-auto max-h-[90vh]">
          <button @click="closeModal" class="absolute top-4 right-4 text-on-surface-variant hover:text-primary transition-colors">
            <span class="material-symbols-outlined">close</span>
          </button>

          <h2 class="font-display text-2xl text-primary mb-8">
            {{ editingProduct ? 'Product bewerken' : 'Product toevoegen' }}
          </h2>

          <form @submit.prevent="saveProduct" class="space-y-5">
            <div>
              <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">
                Naam <span class="text-error">*</span>
              </label>
              <input
                v-model="form.name"
                required
                class="w-full border-b border-outline bg-transparent py-2 font-body text-sm focus:outline-none focus:border-primary transition-colors"
                placeholder="Bijv. Handgemaakte schaal"
              />
            </div>

            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">
                  Prijs (€) <span class="text-error">*</span>
                </label>
                <input
                  v-model.number="form.price"
                  type="number"
                  step="0.01"
                  min="0.01"
                  required
                  class="w-full border-b border-outline bg-transparent py-2 font-body text-sm focus:outline-none focus:border-primary transition-colors"
                  placeholder="45.00"
                />
              </div>
              <div>
                <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">Stock</label>
                <input
                  v-model.number="form.stock"
                  type="number"
                  min="0"
                  class="w-full border-b border-outline bg-transparent py-2 font-body text-sm focus:outline-none focus:border-primary transition-colors"
                  placeholder="0"
                />
              </div>
            </div>

            <div>
              <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">Categorie</label>
              <input
                v-model="form.category"
                class="w-full border-b border-outline bg-transparent py-2 font-body text-sm focus:outline-none focus:border-primary transition-colors"
                placeholder="Bijv. Schalen"
              />
            </div>

            <!-- Image upload -->
            <div>
              <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">Afbeelding</label>
              <div class="flex items-start gap-4">
                <img
                  :src="imagePreview ?? form.imageUrl ?? FALLBACK_IMAGE"
                  alt="Productafbeelding"
                  class="w-20 h-20 object-cover bg-surface-container-low shrink-0"
                />
                <div class="flex-1">
                  <label
                    class="inline-flex items-center gap-2 cursor-pointer border border-outline px-4 py-2 font-body text-xs uppercase tracking-widest text-on-surface-variant hover:border-primary hover:text-primary transition-colors"
                  >
                    <span class="material-symbols-outlined text-sm">upload</span>
                    Kies bestand
                    <input type="file" accept="image/*" class="hidden" @change="onFileSelect" />
                  </label>
                  <p v-if="selectedFile" class="mt-1 font-body text-xs text-on-surface-variant truncate max-w-xs">
                    {{ selectedFile.name }}
                  </p>
                </div>
              </div>
            </div>

            <div>
              <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">Beschrijving</label>
              <textarea
                v-model="form.description"
                rows="3"
                class="w-full border-b border-outline bg-transparent py-2 font-body text-sm focus:outline-none focus:border-primary transition-colors resize-none"
                placeholder="Korte beschrijving van het product…"
              ></textarea>
            </div>

            <p v-if="modalError" class="font-body text-sm text-error">{{ modalError }}</p>

            <div class="flex justify-end gap-4 pt-2">
              <button type="button" @click="closeModal" class="font-body text-sm text-on-surface-variant hover:text-primary transition-colors uppercase tracking-widest">
                Annuleren
              </button>
              <button
                type="submit"
                :disabled="saving"
                class="bg-primary text-white px-8 py-3 font-body text-sm uppercase tracking-widest hover:bg-primary-container transition-colors disabled:opacity-50"
              >
                {{ saving ? 'Opslaan…' : 'Opslaan' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </Transition>
  </Teleport>

    <!-- ─── Delete confirmation modal ───────────────────────────────────────── -->
    <Teleport to="body">
      <Transition name="modal">
        <div v-if="deleteTarget" class="fixed inset-0 z-50 flex items-center justify-center p-4" @click.self="deleteTarget = null">
          <div class="absolute inset-0 bg-black/40"></div>
          <div class="relative bg-white max-w-sm w-full p-8 shadow-xl animate-fade-in-up text-center">
            <span class="material-symbols-outlined text-error mb-4 block" style="font-size: 48px">delete_forever</span>
            <h3 class="font-display text-xl text-primary mb-2">Product verwijderen?</h3>
            <p class="font-body text-sm text-on-surface-variant mb-8">"{{ deleteTarget.name }}" wordt permanent verwijderd.</p>
            <div class="flex justify-center gap-4">
              <button @click="deleteTarget = null" class="font-body text-sm text-on-surface-variant hover:text-primary uppercase tracking-widest transition-colors">
                Annuleren
              </button>
              <button
                @click="executeDelete"
                :disabled="deleting"
                class="bg-error text-white px-8 py-3 font-body text-sm uppercase tracking-widest hover:opacity-90 transition-opacity disabled:opacity-50"
              >
                {{ deleting ? 'Verwijderen…' : 'Verwijderen' }}
              </button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>

    <!-- ─── Verzonden modal ───────────────────────────────────────────────────── -->
    <Teleport to="body">
      <Transition name="modal">
        <div v-if="shippingModal" class="fixed inset-0 z-50 flex items-center justify-center p-4" @click.self="shippingModal = null">
          <div class="absolute inset-0 bg-black/40"></div>
          <div class="relative bg-white max-w-sm w-full p-8 shadow-xl animate-fade-in-up">
            <h3 class="font-display text-xl text-primary mb-2">Bestelling verzenden</h3>
            <p class="font-body text-sm text-on-surface-variant mb-6">
              Voer optioneel een trackingnummer in. De klant ontvangt een verzendbevestiging per e-mail.
            </p>
            <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">Trackingnummer (optioneel)</label>
            <input
              v-model="shippingModal.trackingCode"
              type="text"
              placeholder="bijv. 3SBOL123456789"
              class="w-full border-b border-outline bg-transparent py-2 font-body text-sm focus:outline-none focus:border-primary transition-colors mb-8"
            />
            <div class="flex justify-end gap-4">
              <button @click="shippingModal = null" class="font-body text-sm text-on-surface-variant hover:text-primary uppercase tracking-widest transition-colors">
                Annuleren
              </button>
              <button
                @click="confirmShipped"
                class="bg-primary text-white px-8 py-3 font-body text-sm uppercase tracking-widest hover:bg-primary-container transition-colors"
              >
                Verzenden
              </button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { productService, type Product } from '@/services/productService'
import { adminService, type AdminOrder, type OrderStatus, type ProductRequest } from '@/services/adminService'
import { useAuthStore } from '@/stores/authStore'

const FALLBACK_IMAGE = 'https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

// ─── Tabs ─────────────────────────────────────────────────────────────────────

const tabs = [
  { key: 'products', label: 'Producten' },
  { key: 'orders', label: 'Bestellingen' },
] as const

type TabKey = (typeof tabs)[number]['key']

// Tab state is derived from the URL query param (?tab=orders / ?tab=products)
// so clicking a tab updates the URL and the browser back button works correctly
const activeTab = computed<TabKey>(() =>
  route.query.tab === 'orders' ? 'orders' : 'products',
)

// ─── Logout ───────────────────────────────────────────────────────────────────

async function handleLogout() {
  try {
    await authStore.logout()
  } finally {
    router.push('/admin/login')
  }
}

// ─── Products ─────────────────────────────────────────────────────────────────

const products = ref<Product[]>([])
const productsLoading = ref(true)

async function loadProducts() {
  productsLoading.value = true
  const { data } = await productService.getAll()
  products.value = data
  productsLoading.value = false
}

// ─── Orders ───────────────────────────────────────────────────────────────────

const orders = ref<AdminOrder[]>([])
const ordersLoading = ref(true)
const expandedOrderId = ref<number | null>(null)

const orderStatuses: { value: OrderStatus; label: string }[] = [
  { value: 'PENDING_PAYMENT', label: 'Wacht op betaling' },
  { value: 'PAID', label: 'Betaald' },
  { value: 'PROCESSING', label: 'In verwerking' },
  { value: 'SHIPPED', label: 'Verzonden' },
  { value: 'DELIVERED', label: 'Afgeleverd' },
  { value: 'CANCELLED', label: 'Geannuleerd' },
]

function statusColor(status: OrderStatus): string {
  const map: Record<OrderStatus, string> = {
    PENDING_PAYMENT: 'text-amber-600',
    PAID: 'text-blue-600',
    PROCESSING: 'text-purple-600',
    SHIPPED: 'text-indigo-600',
    DELIVERED: 'text-green-700',
    CANCELLED: 'text-error',
  }
  return map[status] ?? ''
}

function formatDate(iso: string): string {
  return new Date(iso).toLocaleString('nl-NL', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

function toggleOrderDetail(id: number) {
  expandedOrderId.value = expandedOrderId.value === id ? null : id
}

async function loadOrders() {
  ordersLoading.value = true
  const { data } = await adminService.getOrders()
  orders.value = data
  ordersLoading.value = false
}

const shippingModal = ref<{ id: number; trackingCode: string } | null>(null)

async function onStatusChange(id: number, status: string) {
  if (status === 'SHIPPED') {
    shippingModal.value = { id, trackingCode: '' }
    return
  }
  const { data } = await adminService.updateOrderStatus(id, status as OrderStatus)
  const index = orders.value.findIndex((o) => o.id === id)
  if (index !== -1) orders.value[index] = data
}

async function confirmShipped() {
  if (!shippingModal.value) return
  const { id, trackingCode } = shippingModal.value
  const { data } = await adminService.updateOrderStatus(id, 'SHIPPED', trackingCode || undefined)
  const index = orders.value.findIndex((o) => o.id === id)
  if (index !== -1) orders.value[index] = data
  shippingModal.value = null
}

// ─── Product modal ────────────────────────────────────────────────────────────

const modalOpen = ref(false)
const editingProduct = ref<Product | null>(null)
const saving = ref(false)
const modalError = ref<string | null>(null)
const selectedFile = ref<File | null>(null)
const imagePreview = ref<string | null>(null)

const form = reactive<ProductRequest>({
  name: '',
  description: '',
  price: 0,
  imageUrl: '',
  category: '',
  stock: 0,
})

function onFileSelect(event: Event) {
  const file = (event.target as HTMLInputElement).files?.[0] ?? null
  selectedFile.value = file
  if (file) {
    imagePreview.value = URL.createObjectURL(file)
  }
}

function openCreateModal() {
  editingProduct.value = null
  selectedFile.value = null
  imagePreview.value = null
  Object.assign(form, { name: '', description: '', price: 0, imageUrl: '', category: '', stock: 0 })
  modalError.value = null
  modalOpen.value = true
}

function openEditModal(product: Product) {
  editingProduct.value = product
  selectedFile.value = null
  imagePreview.value = null
  Object.assign(form, {
    name: product.name,
    description: product.description ?? '',
    price: Number(product.price),
    imageUrl: product.imageUrl ?? '',
    category: product.category ?? '',
    stock: product.stock,
  })
  modalError.value = null
  modalOpen.value = true
}

function closeModal() {
  if (imagePreview.value) URL.revokeObjectURL(imagePreview.value)
  imagePreview.value = null
  selectedFile.value = null
  modalOpen.value = false
}

async function saveProduct() {
  saving.value = true
  modalError.value = null
  const payload: ProductRequest = {
    name: form.name,
    description: form.description || undefined,
    price: form.price,
    imageUrl: form.imageUrl || undefined,
    category: form.category || undefined,
    stock: form.stock,
  }
  try {
    let saved: Product
    if (editingProduct.value) {
      const { data } = await adminService.updateProduct(editingProduct.value.id, payload)
      saved = data
    } else {
      const { data } = await adminService.createProduct(payload)
      saved = data
    }

    // Upload image if one was selected
    if (selectedFile.value) {
      const formData = new FormData()
      formData.append('file', selectedFile.value)
      const { data: withImage } = await adminService.uploadImage(saved.id, formData)
      saved = withImage
    }

    const index = products.value.findIndex((p) => p.id === saved.id)
    if (index !== -1) {
      products.value[index] = saved
    } else {
      products.value.push(saved)
    }
    closeModal()
  } catch {
    modalError.value = 'Er ging iets mis. Controleer de invoer en probeer opnieuw.'
  } finally {
    saving.value = false
  }
}

// ─── Delete ───────────────────────────────────────────────────────────────────

const deleteTarget = ref<Product | null>(null)
const deleting = ref(false)

function confirmDelete(product: Product) {
  deleteTarget.value = product
}

async function executeDelete() {
  if (!deleteTarget.value) return
  deleting.value = true
  try {
    await adminService.deleteProduct(deleteTarget.value.id)
    products.value = products.value.filter((p) => p.id !== deleteTarget.value!.id)
    deleteTarget.value = null
  } catch {
    // deletion failed — product stays in list
  } finally {
    deleting.value = false
  }
}

// ─── Init ─────────────────────────────────────────────────────────────────────

onMounted(() => {
  loadProducts()
  loadOrders()
})
</script>

