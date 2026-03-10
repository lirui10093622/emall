<template>
  <div class="register-page">
    <!-- Header -->
    <header class="reg-header">
      <div class="header-inner">
        <div class="header-left">
          <div class="logo" @click="goHome">
            <span class="logo-icon">🛒</span>
            <span class="logo-text">E-Mall</span>
          </div>
          <span class="welcome-text">欢迎注册</span>
        </div>
        <div class="header-right">
          已有账号？
          <span class="login-link" @click="goLogin">请登录 ›</span>
        </div>
      </div>
    </header>

    <!-- 主体 -->
    <main class="reg-main">
      <!-- 步骤指示器 -->
      <div class="steps-wrap">
        <div v-for="(step, idx) in steps" :key="step.key" class="step-item">
          <!-- 步骤圆圈 -->
          <div class="step-node" :class="getStepClass(idx)">
            <span v-if="currentStep > idx" class="step-check">✓</span>
            <span v-else>{{ idx + 1 }}</span>
          </div>
          <div class="step-label" :class="getStepClass(idx)">{{ step.label }}</div>
          <!-- 连接线 -->
          <div v-if="idx < steps.length - 1" class="step-line">
            <span v-for="i in 12" :key="i" class="step-dot" :class="{ passed: currentStep > idx }">·</span>
          </div>
        </div>
      </div>

      <!-- ===== Step 1：验证手机号 ===== -->
      <div v-if="currentStep === 0" class="step-content">
        <!-- 手机号输入 -->
        <div class="input-group">
          <div class="phone-input-wrap">
            <div class="country-code" @click="showCountryMenu = !showCountryMenu">
              <span>{{ selectedCountry.flag }} {{ selectedCountry.name }}</span>
              <span class="cc-num">+{{ selectedCountry.code }}</span>
              <span class="cc-arrow">▾</span>
              <div v-if="showCountryMenu" class="country-menu" @click.stop>
                <div
                  v-for="c in countries"
                  :key="c.code"
                  class="country-option"
                  :class="{ active: selectedCountry.code === c.code }"
                  @click="selectCountry(c)"
                >
                  {{ c.flag }} {{ c.name }} +{{ c.code }}
                </div>
              </div>
            </div>
            <input
              v-model="phone"
              class="phone-input"
              placeholder="请输入您的手机号"
              maxlength="11"
              type="tel"
              @input="phone = phone.replace(/\D/g, '')"
            />
          </div>
        </div>

        <!-- 滑块验证 -->
        <div class="slider-wrap">
          <div class="slider-track" @mousemove="onSliderMove" @mouseup="onSliderEnd" @mouseleave="onSliderEnd">
            <div class="slider-fill" :style="{ width: sliderFillWidth }"></div>
            <span class="slider-text">{{ sliderVerified ? '✓ 验证成功' : '点击按钮进行验证' }}</span>
            <div
              class="slider-thumb"
              :class="{ verified: sliderVerified }"
              :style="{ left: sliderThumbLeft }"
              @mousedown.prevent="onSliderStart"
            >
              <span class="thumb-icon">{{ sliderVerified ? '✓' : '›' }}</span>
            </div>
          </div>
        </div>

        <!-- 同意协议按钮 -->
        <button
          class="reg-btn"
          :class="{ disabled: !canProceedStep1 }"
          @click="goStep2"
        >同意协议并继续</button>

        <!-- 协议勾选 -->
        <div class="agreement-row">
          <label class="agreement-check">
            <input type="checkbox" v-model="agreedTerms" />
            <span>我已阅读并同意</span>
          </label>
          <a href="#" class="agreement-link">《用户注册协议》</a>
          <span class="agreement-and">和</span>
          <a href="#" class="agreement-link">《隐私政策》</a>
        </div>

        <!-- 企业注册 -->
        <div class="enterprise-reg" @click="switchToEnterprise">
          <span class="ent-icon">🏢</span>
          <span class="ent-text">企业用户注册</span>
        </div>
      </div>

      <!-- ===== Step 2：填写账号信息 ===== -->
      <div v-if="currentStep === 1" class="step-content">
        <!-- 短信验证码 -->
        <div class="form-row">
          <label class="form-label">验证码</label>
          <div class="sms-row">
            <input
              v-model="smsCode"
              class="form-input sms-code-input"
              placeholder="请输入短信验证码"
              maxlength="6"
            />
            <button class="sms-btn" :disabled="smsCounting" @click="sendSms">
              {{ smsCounting ? `${smsCountdown}s 重发` : '获取验证码' }}
            </button>
          </div>
          <span class="form-hint">验证码已发送至 +{{ selectedCountry.code }} {{ phone }}</span>
        </div>

        <!-- 用户名 -->
        <div class="form-row">
          <label class="form-label">用户名</label>
          <input
            v-model="username"
            class="form-input"
            :class="{ error: usernameError, success: usernameOk }"
            placeholder="请设置用户名（4-16位字母、数字或下划线）"
            maxlength="16"
            @blur="validateUsername"
          />
          <span v-if="usernameError" class="field-error">{{ usernameError }}</span>
          <span v-if="usernameOk" class="field-ok">✓ 用户名可用</span>
        </div>

        <!-- 密码 -->
        <div class="form-row">
          <label class="form-label">密码</label>
          <div class="pwd-wrap">
            <input
              v-model="password"
              :type="showPwd ? 'text' : 'password'"
              class="form-input"
              :class="{ error: passwordError, success: password && !passwordError }"
              placeholder="请设置密码（8-20位，含字母和数字）"
              maxlength="20"
              @input="validatePassword"
            />
            <span class="pwd-toggle" @click="showPwd = !showPwd">{{ showPwd ? '🙈' : '👁️' }}</span>
          </div>
          <!-- 密码强度条 -->
          <div v-if="password" class="pwd-strength">
            <div class="strength-bars">
              <div class="strength-bar" :class="{ active: pwdStrength >= 1, weak: pwdStrength === 1, medium: pwdStrength === 2, strong: pwdStrength >= 3 }"></div>
              <div class="strength-bar" :class="{ active: pwdStrength >= 2, medium: pwdStrength === 2, strong: pwdStrength >= 3 }"></div>
              <div class="strength-bar" :class="{ active: pwdStrength >= 3, strong: pwdStrength >= 3 }"></div>
            </div>
            <span class="strength-label" :class="pwdStrengthClass">{{ pwdStrengthLabel }}</span>
          </div>
          <span v-if="passwordError" class="field-error">{{ passwordError }}</span>
        </div>

        <!-- 确认密码 -->
        <div class="form-row">
          <label class="form-label">确认密码</label>
          <div class="pwd-wrap">
            <input
              v-model="confirmPassword"
              :type="showConfirmPwd ? 'text' : 'password'"
              class="form-input"
              :class="{ error: confirmError, success: confirmPassword && !confirmError }"
              placeholder="请再次输入密码"
              maxlength="20"
              @input="validateConfirm"
            />
            <span class="pwd-toggle" @click="showConfirmPwd = !showConfirmPwd">{{ showConfirmPwd ? '🙈' : '👁️' }}</span>
          </div>
          <span v-if="confirmError" class="field-error">{{ confirmError }}</span>
        </div>

        <div class="step2-btns">
          <button class="reg-btn secondary" @click="currentStep = 0">上一步</button>
          <button class="reg-btn" :class="{ disabled: !canProceedStep2 }" @click="submitRegister">
            <span v-if="regLoading">注册中···</span>
            <span v-else>立即注册</span>
          </button>
        </div>
      </div>

      <!-- ===== Step 3：注册成功 ===== -->
      <div v-if="currentStep === 2" class="step-content success-content">
        <div class="success-icon">🎉</div>
        <div class="success-title">注册成功！</div>
        <div class="success-desc">欢迎加入 E-Mall，您的账号已创建成功</div>
        <div class="success-account">
          <span class="account-label">您的账号：</span>
          <span class="account-val">{{ username }}</span>
        </div>
        <div class="success-btns">
          <button class="reg-btn" @click="goLogin">立即登录</button>
          <button class="reg-btn secondary" @click="goHome">逛逛首页</button>
        </div>
      </div>
    </main>

    <!-- Footer -->
    <footer class="reg-footer">
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
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 步骤
const steps = [
  { key: 'phone', label: '验证手机号' },
  { key: 'info', label: '填写账号信息' },
  { key: 'done', label: '注册成功' },
]
const currentStep = ref(0)

