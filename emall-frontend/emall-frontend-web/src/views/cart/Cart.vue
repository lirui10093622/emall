<template>
  <div class="cart-page">
    <!-- 顶部信息栏 -->
    <div class="top-bar">
      <div class="nav-inner top-bar-inner">
        <div class="top-left">
          <span class="top-link" @click="goHome">E-Mall首页</span>
          <span class="top-sep">|</span>
          <span class="top-link" @click="router.push('/order')">我的订单</span>
          <span class="top-sep">|</span>
          <span class="top-link">你好，欢迎来！</span>
          <span class="top-link red" @click="router.push('/login')">免费注册</span>
          <span class="top-sep">|</span>
          <span class="top-link">网站导航</span>
        </div>
        <div class="top-right">
          <span class="top-link" @click="router.push('/order')">我的订单</span>
          <span class="top-sep">|</span>
          <span class="top-link">企业采购</span>
          <span class="top-sep">|</span>
          <span class="top-link">手机E-Mall</span>
        </div>
      </div>
    </div>

    <!-- Header -->
    <header class="cart-header">
      <div class="nav-inner header-inner">
        <div class="logo" @click="goHome">
          <span class="logo-icon">🛒</span>
          <span class="logo-text">E-Mall</span>
        </div>
        <div class="header-title">
          购物车
          <span class="cart-count-badge">（{{ totalCount }}）</span>
        </div>
        <div class="header-delivery">
          <span class="delivery-label">配送至</span>
          <span class="delivery-addr" @click="showAddrPop = !showAddrPop">
            {{ selectedAddr }} ▾
          </span>
        </div>
        <div class="header-search">
          <input v-model="searchKeyword" class="header-search-input" placeholder="搜索商品" @keyup.enter="handleSearch" />
          <button class="header-search-btn" @click="handleSearch">搜索</button>
        </div>
      </div>
    </header>

    <!-- 主体 -->
    <main class="cart-main">
      <div class="nav-inner">
        <!-- 操作栏 -->
        <div class="action-bar">
          <div class="action-left">
            <label class="check-all">
              <input type="checkbox" :checked="isAllSelected" @change="toggleAll" />
              <span>全选</span>
            </label>
            <span class="action-btn" @click="deleteSelected">删除</span>
            <span class="action-sep">|</span>
            <span class="action-btn" @click="moveSelectedToFav">移入收藏</span>
            <span class="action-sep">|</span>
            <span class="action-btn" @click="clearInvalid">处理购物车</span>
          </div>
          <div class="action-right">
            <span class="filter-btn" :class="{ active: activeFilter === 'sale' }" @click="toggleFilter('sale')">
              <span class="filter-icon">{{ activeFilter === 'sale' ? '★' : '☆' }}</span> 降价过
            </span>
            <span class="filter-btn" :class="{ active: activeFilter === 'fav' }" @click="toggleFilter('fav')">
              <span class="filter-icon">{{ activeFilter === 'fav' ? '★' : '☆' }}</span> 收藏过
            </span>
            <span class="filter-btn" :class="{ active: activeFilter === 'viewed' }" @click="toggleFilter('viewed')">
              <span class="filter-icon">{{ activeFilter === 'viewed' ? '☑' : '☐' }}</span> 降价过
            </span>
          </div>
        </div>

        <!-- 空购物车 -->
        <div v-if="cartGroups.length === 0" class="empty-cart">
          <div class="empty-icon">🛒</div>
          <div class="empty-title">购物车还是空的</div>
          <div class="empty-desc">快去首页选购商品吧～</div>
          <button class="go-shop-btn" @click="goHome">去逛逛</button>
        </div>

        <!-- 购物车列表 -->
        <div v-else class="cart-list">
          <div v-for="group in cartGroups" :key="group.storeId" class="cart-group">
            <!-- 店铺头 -->
            <div class="group-head">
              <label class="store-check">
                <input type="checkbox" :checked="isStoreSelected(group.storeId)" @change="toggleStore(group.storeId)" />
              </label>
              <span class="store-icon">🏪</span>
              <span class="store-name">{{ group.storeName }}</span>
              <span v-if="group.isSelf" class="store-tag">自营</span>
              <span class="store-info-icon">ⓘ</span>
            </div>

            <!-- 商品行 -->
            <div v-for="item in group.items" :key="item.id" class="cart-item" :class="{ invalid: item.invalid }">
              <div class="item-check">
                <input type="checkbox" :checked="item.selected" :disabled="item.invalid" @change="toggleItem(item.id)" />
              </div>
              <div class="item-img" @click="goItem(item.productId)">
                <span class="item-emoji">{{ item.emoji }}</span>
                <span v-if="item.invalid" class="item-invalid-mask">失效</span>
              </div>
              <div class="item-detail" @click="goItem(item.productId)">
                <div class="item-name">{{ item.name }}</div>
                <div class="item-spec-row">
                  <span class="spec-label">品目：</span>
                  <span class="spec-value">{{ item.spec }}</span>
                  <span class="spec-arrow">▾</span>
                </div>
                <div class="item-stock-row">
                  <span class="stock-label">股存：</span>
                  <span class="stock-value" :class="item.stockClass">{{ item.stock }}</span>
                </div>
                <div class="item-tags">
                  <span v-for="tag in item.tags" :key="tag" class="item-tag">{{ tag }}</span>
                </div>
              </div>
              <div class="item-price-wrap">
                <div class="item-price">¥{{ item.price.toFixed(2) }}</div>
                <div v-if="item.originPrice" class="item-origin-price">¥{{ item.originPrice.toFixed(2) }}</div>
                <div v-if="item.discount" class="item-discount-tag">{{ item.discount }}</div>
              </div>
              <div class="item-qty-wrap">
                <div class="qty-ctrl">
                  <button class="qty-btn" @click="changeQty(item.id, -1)" :disabled="item.qty <= 1">−</button>
                  <input class="qty-input" v-model.number="item.qty" @change="onQtyInput(item)" min="1" max="99" />
                  <button class="qty-btn" @click="changeQty(item.id, 1)">+</button>
                </div>
                <div v-if="item.limitText" class="qty-limit">{{ item.limitText }}</div>
              </div>
              <div class="item-subtotal">
                <div class="subtotal-price">¥{{ (item.price * item.qty).toFixed(2) }}</div>
              </div>
              <div class="item-actions">
                <span class="item-action-link" @click="moveToFav(item.id)">移入收藏</span>
                <span class="item-action-link delete" @click="removeItem(item.id)">删除</span>
                <span v-if="item.invalid" class="item-action-link delete" @click="removeItem(item.id)">找相似</span>
              </div>
            </div>

            <!-- 优惠活动条 -->
            <div v-if="group.promotion" class="group-promo">
              <span class="promo-icon">🎁</span>
              <span class="promo-text">{{ group.promotion }}</span>
              <span class="promo-link">去凑单 ›</span>
            </div>
          </div>

          <!-- 猜你喜欢 -->
          <div class="recommend-section">
            <div class="recommend-title">猜你喜欢</div>
            <div class="recommend-list">
              <div v-for="p in recommendProducts" :key="p.id" class="recommend-card" @click="goItem(p.id)">
                <div class="rec-img">{{ p.emoji }}</div>
                <div class="rec-name">{{ p.name }}</div>
                <div class="rec-price">¥{{ p.price }}</div>
                <button class="rec-btn" @click.stop="addToCart(p)">加入购物车</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 结算底部栏（sticky） -->
    <div class="checkout-bar" v-if="cartGroups.length > 0">
      <div class="nav-inner checkout-inner">
        <label class="check-all-bottom">
          <input type="checkbox" :checked="isAllSelected" @change="toggleAll" />
          <span>全选</span>
        </label>
        <span class="bottom-delete" @click="deleteSelected">删除</span>
        <span class="bottom-move" @click="moveSelectedToFav">移入收藏</span>
        <div class="checkout-right">
          <div class="selected-info">
            已选择 <span class="sel-count">{{ selectedCount }}</span> 件商品
          </div>
          <div class="total-wrap">
            <span class="total-label">合计：</span>
            <span class="total-price">
              <span class="total-sym">¥</span>
              <span class="total-num">{{ totalPrice.toFixed(2) }}</span>
            </span>
          </div>
          <div v-if="totalSaved > 0" class="saved-text">已省 ¥{{ totalSaved.toFixed(2) }}</div>
          <button class="checkout-btn" :class="{ disabled: selectedCount === 0 }" @click="checkout">
            去结算（{{ selectedCount }}）
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const searchKeyword = ref('')
const selectedAddr = ref('北京市-朝阳区-望京街道')
const showAddrPop = ref(false)
const activeFilter = ref('')

