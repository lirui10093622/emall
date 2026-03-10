<template>
  <div class="login-page">
    <!-- 顶部 Header -->
    <header class="login-header">
      <div class="header-inner">
        <div class="logo" @click="goHome">
          <span class="logo-icon">🛒</span>
          <span class="logo-text">E-Mall</span>
        </div>
        <div class="header-right">
          <span class="header-link">E-Mall商城</span>
          <span class="header-sep">，</span>
          <span class="header-link">你好！欢迎来</span>
        </div>
      </div>
    </header>

    <!-- 主体 -->
    <main class="login-main">
      <!-- 用户类型 Tab -->
      <div class="user-type-tabs">
        <span
          class="user-type-tab"
          :class="{ active: userType === 'personal' }"
          @click="userType = 'personal'"
        >个人用户登录</span>
        <span
          class="user-type-tab"
          :class="{ active: userType === 'enterprise' }"
          @click="userType = 'enterprise'"
        >企业用户登录</span>
      </div>

      <!-- 登录卡片 -->
      <div class="login-card">
        <!-- 左侧：扫码登录 -->
        <div class="qr-panel">
          <div class="qr-title">手机扫码安全登录</div>
          <div class="qr-apps">
            <span class="qr-app jd">🛒 E-Mall APP</span>
            <span class="qr-app wechat">💬 微信扫码</span>
            <span class="qr-more">更多方式</span>
          </div>
          <div class="qr-code-wrap">
            <div class="qr-code-placeholder">
              <div class="qr-grid">
                <div v-for="i in 64" :key="i" class="qr-cell" :class="{ dark: qrPattern[i - 1] }"></div>
              </div>
            </div>
            <div class="qr-tip">请使用 E-Mall APP 或微信扫码登录</div>
          </div>
        </div>

        <!-- 分割线 -->
        <div class="card-divider"></div>

        <!-- 右侧：账号登录 -->
        <div class="form-panel">
          <!-- 登录方式 Tab -->
          <div class="login-method-tabs">
            <span
              class="method-tab"
              :class="{ active: loginMethod === 'password' }"
              @click="loginMethod = 'password'"
            >密码登录</span>
            <span
              class="method-tab"
              :class="{ active: loginMethod === 'sms' }"
              @click="loginMethod = 'sms'"
            >短信登录</span>
          </div>

          <!-- 表单 -->
          <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" @submit.prevent>
            <el-form-item prop="account">
              <el-input
                v-model="ruleForm.account"
                placeholder="账号名/手机号/邮箱"
                size="large"
                class="login-input"
                @keyup.enter="handleLogin"
              />
            </el-form-item>

            <el-form-item v-if="loginMethod === 'password'" prop="password">
              <el-input
                v-model="ruleForm.password"
                type="password"
                placeholder="密码"
                size="large"
                show-password
                class="login-input"
                @keyup.enter="handleLogin"
              />
            </el-form-item>

            <el-form-item v-else prop="smsCode">
              <div class="sms-row">
                <el-input
                  v-model="smsCode"
                  placeholder="短信验证码"
                  size="large"
                  class="login-input sms-input"
                />
                <button class="sms-btn" :disabled="smsCounting" @click="sendSms">
                  {{ smsCounting ? `${smsCountdown}s 后重发` : '获取验证码' }}
                </button>
              </div>
            </el-form-item>

            <div class="form-options">
              <label class="remember-me">
                <input type="checkbox" v-model="rememberMe" />
                <span>自动登录</span>
              </label>
              <span class="forget-pwd">忘记密码</span>
            </div>

            <el-form-item>
              <button class="login-btn" :class="{ loading: loginLoading }" @click="handleLogin">
                <span v-if="loginLoading" class="loading-dot">···</span>
                <span v-else>登 录</span>
              </button>
            </el-form-item>
          </el-form>

          <!-- 第三方登录 -->
          <div class="third-party">
            <span class="third-item wechat-login">
              <span class="third-icon">💬</span> 微信登录
            </span>
            <span class="third-sep">|</span>
            <span class="third-item qq-login">
              <span class="third-icon">🐧</span> QQ登录
            </span>
            <span class="third-sep">|</span>
            <span class="third-item" @click="goHome">
              <span class="third-icon">🛒</span> 游客逛逛
            </span>
            <span class="third-sep">|</span>
            <span class="third-item register" @click="goRegister">立即注册</span>
          </div>
        </div>
      </div>
    </main>

    <!-- Footer -->
    <footer class="login-footer">
      <div class="footer-links">
        <span v-for="(link, i) in footerLinks" :key="link">
          <a href="#" class="footer-link">{{ link }}</a>
          <span v-if="i < footerLinks.length - 1" class="footer-sep">|</span>
        </span>
      </div>
      <div class="footer-copy">Copyright © 2004-2026 E-Mall.com 版权所有</div>
    </footer>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const ruleFormRef = ref<FormInstance>()
