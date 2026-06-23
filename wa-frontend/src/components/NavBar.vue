<template>
  <header
    class="fixed top-0 left-0 right-0 z-50 bg-[#fbf9f2]/90 backdrop-blur-md border-b border-[#d5c2be]/30"
  >
    <div class="flex justify-between items-center w-full px-5 md:px-16 py-2 max-w-7xl mx-auto h-20">
      <nav class="flex items-center gap-8">
        <button aria-label="Zoeken" class="text-[#34150c] active:opacity-70">
          <span class="material-symbols-outlined">search</span>
        </button>
        <div class="hidden md:flex gap-8">
          <RouterLink
            to="/producten"
            class="text-sm font-semibold tracking-wide text-[#34150c] border-b border-[#34150c] hover:text-[#8b4e3d] transition-colors"
          >
            Shop
          </RouterLink>
          <RouterLink
            to="/over"
            class="text-sm font-semibold tracking-wide text-[#514440] hover:text-[#8b4e3d] transition-colors"
          >
            Over
          </RouterLink>
        </div>
      </nav>

      <RouterLink
        to="/"
        class="text-2xl text-[#34150c] tracking-tight font-bold absolute left-1/2 -translate-x-1/2"
        style="font-family: 'Libre Caslon Text', Georgia, serif"
      >
        Wendy Arthouse
      </RouterLink>

      <div class="flex items-center gap-6">
        <RouterLink to="/winkelwagen" class="relative text-[#34150c] active:opacity-70">
          <span class="material-symbols-outlined">shopping_bag</span>
          <span
            class="absolute -top-1 -right-1 bg-[#8b4e3d] text-white text-[8px] w-4 h-4 flex items-center justify-center rounded-full"
          >
            0
          </span>
        </RouterLink>
      </div>
    </div>
  </header>
</template>