const getStepClass = (idx: number) => {
  if (idx < currentStep.value) return 'passed'
  if (idx === currentStep.value) return 'active'
  return ''
}

// 国家区号
const countries = [
  { flag: '🇨🇳', name: '中国', code: '0086' },
  { flag: '🇭🇰', name: '香港', code: '00852' },
  { flag: '🇲🇴', name: '澳门', code: '00853' },
  { flag: '🇹🇼', name: '台湾', code: '00886' },
  { flag: '🇺🇸', name: '美国', code: '001' },
  { flag: '🇬🇧', name: '英国', code: '0044' },
  { flag: '🇯🇵', name: '日本', code: '0081' },
]
const selectedCountry = ref(countries[0])
const showCountryMenu = ref(false)
const selectCountry = (c: typeof countries[0]) => {
  selectedCountry.value = c
  showCountryMenu.value = false
}

// 手机号 & 滑块
const phone = ref('')
const agreedTerms = ref(false)

// 滑块验证
const sliderX = ref(0)
const sliderDragging = ref(false)
const sliderVerified = ref(false)
const SLIDER_MAX = 280

const sliderFillWidth = computed(() => sliderVerified.value ? '100%' : `${sliderX.value}px`)
const sliderThumbLeft = computed(() => sliderVerified.value ? `calc(100% - 40px)` : `${sliderX.value}px`)

