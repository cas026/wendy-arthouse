<template>
  <main>
    <section class="pt-40 pb-24 px-5 md:px-16 max-w-7xl mx-auto">
      <div class="max-w-2xl">
        <span class="text-xs text-secondary uppercase tracking-widest block mb-6">Over de maker</span>
        <h1 class="font-display text-5xl md:text-6xl text-primary mb-10 leading-tight">Over mij.</h1>
        <p class="text-lg text-on-surface-variant leading-relaxed mb-6">
          Vul hier jouw verhaal in. Hoe ben je begonnen met keramiek? Wat inspireert je?
        </p>
        <p class="text-lg text-on-surface-variant leading-relaxed">
          Elk stuk dat ik maak is uniek — gevormd met de hand, gebakken op hoge temperatuur en voorzien van
          een handmatig aangebracht glazuur. Geen twee stukken zijn hetzelfde.
        </p>
      </div>
    </section>

    <section class="px-5 md:px-16 max-w-7xl mx-auto pb-32">
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-16 items-start">
        <div class="aspect-[3/4] overflow-hidden bg-surface-container-low">
          <img src="/images/about.jpg" alt="Wendy in het atelier" class="w-full h-full object-cover" />
        </div>
        <div class="lg:pt-24">
          <h2 class="font-display text-3xl text-primary mb-8">Het atelier</h2>
          <p class="text-on-surface-variant leading-relaxed mb-6">
            Vul hier informatie in over je werkplek, werkwijze of filosofie achter je keramiek.
          </p>
          <p class="text-on-surface-variant leading-relaxed mb-10">
            Ik werk vanuit mijn atelier in Nederland, waar ik met lokale kleien en eigen glazuurrecepten werk.
            Elke collectie ontstaat vanuit een gevoel, een seizoen of een materiaal dat me intrigeert.
          </p>
          <RouterLink
            to="/producten"
            class="inline-flex items-center gap-4 text-primary text-sm font-semibold group"
          >
            <span class="border-b border-primary py-1">Bekijk de collectie</span>
            <span class="material-symbols-outlined transition-transform group-hover:translate-x-2">arrow_forward</span>
          </RouterLink>
        </div>
      </div>
    </section>
  </main>
</template>
