<template>
  <div class="login-page">
    <div class="login-left">
      <div class="brand">
        <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
          <rect width="48" height="48" rx="12" fill="#5d87ff"/>
          <path d="M24 12L24 36M12 24L36 24" stroke="white" stroke-width="4" stroke-linecap="round"/>
        </svg>
        <div class="brand-text">
          <h1>东软云医院</h1>
          <p>HIS门诊管理系统</p>
        </div>
      </div>
      <div class="features">
        <div class="feature-item">
          <div class="dot"></div>
          <span>患者挂号与就诊管理</span>
        </div>
        <div class="feature-item">
          <div class="dot"></div>
          <span>电子病历与处方管理</span>
        </div>
        <div class="feature-item">
          <div class="dot"></div>
          <span>检查检验与收费一体化</span>
        </div>
      </div>
    </div>
    <div class="login-right">
      <div class="login-card">
        <h2 class="login-title">欢迎登录</h2>
        <p class="login-subtitle">请选择登录方式</p>

        <el-tabs v-model="loginType" class="login-tabs" stretch>
          <el-tab-pane label="管理员 / 医生" name="staff" />
          <el-tab-pane label="患者登录" name="patient" />
        </el-tabs>

        <!-- 员工登录 -->
        <el-form
          v-if="loginType === 'staff'"
          ref="formRef"
          :model="staffForm"
          :rules="staffRules"
          @keyup.enter="handleStaffLogin"
        >
          <el-form-item prop="username">
            <el-input v-model="staffForm.username" placeholder="用户名" :prefix-icon="User" size="large" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="staffForm.password" type="password" placeholder="密码" :prefix-icon="Lock" size="large" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" class="login-btn" size="large" @click="handleStaffLogin" block>
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 患者登录 -->
        <el-form
          v-if="loginType === 'patient'"
          ref="patientFormRef"
          :model="patientForm"
          :rules="patientRules"
          @keyup.enter="handlePatientLogin"
        >
          <el-form-item prop="idCard">
            <el-input v-model="patientForm.idCard" placeholder="身份证号" :prefix-icon="User" size="large" maxlength="18" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="patientForm.password" type="password" placeholder="密码" :prefix-icon="Lock" size="large" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" class="login-btn" size="large" @click="handlePatientLogin" block>
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-tips">
          <p>管理员: admin / admin123</p>
          <p>医生: doctor / 123456</p>
          <p>患者: 身份证号 / 123456</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref(null)
const patientFormRef = ref(null)
const loading = ref(false)
const loginType = ref('staff')

const staffForm = reactive({ username: '', password: '' })
const patientForm = reactive({ idCard: '', password: '' })

const staffRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const patientRules = {
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { min: 18, max: 18, message: '身份证号为18位', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleStaffLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await authStore.login(staffForm)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (err) {
    // 错误已在 request.js 响应拦截器或 auth store 中处理
  } finally { loading.value = false }
}

const handlePatientLogin = async () => {
  await patientFormRef.value.validate()
  loading.value = true
  try {
    await authStore.patientLogin(patientForm)
    ElMessage.success('登录成功')
    router.push('/patient-portal')
  } catch (err) {
    // 错误已在 request.js 响应拦截器或 auth store 中处理
  } finally { loading.value = false }
}
</script>

<style scoped lang="scss">
.login-page {
  height: 100vh;
  display: flex;
}

/* 左侧品牌区 */
.login-left {
  width: 440px;
  background: linear-gradient(180deg, #1e293b 0%, #0f172a 100%);
  padding: 48px 48px;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -50%;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle, rgba(93, 135, 255, 0.08) 0%, transparent 70%);
    pointer-events: none;
  }

  .brand {
    display: flex;
    align-items: center;
    gap: 16px;
    position: relative;

    .brand-text {
      h1 {
        color: #fff;
        font-size: 24px;
        font-weight: 700;
        margin: 0;
      }
      p {
        color: #94a3b8;
        font-size: 13px;
        margin: 4px 0 0;
        letter-spacing: 1px;
      }
    }
  }

  .features {
    margin-top: auto;
    margin-bottom: 120px;
    display: flex;
    flex-direction: column;
    gap: 28px;
    position: relative;

    .feature-item {
      display: flex;
      align-items: center;
      gap: 16px;
      color: #cbd5e1;
      font-size: 15px;

      .dot {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: #5d87ff;
        flex-shrink: 0;
      }
    }
  }
}

/* 右侧登录区 */
.login-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f6fa;
}

.login-card {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
  border: 1px solid #e8ecf1;
}

.login-title {
  font-size: 22px;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 4px;
}

.login-subtitle {
  color: #94a3b8;
  font-size: 14px;
  margin: 0 0 24px;
}

.login-tabs {
  margin-bottom: 24px;

  :deep(.el-tabs__item) {
    font-size: 14px;
    height: 36px;
    line-height: 36px;
    color: #94a3b8;
    &.is-active { color: #5d87ff; font-weight: 600; }
  }

  :deep(.el-tabs__active-bar) {
    background: #5d87ff;
    height: 2px;
  }
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 15px;
  border-radius: 8px;
}

.login-tips {
  margin-top: 20px;
  text-align: center;

  p {
    color: #94a3b8;
    font-size: 12px;
    margin: 4px 0;
  }
}
</style>
