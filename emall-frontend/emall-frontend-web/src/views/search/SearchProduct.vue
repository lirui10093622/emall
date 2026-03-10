<template>
  <div class="search-page" @click="closeDropdowns">
    <!-- 顶部搜索导航 -->
    <header class="search-header">
      <div class="nav-inner header-inner">
        <div class="logo" @click="goHome">
          <span class="logo-icon">🛒</span>
          <span class="logo-text">E-Mall</span>
        </div>

        <div class="search-bar">
          <div class="search-category-dropdown" @click.stop="toggleCategoryMenu">
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
    </header>

    <!-- 快捷链接栏 -->
    <div class="quick-links-bar">
      <div class="nav-inner">
        <span v-for="link in quickLinks" :key="link.id" class="quick-link">{{ link.name }}</span>
      </div>
    </div>

    <!-- 场景 / 功能筛选 -->
    <section class="scene-bar">
      <div class="nav-inner">
        <div class="scene-row scene-row-with-cards">
          <span class="scene-label">场景</span>
          <div class="scene-scroll">
            <button class="scene-arrow" @click="scrollScenes(-1)">&lt;</button>
            <div class="scene-scroll-inner" ref="sceneScrollRef">
              <div
                v-for="scene in scenes"
                :key="scene.id"
                class="scene-card"
                :class="{ active: activeSceneId === scene.id }"
                @click="toggleScene(scene.id)"
              >
                <div class="scene-card-thumb">{{ scene.emoji }}</div>
                <div class="scene-card-name">{{ scene.label }}</div>
              </div>
            </div>
            <button class="scene-arrow" @click="scrollScenes(1)">&gt;</button>
          </div>
        </div>

        <div class="scene-row">
          <span class="scene-label">功能</span>
          <div class="scene-tags">
            <span
              v-for="func in functions"
              :key="func.id"
              class="scene-tag"
              :class="{ active: activeFunctionId === func.id }"
              @click="toggleFunction(func.id)"
            >{{ func.label }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 主结果区 -->
    <main class="search-main">
      <div class="nav-inner">
        <!-- 面包屑 + 结果摘要 -->
        <div class="search-breadcrumb">
          <div class="breadcrumb-left">
            <span class="crumb" @click="goHome">首页</span>
            <span class="crumb-sep">›</span>
            <span class="crumb" @click="goHome">全部商品</span>
            <span class="crumb-sep">›</span>
            <span class="crumb current">{{ displayKeyword }}</span>
          </div>
          <div class="breadcrumb-right">
            找到 <span class="result-keyword">{{ filteredProducts.length }}</span> 件
            <span class="result-kw-text">"{{ displayKeyword }}"</span> 相关商品
          </div>
        </div>

        <!-- 顶部标签 -->
        <div class="top-tabs">
          <span
            v-for="tab in topTabs"
            :key="tab.value"
            class="top-tab"
            :class="{ active: activeTopTab === tab.value }"
            @click="activeTopTab = tab.value"
          >{{ tab.label }}</span>
        </div>

        <!-- 横向属性筛选 -->
        <div class="filter-strip">
          <div class="filter-line" v-for="section in filters" :key="section.id">
            <span class="filter-line-label">{{ section.label }}</span>
            <div class="filter-line-options">
              <span
                v-for="tag in section.options"
                :key="tag"
                class="filter-option"
                :class="{ active: isFilterActive(section.id, tag) }"
                @click="toggleFilter(section.id, tag)"
              >{{ tag }}</span>
            </div>
            <span
              v-if="activeFilters[section.id]?.size"
              class="filter-clear-btn"
              @click="clearFilter(section.id)"
            >清除</span>
          </div>
        </div>

        <!-- 已选筛选标签 -->
        <div class="active-filter-bar" v-if="hasActiveFilters">
          <span class="active-filter-label">已选：</span>
          <span
            v-for="tag in allActiveFilterTags"
            :key="tag.key"
            class="active-filter-tag"
            @click="toggleFilter(tag.sectionId, tag.value)"
          >{{ tag.value }} ×</span>
          <span class="clear-all-btn" @click="clearAllFilters">清除全部</span>
        </div>

        <!-- 排序条 + 视图切换 -->
        <div class="sort-bar">
          <div class="sort-options">
            <span
              v-for="opt in sortOptions"
              :key="opt.value"
              class="sort-option"
              :class="{ active: activeSort === opt.value }"
              @click="changeSort(opt.value)"
            >
              {{ opt.label }}
              <template v-if="opt.value === 'price'">
                <span class="price-arrow" :class="priceOrder === 'asc' ? 'up' : 'down'">
                  {{ activeSort === 'price' ? (priceOrder === 'asc' ? '↑' : '↓') : '↕' }}
                </span>
              </template>
            </span>
          </div>
          <div class="sort-right">
            <label class="sort-extra-check">
              <input type="checkbox" v-model="onlyInStock" />
              <span>仅看有货</span>
            </label>
            <label class="sort-extra-check">
              <input type="checkbox" v-model="onlySelected" />
              <span>精选商品</span>
            </label>
            <!-- 视图切换 -->
            <div class="view-toggle">
              <span class="view-btn" :class="{ active: viewMode === 'grid' }" @click="viewMode = 'grid'" title="网格视图">⊞</span>
              <span class="view-btn" :class="{ active: viewMode === 'list' }" @click="viewMode = 'list'" title="列表视图">☰</span>
            </div>
          </div>
        </div>

        <!-- 商品区域 -->
        <section class="result-panel">
          <!-- 网格视图 -->
          <div v-if="viewMode === 'grid'" class="product-grid">
            <div
              v-for="item in pagedProducts"
              :key="item.id"
              class="product-card"
              @click="goDetail(item.id)"
            >
              <div class="product-img-wrap">
                <div class="product-img-inner">{{ item.emoji }}</div>
                <div class="product-badges">
                  <span v-for="badge in item.badges.slice(0, 2)" :key="badge" class="product-badge" :class="getBadgeClass(badge)">
                    {{ badge }}
                  </span>
                </div>
                <!-- hover 收藏 -->
                <span class="fav-btn" @click.stop="toggleFav(item.id)" :class="{ active: favIds.has(item.id) }">
                  {{ favIds.has(item.id) ? '❤️' : '🤍' }}
                </span>
              </div>

              <div class="product-body">
                <div class="product-price-row">
                  <span class="price-sym">¥</span>
                  <span class="price-num">{{ item.price.toFixed(0) }}</span>
                  <span v-if="item.originPrice" class="price-ori">¥{{ item.originPrice.toFixed(0) }}</span>
                  <span v-if="item.originPrice" class="price-discount">
                    {{ Math.round(item.price / item.originPrice * 10) }}折
                  </span>
                </div>

                <div class="product-title" v-html="highlightKeyword(item.title)"></div>

                <div class="product-desc">{{ item.desc }}</div>

                <div class="product-rating">
                  <span class="star-row">
                    <span v-for="i in 5" :key="i" class="star" :class="{ filled: i <= item.rating }">★</span>
                  </span>
                  <span class="rating-count">{{ item.comments }}条评价</span>
                </div>

                <div class="product-shop">
                  <span class="shop-dot" :class="{ self: item.isSelf }">{{ item.isSelf ? '自营' : '第三方' }}</span>
                  <span class="shop-name">{{ item.shop }}</span>
                </div>

                <button class="add-cart-btn" @click.stop="addToCart(item)">
                  + 加入购物车
                </button>
              </div>
            </div>
          </div>

          <!-- 列表视图 -->
          <div v-else class="product-list-view">
            <div
              v-for="item in pagedProducts"
              :key="item.id"
              class="product-list-card"
              @click="goDetail(item.id)"
            >
              <div class="list-img-wrap">
                <div class="list-img">{{ item.emoji }}</div>
                <span class="list-fav-btn" @click.stop="toggleFav(item.id)" :class="{ active: favIds.has(item.id) }">
                  {{ favIds.has(item.id) ? '❤️' : '🤍' }}
                </span>
              </div>
              <div class="list-info">
                <div class="list-title" v-html="highlightKeyword(item.title)"></div>
                <div class="list-desc">{{ item.desc }}</div>
                <div class="list-badges">
                  <span v-for="badge in item.badges" :key="badge" class="product-badge" :class="getBadgeClass(badge)">{{ badge }}</span>
                </div>
                <div class="list-rating">
                  <span class="star-row">
                    <span v-for="i in 5" :key="i" class="star" :class="{ filled: i <= item.rating }">★</span>
                  </span>
                  <span class="rating-count">{{ item.comments }}条评价</span>
                </div>
                <div class="list-shop">
                  <span class="shop-dot" :class="{ self: item.isSelf }">{{ item.isSelf ? '自营' : '第三方' }}</span>
                  {{ item.shop }}
                </div>
              </div>
              <div class="list-price-area">
                <div class="list-price">
                  <span class="price-sym">¥</span>
                  <span class="price-num">{{ item.price.toFixed(0) }}</span>
                </div>
                <div v-if="item.originPrice" class="list-price-ori">¥{{ item.originPrice.toFixed(0) }}</div>
                <div v-if="item.originPrice" class="list-discount">
                  省 ¥{{ (item.originPrice - item.price).toFixed(0) }}
                </div>
                <button class="add-cart-btn list-cart-btn" @click.stop="addToCart(item)">加入购物车</button>
              </div>
            </div>
          </div>

          <!-- 空状态 -->
          <div v-if="filteredProducts.length === 0" class="empty-state">
            <div class="empty-icon">🔍</div>
            <div class="empty-title">没有找到相关商品</div>
            <div class="empty-desc">试试换个关键词或放宽筛选条件</div>
            <el-button type="danger" plain @click="clearAllFilters">清除筛选条件</el-button>
          </div>

          <!-- 分页 -->
          <div v-if="filteredProducts.length > 0" class="pagination-wrap">
            <span class="pagination-total">共 {{ filteredProducts.length }} 件商品</span>
            <el-pagination
              v-model:current-page="currentPage"
              :page-size="pageSize"
              :total="filteredProducts.length"
              :page-sizes="[20, 40, 60]"
              layout="prev, pager, next, sizes, jumper"
              background
              @size-change="onPageSizeChange"
              @current-change="onPageChange"
            />
          </div>
        </section>
      </div>
    </main>

    <!-- Footer -->
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
import { computed, ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowDown, Camera, ShoppingCart, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const searchKeyword = ref((route.query.keyword as string) || '书桌')
const searchCategory = ref('product')
const searchCategoryLabel = ref('商品')
const categoryMenuVisible = ref(false)
const cartCount = ref(3)
const viewMode = ref<'grid' | 'list'>('grid')
const onlyInStock = ref(false)
const onlySelected = ref(false)
const sceneScrollRef = ref<HTMLElement | null>(null)

const searchTypes = [
  { value: 'product', label: '商品' },
  { value: 'shop', label: '店铺' },
]

const quickLinks = [
  { id: 1, name: '优惠券' }, { id: 2, name: '价值惠' }, { id: 3, name: '新品发布' },
  { id: 4, name: '品牌特卖' }, { id: 5, name: '限时秒杀' }, { id: 6, name: '每日特价' },
  { id: 7, name: '拼团活动' }, { id: 8, name: '积分商城' },
]

const closeDropdowns = () => { categoryMenuVisible.value = false }
const toggleCategoryMenu = () => { categoryMenuVisible.value = !categoryMenuVisible.value }
const selectCategory = (value: string, label: string) => {
  searchCategory.value = value
  searchCategoryLabel.value = label
  categoryMenuVisible.value = false
}
const handleSearch = () => {
  if (!searchKeyword.value.trim()) { ElMessage.warning('请输入搜索关键词'); return }
  router.push({ path: '/search', query: { keyword: searchKeyword.value } })
}
const goHome = () => router.push('/')
const goLogin = () => router.push('/login')
const handleCart = () => router.push('/cart')
const goDetail = (id: number) => router.push(`/item/${id}`)

const displayKeyword = computed(() => searchKeyword.value || '书桌')

// 高亮关键词
const highlightKeyword = (text: string) => {
  const kw = displayKeyword.value.trim()
  if (!kw) return text
  const escaped = kw.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  return text.replace(new RegExp(escaped, 'gi'), `<em class="kw-highlight">$&</em>`)
}

// 场景 & 功能
interface SimpleTag { id: number; label: string; emoji: string }
const scenes: SimpleTag[] = [
  { id: 1, label: '家庭书房', emoji: '🏠' },
  { id: 2, label: '企业办公', emoji: '🏢' },
  { id: 3, label: '宿舍寝室', emoji: '🛏️' },
  { id: 4, label: '直播桌面', emoji: '🎙️' },
  { id: 5, label: '儿童学习', emoji: '📚' },
]
const functions: Array<{ id: number; label: string }> = [
  { id: 11, label: '升降调节' }, { id: 12, label: '收纳储物' },
  { id: 13, label: '折叠移动' }, { id: 14, label: '带机位架' }, { id: 15, label: '多屏布线' },
]
const activeSceneId = ref<number | null>(null)
const activeFunctionId = ref<number | null>(null)

const toggleScene = (id: number) => { activeSceneId.value = activeSceneId.value === id ? null : id; currentPage.value = 1 }
const toggleFunction = (id: number) => { activeFunctionId.value = activeFunctionId.value === id ? null : id; currentPage.value = 1 }
const scrollScenes = (dir: number) => {
  if (sceneScrollRef.value) sceneScrollRef.value.scrollBy({ left: dir * 200, behavior: 'smooth' })
}

// 顶部 Tab
const topTabs = [
  { value: 'all', label: '全部商品' },
  { value: 'enterprise', label: '企业精选商品' },
  { value: 'promo', label: '活动特惠' },
]
const activeTopTab = ref('enterprise')

// 筛选条
interface FilterSection { id: number; label: string; options: string[] }
const filters: FilterSection[] = [
  { id: 1, label: '品牌', options: ['E-Mall自营', '林氏', '源氏木语', '宜家', '喜临门', '慕斯'] },
  { id: 2, label: '价格', options: ['0-300', '300-800', '800-1500', '1500-3000', '3000以上'] },
  { id: 3, label: '材质', options: ['实木', '板木结合', '玻璃', '金属', '环保板材'] },
  { id: 4, label: '尺寸', options: ['100cm以内', '100-120cm', '120-140cm', '140-160cm', '160cm以上'] },
]
const activeFilters = reactive<Record<number, Set<string>>>({})
const isFilterActive = (sectionId: number, tag: string) => activeFilters[sectionId]?.has(tag) ?? false
const toggleFilter = (sectionId: number, tag: string) => {
  if (!activeFilters[sectionId]) activeFilters[sectionId] = new Set()
  if (activeFilters[sectionId].has(tag)) activeFilters[sectionId].delete(tag)
  else activeFilters[sectionId].add(tag)
  currentPage.value = 1
}
const clearFilter = (sectionId: number) => { activeFilters[sectionId] = new Set(); currentPage.value = 1 }
const clearAllFilters = () => { filters.forEach(s => { activeFilters[s.id] = new Set() }); activeSceneId.value = null; activeFunctionId.value = null; currentPage.value = 1 }
const hasActiveFilters = computed(() => filters.some(s => activeFilters[s.id]?.size) || activeSceneId.value || activeFunctionId.value)
const allActiveFilterTags = computed(() => {
  const tags: Array<{ key: string; sectionId: number; value: string }> = []
  filters.forEach(s => {
    activeFilters[s.id]?.forEach(v => tags.push({ key: `${s.id}-${v}`, sectionId: s.id, value: v }))
  })
  return tags
})

// 排序
const sortOptions = [
  { value: 'comprehensive', label: '综合' },
  { value: 'sale', label: '销量' },
  { value: 'price', label: '价格' },
  { value: 'comments', label: '评论数' },
  { value: 'new', label: '上新' },
]
const activeSort = ref('comprehensive')
const priceOrder = ref<'asc' | 'desc'>('asc')
const changeSort = (value: string) => {
  if (value === 'price' && activeSort.value === 'price') {
    priceOrder.value = priceOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    activeSort.value = value
  }
}

// 分页
const currentPage = ref(1)
const pageSize = ref(20)
const onPageChange = (p: number) => { currentPage.value = p; window.scrollTo({ top: 0, behavior: 'smooth' }) }
const onPageSizeChange = (s: number) => { pageSize.value = s; currentPage.value = 1 }

// 收藏
const favIds = ref(new Set<number>())
const toggleFav = (id: number) => {
  if (favIds.value.has(id)) { favIds.value.delete(id); ElMessage.info('已取消收藏') }
  else { favIds.value.add(id); ElMessage.success('已收藏') }
}

// Badge 样式
const getBadgeClass = (badge: string) => {
  if (['自营', 'E-Mall自营'].some(k => badge.includes(k))) return 'badge-self'
  if (['企采', '企业'].some(k => badge.includes(k))) return 'badge-enterprise'
  if (['新品'].some(k => badge.includes(k))) return 'badge-new'
  if (['热销', '爆款'].some(k => badge.includes(k))) return 'badge-hot'
  return 'badge-default'
}

// 商品数据
interface Product {
  id: number; title: string; desc: string; price: number; originPrice?: number
  comments: string; shop: string; badges: string[]; emoji: string
  sceneIds: number[]; functionIds: number[]; rating: number; isSelf: boolean
}

const baseProducts: Product[] = [
  { id: 1, title: '【企业采购推荐】电动升降电脑桌 人体工学办公桌 家用书桌 120×60cm', desc: '一键升降 | 静音电机 | 记忆高度 | 承重稳固', price: 1299, originPrice: 1699, comments: '3.8万', shop: 'E-Mall自营旗舰店', badges: ['自营', '企采专享', '极速达'], emoji: '🖥️', sceneIds: [2, 1], functionIds: [11, 15], rating: 5, isSelf: true },
  { id: 2, title: '实木书桌 书架一体 家用学习桌 写字台 140×70cm', desc: '多层收纳 | 北欧风格 | 安全圆角设计', price: 899, originPrice: 1299, comments: '1.9万', shop: '原木生活家具专营店', badges: ['热销', '晒单返券'], emoji: '🪑', sceneIds: [1, 5], functionIds: [12], rating: 4, isSelf: false },
  { id: 3, title: '简约电脑办公桌 带主机托 双屏位工作台 160×70cm', desc: '稳固框架 | 线缆管理 | 宽大桌面', price: 759, originPrice: 999, comments: '8600', shop: 'E-Mall企业购自营', badges: ['企业购', '自营', '以旧换新'], emoji: '💼', sceneIds: [2], functionIds: [14, 15], rating: 4, isSelf: true },
  { id: 4, title: '移动折叠电脑桌 小户型学习桌 卧室床边笔记本桌', desc: '免安装 | 可折叠 | 带脚轮易移动', price: 269, comments: '2.3万', shop: '小空间家居专营店', badges: ['热销', '精选'], emoji: '📐', sceneIds: [1, 3], functionIds: [13], rating: 4, isSelf: false },
  { id: 5, title: '儿童学习桌椅套装 护眼台灯 可调节高度', desc: '学习好搭档 | 护脊设计 | 环保材质', price: 1199, originPrice: 1499, comments: '1.2万', shop: '成长伙伴母婴专营店', badges: ['自营', '次日达'], emoji: '📚', sceneIds: [5], functionIds: [11, 12], rating: 5, isSelf: true },
  { id: 6, title: '直播工作台 升降主播桌 带灯架与设备托盘', desc: '专为直播场景打造 | 多设备位 | 稳固不晃', price: 1899, originPrice: 2199, comments: '3200', shop: 'LiveStudio官方旗舰店', badges: ['新品', '企业采购'], emoji: '🎙️', sceneIds: [4], functionIds: [11, 14], rating: 4, isSelf: false },
  { id: 7, title: '升降书桌 双电机静音 升降范围60-125cm 办公写字桌', desc: '双电机升降 | 三档记忆 | 适配多种身高', price: 1699, originPrice: 2099, comments: '9800', shop: 'E-Mall职场装备馆', badges: ['自营', '满减'], emoji: '⬆️', sceneIds: [2, 1], functionIds: [11, 15], rating: 5, isSelf: true },
  { id: 8, title: '原木转角书桌 电脑桌一体 多功能收纳主机位', desc: 'L型桌面 | 转角设计 | 大容量抽屉柜', price: 1399, originPrice: 1899, comments: '6500', shop: '原木格调旗舰店', badges: ['热销', '极速达'], emoji: '🪵', sceneIds: [1, 2], functionIds: [12, 15], rating: 4, isSelf: false },
  { id: 9, title: '极简风电脑桌 办公桌 北欧配色 120×60cm', desc: '钢木结构 | 稳固升级 | 适合出租屋/卧室', price: 499, originPrice: 699, comments: '1.1万', shop: 'SimpleLife家居', badges: ['爆款', '新品'], emoji: '✨', sceneIds: [1, 3], functionIds: [14], rating: 4, isSelf: false },
  { id: 10, title: '加宽电脑工作台 适配双显示器 带键盘托与主机架', desc: '140×70cm加宽桌面 | 线缆收纳槽', price: 829, originPrice: 1099, comments: '7800', shop: 'E-Mall企业购自营', badges: ['企业购', '自营'], emoji: '🖱️', sceneIds: [2], functionIds: [14, 15], rating: 5, isSelf: true },
  { id: 11, title: '白色简约升降学习桌 学生写字桌 家用书桌椅套装', desc: '桌椅可调节 | 附带护眼灯 | 适合中小学生', price: 1359, originPrice: 1799, comments: '5400', shop: '智慧校园装备馆', badges: ['次日达', '学生专享'], emoji: '📖', sceneIds: [5], functionIds: [11, 12], rating: 4, isSelf: false },
  { id: 12, title: '小户型壁挂折叠桌 靠墙写字台 可折叠收纳', desc: '免打孔安装 | 折叠不占地 | 多色可选', price: 219, originPrice: 399, comments: '2.2万', shop: '小家也有大空间', badges: ['热销', '小户型推荐'], emoji: '📌', sceneIds: [1, 3], functionIds: [13], rating: 4, isSelf: false },
  { id: 13, title: 'ins风少女书桌 带书架与氛围灯 卧室学习桌', desc: '自带灯带 | 多层置物架 | 适合宿舍/卧室', price: 759, originPrice: 999, comments: '8600', shop: 'DreamRoom美学馆', badges: ['新品', '搭配推荐'], emoji: '💫', sceneIds: [1, 3], functionIds: [12], rating: 4, isSelf: false },
  { id: 14, title: '电竞电脑桌 电竞桌椅套装 带耳机挂钩与杯架', desc: '战损造型 | RGB灯效 | 大尺寸鼠标垫', price: 1199, originPrice: 1599, comments: '1.5万', shop: 'ProGame电竞旗舰店', badges: ['热销', '新品'], emoji: '🎮', sceneIds: [3, 4], functionIds: [14, 15], rating: 5, isSelf: false },
  { id: 15, title: '带柜子组合书桌 文件柜一体 办公储物桌', desc: '三抽屉储物 | 锁匙抽屉 | 适合企业办公', price: 959, originPrice: 1299, comments: '9300', shop: 'OfficePro办公家具', badges: ['企采专享', '满减'], emoji: '🗄️', sceneIds: [2], functionIds: [12, 15], rating: 4, isSelf: false },
]

const allProducts = ref<Product[]>([
  ...baseProducts,
  ...Array.from({ length: 25 }).map((_, i) => {
    const base = baseProducts[i % baseProducts.length]
    return { ...base, id: baseProducts.length + i + 1, title: `${base.title}（推荐${i + 1}）` }
  }),
])

// 价格段过滤
const parsePriceRange = (tag: string) => {
  if (tag === '0-300') return [0, 300]
  if (tag === '300-800') return [300, 800]
  if (tag === '800-1500') return [800, 1500]
  if (tag === '1500-3000') return [1500, 3000]
  if (tag === '3000以上') return [3000, Infinity]
  return null
}

const filteredProducts = computed<Product[]>(() => {
  let list = allProducts.value.slice()

  // 场景 & 功能
  if (activeSceneId.value) list = list.filter(p => p.sceneIds.includes(activeSceneId.value!))
  if (activeFunctionId.value) list = list.filter(p => p.functionIds.includes(activeFunctionId.value!))

  // 属性筛选
  const brandFilters = activeFilters[1]
  const priceFilters = activeFilters[2]
  const matFilters = activeFilters[3]

  if (brandFilters?.size) list = list.filter(p => [...brandFilters].some(b => p.shop.includes(b) || p.badges.includes(b)))
  if (priceFilters?.size) {
    list = list.filter(p => [...priceFilters].some(tag => {
      const range = parsePriceRange(tag)
      return range ? p.price >= range[0] && p.price <= range[1] : true
    }))
  }
  if (matFilters?.size) list = list.filter(p => [...matFilters].some(m => p.desc.includes(m) || p.title.includes(m)))

  // 排序
  if (activeSort.value === 'price') list = list.sort((a, b) => priceOrder.value === 'asc' ? a.price - b.price : b.price - a.price)
  else if (activeSort.value === 'comments') {
    list = list.sort((a, b) => {
      const parse = (v: string) => v.endsWith('万') ? parseFloat(v) * 10000 : parseFloat(v)
      return parse(b.comments) - parse(a.comments)
    })
  } else if (activeSort.value === 'sale') {
    list = list.sort((a, b) => {
      const parse = (v: string) => v.endsWith('万') ? parseFloat(v) * 10000 : parseFloat(v)
      return parse(b.comments) - parse(a.comments)
    })
  }

  return list
})

const pagedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredProducts.value.slice(start, start + pageSize.value)
})

