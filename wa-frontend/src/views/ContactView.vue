<template>
  <main class="pt-40 pb-32 px-5 md:px-16 max-w-4xl mx-auto">
    <span class="text-xs text-secondary uppercase tracking-widest block mb-6">Bereikbaar</span>
    <h1 class="font-display text-5xl text-primary mb-16">Contact</h1>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-20">
      <div>
        <p class="text-on-surface-variant leading-relaxed mb-10">
          Heb je een vraag over een bestelling, wil je een aangepast stuk laten maken, of heb je andere vragen?
          Stuur me een bericht en ik neem zo snel mogelijk contact op.
        </p>

        <div v-if="sent" class="py-10">
          <p class="font-display text-2xl text-primary mb-3">Bericht verzonden!</p>
          <p class="font-body text-sm text-on-surface-variant">Bedankt voor je bericht. Ik neem zo snel mogelijk contact op.</p>
        </div>

        <form v-else @submit.prevent="handleSubmit" class="space-y-8" novalidate>
          <div>
            <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">Naam</label>
            <input
              v-model="form.name"
              type="text"
              maxlength="100"
              @blur="touched.name = true"
              placeholder="Jouw naam"
              class="w-full border-b bg-transparent py-2 font-body text-sm focus:outline-none transition-colors"
              :class="errors.name ? 'border-error' : 'border-outline focus:border-primary'"
            />
            <p v-if="errors.name" class="mt-1 font-body text-xs text-error">{{ errors.name }}</p>
          </div>

          <div>
            <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">E-mailadres</label>
            <input
              v-model="form.email"
              type="email"
              @blur="touched.email = true"
              placeholder="jouw@email.nl"
              class="w-full border-b bg-transparent py-2 font-body text-sm focus:outline-none transition-colors"
              :class="errors.email ? 'border-error' : 'border-outline focus:border-primary'"
            />
            <p v-if="errors.email" class="mt-1 font-body text-xs text-error">{{ errors.email }}</p>
          </div>

          <div>
            <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">Bericht</label>
            <textarea
              v-model="form.message"
              rows="5"
              maxlength="2000"
              @blur="touched.message = true"
              placeholder="Waar kan ik je mee helpen?"
              class="w-full border-b bg-transparent py-2 font-body text-sm focus:outline-none transition-colors resize-none"
              :class="errors.message ? 'border-error' : 'border-outline focus:border-primary'"
            ></textarea>
            <p v-if="errors.message" class="mt-1 font-body text-xs text-error">{{ errors.message }}</p>
          </div>

          <p v-if="serverError" class="font-body text-sm text-error">{{ serverError }}</p>

          <button
            type="submit"
            :disabled="sending"
            class="bg-primary text-white px-10 py-4 font-body text-sm uppercase tracking-widest hover:bg-primary-container transition-colors disabled:opacity-50"
          >
            {{ sending ? 'Verzenden…' : 'Verstuur' }}
          </button>
        </form>
      </div>

      <div class="lg:pt-2 space-y-10">
        <div>
          <h2 class="font-display text-xl text-primary mb-3">E-mail</h2>
          <a
            href="mailto:info@wendyarthouse.nl"
            class="text-on-surface-variant hover:text-primary transition-colors text-sm"
          >
            info@wendyarthouse.nl
          </a>
        </div>
        <div>
          <h2 class="font-display text-xl text-primary mb-3">Atelier</h2>
          <p class="text-on-surface-variant text-sm leading-relaxed">
            Nederland
          </p>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref, computed, watch } from 'vue'
import axios from 'axios'

const sent = ref(false)
const sending = ref(false)
const serverError = ref<string | null>(null)

const form = reactive({ name: '', email: '', message: '' })
const touched = reactive({ name: false, email: false, message: false })

const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

const errors = computed(() => ({
  name: touched.name
    ? form.name.trim().length < 2 ? 'Vul je naam in (minimaal 2 tekens).'
    : form.name.trim().length > 100 ? 'Naam mag maximaal 100 tekens bevatten.'
    : null
    : null,
  email: touched.email && !EMAIL_RE.test(form.email) ? 'Vul een geldig e-mailadres in.' : null,
  message: touched.message
    ? form.message.trim().length < 10 ? 'Je bericht is te kort (minimaal 10 tekens).'
    : form.message.trim().length > 2000 ? 'Bericht mag maximaal 2000 tekens bevatten.'
    : null
    : null,
}))

const isValid = computed(() =>
  form.name.trim().length >= 2 && form.name.trim().length <= 100 &&
  EMAIL_RE.test(form.email) &&
  form.message.trim().length >= 10 && form.message.trim().length <= 2000,
)

watch(() => form.name, () => { touched.name = true })
watch(() => form.email, () => { touched.email = true })
watch(() => form.message, () => { touched.message = true })

async function handleSubmit() {
  touched.name = true
  touched.email = true
  touched.message = true
  if (!isValid.value) return

  sending.value = true
  serverError.value = null
  try {
    await axios.post(
      `${import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api'}/contact`,
      { name: form.name, email: form.email, message: form.message },
    )
    sent.value = true
  } catch {
    serverError.value = 'Er ging iets mis. Probeer het opnieuw of stuur een e-mail.'
  } finally {
    sending.value = false
  }
}
</script>
