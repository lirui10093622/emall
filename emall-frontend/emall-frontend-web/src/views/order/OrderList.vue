<template>
  <div class="order-page" @click="closeDropdowns">
    <!-- 顶部导航 -->
    <header class="order-header">
      <div class="nav-inner header-inner">
        <div class="logo" @click="goHome"><span class="logo-icon">🛒</span><span class="logo-text">E-Mall</span></div>
        <div class="header-center">
          <span class="header-nav-item active" @click="goHome">首页</span>
          <div class="header-nav-item has-dropdown" @click.stop="toggleMyEmall">
            我的E-Mall <span class="nav-arrow">▾</span>
            <div v-if="myEmallVisible" class="nav-dropdown">
              <div class="nav-dd-item" @click="router.push('/order')">我的订单</div>
              <div class="nav-dd-item">个人资料</div>
              <div class="nav-dd-item">收货地址</div>
              <div class="nav-dd-item">优惠券</div>
            </div>
          </div>
          <span class="header-nav-item">企业采购 ▾</span>
          <span class="header-nav-item">客户服务 ▾</span>
          <span class="header-nav-item">网站导购 ▾</span>
        </div>
        <div class="header-right">
          <div class="search-mini">
            <input v-model="headerSearch" class="search-mini-input" placeholder="搜索商品" @keyup.enter="handleHeaderSearch" />
            <button class="search-mini-btn" @click="handleHeaderSearch">搜索</button>
          </div>
          <div class="cart-entry" @click="router.push('/')">
            <span>🛒</span>
            <el-badge :value="3" :max="99"><span class="cart-text">购物车</span></el-badge>
          </div>
        </div>
      </div>
    </header>

    <!-- 面包屑 -->
    <div class="breadcrumb-bar">
      <div class="nav-inner">
        <span class="bc-link" @click="goHome">首页</span>
        <span class="bc-sep">›</span>
        <span class="bc-current">我的订单</span>
      </div>
    </div>

    <!-- 主体布局 -->
    <div class="main-layout nav-inner">
      <!-- 左侧导航 -->
      <aside class="left-sidebar">
        <div class="user-card">
          <div class="user-avatar">👤</div>
          <div class="user-name">E-Mall用户</div>
          <div class="user-level">
            <span class="level-tag">💎 PLUS会员</span>
          </div>
        </div>

        <div v-for="group in sideNavGroups" :key="group.title" class="nav-group">
          <div class="nav-group-title">{{ group.title }}</div>
          <div
            v-for="item in group.items"
            :key="item.label"
            class="nav-item"
            :class="{ active: activeSideNav === item.key }"
            @click="activeSideNav = item.key"
          >
            <span class="nav-item-icon">{{ item.icon }}</span>
            <span class="nav-item-label">{{ item.label }}</span>
            <span v-if="item.badge" class="nav-badge">{{ item.badge }}</span>
          </div>
        </div>
      </aside>

      <!-- 右侧内容 -->
      <div class="order-content">
        <div class="content-title">我的订单</div>

        <!-- Tab 栏 -->
        <div class="order-tabs">
          <span
            v-for="tab in orderTabs"
            :key="tab.value"
            class="order-tab"
            :class="{ active: activeTab === tab.value }"
            @click="switchTab(tab.value)"
          >
            {{ tab.label }}
            <span v-if="tab.count" class="tab-count">{{ tab.count }}</span>
          </span>
          <div class="tab-search">
            <input v-model="orderSearch" class="order-search-input" placeholder="搜索商品名称/订单号" @keyup.enter="doSearch" />
            <button class="order-search-btn" @click="doSearch">搜索</button>
            <button class="order-dl-btn" @click="ElMessage.info('下载功能开发中')">下载</button>
          </div>
        </div>

        <!-- 表格头 -->
        <div class="order-table-head">
          <div class="col-product">
            <select v-model="selectedYear" class="year-select">
              <option v-for="y in years" :key="y" :value="y">{{ y }}年订单</option>
            </select>
            &nbsp;&nbsp;订单商品
          </div>
          <div class="col-buyer">收货人</div>
          <div class="col-amount">金额</div>
          <div class="col-status">订单状态</div>
          <div class="col-actions">操作</div>
        </div>

        <!-- 订单列表 -->
        <div v-if="pagedOrders.length === 0" class="empty-orders">
          <div class="empty-icon">📦</div>
          <div class="empty-tip">暂无订单记录</div>
        </div>

        <div v-for="order in pagedOrders" :key="order.id" class="order-group">
          <!-- 订单头 -->
          <div class="order-head">
            <span class="order-date">{{ order.date }}</span>
            <span class="order-no">订单号：<em>{{ order.no }}</em></span>
            <span v-if="order.tag" class="order-tag" :class="order.tagClass">{{ order.tag }}</span>
            <span class="order-store">{{ order.store }}</span>
            <span class="order-delete" @click="deleteOrder(order.id)" title="删除订单">🗑</span>
          </div>

          <!-- 订单行 -->
          <div class="order-row">
            <!-- 商品列 -->
            <div class="col-product order-products">
              <div v-for="item in order.items" :key="item.id" class="order-item" @click="goItem(item.id)">
                <div class="item-img">{{ item.emoji }}</div>
                <div class="item-info">
                  <div class="item-name">{{ item.name }}</div>
                  <div class="item-spec">{{ item.spec }}</div>
                  <div class="item-tags">
                    <span v-for="t in item.tags" :key="t" class="item-tag">{{ t }}</span>
                  </div>
                </div>
                <div class="item-price-qty">
                  <span class="item-price">¥{{ item.price }}</span>
                  <span class="item-qty">× {{ item.qty }}</span>
                </div>
              </div>
            </div>

            <!-- 收货人 -->
            <div class="col-buyer order-cell">
              <div class="buyer-name">{{ order.buyer }}</div>
              <div class="buyer-via">{{ order.via }}</div>
            </div>

            <!-- 金额 -->
            <div class="col-amount order-cell">
              <div class="amount-total">¥{{ order.total }}</div>
              <div class="amount-note">含运费 ¥{{ order.freight }}</div>
            </div>

            <!-- 状态 -->
            <div class="col-status order-cell">
              <div class="status-badge" :class="order.statusClass">{{ order.status }}</div>
              <div v-if="order.statusNote" class="status-note">{{ order.statusNote }}</div>
            </div>

            <!-- 操作 -->
            <div class="col-actions order-cell">
              <button
                v-for="action in order.actions"
                :key="action.label"
                class="order-action-btn"
                :class="action.type"
                @click.stop="handleAction(action, order)"
              >{{ action.label }}</button>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div v-if="filteredOrders.length > 0" class="pagination-wrap">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="filteredOrders.length"
            layout="prev, pager, next, jumper"
            background
            @current-change="p => { currentPage = p; scrollTop() }"
          />
        </div>
      </div>
    </div>

    <!-- Footer -->
    <footer class="order-footer">
      <div class="footer-promises nav-inner">
        <div v-for="p in promises" :key="p.title" class="promise-item">
          <span class="promise-icon">{{ p.icon }}</span>
          <div>
            <div class="promise-title">{{ p.title }}</div>
            <div class="promise-desc">{{ p.desc }}</div>
          </div>
        </div>
      </div>
      <div class="footer-links-wrap nav-inner">
        <div v-for="col in footerCols" :key="col.title" class="footer-col">
          <div class="footer-col-title">{{ col.title }}</div>
          <div v-for="link in col.links" :key="link" class="footer-col-link">{{ link }}</div>
        </div>
      </div>
      <div class="footer-bottom nav-inner">
        <span>© 2004-2026 E-Mall.com 版权所有</span>
      </div>
    </footer>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const myEmallVisible = ref(false)
