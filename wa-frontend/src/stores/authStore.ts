import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api',
  withCredentials: true,
})

export const useAuthStore = defineStore('auth', () => {
  const username = ref<string | null>(localStorage.getItem('admin_username'))
  const isLoggedIn = computed(() => username.value !== null)

  async function login(user: string, password: string): Promise<void> {
    const { data } = await api.post<{ username: string }>('/auth/login', {
      username: user,
      password,
    })
    username.value = data.username
    localStorage.setItem('admin_username', data.username)
  }

  async function logout(): Promise<void> {
    await api.post('/auth/logout')
    username.value = null
    localStorage.removeItem('admin_username')
  }

  async function checkSession(): Promise<boolean> {
    try {
      await api.get('/auth/me')
      return true
    } catch {
      username.value = null
      localStorage.removeItem('admin_username')
      return false
    }
  }

  return { username, isLoggedIn, login, logout, checkSession }
})
