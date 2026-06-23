<template>
  <RouterLink :to="`/producten/${product.id}`" class="group cursor-pointer block">
    <div class="relative aspect-[4/5] overflow-hidden mb-6 bg-surface-container-low">
      <img
        :src="product.imageUrl ?? 'https://picsum.photos/seed/' + product.id + '/400/500'"
        :alt="product.name"
        class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105"
        loading="lazy"
        width="400"
        height="500"
      />
      <div
        v-if="product.stock <= 5 && product.stock > 0"
        class="absolute top-4 right-4 bg-white/80 backdrop-blur-sm px-3 py-1 rounded-full font-body text-xs text-primary opacity-0 group-hover:opacity-100 transition-opacity"
      >
        Nog {{ product.stock }} beschikbaar
      </div>
      <div
        v-if="product.stock === 0"
        class="absolute top-4 right-4 bg-primary text-white px-3 py-1 rounded-full font-body text-xs"
      >
        Uitverkocht
      </div>
    </div>
    <div class="flex justify-between items-start">
      <div>
        <h3 class="font-display text-xl text-primary mb-1">{{ product.name }}</h3>
        <p class="font-body text-sm text-on-surface-variant">{{ product.category }}</p>
      </div>
      <span class="font-body font-semibold text-sm text-secondary">
        €{{ product.price.toFixed(2).replace('.', ',') }}
      </span>
    </div>
  </RouterLink>
</template>

<script setup lang="ts">
import type { Product } from '@/services/productService'

defineProps<{ product: Product }>()
</script>