const headerSearch = ref('')
const orderSearch = ref('')
const activeTab = ref('all')
const activeSideNav = ref('my-order')
const selectedYear = ref(2025)
const currentPage = ref(1)
const pageSize = 5
const years = [2025, 2024, 2023, 2022]

const toggleMyEmall = () => { myEmallVisible.value = !myEmallVisible.value }
const closeDropdowns = () => { myEmallVisible.value = false }
const goHome = () => router.push('/')
const goItem = (id: number) => router.push(`/item/${id}`)
const handleHeaderSearch = () => { if (headerSearch.value.trim()) router.push({ path: '/search', query: { keyword: headerSearch.value } }) }
const doSearch = () => { currentPage.value = 1 }
const scrollTop = () => window.scrollTo({ top: 0, behavior: 'smooth' })

// 左侧导航
const sideNavGroups = [
  {
    title: '订单中心',
    items: [
      { key: 'my-order', icon: '📋', label: '我的订单' },
      { key: 'pending-review', icon: '✍️', label: '待评价', badge: '2' },
      { key: 'after-sale', icon: '🔄', label: '退换/售后' },
    ],
  },
  {
    title: '购买中心',
    items: [
      { key: 'seckill', icon: '⚡', label: '秒杀订单' },
      { key: 'presale', icon: '🏷️', label: '预售订单' },
      { key: 'group', icon: '👥', label: '拼购订单' },
      { key: 'self', icon: '🏪', label: '自营电器' },
    ],
  },
  {
    title: '客户中心',
    items: [
      { key: 'coupon', icon: '🎫', label: '优惠券', badge: '1' },
      { key: 'gift-card', icon: '🎁', label: '礼品卡' },
      { key: 'points', icon: '🪙', label: '积分' },
    ],
  },
  {
    title: '发票业务',
    items: [
      { key: 'invoice', icon: '🧾', label: '企业购发票' },
      { key: 'e-invoice', icon: '📄', label: '电子发票' },
    ],
  },
  {
    title: '关于',
    items: [
      { key: 'profile', icon: '👤', label: '个人资料' },
      { key: 'address', icon: '📍', label: '地址管理' },
    ],
  },
]

