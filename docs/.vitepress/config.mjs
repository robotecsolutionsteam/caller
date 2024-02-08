import { defineConfig } from 'vitepress'

export default defineConfig({
  title: "Caller",
  description: "Documentação Caller",
  themeConfig: {
    nav: [
      { text: 'Home', link: '/' },
      { text: 'Docs', link: '/instalacao' }
    ],

    sidebar: [
      {
        text: 'SDK Temi',
        items: [
          { text: 'Instalação', link: '/instalacao' },
          { text: 'Exemplos básicos', link: '/exemplos' }
        ]
      }
    ],
  }
})