const toggleFilter = (f: string) => { activeFilter.value = activeFilter.value === f ? '' : f }
const handleSearch = () => { if (searchKeyword.value.trim()) router.push({ path: '/search', query: { keyword: searchKeyword.value } }) }
const goHome = () => router.push('/')
const goItem = (id: number) => router.push(`/item/${id}`)

interface CartItem {
  id: number; productId: number; storeId: number; emoji: string; name: string
  spec: string; stock: string; stockClass: string; tags: string[]
  price: number; originPrice?: number; discount?: string
  qty: number; selected: boolean; invalid: boolean; limitText?: string
}
interface CartGroup {
  storeId: number; storeName: string; isSelf: boolean; promotion?: string; items: CartItem[]
}

const cartItems = ref<CartItem[]>([
  {
    id: 1, productId: 1, storeId: 101, emoji: '🖥️',
    name: '华联台式电脑 M1000Q 酷睿 i7 办公台式机 电脑套机 显示器 i5-13400 16G 台式 电脑组合 显示 i5-13400 16G 1TB SSD',
    spec: '主机+华联单独23.8显示器 I5-13400 16G 1TB SSD', stock: '已持缺席消客享', stockClass: 'stock-ok',
    tags: ['自营', '可开发票'], price: 4459, qty: 1, selected: true, invalid: false,
  },
  {
    id: 2, productId: 2, storeId: 102, emoji: '🥤',
    name: '百事可乐 330ml×24罐 可乐气水碳酸饮料罐 330ml*24罐',
    spec: '330ml*24罐', stock: '可选带宽道尊享', stockClass: 'stock-ok',
    tags: ['品牌直营', '满99减10'], price: 48.9, qty: 2, selected: true, invalid: false,
  },
  {
    id: 3, productId: 3, storeId: 103, emoji: '🔌',
    name: '黑色超级快充45W充电器适配iPhone充电头 正品兼容 iPhone17pro max A/16/15proMax手机ipad数据线【苹果15-17专用】1.5米快充套装iPhone7/16/15',
    spec: '【苹果15-1./牛可】1.5米快充套装 iPhone1/16/15 安全快充…', stock: '可选带宽道尊享', stockClass: 'stock-ok',
    tags: ['7天退换'], price: 99, originPrice: 128, discount: '划平价',
    qty: 1, selected: false, invalid: false,
  },
  {
    id: 4, productId: 4, storeId: 101, emoji: '⌨️',
    name: '罗技 MX Keys Mini 无线蓝牙键盘 超薄办公 背光 跨设备多设备连接',
    spec: '石墨黑', stock: '库存充足', stockClass: 'stock-ok',
    tags: ['自营', '极速达'], price: 599, originPrice: 699, discount: '限时折扣',
    qty: 1, selected: true, invalid: false, limitText: '限购2件',
  },
])