// Tab
const orderTabs = [
  { value: 'all', label: '全部订单', count: 0 },
  { value: 'unpaid', label: '待付款', count: 1 },
  { value: 'undelivered', label: '待收货/使用', count: 2 },
  { value: 'unreview', label: '待评价', count: 2 },
  { value: 'done', label: '已完成', count: 0 },
  { value: 'complaint', label: '投诉举报', count: 0 },
]

// 订单数据
interface OrderItem { id: number; emoji: string; name: string; spec: string; tags: string[]; price: string; qty: number }
interface Order {
  id: number; no: string; date: string; store: string; tag?: string; tagClass?: string
  items: OrderItem[]; buyer: string; via: string; total: string; freight: string
  status: string; statusClass: string; statusNote?: string
  actions: Array<{ label: string; type: string; action: string }>
  tabKey: string; year: number
}

const allOrders = ref<Order[]>([
  {
    id: 1, no: '311340897534', date: '2025-04-04 17:27:41', store: 'E-Mall自营', tag: '公益♥', tagClass: 'tag-charity',
    items: [
      { id: 1, emoji: '📱', name: 'Apple/苹果 iPhone 16 (A3288) 256GB 黑色 支持移动联通电信5G 双卡双待手机', spec: '黑色 256GB', tags: ['自营', '极速达'], price: '5499.00', qty: 1 },
    ],
    buyer: '张**', via: '手机下单', total: '5499.00', freight: '0.00',
    status: '已发货', statusClass: 'status-shipped', statusNote: '已发货，请注意查收',
    actions: [{ label: '确认收货', type: 'btn-primary', action: 'confirm' }, { label: '查看物流', type: 'btn-default', action: 'logistics' }, { label: '申请售后', type: 'btn-default', action: 'after-sale' }],
    tabKey: 'undelivered', year: 2025,
  },
  {
    id: 2, no: '312639116135', date: '2025-04-04 17:20:40', store: 'E-Mall自营', tag: '公益♥', tagClass: 'tag-charity',
    items: [
      { id: 1, emoji: '📱', name: 'Apple/苹果 iPhone 16 (A3288) 256GB 黑色 支持移动联通电信5G 双卡双待手机', spec: '黑色 256GB', tags: ['自营'], price: '5499.00', qty: 1 },
      { id: 2, emoji: '🎁', name: '第二件八折活动商品（双11限时特惠奖励红包）', spec: '活动商品', tags: ['促销'], price: '0.00', qty: 1 },
      { id: 3, emoji: '🏆', name: '第三件九折活动奖品-E-Mall VIP体验礼包', spec: '礼包', tags: ['赠品'], price: '0.00', qty: 1 },
    ],
    buyer: '李**', via: '手机下单', total: '5499.00', freight: '0.00',
    status: '已完成', statusClass: 'status-done', statusNote: undefined,
    actions: [{ label: '再次购买', type: 'btn-primary', action: 'rebuy' }, { label: '立即评价', type: 'btn-default', action: 'review' }, { label: '申请售后', type: 'btn-default', action: 'after-sale' }],
    tabKey: 'done', year: 2025,
  },
  {
    id: 3, no: '317253587/1422', date: '2025-04-04 17:29:23', store: 'E-Mall官方', tag: '免税', tagClass: 'tag-free',
    items: [
      { id: 1, emoji: '📱', name: 'Apple/苹果 iPhone 16 (A3288) 256GB 黑色手机 支持5G 大屏智能手机', spec: '黑色 256GB', tags: ['自营', '电器城'], price: '5205.01', qty: 1 },
    ],
    buyer: '王**', via: '小程序', total: '5205.01', freight: '0.00',
    status: '已取消', statusClass: 'status-cancelled', statusNote: '订单已取消',
    actions: [{ label: '再次购买', type: 'btn-primary', action: 'rebuy' }, { label: '删除订单', type: 'btn-danger', action: 'delete' }],
    tabKey: 'done', year: 2025,
  },
  {
    id: 4, no: '315138627/1967', date: '2025-06-04 16:51:33', store: 'E-Mall自营',
    items: [
      { id: 1, emoji: '📱', name: 'Apple/苹果 iPhone 16 (A3288) 256GB 黑色 支持移动联通电信5G 双卡双待手机', spec: '黑色 256GB', tags: ['自营'], price: '5000.00', qty: 1 },
    ],
    buyer: '赵**', via: '手机下单', total: '5000.00', freight: '0.00',
    status: '待付款', statusClass: 'status-unpaid', statusNote: '请在 23:45:12 内完成付款',
    actions: [{ label: '立即付款', type: 'btn-primary', action: 'pay' }, { label: '取消订单', type: 'btn-danger', action: 'cancel' }],
    tabKey: 'unpaid', year: 2025,
  },
  {
    id: 5, no: '318892047563', date: '2025-03-15 09:12:05', store: 'E-Mall自营',
    items: [
      { id: 1, emoji: '💻', name: '联想 ThinkPad X1 Carbon 2024款 轻薄笔记本电脑 i7-1355U 16G 512G', spec: '经典黑', tags: ['自营', '7天退换'], price: '8999.00', qty: 1 },
    ],
    buyer: '孙**', via: '电脑下单', total: '8999.00', freight: '0.00',
    status: '已完成', statusClass: 'status-done',
    actions: [{ label: '再次购买', type: 'btn-primary', action: 'rebuy' }, { label: '立即评价', type: 'btn-default', action: 'review' }],
    tabKey: 'unreview', year: 2025,
  },
  {
    id: 6, no: '319204867231', date: '2025-03-10 14:30:00', store: '华为官方旗舰店',
    items: [
      { id: 1, emoji: '📲', name: '华为 Mate 70 Pro+ 手机 512GB 翡冷翠 卫星通话 超强夜景拍摄', spec: '翡冷翠 512GB', tags: ['品牌直营'], price: '9999.00', qty: 1 },
      { id: 2, emoji: '🎧', name: '华为 FreeBuds Pro 3 无线蓝牙耳机 原装正品', spec: '冰霜银', tags: ['赠品'], price: '1299.00', qty: 1 },
    ],
    buyer: '周**', via: '手机下单', total: '9999.00', freight: '0.00',
    status: '待评价', statusClass: 'status-review',
    actions: [{ label: '立即评价', type: 'btn-primary', action: 'review' }, { label: '再次购买', type: 'btn-default', action: 'rebuy' }, { label: '申请售后', type: 'btn-default', action: 'after-sale' }],
    tabKey: 'unreview', year: 2025,
  },
  {
    id: 7, no: '320118923445', date: '2024-12-25 10:00:00', store: 'E-Mall自营',
    items: [
      { id: 1, emoji: '🎮', name: '索尼 PlayStation 5 Pro 游戏主机 1TB 国行正品 含手柄', spec: '标准版', tags: ['自营', '现货'], price: '4999.00', qty: 1 },
    ],
    buyer: '吴**', via: '电脑下单', total: '4999.00', freight: '0.00',
    status: '已完成', statusClass: 'status-done',
    actions: [{ label: '再次购买', type: 'btn-primary', action: 'rebuy' }, { label: '申请售后', type: 'btn-default', action: 'after-sale' }],
    tabKey: 'done', year: 2024,
  },
  {
    id: 8, no: '321003478912', date: '2024-11-11 00:00:30', store: 'Nike官方旗舰店',
    items: [
      { id: 1, emoji: '👟', name: 'Nike Air Max 270 男款运动鞋 气垫缓震 黑色 42码', spec: '黑色/42', tags: ['品牌直营'], price: '649.00', qty: 2 },
    ],
    buyer: '郑**', via: '手机下单', total: '1298.00', freight: '0.00',
    status: '已完成', statusClass: 'status-done',
    actions: [{ label: '再次购买', type: 'btn-primary', action: 'rebuy' }, { label: '立即评价', type: 'btn-default', action: 'review' }],
    tabKey: 'done', year: 2024,
  },
])

