import App from './App.vue'
import { createApp } from 'vue'
// 引入 ElementPlus 样式
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
// 引入 tailwind.css
import '@/assets/style/tailwind.css'
import '@/assets/style/media.scss'
// 引入路由
import router from './router'
// 引入svg-icon
import 'virtual:svg-icons-register'
// 引入自定义指令
import vSlideIn from '@/directive/vSlideIn'
import vImgLazy from '@/directive/vImgLazy'

// 引入pinia
import { createPinia } from 'pinia'
// 引入pinia持久化插件
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)
app.use(pinia)
app.use(router)
app.directive('slide-in', vSlideIn)
app.directive('img-lazy', vImgLazy)
app.mount('#app')
