import { createRouter, createWebHistory } from 'vue-router'

const constantRoutes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/home/Home.vue'),
  },
  {
    path: '/search',
    name: 'SearchProduct',
    component: () => import('../views/search/SearchProduct.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/Login.vue'),
  },
  {
    path: '/item/:id',
    name: 'Item',
    component: () => import('../views/item/Item.vue'),
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/register/Register.vue'),
  },
  {
    path: '/order',
    name: 'OrderList',
    component: () => import('../views/order/OrderList.vue'),
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/cart/Cart.vue'),
  },
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