// 过滤 + 搜索
const filteredOrders = computed(() => {
  let list = allOrders.value.filter(o => o.year === selectedYear.value)
  if (activeTab.value !== 'all') list = list.filter(o => o.tabKey === activeTab.value)
  if (orderSearch.value.trim()) {
    const kw = orderSearch.value.trim().toLowerCase()
    list = list.filter(o => o.no.includes(kw) || o.items.some(i => i.name.toLowerCase().includes(kw)))
  }
  return list
})

const pagedOrders = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredOrders.value.slice(start, start + pageSize)
})

const switchTab = (tab: string) => { activeTab.value = tab; currentPage.value = 1 }

const deleteOrder = async (id: number) => {
  await ElMessageBox.confirm('确认删除该订单记录？删除后不可恢复。', '删除订单', {
    confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning',
  })
  const idx = allOrders.value.findIndex(o => o.id === id)
  if (idx !== -1) { allOrders.value.splice(idx, 1); ElMessage.success('订单已删除') }
}

const handleAction = (action: { label: string; type: string; action: string }, order: Order) => {
  const map: Record<string, string> = {
    confirm: '确认收货成功',
    pay: '跳转支付页面...',
    cancel: '订单已取消',
    review: '跳转评价页面...',
    rebuy: '已加入购物车',
    logistics: '查看物流信息...',
    'after-sale': '跳转售后申请...',
    delete: '',
  }
  if (action.action === 'delete') { deleteOrder(order.id); return }
  if (action.action === 'confirm') {
    const idx = allOrders.value.findIndex(o => o.id === order.id)
    if (idx !== -1) { allOrders.value[idx].status = '已完成'; allOrders.value[idx].statusClass = 'status-done' }
  }
  ElMessage.success(map[action.action] || action.label)
}

