import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import VueAmazingUI from 'vue-amazing-ui'
import router from './router'

createApp(App).use(ElementPlus).use(VueAmazingUI).use(router).mount('#app')