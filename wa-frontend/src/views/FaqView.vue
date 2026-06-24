<template>
  <main class="pt-40 pb-32 px-5 md:px-16 max-w-4xl mx-auto">
    <span class="text-xs text-secondary uppercase tracking-widest block mb-6">Veelgestelde vragen</span>
    <h1 class="font-display text-5xl text-primary mb-16">FAQ</h1>

    <div class="space-y-0 divide-y divide-outline-variant/40">
      <div v-for="(item, index) in faqs" :key="index" class="py-6">
        <button
          class="w-full flex justify-between items-start gap-8 text-left group"
          @click="toggle(index)"
        >
          <span class="font-display text-xl text-primary group-hover:text-secondary transition-colors">
            {{ item.question }}
          </span>
          <span class="material-symbols-outlined text-on-surface-variant shrink-0 transition-transform duration-300"
            :class="open === index ? 'rotate-45' : ''">
            add
          </span>
        </button>
        <Transition name="faq">
          <p v-if="open === index" class="mt-4 text-on-surface-variant leading-relaxed text-sm pr-12">
            {{ item.answer }}
          </p>
        </Transition>
      </div>
    </div>

    <div class="mt-20 border-t border-outline-variant/40 pt-10">
      <p class="text-on-surface-variant mb-6">Staat jouw vraag er niet bij?</p>
      <RouterLink
        to="/contact"
        class="inline-flex items-center gap-4 text-primary text-sm font-semibold group"
      >
        <span class="border-b border-primary py-1">Neem contact op</span>
        <span class="material-symbols-outlined transition-transform group-hover:translate-x-2">arrow_forward</span>
      </RouterLink>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const open = ref<number | null>(null)

function toggle(index: number) {
  open.value = open.value === index ? null : index
}

const faqs = [
  {
    question: 'Is elk stuk echt handgemaakt?',
    answer:
      'Ja, elk stuk wordt individueel met de hand gevormd, gebakken en geglazuurd in mijn atelier. Omdat het handwerk is, kunnen kleine variaties voorkomen in kleur, vorm of maat.',
  },
  {
    question: 'Hoe zorg ik goed voor mijn keramiek?',
    answer:
      'De meeste stukken zijn vaatwasserbestendig. Vermijd magnetron gebruik tenzij dit specifiek vermeld staat bij het product.',
  },
  {
    question: 'Kan ik een gepersonaliseerd stuk bestellen?',
    answer:
      'Dat kan zeker. Neem contact op via de contactpagina om de mogelijkheden te bespreken. Denk hierbij aan afwijkende maten, kleuren of een persoonlijke inscriptie. Houd rekening met een langere levertijd.',
  },
  {
    question: 'Welke betaalmethoden worden geaccepteerd?',
    answer:
      'Je kunt betalen met iDEAL | Wero en creditcard (Visa/Mastercard).',
  },
  {
    question: 'Hoe lang duurt verzending?',
    answer:
      'We verzenden alleen naar Nederland. Bestellingen worden binnen 2–3 werkdagen verwerkt en verzonden. Bezorging duurt daarna 1–2 werkdagen. Je ontvangt een e-mail zodra je bestelling onderweg is.',
  },
  {
    question: 'Wat als mijn bestelling beschadigd aankomt?',
    answer:
      'Stuur binnen 48 uur na ontvangst een e-mail met foto\'s van de schade naar info@wendyarthouse.nl. Ik zorg dan voor een passende oplossing, zoals een vervangend stuk of terugbetaling.',
  },
  {
    question: 'Kan ik mijn bestelling annuleren of ruilen?',
    answer:
      'Annuleren is mogelijk zolang je bestelling nog niet verzonden is. Neem zo snel mogelijk contact op. Ruilen is helaas niet mogelijk voor handgemaakte stukken, tenzij er sprake is van een defect.',
  },
]
</script>

<style scoped>
.faq-enter-active,
.faq-leave-active { transition: opacity 0.2s ease, transform 0.2s ease; }
.faq-enter-from,
.faq-leave-to    { opacity: 0; transform: translateY(-6px); }
</style>