// 底部
const promises = [
  { icon: '🛒', title: '品类齐全，轻松购物', desc: '' },
  { icon: '🚚', title: '多仓直发，极速配送', desc: '' },
  { icon: '✅', title: '正品行货，精致服务', desc: '' },
  { icon: '💰', title: '天天低价，畅选无忧', desc: '' },
]

const footerCols = [
  { title: '购物指南', links: ['购物流程', '会员介绍', '生活旅行/团购', '常见问题', '大家问', '联系客服'] },
  { title: '配送方式', links: ['上门自提', '211限时达', '配送服务查询', '配送费收取标准', '海外配送'] },
  { title: '支付方式', links: ['货到付款', '在线支付', '分期付款', '邮局汇款', '公司转账'] },
  { title: '售后服务', links: ['售后政策', '价格保护', '退款说明', '返修/退换货', '取消订单'] },
  { title: '特色服务', links: ['E-Mall会员', 'E-Mall PLUS', '企业采购', '广告服务', 'Sale联盟'] },
]
</script>

<style scoped>
* { box-sizing: border-box; }

.order-page {
  background: #f4f4f4;
  min-height: 100vh;
  font-family: 'Microsoft YaHei', sans-serif;
}

.nav-inner { max-width: 1200px; margin: 0 auto; padding: 0 16px; }

