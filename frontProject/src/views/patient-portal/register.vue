<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">患者注册</h2>
      <el-form :model="form" ref="formRef" :rules="rules" label-width="100px">
        <el-form-item label="姓名" prop="patientName"><el-input v-model="form.patientName" /></el-form-item>
        <el-form-item label="身份证号" prop="idCard"><el-input v-model="form.idCard" maxlength="18" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" maxlength="11" /></el-form-item>
        <el-form-item label="密码" prop="password"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd"><el-input v-model="form.confirmPwd" type="password" show-password /></el-form-item>
        <el-form-item label="性别"><el-radio-group v-model="form.gender"><el-radio :value="1">男</el-radio><el-radio :value="0">女</el-radio></el-radio-group></el-form-item>
        <el-form-item label="年龄"><el-input-number v-model="form.age" :min="0" :max="150" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="handleRegister" style="width:100%">注册</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align:center">
        <router-link to="/login">已有账号？去登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { patientRegister } from '@/api/auth'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref(null)
const submitting = ref(false)
const form = reactive({ patientName: '', idCard: '', phone: '', password: '', confirmPwd: '', gender: 1, age: 30, address: '' })

const validateIdCard = (rule, value, callback) => {
  if (!value || value.length !== 18) callback(new Error('身份证号应为18位'))
  else callback()
}
const validateConfirm = (rule, value, callback) => {
  if (value !== form.password) callback(new Error('两次密码不一致'))
  else callback()
}
const rules = {
  patientName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  idCard: [{ required: true, validator: validateIdCard, trigger: 'blur' }],
  password: [{ required: true, min: 4, message: '密码至少4位', trigger: 'blur' }],
  confirmPwd: [{ required: true, validator: validateConfirm, trigger: 'blur' }]
}

const handleRegister = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    const res = await patientRegister({
      patientName: form.patientName,
      idCard: form.idCard,
      phone: form.phone,
      password: form.password,
      gender: form.gender,
      age: form.age,
      address: form.address
    })
    const data = res.data
    authStore.token = data.token
    authStore.username = data.username
    authStore.realName = data.realName
    authStore.role = data.role
    authStore.idCard = data.idCard || ''
    authStore.phone = data.phone || ''
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    localStorage.setItem('realName', data.realName)
    localStorage.setItem('role', data.role)
    if (data.idCard) localStorage.setItem('idCard', data.idCard)
    if (data.phone) localStorage.setItem('phone', data.phone)
    ElMessage.success('注册成功')
    router.push('/patient-portal')
  } catch { }
  finally { submitting.value = false }
}
</script>

<style scoped lang="scss">
.login-container { display:flex; justify-content:center; align-items:center; min-height:100vh; background:#f0f4ff; }
.login-card { width:460px; background:#fff; border-radius:12px; padding:40px; box-shadow:0 2px 12px rgba(0,0,0,0.08); }
.login-title { text-align:center; margin-bottom:24px; font-size:24px; color:#2563EB; }
</style>