const onSliderStart = () => { if (!sliderVerified.value) sliderDragging.value = true }
const onSliderMove = (e: MouseEvent) => {
  if (!sliderDragging.value || sliderVerified.value) return
  const track = (e.currentTarget as HTMLElement)
  const rect = track.getBoundingClientRect()
  const x = Math.max(0, Math.min(e.clientX - rect.left - 20, SLIDER_MAX))
  sliderX.value = x
  if (x >= SLIDER_MAX) { sliderVerified.value = true; sliderDragging.value = false }
}
const onSliderEnd = () => {
  if (!sliderVerified.value) { sliderX.value = 0; sliderDragging.value = false }
}

const canProceedStep1 = computed(() => phone.value.length >= 11 && sliderVerified.value && agreedTerms.value)

const goStep2 = () => {
  if (!phone.value || phone.value.length < 11) { ElMessage.warning('请输入正确的手机号'); return }
  if (!sliderVerified.value) { ElMessage.warning('请完成滑块验证'); return }
  if (!agreedTerms.value) { ElMessage.warning('请阅读并同意用户协议'); return }
  currentStep.value = 1
  sendSms()
}

const switchToEnterprise = () => ElMessage.info('企业用户注册功能开发中')

// Step 2
const smsCode = ref('')
const smsCounting = ref(false)
const smsCountdown = ref(60)
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const showPwd = ref(false)
const showConfirmPwd = ref(false)
const usernameError = ref('')
const usernameOk = ref(false)
const passwordError = ref('')
const confirmError = ref('')
const regLoading = ref(false)

const sendSms = () => {
  smsCounting.value = true
  smsCountdown.value = 60
  ElMessage.success(`验证码已发送至 ${phone.value}`)
  const timer = setInterval(() => {
    smsCountdown.value--
    if (smsCountdown.value <= 0) { clearInterval(timer); smsCounting.value = false }
  }, 1000)
}