/* ===== Header ===== */
.order-header {
  background: #e1251b;
  padding: 8px 0;
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-inner { display: flex; align-items: center; gap: 20px; }
.logo { display: flex; align-items: center; gap: 6px; cursor: pointer; flex-shrink: 0; }
.logo-icon { font-size: 22px; filter: brightness(10); }
.logo-text { font-size: 22px; font-weight: 900; color: white; letter-spacing: -1px; }

.header-center { display: flex; align-items: center; gap: 4px; flex: 1; }
.header-nav-item {
  font-size: 13px; color: rgba(255,255,255,0.85); padding: 5px 10px;
  cursor: pointer; white-space: nowrap; border-radius: 2px; position: relative;
}
.header-nav-item:hover, .header-nav-item.active { color: white; background: rgba(255,255,255,0.15); }
.header-nav-item.has-dropdown { user-select: none; }
.nav-arrow { font-size: 10px; }
.nav-dropdown {
  position: absolute; top: calc(100% + 4px); left: 0; min-width: 120px;
  background: white; border: 1px solid #e0e0e0; border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.12); z-index: 200; overflow: hidden;
}
.nav-dd-item { padding: 9px 14px; font-size: 13px; color: #333; cursor: pointer; }
.nav-dd-item:hover { background: #fff0f0; color: #e1251b; }

.header-right { display: flex; align-items: center; gap: 12px; flex-shrink: 0; }
.search-mini { display: flex; height: 28px; border: 1px solid rgba(255,255,255,0.5); border-radius: 2px; overflow: hidden; }
.search-mini-input { width: 160px; border: none; outline: none; padding: 0 10px; font-size: 13px; background: rgba(255,255,255,0.9); }
.search-mini-btn { border: none; background: rgba(255,255,255,0.2); color: white; padding: 0 12px; cursor: pointer; font-size: 13px; }
.search-mini-btn:hover { background: rgba(255,255,255,0.3); }
.cart-entry { display: flex; align-items: center; gap: 5px; color: white; cursor: pointer; font-size: 13px; padding: 4px 8px; border-radius: 2px; }
.cart-entry:hover { background: rgba(255,255,255,0.15); }
.cart-text { color: white; }
.cart-entry :deep(.el-badge__content) { background: white; color: #e1251b; font-weight: bold; }

/* 面包屑 */
.breadcrumb-bar { background: white; border-bottom: 1px solid #f0f0f0; padding: 8px 0; font-size: 12px; }
.bc-link { color: #666; cursor: pointer; }
.bc-link:hover { color: #e1251b; }
.bc-sep { color: #ccc; margin: 0 4px; }
.bc-current { color: #e1251b; }

/* ===== 主体布局 ===== */
.main-layout {
  display: flex;
  gap: 14px;
  padding-top: 14px;
  padding-bottom: 24px;
  align-items: flex-start;
}

/* ===== 左侧导航 ===== */
.left-sidebar {
  width: 160px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-card {
  background: white;
  border-radius: 4px;
  padding: 16px;
  text-align: center;
  margin-bottom: 4px;
}
.user-avatar { font-size: 36px; margin-bottom: 6px; }
.user-name { font-size: 14px; color: #333; font-weight: 500; margin-bottom: 4px; }
.level-tag { font-size: 11px; background: linear-gradient(135deg, #9b59b6, #6c3483); color: white; padding: 2px 10px; border-radius: 10px; }

.nav-group { background: white; border-radius: 4px; overflow: hidden; }
.nav-group-title {
  font-size: 12px; color: #999; padding: 8px 14px 4px;
  border-bottom: 1px solid #f5f5f5; font-weight: 500;
}
.nav-item {
  display: flex; align-items: center; gap: 6px;
  padding: 8px 14px; font-size: 13px; color: #555;
  cursor: pointer; transition: all 0.15s; position: relative;
}
.nav-item:hover { background: #fff5f5; color: #e1251b; }
.nav-item.active { background: #fff0f0; color: #e1251b; font-weight: 500; border-left: 3px solid #e1251b; }
.nav-item-icon { font-size: 14px; flex-shrink: 0; }
.nav-item-label { flex: 1; }
.nav-badge {
  background: #e1251b; color: white; font-size: 10px;
  padding: 1px 5px; border-radius: 8px; min-width: 16px; text-align: center;
}

/* ===== 右侧订单内容 ===== */
.order-content { flex: 1; min-width: 0; }

.content-title {
  font-size: 18px; font-weight: 600; color: #333;
  margin-bottom: 10px; padding-bottom: 10px;
  border-bottom: 1px solid #e0e0e0;
}

/* Tab */
.order-tabs {
  display: flex;
  align-items: center;
  background: white;
  border: 1px solid #f0f0f0;
  border-bottom: none;
  border-radius: 4px 4px 0 0;
  padding: 0 12px;
  flex-wrap: wrap;
}
.order-tab {
  display: flex; align-items: center; gap: 4px;
  padding: 12px 16px; font-size: 13px; color: #555;
  cursor: pointer; border-bottom: 2px solid transparent;
  transition: all 0.2s; white-space: nowrap;
}
.order-tab:hover { color: #e1251b; }
.order-tab.active { color: #e1251b; font-weight: 600; border-bottom-color: #e1251b; }
.tab-count {
  background: #e1251b; color: white; font-size: 10px;
  padding: 1px 5px; border-radius: 8px; min-width: 16px; text-align: center;
}
.tab-search { margin-left: auto; display: flex; align-items: center; gap: 6px; padding: 6px 0; }
.order-search-input {
  width: 200px; height: 30px; border: 1px solid #ddd; border-radius: 2px;
  padding: 0 10px; font-size: 13px; outline: none;
}
.order-search-input:focus { border-color: #e1251b; }
.order-search-btn, .order-dl-btn {
  height: 30px; padding: 0 14px; border: 1px solid #ddd;
  border-radius: 2px; font-size: 13px; cursor: pointer; background: white; color: #555;
}
.order-search-btn { background: #e1251b; color: white; border-color: #e1251b; }
.order-search-btn:hover { background: #c0392b; }
.order-dl-btn:hover { border-color: #e1251b; color: #e1251b; }

/* 表格头 */
.order-table-head {
  display: flex; align-items: center;
  background: #f5f5f5; border: 1px solid #e0e0e0; border-top: none;
  padding: 8px 12px; font-size: 12px; color: #999;
}
.year-select {
  border: 1px solid #ddd; border-radius: 2px; padding: 2px 6px;
  font-size: 12px; color: #555; background: white; cursor: pointer; outline: none;
}
.year-select:focus { border-color: #e1251b; }

/* 列宽 */
.col-product { flex: 1; min-width: 0; }
.col-buyer { width: 80px; text-align: center; flex-shrink: 0; }
.col-amount { width: 100px; text-align: center; flex-shrink: 0; }
.col-status { width: 100px; text-align: center; flex-shrink: 0; }
.col-actions { width: 120px; text-align: center; flex-shrink: 0; }

/* 空状态 */
.empty-orders {
  background: white; border: 1px solid #e0e0e0; border-top: none;
  padding: 60px 0; text-align: center;
}
.empty-icon { font-size: 48px; margin-bottom: 12px; }
.empty-tip { font-size: 14px; color: #999; }

/* 订单组 */
.order-group {
  background: white;
  border: 1px solid #e0e0e0;
  border-top: none;
  margin-top: -1px;
}

.order-head {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
  background: #f9f9f9;
  border-bottom: 1px solid #f0f0f0;
  font-size: 12px;
  color: #666;
  flex-wrap: wrap;
}
.order-date { color: #999; }
.order-no { color: #666; }
.order-no em { color: #333; font-style: normal; }
.order-tag { font-size: 11px; padding: 1px 7px; border-radius: 10px; }
.tag-charity { background: #fff3e0; color: #e67e22; border: 1px solid #f5cba7; }
.tag-free { background: #e8f8e9; color: #27ae60; border: 1px solid #a9dfbf; }
.order-store { color: #999; font-size: 11px; }
.order-delete { margin-left: auto; color: #ccc; cursor: pointer; font-size: 15px; transition: color 0.2s; }
.order-delete:hover { color: #e1251b; }

.order-row { display: flex; align-items: stretch; }

/* 商品列 */
.order-products { display: flex; flex-direction: column; border-right: 1px solid #f0f0f0; padding: 10px 12px; gap: 12px; }
.order-item { display: flex; gap: 10px; cursor: pointer; }
.order-item:hover .item-name { color: #e1251b; }
.item-img {
  width: 70px; height: 70px; flex-shrink: 0;
  background: #f9f9f9; border: 1px solid #f0f0f0; border-radius: 4px;
  display: flex; align-items: center; justify-content: center; font-size: 36px;
}
.item-info { flex: 1; min-width: 0; }
.item-name {
  font-size: 13px; color: #333; line-height: 1.5;
  overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
  transition: color 0.2s;
}
.item-spec { font-size: 11px; color: #999; margin-top: 3px; }
.item-tags { display: flex; flex-wrap: wrap; gap: 3px; margin-top: 4px; }
.item-tag { font-size: 10px; padding: 1px 5px; background: #fff0f0; color: #e1251b; border-radius: 2px; }
.item-price-qty { text-align: right; flex-shrink: 0; font-size: 12px; color: #666; white-space: nowrap; margin-left: 8px; }
.item-price { display: block; color: #e1251b; font-weight: 500; }
.item-qty { display: block; color: #999; }

/* 通用单元格 */
.order-cell {
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  padding: 12px 8px; border-right: 1px solid #f0f0f0; font-size: 13px; color: #555;
}
.order-cell:last-child { border-right: none; }

.buyer-name { font-size: 13px; color: #333; }
.buyer-via { font-size: 11px; color: #bbb; margin-top: 3px; }

.amount-total { font-size: 15px; font-weight: 600; color: #e1251b; }
.amount-note { font-size: 11px; color: #bbb; margin-top: 3px; }

.status-badge {
  font-size: 13px; font-weight: 500; padding: 3px 10px; border-radius: 12px;
  white-space: nowrap;
}
.status-shipped { background: #e8f4fd; color: #2980b9; }
.status-done { background: #e8f8e9; color: #27ae60; }
.status-cancelled { background: #f5f5f5; color: #999; }
.status-unpaid { background: #fdecea; color: #e74c3c; }
.status-review { background: #fff3e0; color: #e67e22; }
.status-note { font-size: 11px; color: #bbb; margin-top: 4px; text-align: center; line-height: 1.4; }

/* 操作按钮 */
.col-actions.order-cell { gap: 6px; }
.order-action-btn {
  width: 100%; padding: 6px 8px; border-radius: 3px; font-size: 12px;
  cursor: pointer; transition: all 0.2s; border: 1px solid;
  white-space: nowrap;
}
.btn-primary { background: #e1251b; color: white; border-color: #e1251b; }
.btn-primary:hover { background: #c0392b; border-color: #c0392b; }
.btn-default { background: white; color: #555; border-color: #ddd; }
.btn-default:hover { border-color: #e1251b; color: #e1251b; }
.btn-danger { background: white; color: #e74c3c; border-color: #e74c3c; }
.btn-danger:hover { background: #fdecea; }

/* 分页 */
.pagination-wrap {
  background: white; border: 1px solid #e0e0e0; border-top: none;
  padding: 16px; display: flex; justify-content: flex-end;
}
.pagination-wrap :deep(.is-active) { background: #e1251b !important; }
.pagination-wrap :deep(.el-pager li:hover) { color: #e1251b; }

/* ===== Footer ===== */
.order-footer { background: white; margin-top: 16px; border-top: 1px solid #f0f0f0; }
.footer-promises {
  display: flex; justify-content: space-around; padding: 24px 0;
  border-bottom: 1px solid #f0f0f0;
}
.promise-item { display: flex; align-items: center; gap: 8px; }
.promise-icon { font-size: 28px; }
.promise-title { font-size: 14px; font-weight: 600; color: #333; }

.footer-links-wrap {
  display: flex; gap: 0; justify-content: space-between;
  padding: 24px 0; border-bottom: 1px solid #f0f0f0;
}
.footer-col { flex: 1; }
.footer-col-title { font-size: 13px; font-weight: 600; color: #333; margin-bottom: 10px; }
.footer-col-link { font-size: 12px; color: #666; margin-bottom: 6px; cursor: pointer; }
.footer-col-link:hover { color: #e1251b; }

.footer-bottom { text-align: center; padding: 12px 0; font-size: 12px; color: #999; }
</style>
