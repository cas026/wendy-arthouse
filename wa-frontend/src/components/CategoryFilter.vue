<template>
  <div class="flex flex-wrap gap-3 mb-12">
    <button
      v-for="cat in categories"
      :key="cat.label"
      @click="$emit('select', cat.value)"
      :class="[
        'px-5 py-2 rounded-full font-body text-sm transition-all duration-300',
        selected === cat.value
          ? 'bg-tertiary-fixed text-on-tertiary-fixed'
          : 'text-on-surface-variant hover:bg-warm-sand border border-outline-variant',
      ]"
    >
      {{ cat.label }}
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{ selected: string | null }>()
defineEmits<{ select: [value: string | null] }>()

const categories = [
  { label: 'Alles', value: null },
  { label: 'Schaaltjes', value: 'Schaaltjes' },
  { label: 'Bekers', value: 'Bekers' },
  { label: 'Kommen', value: 'Kommen' },
  { label: 'Borden', value: 'Borden' },
]
</script>
