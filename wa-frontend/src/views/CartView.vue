<template>
  <main class="pt-32 pb-section-gap max-w-7xl mx-auto px-margin-mobile md:px-margin-desktop">
    <header class="mb-16">
      <h1 class="font-display text-5xl text-primary mb-2">Winkelwagen</h1>
      <p class="font-body text-on-surface-variant">
        {{ cartStore.totalItems }} {{ cartStore.totalItems === 1 ? 'artikel' : 'artikelen' }}
      </p>
    </header>

    <div v-if="cartStore.items.length === 0" class="text-center py-32">
      <span class="material-symbols-outlined text-on-surface-variant mb-6 block" style="font-size: 64px">
        shopping_bag
      </span>
      <h2 class="font-display text-2xl text-primary mb-4">Je winkelwagen is leeg</h2>
      <p class="font-body text-on-surface-variant mb-10">
        Ontdek onze handgemaakte keramiek collectie.
      </p>
      <RouterLink
        to="/producten"
        class="inline-block bg-primary text-white px-10 py-4 text-sm font-body font-semibold tracking-widest uppercase hover:bg-primary-container transition-colors"
      >
        Bekijk Collectie
      </RouterLink>
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-12 gap-16">
      <TransitionGroup
        name="cart-item"
        tag="div"
        class="lg:col-span-7 divide-y divide-outline-variant/30 relative"
      >
        <div v-for="item in cartStore.items" :key="item.product.id" class="flex gap-6 py-8">
          <RouterLink :to="`/producten/${item.product.id}`" class="shrink-0 group">
            <div class="w-24 h-24 md:w-32 md:h-32 bg-surface-container-low overflow-hidden">
              <img
                :src="item.product.imageUrl ?? 'https://picsum.photos/seed/' + item.product.id + '/200/200'"
                :alt="item.product.name"
                class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                width="128"
                height="128"
              />
            </div>
          </RouterLink>

          <div class="flex-1 flex flex-col justify-between">
            <div class="flex justify-between items-start gap-4">
              <div>
                <p class="font-body text-xs text-on-surface-variant uppercase tracking-widest mb-1">
                  {{ item.product.category }}
                </p>
                <h3 class="font-display text-xl text-primary">{{ item.product.name }}</h3>
              </div>
              <button
                @click="cartStore.removeItem(item.product.id)"
                class="text-on-surface-variant hover:text-error transition-colors shrink-0"
                :aria-label="`Verwijder ${item.product.name} uit winkelwagen`"
              >
                <span class="material-symbols-outlined text-sm">close</span>
              </button>
            </div>

            <div class="flex items-center justify-between mt-4">
              <div class="flex items-center border border-outline px-4 py-2 rounded-lg">
                <button
                  @click="cartStore.updateQuantity(item.product.id, item.quantity - 1)"
                  class="active:opacity-50"
                  :aria-label="`Verminder hoeveelheid ${item.product.name}`"
                >
                  <span class="material-symbols-outlined text-sm">remove</span>
                </button>
                <span class="w-8 text-center font-body text-sm">{{ item.quantity }}</span>
                <button
                  @click="cartStore.updateQuantity(item.product.id, item.quantity + 1)"
                  class="active:opacity-50"
                  :aria-label="`Verhoog hoeveelheid ${item.product.name}`"
                >
                  <span class="material-symbols-outlined text-sm">add</span>
                </button>
              </div>

              <span class="font-body font-semibold text-primary">
                €{{ (item.product.price * item.quantity).toFixed(2).replace('.', ',') }}
              </span>
            </div>
          </div>
        </div>
      </TransitionGroup>

      <aside class="lg:col-span-5 lg:sticky lg:top-32 self-start">
        <div class="bg-surface-container-low p-8">
          <h2 class="font-display text-2xl text-primary mb-8">Overzicht</h2>

          <ul class="space-y-3 mb-8">
            <li
              v-for="item in cartStore.items"
              :key="item.product.id"
              class="flex justify-between font-body text-sm text-on-surface-variant"
            >
              <span>{{ item.product.name }} × {{ item.quantity }}</span>
              <span>€{{ (item.product.price * item.quantity).toFixed(2).replace('.', ',') }}</span>
            </li>
          </ul>

          <div class="border-t border-outline-variant/30 pt-6 mb-8 space-y-3">
            <div class="flex justify-between font-body text-sm text-on-surface-variant">
              <span>Verzending</span>
              <span>Berekend bij checkout</span>
            </div>
            <div class="flex justify-between font-body font-semibold text-primary text-lg">
              <span>Subtotaal</span>
              <span>€{{ cartStore.totalPrice.toFixed(2).replace('.', ',') }}</span>
            </div>
          </div>

          <button
            class="w-full bg-primary text-white py-4 px-8 font-body text-sm uppercase tracking-widest hover:bg-primary-container transition-colors active:scale-[0.98]"
          >
            Doorgaan naar checkout
          </button>

          <RouterLink
            to="/producten"
            class="block text-center mt-4 font-body text-sm text-on-surface-variant hover:text-primary transition-colors"
          >
            Verder winkelen
          </RouterLink>
        </div>
      </aside>
    </div>
  </main>
</template>

<script setup lang="ts">
import { useCartStore } from '@/stores/cartStore'

const cartStore = useCartStore()
</script>
