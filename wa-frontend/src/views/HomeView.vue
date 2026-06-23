<template>
  <main>
    <section class="relative h-[85vh] overflow-hidden flex items-center justify-center">
      <div class="absolute inset-0 bg-surface-container">
        <!-- Placeholder until real photo is here -->
        <div class="w-full h-full bg-gradient-to-br from-warm-sand to-outline-variant"></div>
      </div>
      <div class="absolute inset-0 bg-black/10"></div>

      <div class="relative z-10 text-center px-5 max-w-4xl mx-auto">
        <h1 class="font-display text-5xl md:text-7xl text-primary mb-8 leading-tight">
          Handgemaakte Keramiek
        </h1>
        <RouterLink
          to="/producten"
          class="inline-block bg-primary text-white px-10 py-4 text-sm font-semibold tracking-widest uppercase hover:bg-primary-container transition-all hover:scale-105 active:scale-95"
        >
          Bekijk Collectie
        </RouterLink>
      </div>

      <div
        class="absolute bottom-8 left-1/2 -translate-x-1/2 flex flex-col items-center gap-2 text-primary/60"
      >
        <span class="text-xs uppercase tracking-widest">Scroll</span>
        <div class="w-px h-12 bg-primary/20 relative overflow-hidden">
          <div class="absolute top-0 left-0 w-full h-1/2 bg-primary/40 animate-bounce"></div>
        </div>
      </div>
    </section>

    <section class="py-32 px-5 md:px-16 max-w-7xl mx-auto">
      <div class="flex flex-col md:flex-row justify-between items-baseline mb-16 gap-4">
        <div>
          <span class="text-xs text-secondary uppercase tracking-widest block mb-4">Onze Selectie</span>
          <h2 class="font-display text-3xl text-primary">Uitgelichte Stukken</h2>
        </div>
        <RouterLink
          to="/producten"
          class="text-sm font-semibold text-on-surface-variant hover:text-primary transition-colors border-b border-outline-variant pb-1"
        >
          Alles Ontdekken
        </RouterLink>
      </div>

      <div v-if="store.loading" class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div v-for="n in 2" :key="n" class="animate-pulse">
          <div class="aspect-[4/5] bg-surface-container mb-6"></div>
          <div class="h-6 bg-surface-container rounded w-1/2 mb-2"></div>
          <div class="h-4 bg-surface-container rounded w-1/3"></div>
        </div>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-12 gap-6">
        <div v-if="store.products[0]" class="md:col-span-5">
          <RouterLink :to="`/producten/${store.products[0].id}`" class="group cursor-pointer block">
            <div class="overflow-hidden aspect-[4/5] bg-surface-container-low mb-6">
              <div
                class="w-full h-full bg-gradient-to-br from-warm-sand to-outline-variant transition-transform duration-700 group-hover:scale-105"
              ></div>
            </div>
            <div class="flex justify-between items-start">
              <div>
                <h3 class="font-display text-2xl text-primary mb-1">
                  {{ store.products[0].name }}
                </h3>
                <p class="text-sm text-on-surface-variant">{{ store.products[0].category }}</p>
              </div>
              <span class="text-sm font-semibold text-secondary">
                €{{ store.products[0].price.toFixed(2).replace('.', ',') }}
              </span>
            </div>
          </RouterLink>
        </div>

        <div v-if="store.products[1]" class="md:col-span-6 md:col-start-7 md:mt-24">
          <RouterLink :to="`/producten/${store.products[1].id}`" class="group cursor-pointer block">
            <div class="overflow-hidden aspect-[4/5] bg-surface-container-low mb-6">
              <div
                class="w-full h-full bg-gradient-to-br from-outline-variant to-[#c4a99e] transition-transform duration-700 group-hover:scale-105"
              ></div>
            </div>
            <div class="flex justify-between items-start">
              <div>
                <h3 class="font-display text-2xl text-primary mb-1">
                  {{ store.products[1].name }}
                </h3>
                <p class="text-sm text-on-surface-variant">{{ store.products[1].category }}</p>
              </div>
              <span class="text-sm font-semibold text-secondary">
                €{{ store.products[1].price.toFixed(2).replace('.', ',') }}
              </span>
            </div>
          </RouterLink>
        </div>
      </div>
    </section>

    <section class="bg-surface-container-low py-32 px-5 md:px-16">
      <div class="max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-2 gap-24 items-center">
        <div>
          <span class="text-xs text-secondary uppercase tracking-widest block mb-6">Over Wendy</span>
          <h2 class="font-display text-4xl md:text-5xl text-primary mb-8 leading-tight">
            Over mij.
          </h2>
          <p class="text-lg text-on-surface-variant mb-8 max-w-lg leading-relaxed">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tincidunt mattis
            sagittis. In ac egestas mauris. Vestibulum in bibendum mauris. Suspendisse id pharetra
            mauris. Nam purus magna, dictum ut lacinia at, hendrerit quis est. Cras volutpat.
          </p>
          <a href="#" class="inline-flex items-center gap-4 text-primary text-sm font-semibold group">
            <span class="border-b border-primary py-1">Lees meer over mij</span>
            <span class="material-symbols-outlined transition-transform group-hover:translate-x-2">arrow_forward</span>
          </a>
        </div>

        <div
          class="aspect-square bg-gradient-to-br from-outline-variant to-[#c4a99e] rotate-3 hover:rotate-0 transition-transform duration-700"
        ></div>
      </div>
    </section>

    <footer class="bg-surface-container-low border-t border-outline-variant/30 mt-16">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 px-5 md:px-16 py-24 max-w-7xl mx-auto">
        <div>
          <h3 class="font-display text-2xl text-primary mb-8">Wendy Arthouse</h3>
          <p class="text-sm text-on-surface-variant mb-4">Atelier<br />Nederland</p>
        </div>
        <div>
          <h4 class="text-xs text-primary mb-8 uppercase tracking-widest font-semibold">
            Klantenservice
          </h4>
          <ul class="flex flex-col gap-4">
            <li>
              <a href="#" class="text-xs text-on-surface-variant hover:text-secondary underline transition-colors">Verzending</a>
            </li>
            <li>
              <a href="#" class="text-xs text-on-surface-variant hover:text-secondary underline transition-colors">Contact</a>
            </li>
            <li>
              <a href="#" class="text-xs text-on-surface-variant hover:text-secondary underline transition-colors">FAQ</a>
            </li>
            <li>
              <a href="#" class="text-xs text-on-surface-variant hover:text-secondary underline transition-colors">Retours</a>
            </li>
          </ul>
        </div>
      </div>
      <div class="px-5 md:px-16 py-4 border-t border-outline-variant/20 max-w-7xl mx-auto">
        <span class="text-xs text-on-surface-variant">© 2025 Wendy Arthouse. Handmade with love in the Netherlands.</span>
      </div>
    </footer>
  </main>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useProductStore } from '@/stores/productStore'

const store = useProductStore()

onMounted(() => {
  if (store.products.length === 0) {
    store.fetchProducts()
  }
})
</script>
