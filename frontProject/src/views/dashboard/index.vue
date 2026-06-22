<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card shadow="never" class="stat-card stat-card-reg">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="28"><Tickets /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ stats.registrationCount }}</div>
              <div class="card-label">今日挂号</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card stat-card-pat">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="28"><UserFilled /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ stats.patientCount }}</div>
              <div class="card-label">今日就诊</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card stat-card-wait">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="28"><Clock /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ stats.registrationWaitCount }}</div>
              <div class="card-label">待诊人数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card stat-card-income">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="28"><Money /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">¥{{ stats.todayIncome }}</div>
              <div class="card-label">今日收入</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="14">
        <el-card shadow="never">
          <template #header>
            <div class="section-header">
              <span>快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <el-button type="primary" plain icon="Tickets" @click="$router.push('/registration')">挂号管理</el-button>
            <el-button type="success" plain icon="UserFilled" @click="$router.push('/patient')">患者管理</el-button>
            <el-button type="warning" plain icon="FirstAidKit" @click="$router.push('/drug')">药品管理</el-button>
            <el-button type="danger" plain icon="Money" @click="$router.push('/payment')">收费管理</el-button>
            <el-button type="info" plain icon="Notebook" @click="$router.push('/medical-record')">病历管理</el-button>
            <el-button type="primary" plain icon="Document" @click="$router.push('/prescription')">处方管理</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <template #header>
            <div class="section-header">
              <span>系统信息</span>
            </div>
          </template>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item label="系统名称">东软云医院HIS系统</el-descriptions-item>
            <el-descriptions-item label="系统版本">v1.0.0</el-descriptions-item>
            <el-descriptions-item label="当前时间">{{ currentTime }}</el-descriptions-item>
            <el-descriptions-item label="登录用户">{{ authStore.realName || authStore.username }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { getDashboardStats } from '@/api/dashboard'

const authStore = useAuthStore()

const stats = ref({
  registrationCount: 0,
  patientCount: 0,
  registrationWaitCount: 0,
  todayIncome: '0.00'
})

const currentTime = ref('')
let timer = null

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN')
}

const fetchStats = async () => {
  try {
    const res = await getDashboardStats()
    if (res.data) {
      stats.value = res.data
    }
  } catch (e) {
    // 出错时使用默认值
  }
}

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
  fetchStats()
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped lang="scss">
.dashboard {
  .stat-cards {
    .stat-card {
      border-radius: 12px;
      border: none;

      :deep(.el-card__body) {
        padding: 20px;
      }

      .card-content {
        display: flex;
        align-items: center;
        gap: 16px;
      }

      .card-icon {
        width: 52px;
        height: 52px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;
      }

      .card-info {
        flex: 1;
      }

      .card-value {
        font-size: 28px;
        font-weight: 700;
        color: #2d3748;
        line-height: 1.2;
      }

      .card-label {
        font-size: 14px;
        color: #94a3b8;
        margin-top: 2px;
      }

      &.stat-card-reg .card-icon {
        background: #eef2ff;
        color: #5d87ff;
      }
      &.stat-card-pat .card-icon {
        background: #e6f7e6;
        color: #38a169;
      }
      &.stat-card-wait .card-icon {
        background: #fff5e6;
        color: #dd6b20;
      }
      &.stat-card-income .card-icon {
        background: #fde8e8;
        color: #e53e3e;
      }
    }
  }

  .section-header {
    font-size: 15px;
    font-weight: 600;
    color: #2d3748;
  }

  .quick-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
}
</style>
