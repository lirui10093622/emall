import { createApp } from 'vue'
// import './style.css'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import App from './App.vue'
import ContainerLayout from './layout/ContainerLayout.vue'
import DefaultMenu from './menu/DefaultMenu.vue'
import CustomMenu from './menu/CustomMenu.vue'
import RouterTest from './router/RouterTest.vue'

import VueAmazingUI from 'vue-amazing-ui'
// import 'vue-amazing-ui/css'


import { createMemoryHistory, createRouter } from 'vue-router'

import router from './router'

// createApp(App).mount('#app')


createApp(ContainerLayout).use(ElementPlus).use(VueAmazingUI).use(router).mount('#app')