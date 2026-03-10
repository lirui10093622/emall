<template>
  <div class="detail-page">
    <!-- 顶部导航栏 -->
    <div class="header-nav">
      <div class="nav-inner header-inner">
        <div class="logo" @click="goHome">
          <span class="logo-icon">🛒</span>
          <span class="logo-text">E-Mall</span>
        </div>
        <div class="search-bar">
          <div class="search-category-dropdown" @click="toggleCategoryMenu">
            <span class="search-cat-text">{{ searchCategoryLabel }}</span>
            <el-icon class="search-cat-arrow" :class="{ rotated: categoryMenuVisible }"><ArrowDown /></el-icon>
            <div v-if="categoryMenuVisible" class="category-dropdown-menu" @click.stop>
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
        <div class="user-actions">
          <el-button text class="action-item" @click="goLogin">
            <el-icon><User /></el-icon>登录 / 注册
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

    <!-- 面包屑导航 -->
    <div class="breadcrumb-bar">
      <div class="nav-inner">
        <el-breadcrumb separator="›">
          <el-breadcrumb-item @click="goHome" class="bc-link">首页</el-breadcrumb-item>
          <el-breadcrumb-item class="bc-link">家居家装</el-breadcrumb-item>
          <el-breadcrumb-item class="bc-link">桌子</el-breadcrumb-item>
          <el-breadcrumb-item>{{ product.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="main-wrap nav-inner">
      <!-- 左侧商品信息 -->
      <div class="product-main">
        <!-- 商品图片+购买区 -->
        <div class="product-top">
          <!-- 图片展示区 -->
          <div class="gallery-area">
            <div class="thumbs-col">
              <div
                v-for="(img, idx) in product.images"
                :key="idx"
                class="thumb-item"
                :class="{ active: activeImg === idx }"
                @mouseenter="activeImg = idx"
              >
                <div class="thumb-img">{{ img.emoji }}</div>
              </div>
            </div>
            <div class="main-img-wrap">
              <div class="main-img">{{ product.images[activeImg]?.emoji }}</div>
              <div class="img-badges">
                <span v-if="product.tag" class="img-badge">{{ product.tag }}</span>
              </div>
              <!-- 放大镜提示 -->
              <div class="zoom-hint">移入查看大图</div>
            </div>
          </div>
          <!-- 商品图片下方分享/收藏 -->
          <div class="img-actions">
            <span class="img-action" @click="toggleFav">
              <el-icon :color="isFav ? '#e1251b' : '#999'"><Star /></el-icon>
              {{ isFav ? '已收藏' : '收藏' }} ({{ product.favCount }})
            </span>
            <span class="img-action">
              <el-icon><Share /></el-icon>
              分享
            </span>
            <span class="img-action">
              <el-icon><ChatDotRound /></el-icon>
              对比
            </span>
          </div>
        </div>

        <!-- 右侧购买区 -->
        <div class="buy-area">
          <!-- 店铺信息条 -->
          <div class="shop-bar">
            <span class="shop-tag">自营</span>
            <span class="shop-name">{{ product.shop }}</span>
            <span class="shop-stars">
              <el-icon v-for="i in 5" :key="i" :color="i <= product.shopStars ? '#f90' : '#ddd'" size="13"><Star /></el-icon>
            </span>
            <span class="shop-score">{{ product.shopScore }}</span>
            <span class="shop-link">进店 ›</span>
            <span class="shop-link">关注</span>
          </div>

          <!-- 价格区 -->
          <div class="price-section">
            <div class="price-label">价格</div>
            <div class="price-main">
              <span class="price-sym">¥</span>
              <span class="price-val">{{ product.price }}</span>
              <span class="price-ori">¥{{ product.originalPrice }}</span>
              <span class="price-discount">{{ product.discount }}折</span>
            </div>
            <div class="price-coupons">
              <span class="coupon-tag" v-for="c in product.coupons" :key="c">{{ c }}</span>
            </div>
          </div>

          <!-- 商品标题 -->
          <div class="product-title-area">
            <span v-if="product.badge" class="title-badge">{{ product.badge }}</span>
            <span class="product-title">{{ product.fullName }}</span>
          </div>

          <!-- 特性标签 -->
          <div class="feature-tags">
            <span v-for="f in product.features" :key="f" class="feature-tag">✓ {{ f }}</span>
          </div>

          <!-- 配送区域 -->
          <div class="info-row">
            <div class="info-label">配送至</div>
            <div class="info-val">
              <span class="delivery-addr" @click="changeAddr">北京市 朝阳区 ›</span>
              <span class="delivery-tips">
                <span class="delivery-badge">快递</span>
                免运费 | 预计
                <span class="delivery-date">{{ deliveryDate }}</span>
                送达
              </span>
            </div>
          </div>

          <!-- 服务保障 -->
          <div class="info-row">
            <div class="info-label">服务</div>
            <div class="info-val service-list">
              <span v-for="s in product.services" :key="s.name" class="service-item">
                <el-icon size="12" color="#e1251b"><Check /></el-icon>
                {{ s.name }}
              </span>
            </div>
          </div>

          <!-- 颜色选择 -->
          <div class="info-row">
            <div class="info-label">颜色</div>
            <div class="info-val">
              <div class="sku-options">
                <div
                  v-for="color in product.colors"
                  :key="color.value"
                  class="sku-option"
                  :class="{ active: selectedColor === color.value }"
                  @click="selectedColor = color.value"
                >
                  <div class="color-dot" :style="{ background: color.hex }"></div>
                  {{ color.label }}
                </div>
              </div>
            </div>
          </div>

          <!-- 尺寸选择 -->
          <div class="info-row">
            <div class="info-label">尺寸</div>
            <div class="info-val">
              <div class="sku-options">
                <div
                  v-for="size in product.sizes"
                  :key="size"
                  class="sku-option"
                  :class="{ active: selectedSize === size }"
                  @click="selectedSize = size"
                >{{ size }}</div>
              </div>
            </div>
          </div>

          <!-- 数量 -->
          <div class="info-row">
            <div class="info-label">数量</div>
            <div class="info-val qty-row">
              <div class="qty-ctrl">
                <button class="qty-btn" @click="changeQty(-1)">−</button>
                <input class="qty-input" v-model.number="quantity" min="1" max="99" type="number" />
                <button class="qty-btn" @click="changeQty(1)">+</button>
              </div>
              <span class="stock-info">库存充足</span>
            </div>
          </div>

          <!-- 购买按钮 -->
          <div class="btn-group">
            <button class="btn-buy-now" @click="buyNow">立即购买</button>
            <button class="btn-add-cart" @click="addToCart">加入购物车</button>
          </div>

          <!-- 底部保障 -->
          <div class="guarantee-row">
            <div v-for="g in guarantees" :key="g.icon" class="guarantee-item">
              <span class="guarantee-icon">{{ g.icon }}</span>
              <span class="guarantee-text">{{ g.text }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：相关信息栏 -->
      <div class="side-panel">
        <div class="side-card">
          <div class="side-card-title">猜你喜欢</div>
          <div v-for="p in relatedProducts" :key="p.id" class="side-product" @click="goProduct(p.id)">
            <div class="side-product-img">{{ p.emoji }}</div>
            <div class="side-product-info">
              <div class="side-product-name">{{ p.name }}</div>
              <div class="side-product-price">¥{{ p.price }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 详情主体 -->
    <div class="detail-body nav-inner">
      <!-- 左侧详情内容 -->
      <div class="detail-content">
        <!-- Tab 导航 -->
        <div class="detail-tabs">
          <div
            v-for="tab in detailTabs"
            :key="tab.key"
            class="detail-tab"
            :class="{ active: activeTab === tab.key }"
            @click="activeTab = tab.key"
          >{{ tab.label }}</div>
        </div>

        <!-- 商品详情 -->
        <div v-if="activeTab === 'detail'" class="tab-content">
          <!-- 图文详情 -->
          <div class="detail-section">
            <div class="detail-sec-title">商品详情图</div>
            <div class="detail-imgs">
              <div v-for="(img, i) in product.detailImages" :key="i" class="detail-img-block">
                <div class="detail-img-placeholder">{{ img.emoji }}</div>
                <p class="detail-img-caption">{{ img.caption }}</p>
              </div>
            </div>
            <div class="detail-text">
              <p v-for="(p, i) in product.description" :key="i">{{ p }}</p>
            </div>
          </div>
        </div>

        <!-- 规格参数 -->
        <div v-if="activeTab === 'spec'" class="tab-content">
          <div class="spec-section">
            <div class="detail-sec-title">规格参数</div>
            <table class="spec-table">
              <tbody>
                <tr v-for="spec in product.specs" :key="spec.name">
                  <td class="spec-name">{{ spec.name }}</td>
                  <td class="spec-val">{{ spec.value }}</td>
                  <td class="spec-name">{{ spec.name2 }}</td>
                  <td class="spec-val">{{ spec.value2 }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- 包装售后 -->
        <div v-if="activeTab === 'after'" class="tab-content">
          <div class="after-section">
            <div class="detail-sec-title">包装清单</div>
            <ul class="package-list">
              <li v-for="item in product.packageList" :key="item">{{ item }}</li>
            </ul>
            <div class="detail-sec-title" style="margin-top:20px">售后政策</div>
            <div class="after-policy">
              <div v-for="p in afterPolicies" :key="p.title" class="policy-item">
                <span class="policy-icon">{{ p.icon }}</span>
                <div>
                  <div class="policy-title">{{ p.title }}</div>
                  <div class="policy-desc">{{ p.desc }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 商品评价 -->
        <div v-if="activeTab === 'review'" class="tab-content">
          <div class="review-section">
            <div class="detail-sec-title">
              商品评价
              <span class="review-count">（{{ product.reviewCount }}条）</span>
            </div>
            <!-- 评分概览 -->
            <div class="review-overview">
              <div class="review-score-big">
                <span class="score-num">{{ product.rating }}</span>
                <span class="score-label">综合评分</span>
                <div class="star-row">
                  <el-icon v-for="i in 5" :key="i" :color="i <= Math.round(product.rating) ? '#f90' : '#ddd'" size="18"><Star /></el-icon>
                </div>
              </div>
              <div class="review-bars">
                <div v-for="bar in ratingBars" :key="bar.label" class="rating-bar-row">
                  <span class="rating-bar-label">{{ bar.label }}</span>
                  <div class="rating-bar-bg">
                    <div class="rating-bar-fill" :style="{ width: bar.pct + '%' }"></div>
                  </div>
                  <span class="rating-bar-pct">{{ bar.pct }}%</span>
                </div>
              </div>
              <div class="review-tags">
                <span
                  v-for="tag in reviewTags"
                  :key="tag.label"
                  class="review-tag"
                  :class="{ active: activeReviewTag === tag.label }"
                  @click="activeReviewTag = tag.label"
                >{{ tag.label }}（{{ tag.count }}）</span>
              </div>
            </div>
            <!-- 评价列表 -->
            <div class="review-list">
              <div v-for="review in displayedReviews" :key="review.id" class="review-item">
                <div class="review-user">
                  <span class="review-avatar">{{ review.avatar }}</span>
                  <div class="review-user-info">
                    <span class="review-username">{{ review.username }}</span>
                    <div class="review-stars">
                      <el-icon v-for="i in 5" :key="i" :color="i <= review.rating ? '#f90' : '#ddd'" size="13"><Star /></el-icon>
                    </div>
                  </div>
                  <span class="review-date">{{ review.date }}</span>
                </div>
                <div class="review-sku-tag">颜色：{{ review.color }} | 尺寸：{{ review.size }}</div>
                <div class="review-text">{{ review.content }}</div>
                <div class="review-imgs" v-if="review.imgs">
                  <div v-for="(img, i) in review.imgs" :key="i" class="review-img">{{ img }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧侧边栏：店铺+规格 -->
      <div class="detail-side">
        <!-- 店铺信息 -->
        <div class="shop-card">
          <div class="shop-card-header">
            <span class="shop-card-logo">🏪</span>
            <div class="shop-card-info">
              <div class="shop-card-name">{{ product.shop }}</div>
              <div class="shop-card-level">金牌店铺 · 自营</div>
            </div>
            <button class="shop-follow-btn">+ 关注</button>
          </div>
          <div class="shop-metrics">
            <div v-for="m in shopMetrics" :key="m.label" class="shop-metric">
              <div class="shop-metric-val" :class="m.color">{{ m.value }}</div>
              <div class="shop-metric-label">{{ m.label }}</div>
            </div>
          </div>
          <button class="btn-enter-shop">进入店铺</button>
        </div>
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
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, ShoppingCart, ArrowDown, Camera, Star, Share, ChatDotRound, Check } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// --- 头部搜索 ---
const searchKeyword = ref('')
const searchCategory = ref('product')
const searchCategoryLabel = ref('商品')
const categoryMenuVisible = ref(false)
const cartCount = ref(3)
const searchTypes = [
  { value: 'product', label: '商品' },
  { value: 'shop', label: '店铺' },
]
const quickLinks = [
  { id: 1, name: '优惠券' }, { id: 2, name: '价值惠' }, { id: 3, name: '新品发布' },
  { id: 4, name: '品牌特卖' }, { id: 5, name: '限时秒杀' }, { id: 6, name: '每日特价' },
  { id: 7, name: '拼团活动' }, { id: 8, name: '积分商城' },
]
const toggleCategoryMenu = () => { categoryMenuVisible.value = !categoryMenuVisible.value }
const selectCategory = (value: string, label: string) => {
  searchCategory.value = value
  searchCategoryLabel.value = label
  categoryMenuVisible.value = false
}
const handleSearch = () => {
  const kw = searchKeyword.value.trim()
  if (!kw) { ElMessage.warning('请输入搜索关键词'); return }
  router.push({ path: '/search', query: { keyword: kw } })
}
const goHome = () => router.push('/')
const goLogin = () => router.push('/login')
const handleCart = () => router.push('/cart')

// --- 商品数据 ---
const product = ref({
  id: route.params.id || 1,
  name: '实木书桌',
  fullName: '【多功能可折叠】实木书桌电脑桌家用简约现代学习桌写字台办公桌卧室小户型桌子',
  badge: '爆款',
  tag: '热卖',
  shop: '木艺家居旗舰店',
  shopStars: 5,
  shopScore: '4.9',
  price: '1299.00',
  originalPrice: '2199.00',
  discount: '5.9',
  favCount: 3241,
  rating: 4.8,
  reviewCount: 12856,
  coupons: ['满2000减200', '领券95折', '新人优惠券'],
  features: ['多功能可折叠', '实木材质', '环保无异味', '简约北欧风', '承重稳固'],
  services: [
    { name: '正品保障' }, { name: '7天无理由退换' }, { name: '上门安装' },
    { name: '以旧换新' }, { name: '极速达' },
  ],
  colors: [
    { value: 'natural', label: '原木色', hex: '#c8a97e' },
    { value: 'white', label: '奶白色', hex: '#f5f5f0' },
    { value: 'walnut', label: '胡桃色', hex: '#5c3a1e' },
    { value: 'black', label: '碳黑色', hex: '#2c2c2c' },
  ],
  sizes: ['80cm桌面', '100cm桌面', '120cm桌面', '140cm桌面'],
  images: [
    { emoji: '🪑' }, { emoji: '🖼️' }, { emoji: '📐' }, { emoji: '🔍' }, { emoji: '✨' },
  ],
  detailImages: [
    { emoji: '🛋️', caption: '整体效果展示，北欧简约风格' },
    { emoji: '🪵', caption: '实木材质特写，纹理自然清晰' },
    { emoji: '📏', caption: '精确尺寸标注，满足不同空间需求' },
    { emoji: '🔩', caption: '五金配件展示，耐用稳固' },
  ],
  description: [
    '本款书桌采用优质橡木实木材料，经过严格烘干处理，确保产品稳定不变形。',
    '桌面经过多道打磨工序，手感细腻光滑，整体造型简约大方，符合北欧风格审美。',
    '可折叠设计方便移动和存储，适合小户型使用，充分节省空间。',
    '独特的隐藏式走线孔设计，让桌面更加整洁，提升工作效率。',
    '支持承重100kg，结构稳固，使用寿命长达10年以上。',
  ],
  specs: [
    { name: '品牌', value: '木艺家居', name2: '材质', value2: '橡木实木' },
    { name: '颜色', value: '原木色/奶白/胡桃/碳黑', name2: '风格', value2: '北欧简约' },
    { name: '桌面尺寸', value: '80/100/120/140cm', name2: '桌高', value2: '75cm' },
    { name: '承重', value: '100kg', name2: '表面处理', value2: 'UV哑光漆' },
    { name: '安装方式', value: '上门安装', name2: '适用场景', value2: '卧室/书房/办公' },
    { name: '产品净重', value: '约28kg', name2: '包装重量', value2: '约32kg' },
    { name: '生产地', value: '中国广东', name2: '质保年限', value2: '3年' },
  ],
  packageList: [
    '书桌桌面 × 1', '桌腿组件 × 4', '横梁支撑 × 2', '走线孔盖 × 1',
    '安装螺丝包 × 1', '安装说明书 × 1', '售后卡 × 1',
  ],
})

const activeImg = ref(0)
const isFav = ref(false)
const selectedColor = ref('natural')
const selectedSize = ref('100cm桌面')
const quantity = ref(1)
const activeTab = ref('detail')
const activeReviewTag = ref('全部')

const detailTabs = [
  { key: 'detail', label: '商品详情' },
  { key: 'spec', label: '规格参数' },
  { key: 'after', label: '包装售后' },
  { key: 'review', label: `商品评价（${(12856).toLocaleString()}）` },
]

const deliveryDate = computed(() => {
  const d = new Date()
  d.setDate(d.getDate() + 2)
  return `${d.getMonth() + 1}月${d.getDate()}日`
})

const toggleFav = () => {
  isFav.value = !isFav.value
  ElMessage.success(isFav.value ? '已收藏' : '已取消收藏')
}

const changeQty = (delta: number) => {
  const n = quantity.value + delta
  if (n < 1 || n > 99) return
  quantity.value = n
}

const changeAddr = () => ElMessage.info('地址选择功能开发中')

const buyNow = () => {
  ElMessage.success('跳转结算页...')
}

const addToCart = () => {
  cartCount.value++
  ElMessage.success(`已将「${product.value.name}」加入购物车`)
}

const goProduct = (id: number) => {
  router.push(`/item/${id}`)
}

// 保障
const guarantees = [
  { icon: '✅', text: '正品保障' },
  { icon: '🔄', text: '7天退换' },
  { icon: '🔒', text: '安全支付' },
  { icon: '🚚', text: '极速配送' },
]

// 售后政策
const afterPolicies = [
  { icon: '🔄', title: '7天无理由退换货', desc: '收货后7天内，凭原包装可无理由退换' },
  { icon: '🛡️', title: '质量问题免费换新', desc: '非人为损坏，15天内免费换新' },
  { icon: '🔧', title: '3年免费维修', desc: '质保3年，免费上门维修' },
  { icon: '🚚', title: '上门安装', desc: '免费提供上门安装服务，工期1小时内' },
]

// 店铺指标
const shopMetrics = [
  { label: '店铺评分', value: '4.9', color: 'text-orange' },
  { label: '服务态度', value: '4.8', color: 'text-orange' },
  { label: '物流速度', value: '4.9', color: 'text-orange' },
]

// 评分柱
const ratingBars = [
  { label: '5星', pct: 76 },
  { label: '4星', pct: 16 },
  { label: '3星', pct: 5 },
  { label: '2星', pct: 2 },
  { label: '1星', pct: 1 },
]

// 评价标签
const reviewTags = [
  { label: '全部', count: 12856 },
  { label: '有图', count: 4321 },
  { label: '好评', count: 11201 },
  { label: '中评', count: 903 },
  { label: '差评', count: 412 },
  { label: '实木材质', count: 3210 },
  { label: '颜值高', count: 2987 },
  { label: '安装简单', count: 1876 },
]

// 评价数据
const reviews = [
  {
    id: 1, avatar: '👩', username: '张*梅', rating: 5, date: '2024-03-01',
    color: '原木色', size: '120cm桌面',
    content: '桌子质量非常好，实木的纹理很漂亮，颜色跟图片基本一致，做工精细，榫卯结构很稳固。安装也很方便，工人上门帮忙装好的。放在卧室里非常好看，推荐！',
    imgs: ['📸', '📷', '🖼️'],
  },
  {
    id: 2, avatar: '👨', username: '李*强', rating: 5, date: '2024-02-28',
    color: '胡桃色', size: '100cm桌面',
    content: '买回来用了一个月了，质量很好，没有任何异味，朋友来家里都夸好看。唯一就是等了几天上门安装，但是整体还是很满意。',
    imgs: null,
  },
  {
    id: 3, avatar: '👩', username: '王*', rating: 4, date: '2024-02-25',
    color: '奶白色', size: '80cm桌面',
    content: '整体很满意，桌面很光滑，颜色也很好看。不过尺寸稍微小了一点，下次买大一号的。',
    imgs: ['📸'],
  },
  {
    id: 4, avatar: '👦', username: '赵*博', rating: 5, date: '2024-02-20',
    color: '碳黑色', size: '140cm桌面',
    content: '买来做电脑桌用，140cm的面积非常够用，显示器、笔记本、文件一起摆放完全没问题。桌子很稳，敲键盘完全不抖，好评！',
    imgs: ['📸', '📷'],
  },
]

const displayedReviews = computed(() => reviews)

// 相关推荐
const relatedProducts = [
  { id: 101, name: '北欧实木椅子配套款', price: '399', emoji: '🪑' },
  { id: 102, name: '桌面收纳架储物神器', price: '89', emoji: '📚' },
  { id: 103, name: '书房绿植装饰花盆', price: '49', emoji: '🌿' },
  { id: 104, name: '护眼台灯学习专用', price: '159', emoji: '💡' },
  { id: 105, name: '电脑桌椅套装组合', price: '1599', emoji: '🖥️' },
]

// 底部承诺
const promises = [
  { id: 1, icon: '🚚', title: '极速配送', desc: '次日达 / 小时购' },
  { id: 2, icon: '✅', title: '正品保障', desc: '假一赔十' },
  { id: 3, icon: '🔄', title: '无忧退换', desc: '7天无理由' },
  { id: 4, icon: '🔒', title: '安全支付', desc: '多重加密保护' },
  { id: 5, icon: '🎁', title: '会员特权', desc: '专属优惠折扣' },
]
</script>

<style scoped>
* { box-sizing: border-box; }

.detail-page {
  background: #f4f4f4;
  min-height: 100vh;
  font-family: 'Microsoft YaHei', sans-serif;
}

.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
}

/* ===== 头部导航 ===== */
.header-nav {
  background: white;
  border-bottom: 2px solid #e1251b;
  padding: 12px 0;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.header-inner {
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
.logo-icon { font-size: 28px; }
.logo-text { font-size: 26px; font-weight: 900; color: #e1251b; letter-spacing: -1px; }

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
.search-category-dropdown:hover .search-cat-text { color: #e1251b; }
.search-cat-text { font-size: 14px; color: #333; }
.search-cat-arrow { font-size: 11px; color: #666; transition: transform 0.2s; }
.search-cat-arrow.rotated { transform: rotate(180deg); }
.category-dropdown-menu {
  position: absolute; top: calc(100% + 4px); left: -2px; width: 80px;
  background: white; border: 1px solid #e4e7ed; border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.12); z-index: 300; overflow: hidden;
}
.category-dropdown-item { padding: 9px 14px; font-size: 13px; color: #333; cursor: pointer; }
.category-dropdown-item:hover { background: #fff0f0; color: #e1251b; }
.category-dropdown-item.active { color: #e1251b; font-weight: 500; background: #fff5f5; }
.search-divider { width: 1px; background: #dcdfe6; margin: 8px 0; flex-shrink: 0; }
.search-input-wrap { flex: 1; display: flex; align-items: center; position: relative; min-width: 0; }
.search-native-input {
  flex: 1; height: 100%; border: none; outline: none;
  padding: 0 40px 0 12px; font-size: 14px; color: #333; background: transparent;
}
.search-native-input::placeholder { color: #bbb; }
.search-camera-icon {
  position: absolute; right: 10px; top: 50%; transform: translateY(-50%);
  color: #bbb; cursor: pointer; display: flex; align-items: center;
}
.search-camera-icon:hover { color: #e1251b; }
.search-btn {
  flex-shrink: 0; border: none; outline: none; background: #e1251b; color: white;
  font-size: 15px; font-weight: 500; padding: 0 26px; cursor: pointer;
  letter-spacing: 2px; margin: -2px -2px -2px 0; height: 44px;
  display: flex; align-items: center; white-space: nowrap;
}
.search-btn:hover { background: #c0392b; }
.user-actions { display: flex; align-items: center; gap: 16px; flex-shrink: 0; }
.action-item { display: flex; align-items: center; gap: 4px; font-size: 14px; color: #333; cursor: pointer; white-space: nowrap; }
.cart-btn { display: flex; align-items: center; gap: 6px; padding: 6px 12px; border-radius: 4px; }
.cart-btn:hover { background: #f5f5f5; }
.cart-badge { display: inline-flex; align-items: center; line-height: 1; }
.cart-badge :deep(.el-badge__content) { background: #e1251b; }

/* 快捷链接栏 */
.quick-links-bar { background: white; border-bottom: 1px solid #f0f0f0; padding: 6px 0; }
.quick-links-bar .nav-inner { display: flex; flex-wrap: wrap; }
.quick-link { font-size: 12px; color: #555; padding: 2px 12px; cursor: pointer; border-right: 1px solid #e8e8e8; white-space: nowrap; }
.quick-link:hover { color: #e1251b; }
.quick-link:last-child { border-right: none; }

/* 面包屑 */
.breadcrumb-bar { background: white; border-bottom: 1px solid #f0f0f0; padding: 8px 0; }
.bc-link { cursor: pointer; }
.bc-link :deep(.el-breadcrumb__inner):hover { color: #e1251b; }

/* ===== 主体布局 ===== */
.main-wrap {
  display: flex;
  gap: 12px;
  padding-top: 12px;
  align-items: flex-start;
}

.product-main {
  flex: 1;
  min-width: 0;
  background: white;
  border-radius: 4px;
  padding: 20px;
  display: flex;
  gap: 24px;
}

/* 图片展示区 */
.gallery-area {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}
.thumbs-col {
  display: flex;
  flex-direction: column;
  gap: 6px;
  width: 60px;
}
.thumb-item {
  width: 60px;
  height: 60px;
  border: 2px solid #e8e8e8;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.2s;
  background: #fafafa;
}
.thumb-item:hover, .thumb-item.active { border-color: #e1251b; }
.thumb-img { font-size: 30px; }
.main-img-wrap {
  width: 360px;
  height: 360px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: #fafafa;
  overflow: hidden;
}
.main-img { font-size: 160px; }
.img-badges { position: absolute; top: 10px; left: 10px; display: flex; gap: 6px; }
.img-badge { background: #e1251b; color: white; font-size: 11px; padding: 2px 8px; border-radius: 10px; }
.zoom-hint {
  position: absolute; bottom: 8px; right: 8px;
  font-size: 11px; color: #999; background: rgba(255,255,255,0.8);
  padding: 3px 8px; border-radius: 10px;
}
.img-actions {
  display: flex;
  gap: 16px;
  margin-top: 10px;
  padding-left: 68px;
}
.img-action {
  display: flex; align-items: center; gap: 4px;
  font-size: 12px; color: #666; cursor: pointer;
}
.img-action:hover { color: #e1251b; }

/* 购买区 */
.buy-area { flex: 1; min-width: 0; display: flex; flex-direction: column; gap: 0; }

.shop-bar {
  display: flex; align-items: center; gap: 8px;
  padding: 8px 12px; background: #f9f0e6;
  border-radius: 4px; margin-bottom: 14px;
  font-size: 13px;
}
.shop-tag {
  background: #e1251b; color: white; font-size: 11px;
  padding: 2px 6px; border-radius: 2px; font-weight: bold;
}
.shop-name { font-weight: 500; color: #333; }
.shop-stars { display: flex; align-items: center; gap: 1px; }
.shop-score { color: #f90; font-weight: bold; font-size: 12px; }
.shop-link { color: #e1251b; cursor: pointer; font-size: 12px; }
.shop-link:hover { text-decoration: underline; }

/* 价格区 */
.price-section {
  background: #f9f0e6;
  padding: 14px 16px;
  border-radius: 4px;
  margin-bottom: 14px;
}
.price-label { font-size: 12px; color: #999; margin-bottom: 6px; }
.price-main { display: flex; align-items: baseline; gap: 8px; }
.price-sym { font-size: 18px; color: #e1251b; font-weight: bold; }
.price-val { font-size: 36px; color: #e1251b; font-weight: 900; line-height: 1; }
.price-ori { font-size: 14px; color: #999; text-decoration: line-through; }
.price-discount {
  background: #e1251b; color: white; font-size: 12px; font-weight: bold;
  padding: 2px 8px; border-radius: 10px;
}
.price-coupons { display: flex; gap: 6px; margin-top: 10px; flex-wrap: wrap; }
.coupon-tag {
  font-size: 11px; color: #e1251b; border: 1px solid #e1251b;
  padding: 2px 8px; border-radius: 2px; cursor: pointer;
}
.coupon-tag:hover { background: #fff0f0; }

/* 商品标题 */
.product-title-area { margin-bottom: 10px; }
.title-badge {
  background: linear-gradient(135deg, #e1251b, #ff6b35); color: white;
  font-size: 12px; padding: 2px 8px; border-radius: 2px;
  margin-right: 6px; font-weight: bold; display: inline-block;
}
.product-title { font-size: 16px; font-weight: 500; color: #333; line-height: 1.5; }

/* 特性标签 */
.feature-tags { display: flex; flex-wrap: wrap; gap: 6px; margin-bottom: 14px; }
.feature-tag {
  font-size: 12px; color: #666;
  background: #f5f5f5; padding: 4px 10px; border-radius: 12px;
}

/* 信息行 */
.info-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 13px;
}
.info-label { color: #999; flex-shrink: 0; width: 42px; padding-top: 2px; }
.info-val { flex: 1; color: #333; }

.delivery-addr { color: #e1251b; cursor: pointer; font-size: 13px; }
.delivery-addr:hover { text-decoration: underline; }
.delivery-tips { color: #666; font-size: 12px; margin-top: 4px; display: block; }
.delivery-badge {
  display: inline-block; background: #e1251b; color: white;
  font-size: 11px; padding: 1px 5px; border-radius: 2px; margin-right: 4px;
}
.delivery-date { color: #e1251b; font-weight: bold; }

.service-list { display: flex; flex-wrap: wrap; gap: 8px; }
.service-item { display: flex; align-items: center; gap: 3px; font-size: 12px; color: #555; }

/* SKU 选项 */
.sku-options { display: flex; flex-wrap: wrap; gap: 8px; }
.sku-option {
  display: flex; align-items: center; gap: 6px;
  padding: 6px 12px; border: 1px solid #e0e0e0; border-radius: 4px;
  cursor: pointer; font-size: 13px; color: #333;
  transition: all 0.2s;
}
.sku-option:hover { border-color: #e1251b; color: #e1251b; }
.sku-option.active { border-color: #e1251b; color: #e1251b; background: #fff5f5; }
.color-dot { width: 14px; height: 14px; border-radius: 50%; border: 1px solid #e0e0e0; }

/* 数量 */
.qty-row { display: flex; align-items: center; gap: 12px; }
.qty-ctrl { display: flex; align-items: center; border: 1px solid #e0e0e0; border-radius: 4px; overflow: hidden; }
.qty-btn {
  width: 32px; height: 32px; border: none; background: #f5f5f5;
  font-size: 18px; cursor: pointer; color: #333; display: flex; align-items: center; justify-content: center;
}
.qty-btn:hover { background: #e1251b; color: white; }
.qty-input {
  width: 48px; height: 32px; border: none; border-left: 1px solid #e0e0e0;
  border-right: 1px solid #e0e0e0; text-align: center; font-size: 14px;
  outline: none;
}
.qty-input::-webkit-inner-spin-button, .qty-input::-webkit-outer-spin-button { -webkit-appearance: none; }
.stock-info { font-size: 12px; color: #27ae60; }

/* 购买按钮 */
.btn-group { display: flex; gap: 12px; margin-top: 20px; margin-bottom: 14px; }
.btn-buy-now, .btn-add-cart {
  flex: 1; height: 46px; border: none; border-radius: 4px;
  font-size: 16px; font-weight: 500; cursor: pointer; transition: all 0.2s;
}
.btn-buy-now { background: #e1251b; color: white; }
.btn-buy-now:hover { background: #c0392b; }
.btn-add-cart { background: #ff6b35; color: white; }
.btn-add-cart:hover { background: #e5572a; }

/* 保障条 */
.guarantee-row {
  display: flex; gap: 0;
  border: 1px solid #f0f0f0; border-radius: 4px;
  padding: 10px 0;
}
.guarantee-item {
  flex: 1; display: flex; flex-direction: column; align-items: center; gap: 4px;
  font-size: 12px; color: #666; border-right: 1px solid #f0f0f0; padding: 0 8px;
}
.guarantee-item:last-child { border-right: none; }
.guarantee-icon { font-size: 20px; }

/* 右侧猜你喜欢 */
.side-panel { width: 190px; flex-shrink: 0; }
.side-card {
  background: white; border-radius: 4px; padding: 14px;
}
.side-card-title {
  font-size: 14px; font-weight: bold; color: #333;
  padding-bottom: 10px; border-bottom: 1px solid #f0f0f0; margin-bottom: 10px;
}
.side-product {
  display: flex; gap: 8px; padding: 8px 0;
  border-bottom: 1px solid #f9f9f9; cursor: pointer;
}
.side-product:hover { background: #fff5f5; margin: 0 -14px; padding: 8px 14px; }
.side-product-img { font-size: 36px; flex-shrink: 0; }
.side-product-info { flex: 1; min-width: 0; }
.side-product-name { font-size: 12px; color: #333; line-height: 1.4; overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
.side-product-price { font-size: 14px; color: #e1251b; font-weight: bold; margin-top: 4px; }

/* ===== 详情主体 ===== */
.detail-body {
  display: flex;
  gap: 12px;
  padding: 12px 16px;
  align-items: flex-start;
}

.detail-content {
  flex: 1;
  min-width: 0;
  background: white;
  border-radius: 4px;
}

/* Tab 导航 */
.detail-tabs {
  display: flex;
  border-bottom: 2px solid #f0f0f0;
  background: white;
  position: sticky;
  top: 66px;
  z-index: 50;
}
.detail-tab {
  padding: 14px 24px;
  font-size: 14px;
  color: #555;
  cursor: pointer;
  position: relative;
  transition: color 0.2s;
  white-space: nowrap;
}
.detail-tab:hover { color: #e1251b; }
.detail-tab.active {
  color: #e1251b;
  font-weight: 600;
}
.detail-tab.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 2px;
  background: #e1251b;
}

.tab-content { padding: 24px; }

/* 商品详情 */
.detail-sec-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 16px;
}
.detail-imgs {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}
.detail-img-block { text-align: center; }
.detail-img-placeholder {
  background: #f5f5f5;
  border-radius: 8px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 80px;
  margin-bottom: 8px;
}
.detail-img-caption { font-size: 13px; color: #666; }
.detail-text p {
  font-size: 14px;
  color: #555;
  line-height: 1.8;
  margin-bottom: 10px;
}

/* 规格参数 */
.spec-table { width: 100%; border-collapse: collapse; }
.spec-table tr:nth-child(odd) { background: #fafafa; }
.spec-name {
  width: 20%;
  padding: 10px 14px;
  font-size: 13px;
  color: #999;
  border: 1px solid #f0f0f0;
}
.spec-val {
  width: 30%;
  padding: 10px 14px;
  font-size: 13px;
  color: #333;
  border: 1px solid #f0f0f0;
}

/* 包装售后 */
.package-list {
  list-style: none;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
  margin-bottom: 16px;
}
.package-list li {
  font-size: 13px;
  color: #555;
  padding: 8px 12px;
  background: #f9f9f9;
  border-radius: 4px;
  border-left: 3px solid #e1251b;
}
.after-policy { display: flex; flex-direction: column; gap: 14px; }
.policy-item { display: flex; gap: 12px; align-items: flex-start; }
.policy-icon { font-size: 24px; flex-shrink: 0; }
.policy-title { font-size: 14px; font-weight: 500; color: #333; margin-bottom: 4px; }
.policy-desc { font-size: 13px; color: #666; }

/* 评价 */
.review-count { font-size: 14px; color: #999; font-weight: normal; }
.review-overview {
  display: flex;
  gap: 32px;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 20px;
  align-items: flex-start;
  flex-wrap: wrap;
}
.review-score-big { text-align: center; flex-shrink: 0; }
.score-num { font-size: 48px; font-weight: 900; color: #e1251b; display: block; line-height: 1; }
.score-label { font-size: 12px; color: #999; margin: 4px 0 8px; display: block; }
.star-row { display: flex; gap: 2px; justify-content: center; }
.review-bars { flex: 1; min-width: 160px; display: flex; flex-direction: column; gap: 6px; }
.rating-bar-row { display: flex; align-items: center; gap: 8px; font-size: 12px; }
.rating-bar-label { color: #999; width: 28px; flex-shrink: 0; }
.rating-bar-bg { flex: 1; height: 8px; background: #f0f0f0; border-radius: 4px; overflow: hidden; }
.rating-bar-fill { height: 100%; background: linear-gradient(90deg, #f90, #ffcc00); border-radius: 4px; }
.rating-bar-pct { color: #999; width: 36px; text-align: right; }
.review-tags { display: flex; flex-wrap: wrap; gap: 8px; flex: 1; min-width: 200px; align-content: flex-start; }
.review-tag {
  font-size: 12px; color: #666;
  border: 1px solid #e0e0e0; padding: 5px 14px; border-radius: 16px;
  cursor: pointer; transition: all 0.2s;
}
.review-tag:hover, .review-tag.active { border-color: #e1251b; color: #e1251b; background: #fff5f5; }
.review-list { display: flex; flex-direction: column; gap: 0; }
.review-item { padding: 20px 0; border-bottom: 1px solid #f5f5f5; }
.review-user { display: flex; align-items: center; gap: 10px; margin-bottom: 8px; }
.review-avatar { font-size: 28px; }
.review-user-info { flex: 1; }
.review-username { font-size: 13px; color: #333; font-weight: 500; }
.review-stars { display: flex; gap: 2px; margin-top: 2px; }
.review-date { font-size: 12px; color: #bbb; }
.review-sku-tag { font-size: 12px; color: #bbb; margin-bottom: 8px; }
.review-text { font-size: 14px; color: #444; line-height: 1.7; }
.review-imgs { display: flex; gap: 8px; margin-top: 10px; }
.review-img {
  width: 80px; height: 80px; background: #f5f5f5; border-radius: 4px;
  display: flex; align-items: center; justify-content: center; font-size: 30px; cursor: pointer;
}

/* 店铺卡片（详情侧边） */
.detail-side { width: 190px; flex-shrink: 0; }
.shop-card {
  background: white; border-radius: 4px; padding: 14px;
  position: sticky; top: 80px;
}
.shop-card-header { display: flex; gap: 10px; align-items: center; margin-bottom: 12px; }
.shop-card-logo { font-size: 32px; }
.shop-card-info { flex: 1; min-width: 0; }
.shop-card-name { font-size: 13px; font-weight: 600; color: #333; }
.shop-card-level { font-size: 11px; color: #f90; margin-top: 2px; }
.shop-follow-btn {
  background: white; color: #e1251b; border: 1px solid #e1251b;
  font-size: 12px; padding: 4px 8px; border-radius: 4px; cursor: pointer;
  flex-shrink: 0; white-space: nowrap;
}
.shop-follow-btn:hover { background: #fff5f5; }
.shop-metrics { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; margin-bottom: 12px; }
.shop-metric { text-align: center; }
.shop-metric-val { font-size: 16px; font-weight: bold; color: #f90; }
.shop-metric-label { font-size: 11px; color: #999; }
.text-orange { color: #f90; }
.btn-enter-shop {
  width: 100%; background: white; color: #e1251b; border: 1px solid #e1251b;
  padding: 8px; border-radius: 4px; cursor: pointer; font-size: 13px;
}
.btn-enter-shop:hover { background: #fff5f5; }

/* Footer */
.footer { background: #333; color: #ccc; margin-top: 16px; padding: 24px 0 12px; }
.footer-promises { display: flex; justify-content: space-between; padding-bottom: 20px; border-bottom: 1px solid #444; }
.promise-item { display: flex; align-items: center; gap: 10px; }
.promise-icon { font-size: 24px; }
.promise-title { font-size: 14px; color: #eee; font-weight: 500; }
.promise-desc { font-size: 12px; color: #999; }
.footer-bottom { display: flex; justify-content: space-between; align-items: center; padding-top: 12px; font-size: 12px; color: #888; }
.footer-links { display: flex; gap: 16px; }
.footer-links a { color: #888; text-decoration: none; }
.footer-links a:hover { color: #fff; }
</style>
