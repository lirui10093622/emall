import { createApp } from 'vue'
// import './style.css'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import Buttons from './buttons/Buttons.vue'
import App from './App.vue'
import ContainerLayout from './layout/ContainerLayout.vue'
import Login from './login/Login.vue'
import DefaultMenu from './menu/DefaultMenu.vue'
import CustomMenu from './menu/CustomMenu.vue'

// createApp(App).mount('#app')


createApp(CustomMenu).use(ElementPlus).mount('#app')