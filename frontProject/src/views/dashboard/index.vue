<template>
  <!-- 首页仪表盘组件：展示统计卡片、快捷操作和系统信息 -->
  <div class="dashboard">
    <!-- 顶部统计卡片行：挂号总数、已就诊数、待诊人数、总收入 -->
    <el-row :gutter="20" class="stat-cards">
      <!-- 今日挂号统计卡片 -->
      <el-col :span="6">
        <el-card shadow="never" class="stat-card stat-card-reg">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="28"><Tickets /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ stats.registrationCount }}</div>
              <div class="card-label">挂号总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <!-- 今日就诊统计卡片 -->
      <el-col :span="6">
        <el-card shadow="never" class="stat-card stat-card-pat">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="28"><UserFilled /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ stats.patientCount }}</div>
              <div class="card-label">已就诊数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <!-- 待诊人数统计卡片 -->
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
      <!-- 今日收入统计卡片 -->
      <el-col :span="6">
        <el-card shadow="never" class="stat-card stat-card-income">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="28"><Money /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">¥{{ stats.todayIncome }}</div>
              <div class="card-label">总收入</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部功能区：快捷操作 + 系统信息 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 快捷操作区域 -->
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
      <!-- 系统信息区域 -->
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
/**
 * Dashboard - 首页仪表盘组件
 * 功能：展示今日统计数据（挂号数/就诊数/待诊数/收入）、快捷操作入口和系统信息
 */
import { ref, onMounted, onUnmounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { getDashboardStats } from '@/api/dashboard'

const authStore = useAuthStore()

/** 统计数据对象：包含今日挂号、就诊、待诊和收入 */
const stats = ref({
  registrationCount: 0,
  patientCount: 0,
  registrationWaitCount: 0,
  todayIncome: '0.00'
})

/** 当前系统时间（实时更新） */
const currentTime = ref('')
let timer = null

/** 更新当前时间为本地时间字符串 */
const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN')
}

/** 从后端获取仪表盘统计数据 */
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

/** 组件挂载时：初始化时间并开始定时刷新，获取统计数据 */
onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
  fetchStats()
})

/** 组件卸载时：清除时间定时器 */
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
