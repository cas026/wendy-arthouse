<template>
  <main class="pt-40 pb-32 px-5 md:px-16 max-w-4xl mx-auto">
    <span class="text-xs text-secondary uppercase tracking-widest block mb-6">Klantenservice</span>
    <h1 class="font-display text-5xl text-primary mb-16">Verzending</h1>

    <div class="space-y-12">
      <div class="border-t border-outline-variant/40 pt-10">
        <h2 class="font-display text-2xl text-primary mb-4">Levertijd</h2>
        <p class="text-on-surface-variant leading-relaxed mb-3">
          Bestellingen worden binnen <strong class="text-on-surface">2–3 werkdagen</strong> verwerkt en verzonden.
          Omdat elk stuk met de hand is gemaakt, kan de levertijd bij grote vraag iets langer zijn.
          Je ontvangt altijd een e-mail zodra je bestelling onderweg is.
        </p>
        <p class="text-on-surface-variant leading-relaxed">
          Bezorging in Nederland duurt doorgaans <strong class="text-on-surface">1–2 werkdagen</strong> na verzending.
        </p>
      </div>

      <div class="border-t border-outline-variant/40 pt-10">
        <h2 class="font-display text-2xl text-primary mb-6">Verzendkosten</h2>
        <table class="w-full font-body text-sm">
          <thead>
            <tr class="border-b border-outline-variant/40 text-left">
              <th class="pb-3 pr-8 font-semibold text-on-surface-variant uppercase tracking-widest text-xs">Bestemming</th>
              <th class="pb-3 font-semibold text-on-surface-variant uppercase tracking-widest text-xs">Kosten</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-outline-variant/20">
            <tr>
              <td class="py-4 pr-8 text-on-surface">Nederland</td>
              <td class="py-4 text-on-surface">€4,95</td>
            </tr>
          </tbody>
        </table>
        <p class="mt-6 text-sm text-secondary font-semibold">Gratis verzending in Nederland bij bestellingen vanaf €75.</p>
      </div>

      <div class="border-t border-outline-variant/40 pt-10">
        <h2 class="font-display text-2xl text-primary mb-4">Verpakking</h2>
        <p class="text-on-surface-variant leading-relaxed">
          Alle stukken worden zorgvuldig verpakt met gerecycled materiaal zodat ze veilig aankomen.
          Keramiek is breekbaar — we nemen geen risico's met de verpakking.
        </p>
      </div>

      <div class="border-t border-outline-variant/40 pt-10">
        <h2 class="font-display text-2xl text-primary mb-4">Vragen?</h2>
        <p class="text-on-surface-variant leading-relaxed mb-6">
          Heb je een vraag over je bestelling? Neem gerust contact op.
        </p>
        <RouterLink
          to="/contact"
          class="inline-flex items-center gap-4 text-primary text-sm font-semibold group"
        >
          <span class="border-b border-primary py-1">Stuur een bericht</span>
          <span class="material-symbols-outlined transition-transform group-hover:translate-x-2">arrow_forward</span>
        </RouterLink>
      </div>
    </div>
  </main>
</template>
