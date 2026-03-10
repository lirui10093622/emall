<template>
  <div class="home-page">
    <!-- 顶部活动横幅 -->
    <div class="top-banner">
      <div class="banner-content">
        <span class="banner-tag">38节</span>
        <span class="banner-text">38节礼遇季 · 官方直降5折起，好物尽在好价！</span>
        <el-button size="small" class="banner-btn" round>立即抢购</el-button>
      </div>
    </div>

    <!-- 主导航栏 -->
    <div class="header-nav">
      <div class="nav-inner">
        <!-- Logo -->
        <div class="logo" @click="scrollToTop">
          <span class="logo-icon">🛒</span>
          <span class="logo-text">E-Mall</span>
        </div>

        <!-- 搜索框 -->
        <div class="search-bar">
          <!-- 左侧分类下拉 -->
          <div class="search-category-dropdown" @click="toggleCategoryMenu">
            <span class="search-cat-text">{{ searchCategoryLabel }}</span>
            <el-icon class="search-cat-arrow" :class="{ rotated: categoryMenuVisible }"><ArrowDown /></el-icon>
            <!-- 下拉菜单：仅商品/店铺 -->
            <div v-if="categoryMenuVisible" class="category-dropdown-menu">
              <div
                v-for="type in searchTypes"
                :key="type.value"
                class="category-dropdown-item"
                :class="{ active: searchCategory === type.value }"
                @click.stop="selectCategory(type.value, type.label)"
              >{{ type.label }}</div>
            </div>
          </div>
          <div class="search-divider"></div>
          <!-- 输入框 + 相机图标 -->
          <div class="search-input-wrap">
            <input
              v-model="searchKeyword"
              class="search-native-input"
              placeholder="搜索商品、品牌、店铺"
              @keyup.enter="handleSearch"
            />
            <span class="search-camera-icon" title="拍照搜索">
              <el-icon size="18"><Camera /></el-icon>
            </span>
          </div>
          <button class="search-btn" @click="handleSearch">搜索</button>
        </div>

        <!-- 用户操作区 -->
        <div class="user-actions">
          <el-button text class="action-item" @click="goLogin">
            <el-icon><User /></el-icon>
            登录 / 注册
          </el-button>
          <div class="action-item cart-btn" @click="handleCart">
            <el-badge :value="cartCount" :max="99" class="cart-badge">
              <el-icon size="22"><ShoppingCart /></el-icon>
            </el-badge>
            <span>购物车</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 快捷链接栏 -->
    <div class="quick-links-bar">
      <div class="nav-inner">
        <span v-for="link in quickLinks" :key="link.id" class="quick-link">{{ link.name }}</span>
      </div>
    </div>

    <!-- 分类图标导航 -->
    <div class="category-icon-nav">
      <div class="nav-inner">
        <div v-for="item in iconCategories" :key="item.id" class="icon-cat-item">
          <span class="icon-cat-icon">{{ item.icon }}</span>
          <span class="icon-cat-name">{{ item.name }}</span>
        </div>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="main-content nav-inner">
      <!-- 左侧分类列表 -->
      <div class="left-sidebar">
        <div
          v-for="cat in categories"
          :key="cat.id"
          class="cat-item"
          @mouseenter="activeCat = cat.id"
          @mouseleave="activeCat = null"
        >
          <span class="cat-name">{{ cat.name }}</span>
          <span class="cat-sub">{{ cat.subs.join(' / ') }}</span>
          <!-- 子分类浮层 -->
          <div v-if="activeCat === cat.id" class="sub-panel">
            <div class="sub-panel-title">{{ cat.name }}</div>
            <div class="sub-panel-items">
              <span v-for="sub in cat.subs" :key="sub" class="sub-panel-item">{{ sub }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间轮播 + 爆款商品 -->
      <div class="center-content">
        <!-- 轮播图 -->
        <el-carousel height="320px" class="main-carousel" indicator-position="outside">
          <el-carousel-item v-for="slide in banners" :key="slide.id">
            <div class="carousel-slide" :style="{ background: slide.bg }">
              <div class="slide-content">
                <div class="slide-tag">{{ slide.tag }}</div>
                <div class="slide-title">{{ slide.title }}</div>
                <div class="slide-desc">{{ slide.desc }}</div>
                <el-button type="danger" round class="slide-btn">{{ slide.btnText }}</el-button>
              </div>
              <div class="slide-image">{{ slide.emoji }}</div>
            </div>
          </el-carousel-item>
        </el-carousel>

        <!-- 爆款商品横向区 -->
        <div class="hot-section">
          <div class="hot-header">
            <span class="hot-title">🔥 今日爆款</span>
            <span class="hot-more">查看更多 ›</span>
          </div>
          <div class="hot-products">
            <div v-for="product in hotProducts" :key="product.id" class="hot-product-card">
              <div class="hot-product-image">{{ product.emoji }}</div>
              <div class="hot-product-name">{{ product.name }}</div>
              <div class="hot-product-price">
                <span class="price-symbol">¥</span>{{ product.price }}
              </div>
              <div class="hot-product-original">¥{{ product.originalPrice }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧用户信息 -->
      <div class="right-panel">
        <!-- 登录卡片 -->
        <div class="user-card">
          <div class="user-avatar">👤</div>
          <div class="user-info-text">
            <div class="user-greeting">Hi，欢迎来到E-Mall</div>
            <div class="user-btns">
              <el-button type="danger" size="small" @click="goLogin">登录</el-button>
              <el-button size="small" @click="goLogin">注册</el-button>
            </div>
          </div>
        </div>
        <!-- 用户数据 -->
        <div class="user-stats">
          <div v-for="stat in userStats" :key="stat.id" class="stat-item">
            <div class="stat-num">{{ stat.num }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
        <!-- 快捷入口 -->
        <div class="quick-entry-title">我的常用</div>
        <div class="quick-entries">
          <div v-for="entry in quickEntries" :key="entry.id" class="quick-entry">
            <span class="entry-icon">{{ entry.icon }}</span>
            <span class="entry-name">{{ entry.name }}</span>
          </div>
        </div>

        <!-- 品牌入口 -->
        <div class="brand-section">
          <div class="quick-entry-title">品牌馆</div>
          <div class="brand-list">
            <div v-for="brand in brands" :key="brand.id" class="brand-item">
              {{ brand.name }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 推荐标签 + 商品网格 -->
    <div class="recommend-section nav-inner">
      <el-tabs v-model="activeTab" class="recommend-tabs">
        <el-tab-pane label="❤️ 为你推荐" name="recommend" />
        <el-tab-pane label="🔥 热销榜" name="hot" />
        <el-tab-pane label="🆕 新品上市" name="new" />
        <el-tab-pane label="💰 特价专区" name="sale" />
      </el-tabs>

      <div class="product-grid">
        <div
          v-for="product in filteredProducts"
          :key="product.id"
          class="product-card"
          @click="handleProductClick(product)"
        >
          <div class="product-image-wrap">
            <div class="product-image">{{ product.emoji }}</div>
            <div v-if="product.tag" class="product-tag" :class="product.tagType">{{ product.tag }}</div>
          </div>
          <div class="product-info">
            <div class="product-name">{{ product.name }}</div>
            <div class="product-desc">{{ product.desc }}</div>
            <div class="product-bottom">
              <div class="product-price">
                <span class="price-symbol">¥</span>
                <span class="price-value">{{ product.price }}</span>
                <span class="price-original">¥{{ product.originalPrice }}</span>
              </div>
              <div class="product-sales">已售 {{ product.sales }}</div>
            </div>
            <el-button type="danger" size="small" class="add-cart-btn" @click.stop="addToCart(product)">
              加入购物车
            </el-button>
          </div>
        </div>
      </div>

      <!-- 加载更多 -->
      <div class="load-more">
        <el-button plain round @click="loadMore">加载更多商品</el-button>
      </div>
    </div>

    <!-- 底部 Footer -->
    <div class="footer">
      <div class="footer-inner nav-inner">
        <div class="footer-promises">
          <div v-for="p in promises" :key="p.id" class="promise-item">
            <span class="promise-icon">{{ p.icon }}</span>
            <div>
              <div class="promise-title">{{ p.title }}</div>
              <div class="promise-desc">{{ p.desc }}</div>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <span>© 2024 E-Mall 电商平台</span>
          <span class="footer-links">
            <a href="#">关于我们</a>
            <a href="#">联系客服</a>
            <a href="#">隐私政策</a>
            <a href="#">用户协议</a>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, User, ShoppingCart, ArrowDown, Camera } from '@element-plus/icons-vue'

const router = useRouter()
const searchKeyword = ref('')
const searchCategory = ref('product')
const searchCategoryLabel = ref('商品')
const categoryMenuVisible = ref(false)

const searchTypes = [
  { value: 'product', label: '商品' },
  { value: 'shop', label: '店铺' },
]
const activeCat = ref<number | null>(null)
const activeTab = ref('recommend')
const cartCount = ref(3)

const toggleCategoryMenu = () => {
  categoryMenuVisible.value = !categoryMenuVisible.value
}

const selectCategory = (value: string, label: string) => {
  searchCategory.value = value
  searchCategoryLabel.value = label
  categoryMenuVisible.value = false
}

// 快捷链接
const quickLinks = [
  { id: 1, name: '优惠券' },
  { id: 2, name: '价值惠' },
  { id: 3, name: '新品发布' },
  { id: 4, name: '品牌特卖' },
  { id: 5, name: '限时秒杀' },
  { id: 6, name: '每日特价' },
  { id: 7, name: '拼团活动' },
  { id: 8, name: '积分商城' },
]

// 顶部分类（用于搜索下拉）
const topCategories = [
  { id: 1, name: '手机数码' },
  { id: 2, name: '家用电器' },
  { id: 3, name: '电脑办公' },
  { id: 4, name: '服装鞋包' },
  { id: 5, name: '食品生鲜' },
  { id: 6, name: '美妆护肤' },
  { id: 7, name: '家居家装' },
  { id: 8, name: '运动户外' },
]

// 图标分类导航
const iconCategories = [
  { id: 1, icon: '💄', name: '美妆馆' },
  { id: 2, icon: '🛒', name: '京东超市' },
  { id: 3, icon: '🏢', name: '企业购' },
  { id: 4, icon: '🌍', name: '全球购' },
  { id: 5, icon: '📱', name: '手机数码' },
  { id: 6, icon: '🍎', name: '生鲜食品' },
  { id: 7, icon: '🏠', name: '家居家装' },
  { id: 8, icon: '📺', name: '家用电器' },
  { id: 9, icon: '🎽', name: '运动户外' },
  { id: 10, icon: '🏷️', name: '新品特卖' },
  { id: 11, icon: '💻', name: '电脑办公' },
  { id: 12, icon: '🎮', name: '游戏动漫' },
]

// 左侧商品分类
const categories = [
  { id: 1, name: '冰洗 / 空调 / 电视', subs: ['冰箱', '洗衣机', '空调', '电视'] },
  { id: 2, name: '电脑 / 办公 / 文具', subs: ['笔记本', '台式机', '打印机', '键盘鼠标'] },
  { id: 3, name: '手机 / 运营商 / 数码', subs: ['手机', '平板', '耳机', '智能手表'] },
  { id: 4, name: '生活电器 / 厨卫小家电', subs: ['吸尘器', '电饭锅', '微波炉', '电热水壶'] },
  { id: 5, name: '食品 / 酒类 / 生鲜 / 特产', subs: ['零食', '白酒', '水果', '海鲜'] },
  { id: 6, name: '美妆 / 个护 / 清洁', subs: ['护肤', '彩妆', '香水', '口腔护理'] },
  { id: 7, name: '男装 / 女装 / 童装', subs: ['T恤', '外套', '裤子', '连衣裙'] },
  { id: 8, name: '运动 / 户外', subs: ['运动鞋', '健身器材', '户外装备', '骑行'] },
  { id: 9, name: '汽车 / 摩托 / 配件', subs: ['车载电器', '汽车用品', '轮胎', '机油'] },
  { id: 10, name: '玩具 / 乐器 / 文具', subs: ['积木', '乐高', '吉他', '画具'] },
]

// 轮播图数据
const banners = [
  {
    id: 1,
    bg: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    tag: '限时特惠',
    title: '春季焕新大促',
    desc: '全场5折起 · 爆款直降',
    btnText: '立即抢购',
    emoji: '🌸',
  },
  {
    id: 2,
    bg: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    tag: '数码新品',
    title: '旗舰手机发布',
    desc: '全新系列上市 · 预售优惠',
    btnText: '查看详情',
    emoji: '📱',
  },
  {
    id: 3,
    bg: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    tag: '家电特卖',
    title: '家电以旧换新',
    desc: '补贴最高1500元',
    btnText: '参与活动',
    emoji: '📺',
  },
  {
    id: 4,
    bg: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    tag: '生鲜直送',
    title: '每日新鲜到家',
    desc: '产地直发 · 次日达',
    btnText: '马上购买',
    emoji: '🥦',
  },
]

// 今日爆款
const hotProducts = [
  { id: 1, name: '雀巢奶粉', price: '216.51', originalPrice: '259', emoji: '🥛' },
  { id: 2, name: '苹果笔记本', price: '7069', originalPrice: '7999', emoji: '💻' },
  { id: 3, name: '护肤套装', price: '79.9', originalPrice: '158', emoji: '💆' },
  { id: 4, name: '不锈钢锅', price: '59.15', originalPrice: '129', emoji: '🍳' },
  { id: 5, name: '运动耳机', price: '199', originalPrice: '399', emoji: '🎧' },
]

// 右侧用户数据
const userStats = [
  { id: 1, num: '0', label: '优惠券' },
  { id: 2, num: '0', label: '收藏' },
  { id: 3, num: '0', label: '足迹' },
  { id: 4, num: '0', label: '关注店' },
]

// 快捷入口
const quickEntries = [
  { id: 1, icon: '📦', name: '我的订单' },
  { id: 2, icon: '❤️', name: '我的收藏' },
  { id: 3, icon: '👣', name: '浏览历史' },
  { id: 4, icon: '🏷️', name: '我的优惠' },
  { id: 5, icon: '📍', name: '收货地址' },
  { id: 6, icon: '💬', name: '联系客服' },
]

// 品牌
const brands = [
  { id: 1, name: 'Apple' },
  { id: 2, name: 'HUAWEI' },
  { id: 3, name: 'Samsung' },
  { id: 4, name: 'Nike' },
]

// 商品列表
const allProducts = [
  { id: 1, name: 'Apple iPhone 15 Pro 256G 黑色钛金属', desc: '超瓷晶玻璃 · A17 Pro芯片', price: '8999', originalPrice: '9999', sales: '1.2万', emoji: '📱', tag: '热卖', tagType: 'danger', type: ['recommend', 'hot'] },
  { id: 2, name: '华为 Mate 60 Pro 512G 宣纸白', desc: '卫星通话 · 昆仑玻璃', price: '6999', originalPrice: '7999', sales: '8.6千', emoji: '📲', tag: '新品', tagType: 'success', type: ['recommend', 'new'] },
  { id: 3, name: '小米 14 Ultra 大师版 16+1T', desc: '徕卡光学 · 骁龙8Gen3', price: '5999', originalPrice: '6499', sales: '5.3千', emoji: '🔴', tag: '特价', tagType: 'warning', type: ['recommend', 'sale'] },
  { id: 4, name: 'OPPO Find X7 Ultra 天空之镜版', desc: '哈苏镜头 · 100W快充', price: '5499', originalPrice: '5999', sales: '3.1千', emoji: '🟤', tag: '', tagType: '', type: ['recommend', 'hot'] },
  { id: 5, name: '戴尔 XPS 14 超薄本 i7-13700H', desc: 'OLED触控屏 · 2.8K分辨率', price: '9999', originalPrice: '11999', sales: '2.7千', emoji: '💻', tag: '热卖', tagType: 'danger', type: ['recommend', 'hot'] },
  { id: 6, name: '索尼 WH-1000XM5 降噪耳机', desc: '行业最强降噪 · 30小时续航', price: '2299', originalPrice: '2799', sales: '4.5千', emoji: '🎧', tag: '新品', tagType: 'success', type: ['recommend', 'new'] },
  { id: 7, name: '雅诗兰黛 小棕瓶精华 100ml', desc: '修护屏障 · 淡纹紧致', price: '699', originalPrice: '1080', sales: '2.1万', emoji: '✨', tag: '特价', tagType: 'warning', type: ['recommend', 'sale'] },
  { id: 8, name: 'Nike Air Max 270 运动鞋', desc: '气垫缓震 · 多色可选', price: '649', originalPrice: '999', sales: '6.8千', emoji: '👟', tag: '', tagType: '', type: ['recommend', 'sale'] },
  { id: 9, name: '格力 1.5匹 变频空调 冷暖两用', desc: '一级能效 · 自清洁', price: '2599', originalPrice: '3199', sales: '9.2千', emoji: '❄️', tag: '热卖', tagType: 'danger', type: ['hot'] },
  { id: 10, name: '飞利浦 55寸 QLED 4K电视', desc: '120Hz · HDR10+ · 量子点', price: '3299', originalPrice: '4599', sales: '1.8千', emoji: '📺', tag: '', tagType: '', type: ['hot', 'new'] },
  { id: 11, name: '乐高 积木 创意百变 42156', desc: '1:8 迈凯伦跑车 · 成人收藏版', price: '1299', originalPrice: '1599', sales: '3.4千', emoji: '🧩', tag: '新品', tagType: 'success', type: ['new'] },
  { id: 12, name: '美的 变频冰箱 双开门 502L', desc: '风冷无霜 · 精准控温', price: '2799', originalPrice: '3599', sales: '7.1千', emoji: '🧊', tag: '特价', tagType: 'warning', type: ['sale', 'hot'] },
  { id: 13, name: '茅台 飞天 53度 500ml', desc: '酱香型 · 正品授权', price: '2499', originalPrice: '2999', sales: '1.5万', emoji: '🍶', tag: '热卖', tagType: 'danger', type: ['hot', 'recommend'] },
  { id: 14, name: '舒肤佳 抑菌洗手液 4件套', desc: '温和配方 · 99.9%抑菌', price: '39.9', originalPrice: '79', sales: '3.2万', emoji: '🧴', tag: '特价', tagType: 'warning', type: ['sale'] },
  { id: 15, name: 'Dyson V15 Detect 吸尘器', desc: '激光检测 · 60min续航', price: '4490', originalPrice: '5490', sales: '6.3千', emoji: '🌀', tag: '', tagType: '', type: ['recommend', 'new'] },
  { id: 16, name: '安踏 儿童运动套装 2024春款', desc: '速干面料 · 弹力舒适', price: '199', originalPrice: '359', sales: '8.9千', emoji: '🧒', tag: '新品', tagType: 'success', type: ['new', 'sale'] },
  { id: 17, name: '小米 Redmi Note 13 Pro 256G', desc: '2亿像素 · 67W快充', price: '1599', originalPrice: '1999', sales: '1.1万', emoji: '📳', tag: '特价', tagType: 'warning', type: ['sale', 'recommend'] },
  { id: 18, name: '海尔 洗烘一体机 10kg', desc: '直驱电机 · 智能烘干', price: '3299', originalPrice: '4199', sales: '4.7千', emoji: '🌊', tag: '', tagType: '', type: ['hot', 'new'] },
  { id: 19, name: '百草味 坚果大礼包 1kg', desc: '8种坚果 · 原味无添加', price: '79.9', originalPrice: '128', sales: '5.6万', emoji: '🥜', tag: '热卖', tagType: 'danger', type: ['hot', 'sale'] },
  { id: 20, name: 'SK-II 神仙水精华液 230ml', desc: 'Pitera™ 精华 · 提亮肤色', price: '1099', originalPrice: '1490', sales: '2.4万', emoji: '💎', tag: '', tagType: '', type: ['recommend'] },
]

const filteredProducts = computed(() => {
  if (activeTab.value === 'recommend') return allProducts.filter(p => p.type.includes('recommend'))
  if (activeTab.value === 'hot') return allProducts.filter(p => p.type.includes('hot'))
  if (activeTab.value === 'new') return allProducts.filter(p => p.type.includes('new'))
  if (activeTab.value === 'sale') return allProducts.filter(p => p.type.includes('sale'))
  return allProducts
})

// 底部承诺
const promises = [
  { id: 1, icon: '🚚', title: '极速配送', desc: '次日达 / 小时购' },
  { id: 2, icon: '✅', title: '正品保障', desc: '假一赔十' },
  { id: 3, icon: '🔄', title: '无忧退换', desc: '7天无理由' },
  { id: 4, icon: '🔒', title: '安全支付', desc: '多重加密保护' },
  { id: 5, icon: '🎁', title: '会员特权', desc: '专属优惠折扣' },
]

const handleSearch = () => {
  const keyword = searchKeyword.value.trim()
  if (!keyword) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  router.push({
    path: '/search',
    query: {
      keyword,
    },
  })
}

const goLogin = () => {
  router.push('/login')
}

const handleCart = () => router.push('/cart')

const handleProductClick = (product: typeof allProducts[0]) => {
  router.push(`/item/${product.id}`)
}

const addToCart = (product: typeof allProducts[0]) => {
  cartCount.value++
  ElMessage.success(`已将「${product.name}」加入购物车`)
}

const loadMore = () => {
  ElMessage.info('加载更多商品...')
}

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.home-page {
  background-color: #f4f4f4;
  min-height: 100vh;
  font-family: 'Microsoft YaHei', sans-serif;
}

.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
}

/* 顶部活动横幅 */
.top-banner {
  background: linear-gradient(90deg, #c0392b, #e1251b, #c0392b);
  color: white;
  padding: 8px 0;
  text-align: center;
}

.banner-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.banner-tag {
  background: white;
  color: #e1251b;
  font-weight: bold;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

.banner-text {
  font-size: 14px;
  font-weight: 500;
}

.banner-btn {
  background: white;
  color: #e1251b;
  border: none;
  font-weight: bold;
  font-size: 12px;
}

/* 导航栏 */
.header-nav {
  background: white;
  border-bottom: 2px solid #e1251b;
  padding: 12px 0;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.header-nav .nav-inner {
  display: flex;
  align-items: center;
  gap: 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  flex-shrink: 0;
}

.logo-icon {
  font-size: 28px;
}

.logo-text {
  font-size: 26px;
  font-weight: 900;
  color: #e1251b;
  letter-spacing: -1px;
}

.search-bar {
  flex: 1;
  display: flex;
  align-items: stretch;
  max-width: 650px;
  height: 40px;
  border: 2px solid #e1251b;
  background: white;
  position: relative;
  box-sizing: content-box;
}

/* 左侧分类下拉 */
.search-category-dropdown {
  position: relative;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 0 10px 0 14px;
  cursor: pointer;
  flex-shrink: 0;
  user-select: none;
  white-space: nowrap;
}

.search-category-dropdown:hover .search-cat-text {
  color: #e1251b;
}

.search-cat-text {
  font-size: 14px;
  color: #333;
  transition: color 0.2s;
}

.search-cat-arrow {
  font-size: 11px;
  color: #666;
  transition: transform 0.2s;
  display: flex;
  align-items: center;
}

.search-cat-arrow.rotated {
  transform: rotate(180deg);
}

.category-dropdown-menu {
  position: absolute;
  top: calc(100% + 4px);
  left: -2px;
  width: 80px;
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  z-index: 300;
  overflow: hidden;
}

.category-dropdown-item {
  padding: 9px 14px;
  font-size: 13px;
  color: #333;
  cursor: pointer;
  transition: background 0.15s;
}

.category-dropdown-item:hover {
  background: #fff0f0;
  color: #e1251b;
}

.category-dropdown-item.active {
  color: #e1251b;
  font-weight: 500;
  background: #fff5f5;
}

/* 分隔线 */
.search-divider {
  width: 1px;
  background: #dcdfe6;
  margin: 8px 0;
  flex-shrink: 0;
  align-self: auto;
}

/* 输入框区域 */
.search-input-wrap {
  flex: 1;
  display: flex;
  align-items: center;
  position: relative;
  min-width: 0;
  overflow: hidden;
}

.search-native-input {
  flex: 1;
  height: 100%;
  border: none;
  outline: none;
  padding: 0 40px 0 12px;
  font-size: 14px;
  color: #333;
  background: transparent;
  min-width: 0;
  box-sizing: border-box;
}

.search-native-input::placeholder {
  color: #bbb;
}

.search-camera-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #bbb;
  cursor: pointer;
  display: flex;
  align-items: center;
  flex-shrink: 0;
  transition: color 0.2s;
}

.search-camera-icon:hover {
  color: #e1251b;
}

/* 搜索按钮 */
.search-btn {
  flex-shrink: 0;
  border: none;
  outline: none;
  background: #e1251b;
  color: white;
  font-size: 15px;
  font-weight: 500;
  padding: 0 26px;
  cursor: pointer;
  letter-spacing: 2px;
  transition: background 0.2s;
  /* 用负 margin 盖住父容器的边框，让按钮视觉上无缝贴边 */
  margin: -2px -2px -2px 0;
  height: 44px;
  display: flex;
  align-items: center;
  white-space: nowrap;
}

.search-btn:hover {
  background: #c0392b;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  white-space: nowrap;
}

.cart-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 4px;
  transition: background 0.2s;
}

.cart-btn:hover {
  background: #f5f5f5;
}

.cart-badge { display: inline-flex; align-items: center; line-height: 1; }
.cart-badge :deep(.el-badge__content) {
  background: #e1251b;
}

/* 快捷链接栏 */
.quick-links-bar {
  background: white;
  border-bottom: 1px solid #f0f0f0;
  padding: 6px 0;
}

.quick-links-bar .nav-inner {
  display: flex;
  gap: 0;
  flex-wrap: wrap;
}

.quick-link {
  font-size: 12px;
  color: #555;
  padding: 2px 12px;
  cursor: pointer;
  border-right: 1px solid #e8e8e8;
  white-space: nowrap;
  transition: color 0.2s;
}

.quick-link:hover {
  color: #e1251b;
}

.quick-link:last-child {
  border-right: none;
}

/* 分类图标导航 */
.category-icon-nav {
  background: white;
  margin-bottom: 4px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.category-icon-nav .nav-inner {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 4px;
}

.icon-cat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 6px 10px;
  cursor: pointer;
  border-radius: 6px;
  transition: background 0.2s;
  min-width: 70px;
}

.icon-cat-item:hover {
  background: #fff0f0;
}

.icon-cat-icon {
  font-size: 22px;
}

.icon-cat-name {
  font-size: 12px;
  color: #333;
  white-space: nowrap;
}

/* 主内容三栏 */
.main-content {
  display: flex;
  gap: 8px;
  padding-top: 8px;
  padding-bottom: 8px;
  align-items: flex-start;
}

/* 左侧分类 */
.left-sidebar {
  width: 190px;
  flex-shrink: 0;
  background: white;
  border-radius: 4px;
  padding: 4px 0;
  position: relative;
}

.cat-item {
  display: flex;
  flex-direction: column;
  padding: 8px 12px;
  cursor: pointer;
  position: relative;
  transition: background 0.15s;
}

.cat-item:hover {
  background: #fff0f0;
}

.cat-name {
  font-size: 13px;
  color: #333;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.cat-sub {
  font-size: 11px;
  color: #999;
  margin-top: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sub-panel {
  position: absolute;
  left: 190px;
  top: 0;
  width: 280px;
  background: white;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  padding: 12px;
  box-shadow: 4px 0 12px rgba(0, 0, 0, 0.1);
  z-index: 50;
}

.sub-panel-title {
  font-size: 13px;
  font-weight: bold;
  color: #e1251b;
  margin-bottom: 8px;
  padding-bottom: 6px;
  border-bottom: 1px solid #f0f0f0;
}

.sub-panel-items {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.sub-panel-item {
  font-size: 12px;
  color: #555;
  padding: 3px 10px;
  background: #f5f5f5;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.sub-panel-item:hover {
  background: #e1251b;
  color: white;
}

/* 中间内容 */
.center-content {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.main-carousel {
  border-radius: 8px;
  overflow: hidden;
}

.carousel-slide {
  height: 320px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 40px 60px;
}

.slide-content {
  color: white;
}

.slide-tag {
  display: inline-block;
  background: rgba(255, 255, 255, 0.3);
  color: white;
  font-size: 12px;
  padding: 3px 12px;
  border-radius: 12px;
  margin-bottom: 12px;
  border: 1px solid rgba(255, 255, 255, 0.6);
}

.slide-title {
  font-size: 32px;
  font-weight: 900;
  margin-bottom: 10px;
  line-height: 1.2;
}

.slide-desc {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 20px;
}

.slide-btn {
  font-size: 14px;
  font-weight: bold;
}

.slide-image {
  font-size: 100px;
  opacity: 0.9;
}

/* 爆款区 */
.hot-section {
  background: white;
  border-radius: 8px;
  padding: 12px;
}

.hot-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.hot-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.hot-more {
  font-size: 12px;
  color: #e1251b;
  cursor: pointer;
}

.hot-products {
  display: flex;
  gap: 10px;
  overflow-x: auto;
}

.hot-product-card {
  flex: 1;
  min-width: 100px;
  background: #fff8f8;
  border-radius: 6px;
  padding: 10px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
  border: 1px solid #f5e6e6;
}

.hot-product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(225, 37, 27, 0.15);
}

.hot-product-image {
  font-size: 36px;
  margin-bottom: 6px;
}

.hot-product-name {
  font-size: 11px;
  color: #333;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hot-product-price {
  font-size: 14px;
  color: #e1251b;
  font-weight: bold;
}

.hot-product-original {
  font-size: 11px;
  color: #aaa;
  text-decoration: line-through;
}

/* 右侧面板 */
.right-panel {
  width: 190px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.user-card {
  background: white;
  border-radius: 8px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  font-size: 40px;
}

.user-info-text {
  text-align: center;
}

.user-greeting {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
}

.user-btns {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.user-stats {
  background: white;
  border-radius: 8px;
  padding: 10px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4px;
}

.stat-item {
  text-align: center;
  padding: 6px 4px;
  cursor: pointer;
  border-radius: 4px;
  transition: background 0.2s;
}

.stat-item:hover {
  background: #f5f5f5;
}

.stat-num {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 11px;
  color: #999;
}

.quick-entry-title {
  font-size: 13px;
  font-weight: bold;
  color: #333;
  padding: 6px 0 4px 0;
  border-bottom: 1px solid #f0f0f0;
}

.quick-entries {
  background: white;
  border-radius: 8px;
  padding: 8px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4px;
}

.quick-entry {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
  padding: 8px 4px;
  cursor: pointer;
  border-radius: 4px;
  transition: background 0.2s;
}

.quick-entry:hover {
  background: #f5f5f5;
}

.entry-icon {
  font-size: 18px;
}

.entry-name {
  font-size: 11px;
  color: #555;
}

.brand-section {
  background: white;
  border-radius: 8px;
  padding: 10px;
}

.brand-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4px;
  margin-top: 8px;
}

.brand-item {
  text-align: center;
  padding: 6px;
  background: #f8f8f8;
  border-radius: 4px;
  font-size: 12px;
  color: #333;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
}

.brand-item:hover {
  background: #e1251b;
  color: white;
}

/* 推荐区 */
.recommend-section {
  background: white;
  border-radius: 8px;
  padding: 16px;
  margin: 8px auto;
}

.recommend-tabs :deep(.el-tabs__item) {
  font-size: 14px;
  font-weight: 500;
}

.recommend-tabs :deep(.el-tabs__item.is-active) {
  color: #e1251b;
}

.recommend-tabs :deep(.el-tabs__active-bar) {
  background: #e1251b;
}

/* 商品网格 */
.product-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
  margin-top: 12px;
}

.product-card {
  background: white;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.2s;
}

.product-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
  border-color: #e1251b;
}

.product-image-wrap {
  position: relative;
  background: #f9f9f9;
  height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-image {
  font-size: 70px;
}

.product-tag {
  position: absolute;
  top: 8px;
  left: 8px;
  font-size: 11px;
  padding: 2px 7px;
  border-radius: 10px;
  font-weight: 500;
}

.product-tag.danger {
  background: #ffe4e3;
  color: #e1251b;
}

.product-tag.success {
  background: #e8f8e9;
  color: #27ae60;
}

.product-tag.warning {
  background: #fff8e1;
  color: #e67e22;
}

.product-info {
  padding: 10px;
}

.product-name {
  font-size: 13px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.4;
  min-height: 36px;
}

.product-desc {
  font-size: 11px;
  color: #999;
  margin-top: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: 8px;
}

.product-price {
  color: #e1251b;
  line-height: 1;
}

.price-symbol {
  font-size: 12px;
  font-weight: bold;
}

.price-value {
  font-size: 18px;
  font-weight: bold;
}

.price-original {
  font-size: 11px;
  color: #bbb;
  text-decoration: line-through;
  margin-left: 4px;
}

.product-sales {
  font-size: 11px;
  color: #999;
}

.add-cart-btn {
  width: 100%;
  margin-top: 8px;
}

.load-more {
  text-align: center;
  padding: 24px 0 8px;
}

/* Footer */
.footer {
  background: #333;
  color: #ccc;
  margin-top: 16px;
  padding: 24px 0 12px;
}

.footer-promises {
  display: flex;
  justify-content: space-between;
  padding-bottom: 20px;
  border-bottom: 1px solid #444;
}

.promise-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.promise-icon {
  font-size: 24px;
}

.promise-title {
  font-size: 14px;
  color: #eee;
  font-weight: 500;
}

.promise-desc {
  font-size: 12px;
  color: #999;
}

.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  font-size: 12px;
  color: #888;
}

.footer-links {
  display: flex;
  gap: 16px;
}

.footer-links a {
  color: #888;
  text-decoration: none;
  transition: color 0.2s;
}

.footer-links a:hover {
  color: #fff;
}
</style>