const loginLoading = ref(false)
const userType = ref<'personal' | 'enterprise'>('personal')
const loginMethod = ref<'password' | 'sms'>('password')
const rememberMe = ref(false)
const smsCode = ref('')
const smsCounting = ref(false)
const smsCountdown = ref(60)

const ruleForm = reactive({
  accountType: 'PHONE',
  account: '',
  password: '',
  captchaText: 'test',
  device: 'WEB',
})

const rules = reactive<FormRules<typeof ruleForm>>({
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
})

// 模拟二维码图案
const qrPattern = Array.from({ length: 64 }, (_, i) => {
  const row = Math.floor(i / 8)
  const col = i % 8
  // 四角定位块
  if ((row < 2 && col < 2) || (row < 2 && col > 5) || (row > 5 && col < 2)) return true
  return Math.random() > 0.5
})

const sendSms = () => {
  if (!ruleForm.account) { ElMessage.warning('请先输入手机号'); return }
  smsCounting.value = true
  smsCountdown.value = 60
  ElMessage.success('验证码已发送')
  const timer = setInterval(() => {
    smsCountdown.value--
    if (smsCountdown.value <= 0) { clearInterval(timer); smsCounting.value = false }
  }, 1000)
}

axios.defaults.baseURL = 'https://test.nengliangtech.com'
axios.defaults.headers.post['Content-Type'] = 'application/json'

const handleLogin = async () => {
  const valid = await ruleFormRef.value?.validate().catch(() => false)
  if (!valid) return
  loginLoading.value = true
  try {
    const res = await axios.post('/api/org/employeeLogin/login', ruleForm)
    if (res.data?.code === 200 || res.status === 200) {
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(res.data?.message || '登录失败，请检查账号密码')
    }
  } catch (err: any) {
    ElMessage.error(err?.response?.data?.message || '登录请求失败，请稍后重试')
  } finally {
    loginLoading.value = false
  }
}

const goHome = () => router.push('/')
const goRegister = () => router.push('/register')

const footerLinks = ['关于我们', '联系我们', '人才招聘', '商务合作', '手机E-Mall', '友情链接', '销售联盟', '京E-Mall公益', 'English Site']
</script>

<style scoped>
* { box-sizing: border-box; }

.login-page {
  min-height: 100vh;
  background: #f0f0f0;
  display: flex;
  flex-direction: column;
  font-family: 'Microsoft YaHei', sans-serif;
}