const validateUsername = () => {
  usernameError.value = ''
  usernameOk.value = false
  if (!username.value) { usernameError.value = '请输入用户名'; return }
  if (username.value.length < 4) { usernameError.value = '用户名至少4位'; return }
  if (!/^[a-zA-Z0-9_]+$/.test(username.value)) { usernameError.value = '只能包含字母、数字和下划线'; return }
  usernameOk.value = true
}

const validatePassword = () => {
  passwordError.value = ''
  if (!password.value) return
  if (password.value.length < 8) { passwordError.value = '密码至少8位'; return }
  if (!/[a-zA-Z]/.test(password.value) || !/[0-9]/.test(password.value)) {
    passwordError.value = '密码需同时包含字母和数字'
  }
  if (confirmPassword.value) validateConfirm()
}

const validateConfirm = () => {
  confirmError.value = ''
  if (confirmPassword.value && confirmPassword.value !== password.value) {
    confirmError.value = '两次输入的密码不一致'
  }
}

const pwdStrength = computed(() => {
  const p = password.value
  if (!p) return 0
  let score = 0
  if (p.length >= 8) score++
  if (/[a-zA-Z]/.test(p) && /[0-9]/.test(p)) score++
  if (/[^a-zA-Z0-9]/.test(p) || p.length >= 12) score++
  return score
})
const pwdStrengthLabel = computed(() => ['', '弱', '中', '强'][pwdStrength.value] ?? '')
const pwdStrengthClass = computed(() => ['', 'weak', 'medium', 'strong'][pwdStrength.value] ?? '')

const canProceedStep2 = computed(() =>
  smsCode.value.length === 6 &&
  usernameOk.value &&
  password.value &&
  !passwordError.value &&
  confirmPassword.value === password.value
)

const submitRegister = async () => {
  validateUsername()
  validatePassword()
  validateConfirm()
  if (!canProceedStep2.value) { ElMessage.warning('请填写完整注册信息'); return }
  regLoading.value = true
  await new Promise(r => setTimeout(r, 1200))
  regLoading.value = false
  currentStep.value = 2
  ElMessage.success('注册成功！')
}

const goHome = () => router.push('/')
const goLogin = () => router.push('/login')

const footerLinks = ['关于我们', '联系我们', '人才招聘', '商务合作', '手机E-Mall', '友情链接', '销售联盟', 'E-Mall公益']
</script>

<style scoped>
* { box-sizing: border-box; }

.register-page {
  min-height: 100vh;
  background: #f0f0f0;
  display: flex;
  flex-direction: column;
  font-family: 'Microsoft YaHei', sans-serif;
}