const addToCart = (item: Product) => {
  cartCount.value++
  ElMessage.success({ message: `已将「${item.title.slice(0, 14)}...」加入购物车`, duration: 2000 })
}

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

.search-page {
  background: #f4f4f4;
  min-height: 100vh;
  font-family: 'Microsoft YaHei', sans-serif;
}

.nav-inner { max-width: 1200px; margin: 0 auto; padding: 0 16px; }

/* ===== 头部 ===== */
.search-header {
  background: #fff;
  border-bottom: 2px solid #e1251b;
  padding: 12px 0;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.header-inner { display: flex; align-items: center; gap: 24px; }
.logo { display: flex; align-items: center; gap: 8px; cursor: pointer; flex-shrink: 0; }
.logo-icon { font-size: 28px; }
.logo-text { font-size: 26px; font-weight: 900; color: #e1251b; letter-spacing: -1px; }

.search-bar {
  flex: 1; display: flex; align-items: stretch; max-width: 650px; height: 40px;
  border: 2px solid #e1251b; background: #fff; position: relative; box-sizing: content-box;
}
.search-category-dropdown {
  position: relative; display: flex; align-items: center; gap: 4px;
  padding: 0 10px 0 14px; cursor: pointer; flex-shrink: 0; user-select: none; white-space: nowrap;
}
.search-category-dropdown:hover .search-cat-text { color: #e1251b; }
.search-cat-text { font-size: 14px; color: #333; }
.search-cat-arrow { font-size: 11px; color: #666; transition: transform 0.2s; }
.search-cat-arrow.rotated { transform: rotate(180deg); }
.category-dropdown-menu {
  position: absolute; top: calc(100% + 4px); left: -2px; width: 80px;
  background: #fff; border: 1px solid #e4e7ed; border-radius: 4px;
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
  flex-shrink: 0; border: none; outline: none; background: #e1251b; color: #fff;
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

/* 快捷链接 */
.quick-links-bar { background: white; border-bottom: 1px solid #f0f0f0; padding: 6px 0; }
.quick-links-bar .nav-inner { display: flex; flex-wrap: wrap; }
.quick-link { font-size: 12px; color: #555; padding: 2px 12px; cursor: pointer; border-right: 1px solid #e8e8e8; white-space: nowrap; }
.quick-link:hover { color: #e1251b; }
.quick-link:last-child { border-right: none; }

/* ===== 场景筛选 ===== */
.scene-bar { background: #fff; border-bottom: 1px solid #f0f0f0; padding: 10px 0; margin-top: 4px; }
.scene-row { display: flex; align-items: flex-start; margin-bottom: 8px; }
.scene-row:last-child { margin-bottom: 0; }
.scene-label { width: 48px; font-size: 12px; color: #999; flex-shrink: 0; padding-top: 4px; }
.scene-tags { flex: 1; display: flex; flex-wrap: wrap; gap: 6px 10px; }
.scene-tag {
  font-size: 12px; color: #333; padding: 4px 12px; border-radius: 14px;
  background: #f5f5f5; cursor: pointer; transition: all 0.2s; border: 1px solid transparent;
}
.scene-tag.active, .scene-tag:hover { background: #fff0f0; color: #e1251b; border-color: #e1251b; }

.scene-row-with-cards .scene-scroll { flex: 1; display: flex; align-items: center; gap: 8px; }
.scene-arrow {
  width: 26px; height: 26px; border-radius: 50%; border: 1px solid #e8e8e8;
  background: #fff; cursor: pointer; font-size: 12px; color: #666;
  display: flex; align-items: center; justify-content: center; flex-shrink: 0;
}
.scene-arrow:hover { border-color: #e1251b; color: #e1251b; }
.scene-scroll-inner { flex: 1; display: flex; overflow-x: auto; gap: 8px; padding: 2px 0; scrollbar-width: none; }
.scene-scroll-inner::-webkit-scrollbar { display: none; }
.scene-card {
  min-width: 110px; background: #fff; border-radius: 8px; border: 1.5px solid #f0f0f0;
  padding: 6px 6px 8px; cursor: pointer; transition: all 0.2s; text-align: center; flex-shrink: 0;
}
.scene-card:hover { border-color: #ffa0a0; }
.scene-card.active { border-color: #e1251b; box-shadow: 0 0 0 1px rgba(225,37,27,0.25); }
.scene-card-thumb { height: 44px; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 26px; margin-bottom: 4px; background: #f9f9f9; }
.scene-card-name { font-size: 12px; color: #333; white-space: nowrap; }

/* ===== 主区域 ===== */
.search-main { padding: 12px 0 32px; }

/* 面包屑 */
.search-breadcrumb {
  display: flex; justify-content: space-between; align-items: center;
  font-size: 12px; color: #666; margin-bottom: 8px;
}
.breadcrumb-left { display: flex; align-items: center; gap: 4px; }
.crumb { cursor: pointer; transition: color 0.2s; }
.crumb:hover { color: #e1251b; }
.crumb.current { color: #e1251b; font-weight: 500; }
.crumb-sep { color: #ccc; }
.breadcrumb-right { font-size: 12px; color: #999; }
.result-keyword { color: #e1251b; font-weight: bold; font-size: 14px; }
.result-kw-text { color: #333; }

/* 顶部 Tab */
.top-tabs {
  background: #fff; border-radius: 4px 4px 0 0; padding: 8px 16px 0;
  border: 1px solid #f0f0f0; border-bottom: none;
}
.top-tab { display: inline-block; padding: 8px 18px; font-size: 13px; color: #555; cursor: pointer; transition: color 0.2s; }
.top-tab:hover { color: #e1251b; }
.top-tab.active { color: #e1251b; font-weight: 600; border-bottom: 2px solid #e1251b; }

/* 筛选条 */
.filter-strip {
  background: #fff; border: 1px solid #f0f0f0; border-top: none;
  padding: 6px 16px 4px;
}
.filter-line { display: flex; align-items: center; padding: 5px 0; border-bottom: 1px solid #f9f9f9; }
.filter-line:last-child { border-bottom: none; }
.filter-line-label { width: 52px; flex-shrink: 0; font-size: 12px; color: #999; }
.filter-line-options { flex: 1; display: flex; flex-wrap: wrap; gap: 5px 8px; }
.filter-option {
  font-size: 12px; color: #444; padding: 3px 10px; cursor: pointer; border-radius: 12px;
  background: #f5f5f5; border: 1px solid transparent; transition: all 0.15s;
}
.filter-option:hover { background: #fff0f0; color: #e1251b; border-color: #fcc; }
.filter-option.active { background: #fff0f0; color: #e1251b; border-color: #e1251b; font-weight: 500; }
.filter-clear-btn {
  font-size: 11px; color: #999; cursor: pointer; white-space: nowrap;
  margin-left: 8px; padding: 2px 6px; border-radius: 8px; border: 1px solid #e0e0e0;
}
.filter-clear-btn:hover { color: #e1251b; border-color: #e1251b; }

/* 已选筛选 */
.active-filter-bar {
  background: #fff8f8; border: 1px solid #f8ddd8; border-top: none;
  padding: 6px 16px; display: flex; align-items: center; flex-wrap: wrap; gap: 6px;
  font-size: 12px;
}
.active-filter-label { color: #999; flex-shrink: 0; }
.active-filter-tag {
  background: #fff; border: 1px solid #e1251b; color: #e1251b;
  padding: 2px 10px; border-radius: 12px; cursor: pointer; font-size: 12px;
}
.active-filter-tag:hover { background: #ffe4e3; }
.clear-all-btn { color: #999; cursor: pointer; margin-left: 4px; }
.clear-all-btn:hover { color: #e1251b; text-decoration: underline; }

/* 排序条 */
.sort-bar {
  background: #fff; border: 1px solid #f0f0f0; border-top: none;
  padding: 8px 16px; display: flex; justify-content: space-between; align-items: center;
  font-size: 13px;
}
.sort-options { display: flex; gap: 2px; }
.sort-option {
  padding: 5px 16px; cursor: pointer; color: #555; background: #fafafa;
  border-right: 1px solid #f0f0f0; display: flex; align-items: center; gap: 3px;
  transition: all 0.15s;
}
.sort-option:hover { color: #e1251b; background: #fff; }
.sort-option.active { color: #e1251b; font-weight: 600; background: #fff; }
.price-arrow { font-size: 12px; color: #bbb; }
.sort-option.active .price-arrow { color: #e1251b; }

.sort-right { display: flex; align-items: center; gap: 14px; }
.sort-extra-check { display: flex; align-items: center; gap: 4px; font-size: 12px; color: #666; cursor: pointer; }
.sort-extra-check input { cursor: pointer; accent-color: #e1251b; }
.sort-extra-check:hover span { color: #e1251b; }

.view-toggle { display: flex; gap: 2px; border: 1px solid #e0e0e0; border-radius: 4px; overflow: hidden; }
.view-btn {
  padding: 4px 9px; cursor: pointer; font-size: 15px; color: #999;
  background: #fafafa; transition: all 0.15s;
}
.view-btn:hover, .view-btn.active { background: #e1251b; color: #fff; }

/* 结果面板 */
.result-panel {
  background: #fff; border: 1px solid #f0f0f0; border-top: none;
  padding: 16px 12px 20px;
}

/* 网格视图 */
.product-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 12px; }

.product-card {
  background: #fff; border-radius: 6px; border: 1.5px solid #f0f0f0;
  overflow: hidden; cursor: pointer; transition: all 0.2s; position: relative;
}
.product-card:hover { border-color: #e1251b; box-shadow: 0 6px 20px rgba(0,0,0,0.1); transform: translateY(-3px); }

.product-img-wrap {
  position: relative; background: #f9f9f9; height: 180px;
  display: flex; align-items: center; justify-content: center;
}
.product-img-inner { font-size: 72px; line-height: 1; }
.product-badges { position: absolute; top: 8px; left: 8px; display: flex; flex-wrap: wrap; gap: 3px; }
.product-badge {
  font-size: 11px; padding: 2px 6px; border-radius: 2px;
  background: #ffe4e3; color: #e1251b;
}
.product-badge.badge-self { background: #e1251b; color: #fff; }
.product-badge.badge-enterprise { background: #fff3cd; color: #d68910; }
.product-badge.badge-new { background: #d4edda; color: #27ae60; }
.product-badge.badge-hot { background: #fdecea; color: #e74c3c; }
.product-badge.badge-default { background: #f5f5f5; color: #666; }

.fav-btn {
  position: absolute; top: 8px; right: 8px; font-size: 18px; cursor: pointer;
  opacity: 0; transition: opacity 0.2s; line-height: 1;
}
.product-card:hover .fav-btn { opacity: 1; }
.fav-btn.active { opacity: 1; }

.product-body { padding: 10px; }

.product-price-row { display: flex; align-items: baseline; gap: 4px; margin-bottom: 6px; }
.price-sym { font-size: 12px; color: #e1251b; font-weight: bold; }
.price-num { font-size: 22px; color: #e1251b; font-weight: 800; line-height: 1; }
.price-ori { font-size: 11px; color: #bbb; text-decoration: line-through; margin-left: 2px; }
.price-discount {
  font-size: 11px; background: #fff3e0; color: #e67e22;
  padding: 1px 5px; border-radius: 8px; font-weight: 500;
}

.product-title {
  font-size: 13px; color: #333; line-height: 1.5;
  overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
  min-height: 38px; margin-bottom: 4px;
}
:deep(.kw-highlight) { color: #e1251b; font-style: normal; font-weight: 500; }

.product-desc { font-size: 11px; color: #999; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-bottom: 6px; }

.product-rating { display: flex; align-items: center; gap: 5px; margin-bottom: 4px; }
.star-row { display: flex; gap: 1px; }
.star { font-size: 12px; color: #ddd; }
.star.filled { color: #f90; }
.rating-count { font-size: 11px; color: #bbb; }

.product-shop { display: flex; align-items: center; gap: 5px; font-size: 11px; color: #999; margin-bottom: 8px; }
.shop-dot {
  font-size: 10px; padding: 1px 5px; border-radius: 2px;
  background: #f5f5f5; color: #888;
}
.shop-dot.self { background: #e1251b; color: #fff; }
.shop-name { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }

.add-cart-btn {
  width: 100%; padding: 7px 0; border: none; border-radius: 4px;
  background: #e1251b; color: #fff; font-size: 13px; cursor: pointer; transition: background 0.2s;
}
.add-cart-btn:hover { background: #c0392b; }

/* 列表视图 */
.product-list-view { display: flex; flex-direction: column; gap: 10px; }

.product-list-card {
  display: flex; gap: 16px; background: #fff; border: 1.5px solid #f0f0f0;
  border-radius: 6px; padding: 14px; cursor: pointer; transition: all 0.2s; position: relative;
}
.product-list-card:hover { border-color: #e1251b; box-shadow: 0 4px 14px rgba(0,0,0,0.08); }

.list-img-wrap {
  width: 140px; height: 140px; background: #f9f9f9; border-radius: 4px;
  flex-shrink: 0; display: flex; align-items: center; justify-content: center; position: relative;
}
.list-img { font-size: 64px; }
.list-fav-btn {
  position: absolute; top: 6px; right: 6px; font-size: 16px; cursor: pointer;
  opacity: 0; transition: opacity 0.2s;
}
.product-list-card:hover .list-fav-btn { opacity: 1; }
.list-fav-btn.active { opacity: 1; }

.list-info { flex: 1; min-width: 0; display: flex; flex-direction: column; gap: 6px; }
.list-title { font-size: 14px; color: #333; line-height: 1.6; }
.list-desc { font-size: 12px; color: #999; }
.list-badges { display: flex; flex-wrap: wrap; gap: 4px; }
.list-rating { display: flex; align-items: center; gap: 5px; }
.list-shop { font-size: 12px; color: #999; display: flex; align-items: center; gap: 5px; }

.list-price-area { flex-shrink: 0; width: 130px; display: flex; flex-direction: column; align-items: flex-end; gap: 4px; }
.list-price { display: flex; align-items: baseline; gap: 2px; }
.list-price .price-num { font-size: 26px; }
.list-price-ori { font-size: 12px; color: #bbb; text-decoration: line-through; }
.list-discount { font-size: 12px; color: #27ae60; font-weight: 500; }
.list-cart-btn { width: 120px; margin-top: auto; }

/* 空状态 */
.empty-state { text-align: center; padding: 60px 0; }
.empty-icon { font-size: 56px; margin-bottom: 16px; }
.empty-title { font-size: 18px; color: #333; margin-bottom: 8px; }
.empty-desc { font-size: 14px; color: #999; margin-bottom: 20px; }

/* 分页 */
.pagination-wrap {
  display: flex; align-items: center; justify-content: space-between;
  margin-top: 24px; padding-top: 16px; border-top: 1px solid #f0f0f0;
}
.pagination-total { font-size: 13px; color: #999; }
.pagination-wrap :deep(.el-pagination .is-active) { background: #e1251b !important; }
.pagination-wrap :deep(.el-pager li:hover) { color: #e1251b; }

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