const cartGroups = computed<CartGroup[]>(() => {
  const storeMap: Record<number, CartGroup> = {
    101: { storeId: 101, storeName: '黎耀台式电脑整机写室运', isSelf: true, promotion: '满5000减300，再买¥236即可享受优惠', items: [] },
    102: { storeId: 102, storeName: '中钻 东方旗舰店', isSelf: false, promotion: '满100减15，还差¥51.1可享受优惠', items: [] },
    103: { storeId: 103, storeName: '新亮源供电组件小店', isSelf: false, items: [] },
  }
  cartItems.value.forEach(item => { if (storeMap[item.storeId]) storeMap[item.storeId].items.push(item) })
  return Object.values(storeMap).filter(g => g.items.length > 0)
})

const totalCount = computed(() => cartItems.value.length)
const selectedItems = computed(() => cartItems.value.filter(i => i.selected && !i.invalid))
const selectedCount = computed(() => selectedItems.value.reduce((s, i) => s + i.qty, 0))
const totalPrice = computed(() => selectedItems.value.reduce((s, i) => s + i.price * i.qty, 0))
const totalSaved = computed(() => selectedItems.value.reduce((s, i) => s + ((i.originPrice ?? i.price) - i.price) * i.qty, 0))

const isAllSelected = computed(() => cartItems.value.filter(i => !i.invalid).every(i => i.selected))
const isStoreSelected = (storeId: number) => cartItems.value.filter(i => i.storeId === storeId && !i.invalid).every(i => i.selected)

