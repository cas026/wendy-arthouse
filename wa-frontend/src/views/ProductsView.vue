<template>
  <main class="pt-32 pb-section-gap max-w-7xl mx-auto px-margin-mobile md:px-margin-desktop">
    <header class="mb-16">
      <nav class="flex items-center gap-2 text-on-surface-variant text-xs font-body mb-4">
        <RouterLink to="/" class="hover:text-primary">Home</RouterLink>
        <span class="material-symbols-outlined text-xs">chevron_right</span>
        <span class="text-primary">Collecties</span>
      </nav>
      <h1 class="font-display text-5xl text-primary mb-4">Alle Keramiek</h1>
      <p class="font-body text-lg text-on-surface-variant max-w-xl">
        Een samengestelde selectie van handgemaakte stukken. Elk object viert de imperfecties van
        natuurlijke klei.
      </p>
    </header>

    <CategoryFilter :selected="activeCategory" @select="onCategorySelect" />

    <div
      v-if="store.loading"
      class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-x-gutter gap-y-16"
    >
      <div v-for="n in 8" :key="n" class="animate-pulse">
        <div class="aspect-[4/5] bg-surface-container-low mb-6 rounded"></div>
        <div class="h-5 bg-surface-container-low rounded w-3/4 mb-2"></div>
        <div class="h-4 bg-surface-container-low rounded w-1/2"></div>
      </div>
    </div>

    <p v-else-if="store.error" class="text-error font-body">{{ store.error }}</p>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-x-gutter gap-y-16">
      <div
        v-for="(product, index) in store.products"
        :key="product.id"
        class="animate-fade-in-up"
        :style="{ animationDelay: `${Math.min(index, 7) * 70}ms` }"
      >
        <ProductCard :product="product" />
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useProductStore } from '@/stores/productStore'
import ProductCard from '@/components/ProductCard.vue'
import CategoryFilter from '@/components/CategoryFilter.vue'

const store = useProductStore()
const activeCategory = ref<string | null>(null)

onMounted(() => store.fetchProducts())

function onCategorySelect(category: string | null) {
  activeCategory.value = category
  store.fetchProducts(category ?? undefined)
}
</script>
