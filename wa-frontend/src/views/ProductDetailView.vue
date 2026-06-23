<template>
  <main
    class="pt-20 md:pt-32 max-w-7xl mx-auto px-margin-mobile md:px-margin-desktop pb-section-gap"
  >
    <div v-if="store.loading" class="animate-pulse space-y-8 mt-8">
      <div class="h-8 bg-surface-container-low rounded w-1/3"></div>
      <div class="aspect-[4/5] bg-surface-container-low rounded"></div>
    </div>

    <p v-else-if="store.error" class="text-error font-body mt-8">{{ store.error }}</p>

    <div v-else-if="store.currentProduct" class="grid grid-cols-1 lg:grid-cols-12 gap-16 mt-8">
      <div class="lg:col-span-7">
        <div class="aspect-[4/5] overflow-hidden bg-surface-container-low">
          <img
            :src="
              store.currentProduct.imageUrl ??
              'https://picsum.photos/seed/' + store.currentProduct.id + '/800/1000'
            "
            :alt="store.currentProduct.name"
            class="w-full h-full object-cover"
            width="800"
            height="1000"
          />
        </div>
      </div>

      <!-- Info -->
      <div class="lg:col-span-5 lg:sticky lg:top-32 self-start">
        <button
          @click="router.back()"
          class="inline-flex items-center gap-2 text-[#514440] hover:text-[#34150c] transition-colors mb-8 group"
        >
          <span class="material-symbols-outlined transition-transform group-hover:-translate-x-1"
            >arrow_back</span
          >
          <span class="text-sm font-semibold">Terug</span>
        </button>
        <nav
          class="mb-8 text-xs font-body text-on-surface-variant uppercase tracking-widest flex gap-2"
        >
          <RouterLink to="/producten" class="hover:text-primary">Shop</RouterLink>
          <span>/</span>
          <span>{{ store.currentProduct.category }}</span>
        </nav>

        <h1 class="font-display text-5xl text-primary mb-3">{{ store.currentProduct.name }}</h1>
        <p class="font-body text-2xl text-secondary mb-8">
          €{{ store.currentProduct.price.toFixed(2).replace('.', ',') }}
        </p>
        <p class="font-body text-lg text-on-surface-variant mb-10 leading-relaxed max-w-md">
          {{ store.currentProduct.description }}
        </p>

        <div class="border-t border-warm-sand pt-8 space-y-4">
          <div class="flex items-center gap-4">
            <div class="flex items-center border border-outline px-4 py-3 rounded-lg">
              <button @click="qty > 1 && qty--" class="active:opacity-50">
                <span class="material-symbols-outlined text-sm">remove</span>
              </button>
              <span class="w-10 text-center font-body text-sm">{{ qty }}</span>
              <button @click="qty++" class="active:opacity-50">
                <span class="material-symbols-outlined text-sm">add</span>
              </button>
            </div>
            <button
              class="flex-1 bg-primary text-white py-4 px-8 font-body text-sm uppercase tracking-widest hover:bg-primary-container transition-colors active:scale-[0.98]"
            >
              In winkelwagen
            </button>
          </div>
          <p
            v-if="store.currentProduct.stock > 0"
            class="text-muted-sage font-body text-sm flex items-center gap-2"
          >
            <span class="material-symbols-outlined text-sm">check_circle</span>
            Op voorraad — klaar voor verzending
          </p>
          <p v-else class="text-error font-body text-sm">Uitverkocht</p>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useProductStore } from '@/stores/productStore'

const route = useRoute()
const router = useRouter()
const store = useProductStore()
const qty = ref(1)

onMounted(() => store.fetchProductById(Number(route.params.id)))
</script>