const toggleAll = () => {
  const next = !isAllSelected.value
  cartItems.value.forEach(i => { if (!i.invalid) i.selected = next })
}
const toggleStore = (storeId: number) => {
  const storeItems = cartItems.value.filter(i => i.storeId === storeId && !i.invalid)
  const next = !storeItems.every(i => i.selected)
  storeItems.forEach(i => { i.selected = next })
}
const toggleItem = (id: number) => {
  const item = cartItems.value.find(i => i.id === id)
  if (item) item.selected = !item.selected
}

const changeQty = (id: number, delta: number) => {
  const item = cartItems.value.find(i => i.id === id)
  if (!item) return
  const n = item.qty + delta
  if (n < 1 || n > 99) return
  item.qty = n
}
const onQtyInput = (item: CartItem) => {
  if (!item.qty || item.qty < 1) item.qty = 1
  if (item.qty > 99) item.qty = 99
}

const removeItem = async (id: number) => {
  await ElMessageBox.confirm('确认删除该商品？', '删除提示', {
    confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning',
  }).catch(() => null)
  cartItems.value = cartItems.value.filter(i => i.id !== id)
  ElMessage.success('已删除')
}

const deleteSelected = async () => {
  if (!selectedItems.value.length) { ElMessage.warning('请先选择要删除的商品'); return }
  await ElMessageBox.confirm(`确认删除已选的 ${selectedItems.value.length} 件商品？`, '批量删除', {
    confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning',
  }).catch(() => { throw new Error('cancel') })
  const ids = new Set(selectedItems.value.map(i => i.id))
  cartItems.value = cartItems.value.filter(i => !ids.has(i.id))
  ElMessage.success('已删除所选商品')
}

const moveToFav = (id: number) => {
  cartItems.value = cartItems.value.filter(i => i.id !== id)
  ElMessage.success('已移入收藏夹')
}

const moveSelectedToFav = () => {
  if (!selectedItems.value.length) { ElMessage.warning('请先选择商品'); return }
  const ids = new Set(selectedItems.value.map(i => i.id))
  cartItems.value = cartItems.value.filter(i => !ids.has(i.id))
  ElMessage.success('已移入收藏夹')
}

const clearInvalid = () => {
  const before = cartItems.value.length
  cartItems.value = cartItems.value.filter(i => !i.invalid)
  ElMessage.success(before === cartItems.value.length ? '暂无失效商品' : '已清理失效商品')
}

const checkout = () => {
  if (selectedCount.value === 0) { ElMessage.warning('请先选择商品'); return }
  ElMessage.success(`去结算 ${selectedCount.value} 件商品，合计 ¥${totalPrice.value.toFixed(2)}`)
}

// 推荐商品
const recommendProducts = [
  { id: 1, name: 'Apple iPhone 16 256G 黑色', price: '5499', emoji: '📱' },
  { id: 2, name: '索尼 WH-1000XM5 降噪耳机', price: '2299', emoji: '🎧' },
  { id: 3, name: '小米 14 Ultra 16+512G', price: '5999', emoji: '📲' },
  { id: 4, name: '戴尔 XPS 14 轻薄笔记本', price: '9999', emoji: '💻' },
  { id: 5, name: 'Nike Air Max 270 运动鞋', price: '649', emoji: '👟' },
  { id: 6, name: '雅诗兰黛 小棕瓶精华 100ml', price: '699', emoji: '✨' },
]

const addToCart = (p: typeof recommendProducts[0]) => {
  cartItems.value.push({
    id: Date.now(), productId: p.id, storeId: 101, emoji: p.emoji,
    name: p.name, spec: '默认规格', stock: '库存充足', stockClass: 'stock-ok',
    tags: ['自营'], price: parseFloat(p.price), qty: 1, selected: true, invalid: false,
  })
  ElMessage.success(`已加入购物车`)
}
</script>

