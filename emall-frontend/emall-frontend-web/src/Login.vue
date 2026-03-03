<template>
  <el-form
    ref="ruleFormRef"
    style="max-width: 600px"
    :model="ruleForm"
    status-icon
    :rules="rules"
    label-width="auto"
    class="demo-ruleForm"
  >
    <el-form-item label="登录名" prop="loginName">
      <el-input v-model="ruleForm.loginName" />
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input v-model="ruleForm.password" type="password" autocomplete="off" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm(ruleFormRef)">
        提交
      </el-button>
      <el-button @click="resetForm(ruleFormRef)">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'

import type { FormInstance, FormRules } from 'element-plus'

import axios, { Axios } from 'axios'


const ruleFormRef = ref<FormInstance>()

const checkLoginName = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('请输入登录名'))
  }
  callback()
}

const validatePassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (ruleForm.password !== '') {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField('validatePassword')
    }
    callback()
  }
}

const ruleForm = reactive({
  loginName: '',
  password: '',
})

const rules = reactive<FormRules<typeof ruleForm>>({
  loginName: [{ validator: checkLoginName, trigger: 'blur' }],
  password: [{ validator: validatePassword, trigger: 'blur' }],
})

axios.defaults.baseURL = 'http://localhost:80'
axios.defaults.headers.post['Content-Type'] = 'application/json'
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*'

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      console.log('submit!')
      axios.post('/api/mock/login', ruleForm).then((res) => {
        console.log(res)
      })
    } else {
      console.log('error submit!')
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>
