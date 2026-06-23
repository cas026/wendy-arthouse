export default {
  content: ['./index.html', './src/**/*.{vue,js,ts}'],
  theme: {
    extend: {
      colors: {
        primary: '#34150c',
        'primary-container': '#4d291f',
        'primary-fixed-dim': '#f2b9aa',
        secondary: '#8b4e3d',
        'secondary-container': '#fdad98',
        background: '#fbf9f2',
        surface: '#fbf9f2',
        'surface-container': '#efeee7',
        'surface-container-low': '#f5f4ed',
        'surface-dim': '#dbdad3',
        'on-surface': '#1b1c18',
        'on-surface-variant': '#514440',
        'on-primary': '#ffffff',
        outline: '#837470',
        'outline-variant': '#d5c2be',
        'warm-sand': '#EFEBE6',
        'muted-sage': '#73996B',
        'tertiary-fixed': '#cae9e5',
        'on-tertiary-fixed': '#02201e',
        error: '#ba1a1a',
      },
      fontFamily: {
        display: ['"Libre Caslon Text"', 'Georgia', 'serif'],
        body: ['"Hanken Grotesk"', 'sans-serif'],
      },
      spacing: {
        gutter: '24px',
        'margin-mobile': '20px',
        'margin-desktop': '64px',
        'section-gap': '120px',
      },
    },
  },
}