<style scoped>
* { box-sizing: border-box; }

.cart-page {
  background: #f4f4f4;
  min-height: 100vh;
  font-family: 'Microsoft YaHei', sans-serif;
  padding-bottom: 60px;
}

.nav-inner { max-width: 1200px; margin: 0 auto; padding: 0 16px; }

/* 顶部信息栏 */
.top-bar { background: #f0f0f0; border-bottom: 1px solid #e0e0e0; padding: 4px 0; }
.top-bar-inner { display: flex; justify-content: space-between; align-items: center; }
.top-left, .top-right { display: flex; align-items: center; gap: 0; }
.top-link { font-size: 12px; color: #666; padding: 0 6px; cursor: pointer; white-space: nowrap; }
.top-link:hover { color: #e1251b; }
.top-link.red { color: #e1251b; }
.top-sep { color: #ccc; font-size: 12px; }

/* Header */
.cart-header { background: white; padding: 14px 0; border-bottom: 1px solid #f0f0f0; }
.header-inner { display: flex; align-items: center; gap: 20px; }
.logo { display: flex; align-items: center; gap: 6px; cursor: pointer; flex-shrink: 0; }
.logo-icon { font-size: 28px; }
.logo-text { font-size: 28px; font-weight: 900; color: #e1251b; letter-spacing: -1px; }
.header-title {
  font-size: 22px; color: #666; font-weight: 400;
  border-left: 1px solid #e0e0e0; padding-left: 18px; flex-shrink: 0;
}
.cart-count-badge { font-size: 18px; color: #e1251b; }
.header-delivery { display: flex; align-items: center; gap: 6px; font-size: 13px; color: #999; flex-shrink: 0; }
.delivery-label { color: #999; }
.delivery-addr { color: #333; cursor: pointer; border-bottom: 1px dashed #999; }
.delivery-addr:hover { color: #e1251b; border-color: #e1251b; }
.header-search {
  flex: 1; display: flex; max-width: 500px; height: 36px;
  border: 2px solid #e1251b; margin-left: auto;
}
.header-search-input { flex: 1; border: none; outline: none; padding: 0 14px; font-size: 14px; color: #333; }
.header-search-btn {
  background: #e1251b; color: white; border: none; padding: 0 22px;
  font-size: 15px; cursor: pointer; font-weight: 500;
}
.header-search-btn:hover { background: #c0392b; }

/* 主体 */
.cart-main { padding: 14px 0 24px; }

/* 操作栏 */
.action-bar {
  display: flex; align-items: center; justify-content: space-between;
  background: #f9f9f9; border: 1px solid #e8e8e8; border-radius: 4px 4px 0 0;
  padding: 10px 16px; margin-bottom: -1px;
}
.action-left { display: flex; align-items: center; gap: 8px; }
.check-all { display: flex; align-items: center; gap: 5px; font-size: 14px; color: #333; cursor: pointer; user-select: none; }
.check-all input { accent-color: #e1251b; width: 15px; height: 15px; cursor: pointer; }
.action-btn { font-size: 13px; color: #555; cursor: pointer; padding: 3px 0; }
.action-btn:hover { color: #e1251b; }
.action-sep { color: #ddd; }
.action-right { display: flex; align-items: center; gap: 16px; }
.filter-btn { font-size: 12px; color: #999; cursor: pointer; display: flex; align-items: center; gap: 3px; transition: color 0.2s; }
.filter-btn:hover, .filter-btn.active { color: #e1251b; }
.filter-icon { font-size: 14px; }

/* 空购物车 */
.empty-cart { background: white; padding: 80px 0; text-align: center; border: 1px solid #e8e8e8; }
.empty-icon { font-size: 64px; margin-bottom: 16px; }
.empty-title { font-size: 18px; color: #333; margin-bottom: 8px; }
.empty-desc { font-size: 14px; color: #999; margin-bottom: 24px; }
.go-shop-btn {
  background: #e1251b; color: white; border: none; border-radius: 4px;
  padding: 10px 36px; font-size: 16px; cursor: pointer;
}
.go-shop-btn:hover { background: #c0392b; }

/* 购物车列表 */
.cart-list { display: flex; flex-direction: column; gap: 0; }

/* 店铺分组 */
.cart-group { background: white; border: 1px solid #e8e8e8; border-top: none; }
.group-head {
  display: flex; align-items: center; gap: 8px;
  padding: 10px 16px; background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}
.store-check input { accent-color: #e1251b; width: 15px; height: 15px; cursor: pointer; }
.store-icon { font-size: 16px; }
.store-name { font-size: 14px; color: #333; font-weight: 500; }
.store-tag {
  font-size: 11px; background: #e1251b; color: white;
  padding: 1px 6px; border-radius: 2px;
}
.store-info-icon { font-size: 14px; color: #bbb; cursor: pointer; margin-left: 4px; }

/* 商品行 */
.cart-item {
  display: flex; align-items: center; gap: 0;
  padding: 16px; border-bottom: 1px solid #f5f5f5;
  transition: background 0.15s;
}
.cart-item:last-child { border-bottom: none; }
.cart-item:hover { background: #fffef8; }
.cart-item.invalid { opacity: 0.6; }

.item-check { width: 40px; flex-shrink: 0; display: flex; align-items: center; justify-content: center; }
.item-check input { accent-color: #e1251b; width: 15px; height: 15px; cursor: pointer; }

.item-img {
  width: 80px; height: 80px; flex-shrink: 0; position: relative;
  background: #f9f9f9; border: 1px solid #f0f0f0; border-radius: 4px;
  display: flex; align-items: center; justify-content: center; cursor: pointer; margin-right: 14px;
}
.item-emoji { font-size: 44px; }
.item-invalid-mask {
  position: absolute; inset: 0; background: rgba(0,0,0,0.4);
  display: flex; align-items: center; justify-content: center;
  color: white; font-size: 13px; border-radius: 4px;
}

.item-detail { flex: 1; min-width: 0; padding-right: 16px; cursor: pointer; }
.item-name {
  font-size: 13px; color: #333; line-height: 1.5;
  overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
  margin-bottom: 6px;
}
.item-name:hover { color: #e1251b; }
.item-spec-row, .item-stock-row { display: flex; align-items: center; gap: 4px; font-size: 12px; margin-bottom: 3px; }
.spec-label, .stock-label { color: #999; flex-shrink: 0; }
.spec-value { color: #555; }
.spec-arrow { color: #bbb; font-size: 10px; cursor: pointer; }
.spec-arrow:hover { color: #e1251b; }
.stock-ok { color: #27ae60; }
.stock-warn { color: #e67e22; }
.stock-out { color: #e1251b; }
.item-tags { display: flex; flex-wrap: wrap; gap: 4px; margin-top: 6px; }
.item-tag { font-size: 11px; padding: 1px 5px; background: #fff0f0; color: #e1251b; border-radius: 2px; }

.item-price-wrap { width: 120px; flex-shrink: 0; text-align: center; }
.item-price { font-size: 16px; color: #e1251b; font-weight: 600; }
.item-origin-price { font-size: 12px; color: #bbb; text-decoration: line-through; margin-top: 2px; }
.item-discount-tag {
  display: inline-block; font-size: 11px; color: #e67e22;
  border: 1px solid #e67e22; padding: 1px 5px; border-radius: 2px; margin-top: 3px;
}

.item-qty-wrap { width: 110px; flex-shrink: 0; display: flex; flex-direction: column; align-items: center; gap: 4px; }
.qty-ctrl { display: flex; align-items: center; border: 1px solid #ddd; border-radius: 2px; overflow: hidden; }
.qty-btn {
  width: 28px; height: 28px; border: none; background: #f5f5f5;
  font-size: 16px; cursor: pointer; color: #333; display: flex; align-items: center; justify-content: center;
  transition: all 0.15s; flex-shrink: 0;
}
.qty-btn:hover:not(:disabled) { background: #e1251b; color: white; }
.qty-btn:disabled { color: #ccc; cursor: not-allowed; }
.qty-input {
  width: 36px; height: 28px; border: none;
  border-left: 1px solid #ddd; border-right: 1px solid #ddd;
  text-align: center; font-size: 13px; outline: none; color: #333;
}
.qty-input::-webkit-inner-spin-button, .qty-input::-webkit-outer-spin-button { -webkit-appearance: none; }
.qty-limit { font-size: 11px; color: #bbb; }

.item-subtotal { width: 110px; flex-shrink: 0; text-align: center; }
.subtotal-price { font-size: 16px; color: #e1251b; font-weight: 700; }

.item-actions { width: 80px; flex-shrink: 0; display: flex; flex-direction: column; align-items: center; gap: 6px; }
.item-action-link { font-size: 12px; color: #999; cursor: pointer; transition: color 0.2s; }
.item-action-link:hover { color: #e1251b; }
.item-action-link.delete:hover { color: #e74c3c; }

/* 优惠活动 */
.group-promo {
  display: flex; align-items: center; gap: 8px;
  padding: 8px 16px; background: #fffbf0; border-top: 1px dashed #f5e6a0;
  font-size: 12px; color: #e67e22;
}
.promo-icon { font-size: 14px; }
.promo-text { flex: 1; }
.promo-link { color: #e1251b; cursor: pointer; flex-shrink: 0; }
.promo-link:hover { text-decoration: underline; }

/* 推荐 */
.recommend-section {
  background: white; border: 1px solid #e8e8e8; border-top: none;
  padding: 16px; margin-top: 0;
}
.recommend-title { font-size: 16px; font-weight: 600; color: #333; margin-bottom: 14px; }
.recommend-list { display: grid; grid-template-columns: repeat(6, 1fr); gap: 12px; }
.recommend-card {
  border: 1px solid #f0f0f0; border-radius: 4px; padding: 12px;
  text-align: center; cursor: pointer; transition: all 0.2s;
}
.recommend-card:hover { border-color: #e1251b; box-shadow: 0 4px 12px rgba(0,0,0,0.08); transform: translateY(-2px); }
.rec-img { font-size: 48px; margin-bottom: 8px; }
.rec-name { font-size: 12px; color: #333; overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; line-height: 1.4; min-height: 34px; margin-bottom: 6px; }
.rec-price { font-size: 15px; color: #e1251b; font-weight: 700; margin-bottom: 8px; }
.rec-btn {
  width: 100%; padding: 5px 0; border: 1px solid #e1251b; border-radius: 3px;
  background: white; color: #e1251b; font-size: 12px; cursor: pointer; transition: all 0.2s;
}
.rec-btn:hover { background: #e1251b; color: white; }

/* 结算底栏 */
.checkout-bar {
  position: fixed; bottom: 0; left: 0; right: 0;
  background: white; border-top: 2px solid #e1251b;
  box-shadow: 0 -2px 12px rgba(0,0,0,0.1);
  z-index: 100; padding: 0;
}
.checkout-inner {
  display: flex; align-items: center; gap: 16px;
  height: 52px;
}
.check-all-bottom { display: flex; align-items: center; gap: 5px; font-size: 14px; color: #333; cursor: pointer; user-select: none; flex-shrink: 0; }
.check-all-bottom input { accent-color: #e1251b; width: 15px; height: 15px; cursor: pointer; }
.bottom-delete, .bottom-move { font-size: 13px; color: #555; cursor: pointer; flex-shrink: 0; }
.bottom-delete:hover, .bottom-move:hover { color: #e1251b; }

.checkout-right { margin-left: auto; display: flex; align-items: center; gap: 16px; }
.selected-info { font-size: 13px; color: #666; flex-shrink: 0; }
.sel-count { color: #e1251b; font-weight: bold; }
.total-wrap { display: flex; align-items: baseline; gap: 2px; flex-shrink: 0; }
.total-label { font-size: 14px; color: #333; }
.total-price { display: flex; align-items: baseline; }
.total-sym { font-size: 16px; color: #e1251b; font-weight: 700; }
.total-num { font-size: 28px; color: #e1251b; font-weight: 900; line-height: 1; }
.saved-text { font-size: 12px; color: #27ae60; flex-shrink: 0; white-space: nowrap; }
.checkout-btn {
  height: 52px; width: 160px; background: #e1251b; color: white;
  border: none; font-size: 18px; font-weight: 600; cursor: pointer;
  transition: background 0.2s; flex-shrink: 0; letter-spacing: 1px;
}
.checkout-btn:hover { background: #c0392b; }
.checkout-btn.disabled { background: #f5a29e; cursor: not-allowed; }
</style>