/* ===== Header ===== */
.login-header {
  background: white;
  border-bottom: 1px solid #e8e8e8;
  padding: 14px 0;
}
.header-inner {
  max-width: 980px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  text-decoration: none;
}
.logo-icon { font-size: 30px; }
.logo-text { font-size: 28px; font-weight: 900; color: #e1251b; letter-spacing: -1px; }
.header-right { font-size: 13px; color: #999; }
.header-link { color: #e1251b; cursor: pointer; }
.header-sep { color: #999; }

/* ===== Main ===== */
.login-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 0 60px;
}

/* 用户类型 Tab */
.user-type-tabs {
  display: flex;
  gap: 0;
  margin-bottom: 0;
}
.user-type-tab {
  font-size: 16px;
  color: #666;
  padding: 10px 28px;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
}
.user-type-tab:hover { color: #e1251b; }
.user-type-tab.active {
  color: #333;
  font-weight: 500;
  border-bottom-color: #e1251b;
}

/* 卡片 */
.login-card {
  width: 560px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.08);
  display: flex;
  padding: 36px 28px 28px;
  gap: 0;
}

/* 左侧扫码 */
.qr-panel {
  width: 200px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-right: 24px;
}
.qr-title {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  margin-bottom: 10px;
  white-space: nowrap;
}
.qr-apps {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 12px;
  flex-wrap: wrap;
  justify-content: center;
}
.qr-app {
  font-size: 11px;
  padding: 2px 7px;
  border-radius: 10px;
  cursor: pointer;
  white-space: nowrap;
}
.qr-app.jd { color: #e1251b; background: #fff0f0; border: 1px solid #fcc; }
.qr-app.wechat { color: #07c160; background: #f0faf4; border: 1px solid #b7e8c7; }
.qr-more { font-size: 11px; color: #999; cursor: pointer; text-decoration: underline; }

.qr-code-wrap { display: flex; flex-direction: column; align-items: center; gap: 8px; }
.qr-code-placeholder {
  width: 120px;
  height: 120px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 6px;
  background: white;
}
.qr-grid {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 1px;
}
.qr-cell { border-radius: 1px; background: #f0f0f0; }
.qr-cell.dark { background: #222; }
.qr-tip { font-size: 11px; color: #bbb; text-align: center; line-height: 1.4; max-width: 130px; }

/* 分割线 */
.card-divider {
  width: 1px;
  background: #f0f0f0;
  margin: 0 24px;
  flex-shrink: 0;
}

/* 右侧表单 */
.form-panel {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.login-method-tabs {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}
.method-tab {
  font-size: 14px;
  color: #999;
  padding-bottom: 10px;
  cursor: pointer;
  position: relative;
  transition: color 0.2s;
}
.method-tab:hover { color: #333; }
.method-tab.active {
  color: #e1251b;
  font-weight: 500;
}
.method-tab.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background: #e1251b;
}

.login-input :deep(.el-input__wrapper) {
  border-radius: 2px;
  border: 1px solid #ddd;
  box-shadow: none;
  background: #f7f7f7;
}
.login-input :deep(.el-input__wrapper:hover),
.login-input :deep(.el-input__wrapper.is-focus) {
  border-color: #e1251b;
  box-shadow: none;
  background: white;
}
.login-input :deep(.el-input__inner) {
  font-size: 14px;
  color: #333;
}

.sms-row { display: flex; gap: 8px; width: 100%; }
.sms-input { flex: 1; }
.sms-btn {
  flex-shrink: 0;
  white-space: nowrap;
  padding: 0 12px;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 2px;
  background: white;
  font-size: 12px;
  color: #e1251b;
  cursor: pointer;
  transition: all 0.2s;
}
.sms-btn:hover:not(:disabled) { border-color: #e1251b; background: #fff5f5; }
.sms-btn:disabled { color: #bbb; cursor: not-allowed; }

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
  margin-top: -4px;
}
.remember-me {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
  color: #666;
  cursor: pointer;
}
.remember-me input { accent-color: #e1251b; cursor: pointer; }
.forget-pwd { font-size: 12px; color: #999; cursor: pointer; }
.forget-pwd:hover { color: #e1251b; }

.login-btn {
  width: 100%;
  height: 40px;
  background: #e1251b;
  color: white;
  border: none;
  border-radius: 2px;
  font-size: 15px;
  font-weight: 500;
  letter-spacing: 4px;
  cursor: pointer;
  transition: background 0.2s;
}
.login-btn:hover { background: #c0392b; }
.login-btn.loading { background: #e86a62; cursor: not-allowed; letter-spacing: 0; }
.loading-dot { letter-spacing: 4px; }

/* 第三方登录 */
.third-party {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 16px;
  font-size: 12px;
}
.third-sep { color: #e0e0e0; }
.third-item {
  color: #999;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 3px;
  transition: color 0.2s;
}
.third-item:hover { color: #333; }
.wechat-login:hover { color: #07c160; }
.qq-login:hover { color: #12b7f5; }
.register { color: #e1251b; }
.register:hover { color: #c0392b; }
.third-icon { font-size: 14px; }

/* ===== Footer ===== */
.login-footer {
  background: white;
  border-top: 1px solid #f0f0f0;
  text-align: center;
  padding: 16px 0 20px;
}
.footer-links {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  margin-bottom: 8px;
}
.footer-link { font-size: 12px; color: #666; text-decoration: none; }
.footer-link:hover { color: #e1251b; }
.footer-sep { font-size: 12px; color: #ccc; }
.footer-copy { font-size: 12px; color: #999; }
</style>

<style>
.login-page .el-form-item { margin-bottom: 14px; }
.login-page .el-form-item__error { font-size: 11px; }
</style>
