/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  darkMode: 'selector',
  theme: {
    extend: {
      screens: {
        'max-2xl': { max: '1536px' },
        'max-xl': { max: '1280px' },
        'max-lg': { max: '1024px' },
        'max-md': { max: '768px' },
        'max-sm': { max: '640px' },
        'max-xs': { max: '480px' }
      }
    }
  },
  plugins: []
}
