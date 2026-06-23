<template>
  <main class="min-h-screen flex items-center justify-center bg-surface px-margin-mobile">
    <div class="w-full max-w-sm">
      <div class="text-center mb-10">
        <p class="font-body text-xs text-on-surface-variant uppercase tracking-widest mb-2">Intern</p>
        <h1 class="font-display text-4xl text-primary">Admin</h1>
      </div>

      <form @submit.prevent="submit" class="space-y-6">
        <div>
          <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">
            Gebruikersnaam
          </label>
          <input
            v-model="form.username"
            type="text"
            required
            autocomplete="username"
            class="w-full border-b border-outline bg-transparent py-2 font-body text-sm focus:outline-none focus:border-primary transition-colors"
          />
        </div>

        <div>
          <label class="block font-body text-xs uppercase tracking-widest text-on-surface-variant mb-2">
            Wachtwoord
          </label>
          <input
            v-model="form.password"
            type="password"
            required
            autocomplete="current-password"
            class="w-full border-b border-outline bg-transparent py-2 font-body text-sm focus:outline-none focus:border-primary transition-colors"
          />
        </div>

        <p v-if="error" class="font-body text-sm text-error">{{ error }}</p>

        <button
          type="submit"
          :disabled="loading"
          class="w-full bg-primary text-white py-4 font-body text-sm uppercase tracking-widest hover:bg-primary-container transition-colors disabled:opacity-50"
        >
          {{ loading ? 'Inloggen…' : 'Inloggen' }}
        </button>
      </form>
    </div>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({ username: '', password: '' })
const loading = ref(false)
const error = ref<string | null>(null)

async function submit() {
  loading.value = true
  error.value = null
  try {
    await authStore.login(form.username, form.password)
    router.replace('/admin')
  } catch {
    error.value = 'Ongeldige gebruikersnaam of wachtwoord.'
  } finally {
    loading.value = false
  }
}
</script>
