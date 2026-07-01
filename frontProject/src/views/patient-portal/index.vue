<template>
  <!-- 患者门户首页组件：展示个人信息、就诊概览和快捷入口 -->
  <div class="patient-home">
    <el-row :gutter="20">
      <!-- 左侧：个人信息卡片 -->
      <el-col :span="8">
        <el-card shadow="never" class="info-card">
          <template #header>
            <div class="card-title">
              <el-icon><UserFilled /></el-icon>
              <span>个人信息</span>
            </div>
          </template>
          <div class="patient-info">
            <!-- 患者头像（姓名首字） -->
            <div class="avatar">{{ authStore.realName?.charAt(0) }}</div>
            <div class="name">{{ authStore.realName }}</div>
            <!-- 身份证号和手机号 -->
            <div class="detail">身份证号: {{ patientInfo.idCard || '-' }}</div>
            <div class="detail">手机号: {{ patientInfo.phone || '-' }}</div>
          </div>
        </el-card>
      </el-col>
      <!-- 右侧：就诊概览和快捷入口 -->
      <el-col :span="16">
        <!-- 就诊概览统计卡片 -->
        <el-card shadow="never" class="info-card">
          <template #header>
            <div class="card-title">
              <el-icon><DataAnalysis /></el-icon>
              <span>就诊概览</span>
            </div>
          </template>
          <el-row :gutter="20" class="stats-row">
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-value">{{ stats.registrations }}</div>
                <div class="stat-label">挂号总数</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-value">{{ stats.records }}</div>
                <div class="stat-label">病历数</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-value">{{ stats.prescriptions }}</div>
                <div class="stat-label">处方数</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
        <!-- 快捷入口卡片 -->
        <el-card shadow="never" class="info-card" style="margin-top: 16px;">
          <template #header>
            <div class="card-title">
              <el-icon><Link /></el-icon>
              <span>快捷入口</span>
            </div>
          </template>
          <div class="quick-links">
            <el-button type="primary" plain icon="Tickets" @click="$router.push('/patient-portal/registrations')">我的挂号</el-button>
            <el-button type="success" plain icon="Notebook" @click="$router.push('/patient-portal/records')">我的病历</el-button>
            <el-button type="warning" plain icon="Document" @click="$router.push('/patient-portal/prescriptions')">我的处方</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
/**
 * PatientHome - 患者门户首页组件
 * 功能：展示患者个人信息、挂号/病历/处方统计数据及快捷导航入口
 */
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { getPatientInfo, getMyRegistrations, getMyRecords, getMyPrescriptions } from '@/api/patient-portal'

const authStore = useAuthStore()

/** 患者个人信息（身份证号和手机号） */
const patientInfo = ref({ idCard: '-', phone: '-' })
/** 就诊统计数据（挂号总数、病历数、处方数） */
const stats = ref({ registrations: 0, records: 0, prescriptions: 0 })

/** 组件挂载时：获取患者信息及各项统计 */
onMounted(async () => {
  try {
    // 获取患者个人信息
    const infoRes = await getPatientInfo()
    if (infoRes.data) {
      patientInfo.value = {
        idCard: infoRes.data.idCard || '-',
        phone: infoRes.data.phone || '-'
      }
    }

    // 获取各项统计（通过分页查询获取总条数）
    const [regRes, recRes, preRes] = await Promise.all([
      getMyRegistrations({ page: 1, size: 1 }),
      getMyRecords({ page: 1, size: 1 }),
      getMyPrescriptions({ page: 1, size: 1 })
    ])
    stats.value = {
      registrations: regRes.data?.total || 0,
      records: recRes.data?.total || 0,
      prescriptions: preRes.data?.total || 0
    }
  } catch (e) {
    // ignore
  }
})
</script>

<style scoped lang="scss">
.patient-home {
  .info-card {
    border-radius: 12px;
    border: 1px solid #e8ecf1;
    background: #fff;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);

    .card-title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 15px;
      font-weight: 600;
      color: #2d3748;
    }
  }

  .patient-info {
    text-align: center;
    padding: 8px 0;

    .avatar {
      width: 64px;
      height: 64px;
      border-radius: 50%;
      background: linear-gradient(135deg, #5d87ff, #3b5fcf);
      color: #fff;
      font-size: 24px;
      font-weight: 600;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 12px;
    }

    .name {
      font-size: 20px;
      font-weight: 600;
      color: #2d3748;
      margin-bottom: 12px;
    }

    .detail {
      font-size: 13px;
      color: #64748b;
      margin-bottom: 6px;
    }
  }

  .stats-row {
    .stat-item {
      text-align: center;
      padding: 16px 0;
      background: #f8f9fb;
      border-radius: 8px;

      .stat-value {
        font-size: 28px;
        font-weight: 700;
        color: #5d87ff;
      }
      .stat-label {
        font-size: 13px;
        color: #64748b;
        margin-top: 4px;
      }
    }
  }

  .quick-links {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
  }
}
</style>