/* ===== Header ===== */
.reg-header {
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
.header-left { display: flex; align-items: center; gap: 16px; }
.logo { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.logo-icon { font-size: 28px; }
.logo-text { font-size: 26px; font-weight: 900; color: #e1251b; letter-spacing: -1px; }
.welcome-text { font-size: 20px; color: #333; font-weight: 400; }
.header-right { font-size: 13px; color: #999; }
.login-link { color: #e1251b; cursor: pointer; font-weight: 500; }
.login-link:hover { text-decoration: underline; }

/* ===== Main ===== */
.reg-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 0 60px;
}

/* 步骤指示器 */
.steps-wrap {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  margin-bottom: 40px;
  gap: 0;
}
.step-item {
  display: flex;
  align-items: center;
  gap: 0;
}
.step-node {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  border: 2px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  color: #bbb;
  background: white;
  font-weight: 600;
  flex-shrink: 0;
  position: relative;
  z-index: 1;
  transition: all 0.3s;
}
.step-node.active { border-color: #e1251b; color: #e1251b; box-shadow: 0 0 0 3px rgba(225,37,27,0.15); }
.step-node.passed { border-color: #e1251b; background: #e1251b; color: white; }
.step-label {
  font-size: 12px;
  color: #bbb;
  text-align: center;
  margin-top: 6px;
  position: absolute;
  top: 28px;
  left: 50%;
  transform: translateX(-50%);
  white-space: nowrap;
}

/* 让 step-item 相对定位以放 label */
.step-item { position: relative; padding-bottom: 24px; }
.step-label.active { color: #e1251b; font-weight: 500; }
.step-label.passed { color: #e1251b; }

.step-line {
  display: flex;
  align-items: center;
  gap: 2px;
  margin: 0 4px;
  padding-bottom: 24px;
}
.step-dot { font-size: 14px; color: #ddd; line-height: 28px; transition: color 0.3s; }
.step-dot.passed { color: #e1251b; }

/* ===== 步骤内容通用 ===== */
.step-content {
  background: white;
  width: 500px;
  padding: 36px 48px 40px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 手机号输入 */
.input-group { display: flex; flex-direction: column; gap: 4px; }
.phone-input-wrap {
  display: flex;
  border: 1px solid #ddd;
  border-radius: 2px;
  overflow: visible;
  background: white;
  transition: border-color 0.2s;
  position: relative;
}
.phone-input-wrap:focus-within { border-color: #e1251b; }
.country-code {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 0 12px;
  font-size: 13px;
  color: #333;
  cursor: pointer;
  border-right: 1px solid #eee;
  white-space: nowrap;
  user-select: none;
  flex-shrink: 0;
  position: relative;
}
.country-code:hover { background: #fafafa; }
.cc-num { color: #666; }
.cc-arrow { font-size: 10px; color: #999; }
.country-menu {
  position: absolute;
  top: calc(100% + 4px);
  left: -1px;
  width: 200px;
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
  z-index: 200;
  max-height: 220px;
  overflow-y: auto;
}
.country-option {
  padding: 9px 14px;
  font-size: 13px;
  color: #333;
  cursor: pointer;
  transition: background 0.15s;
}
.country-option:hover { background: #fff0f0; color: #e1251b; }
.country-option.active { color: #e1251b; font-weight: 500; background: #fff5f5; }
.phone-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 12px 14px;
  font-size: 14px;
  color: #333;
  background: transparent;
}
.phone-input::placeholder { color: #bbb; }

/* 滑块验证 */
.slider-wrap { position: relative; }
.slider-track {
  position: relative;
  height: 44px;
  background: #f5f5f5;
  border: 1px solid #e0e0e0;
  border-radius: 2px;
  cursor: default;
  user-select: none;
  overflow: hidden;
}
.slider-fill {
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  background: #d4edda;
  transition: width 0.05s;
}
.slider-text {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  font-size: 13px;
  color: #999;
  pointer-events: none;
  white-space: nowrap;
}
.slider-thumb {
  position: absolute;
  top: 2px;
  width: 40px;
  height: 38px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: ew-resize;
  transition: background 0.2s, border-color 0.2s;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
  z-index: 2;
}
.slider-thumb:hover { border-color: #e1251b; }
.slider-thumb.verified { background: #27ae60; border-color: #27ae60; cursor: default; }
.thumb-icon { font-size: 16px; color: #999; }
.slider-thumb.verified .thumb-icon { color: white; font-weight: bold; }

/* 按钮 */
.reg-btn {
  width: 100%;
  height: 44px;
  background: #e1251b;
  color: white;
  border: none;
  border-radius: 2px;
  font-size: 15px;
  font-weight: 500;
  letter-spacing: 2px;
  cursor: pointer;
  transition: background 0.2s;
}
.reg-btn:hover { background: #c0392b; }
.reg-btn.disabled { background: #f5a29e; cursor: not-allowed; pointer-events: none; }
.reg-btn.secondary {
  background: white;
  color: #666;
  border: 1px solid #ddd;
  letter-spacing: 0;
}
.reg-btn.secondary:hover { border-color: #e1251b; color: #e1251b; background: #fff5f5; }

/* 协议 */
.agreement-row {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #666;
  flex-wrap: wrap;
}
.agreement-check { display: flex; align-items: center; gap: 4px; cursor: pointer; }
.agreement-check input { accent-color: #e1251b; cursor: pointer; }
.agreement-link { color: #e1251b; text-decoration: none; font-size: 12px; }
.agreement-link:hover { text-decoration: underline; }
.agreement-and { color: #666; }

/* 企业注册 */
.enterprise-reg {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 5px;
  cursor: pointer;
  font-size: 13px;
  color: #999;
}
.enterprise-reg:hover .ent-text { color: #e1251b; }

/* ===== Step 2 表单 ===== */
.form-row { display: flex; flex-direction: column; gap: 5px; }
.form-label { font-size: 13px; color: #666; }
.form-input {
  width: 100%;
  height: 42px;
  border: 1px solid #ddd;
  border-radius: 2px;
  padding: 0 14px;
  font-size: 14px;
  color: #333;
  outline: none;
  background: #fafafa;
  transition: border-color 0.2s, background 0.2s;
}
.form-input:focus { border-color: #e1251b; background: white; }
.form-input.error { border-color: #e1251b; background: #fff8f8; }
.form-input.success { border-color: #27ae60; }
.field-error { font-size: 12px; color: #e1251b; }
.field-ok { font-size: 12px; color: #27ae60; }
.form-hint { font-size: 11px; color: #bbb; }

.sms-row { display: flex; gap: 8px; }
.sms-code-input { flex: 1; }
.sms-btn {
  flex-shrink: 0;
  width: 110px;
  height: 42px;
  border: 1px solid #e1251b;
  background: white;
  color: #e1251b;
  border-radius: 2px;
  font-size: 13px;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.2s;
}
.sms-btn:hover:not(:disabled) { background: #fff5f5; }
.sms-btn:disabled { border-color: #ddd; color: #bbb; cursor: not-allowed; }

.pwd-wrap { position: relative; }
.pwd-wrap .form-input { padding-right: 42px; }
.pwd-toggle {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  font-size: 16px;
  line-height: 1;
}

/* 密码强度 */
.pwd-strength { display: flex; align-items: center; gap: 8px; margin-top: 4px; }
.strength-bars { display: flex; gap: 4px; }
.strength-bar {
  width: 40px;
  height: 4px;
  background: #e8e8e8;
  border-radius: 2px;
  transition: background 0.3s;
}
.strength-bar.active.weak { background: #e74c3c; }
.strength-bar.active.medium { background: #f39c12; }
.strength-bar.active.strong { background: #27ae60; }
.strength-label { font-size: 12px; }
.strength-label.weak { color: #e74c3c; }
.strength-label.medium { color: #f39c12; }
.strength-label.strong { color: #27ae60; }

.step2-btns { display: flex; gap: 12px; }
.step2-btns .reg-btn { flex: 1; }

/* ===== Step 3 成功 ===== */
.success-content { align-items: center; text-align: center; }
.success-icon { font-size: 64px; margin-bottom: 8px; }
.success-title { font-size: 24px; font-weight: 600; color: #333; margin-bottom: 8px; }
.success-desc { font-size: 14px; color: #666; margin-bottom: 16px; }
.success-account {
  background: #f9f9f9;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
  padding: 10px 24px;
  font-size: 14px;
  margin-bottom: 8px;
}
.account-label { color: #999; }
.account-val { color: #e1251b; font-weight: 600; font-size: 16px; }
.success-btns { display: flex; gap: 12px; width: 100%; }
.success-btns .reg-btn { flex: 1; letter-spacing: 0; }

/* ===== Footer ===== */
.reg-footer {
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
.footer-sep { font-size: 12px; color: #ccc; margin: 0 2px; }
.footer-copy { font-size: 12px; color: #999; }
</style>
