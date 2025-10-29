import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../view/HomeView.vue'
import AboutView from '../view/AboutView.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'About',
    component: AboutView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  // console.log(to, from)
  next()
});

export default router