import { createApp } from 'vue'
// import './style.css'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import Buttons from './buttons/Buttons.vue'
import App from './App.vue'
import ContainerLayout from './layout/ContainerLayout.vue'

// createApp(App).mount('#app')


createApp(ContainerLayout).use(ElementPlus).mount('#app')