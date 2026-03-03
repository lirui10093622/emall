import { createRouter, createWebHistory } from 'vue-router'

const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../Login.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
})

// 添加调试代码，检查路由变化
router.beforeEach((to, from) => {
  console.log('URL 变化:', from.path, '->', to.path)
})

export default router