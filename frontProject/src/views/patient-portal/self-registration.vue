<template>
  <!-- 患者自助挂号页面：4步向导（选择科室→选择医生→选择类型→确认提交） -->
  <div class="portal-page">
    <el-card shadow="never" class="portal-card">
      <template #header>
        <div class="card-title">
          <el-icon><Plus /></el-icon>
          <span>自助挂号</span>
        </div>
      </template>

      <!-- 步骤指示器 -->
      <el-steps :active="success ? 5 : step - 1" align-center class="steps-bar">
        <el-step title="选择科室" />
        <el-step title="选择医生" />
        <el-step title="挂号类型" />
        <el-step title="症状描述" />
        <el-step title="确认提交" />
      </el-steps>

      <!-- Step 1: 选择科室 -->
      <div v-if="step === 1" class="step-container">
        <h3 class="step-title">请选择就诊科室</h3>
        <el-row :gutter="16">
          <el-col v-for="d in departments" :key="d.id" :span="6">
            <div class="option-card" :class="{ active: form.deptId === d.id }" @click="selectDept(d)">
              <div class="option-icon">
                <svg width="32" height="32" viewBox="0 0 48 48" fill="none">
                  <rect width="48" height="48" rx="10" fill="#eef2ff"/>
                  <path d="M24 12L24 36M12 24L36 24" stroke="#5d87ff" stroke-width="3" stroke-linecap="round"/>
                </svg>
              </div>
              <div class="option-name">{{ d.deptName }}</div>
              <div class="option-desc">{{ d.location || '' }}</div>
            </div>
          </el-col>
        </el-row>
        <div class="step-actions">
          <el-button type="primary" size="large" :disabled="!form.deptId" @click="step = 2">
            下一步
          </el-button>
        </div>
      </div>

      <!-- Step 2: 选择医生 -->
      <div v-if="step === 2" class="step-container">
        <h3 class="step-title">
          已选科室：<el-tag type="primary">{{ selectedDept?.deptName }}</el-tag>
          ，请选择医生
        </h3>
        <el-row :gutter="16">
          <el-col v-for="d in doctors" :key="d.id" :span="8">
            <div class="option-card doctor-card" :class="{ active: form.doctorId === d.id }" @click="form.doctorId = d.id">
              <div class="doctor-avatar">{{ d.doctorName?.charAt(0) }}</div>
              <div class="doctor-info">
                <div class="doctor-name">{{ d.doctorName }}</div>
                <div class="doctor-title">{{ d.title }}</div>
                <div class="doctor-specialty">{{ d.specialty }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <div v-if="doctors.length === 0" class="empty-hint">该科室暂无医生信息</div>
        <div class="step-actions">
          <el-button size="large" @click="step = 1">上一步</el-button>
          <el-button type="primary" size="large" :disabled="!form.doctorId" @click="step = 3">
            下一步
          </el-button>
        </div>
      </div>

      <!-- Step 3: 选择挂号类型 -->
      <div v-if="step === 3" class="step-container">
        <h3 class="step-title">请选择挂号类型</h3>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="option-card type-card" :class="{ active: form.regType === '普通' }" @click="form.regType = '普通'">
              <div class="type-name">普通号</div>
              <div class="type-fee">¥25.00</div>
              <div class="type-desc">适合常规就诊</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="option-card type-card" :class="{ active: form.regType === '专家' }" @click="form.regType = '专家'">
              <div class="type-name">专家号</div>
              <div class="type-fee">¥50.00</div>
              <div class="type-desc">适合疑难杂症</div>
            </div>
          </el-col>
        </el-row>
        <div class="step-actions">
          <el-button size="large" @click="step = 2">上一步</el-button>
          <el-button type="primary" size="large" :disabled="!form.regType" @click="step = 4">
            下一步
          </el-button>
        </div>
      </div>

      <!-- Step 4: 症状描述 -->
      <div v-if="step === 4" class="step-container">
        <h3 class="step-title">请描述您的症状</h3>
        <p class="step-hint">以下信息将提供给医生，帮助医生更快了解您的情况</p>
        <el-form label-position="top" class="symptom-form">
          <el-form-item label="主诉（哪里不舒服）" required>
            <el-input v-model="form.chiefComplaint" type="textarea" :rows="3" placeholder="例如：头痛3天，伴有发热、咳嗽" maxlength="500" show-word-limit />
          </el-form-item>
          <el-form-item label="现病史（详细描述）">
            <el-input v-model="form.presentIllness" type="textarea" :rows="4" placeholder="例如：3天前开始出现头痛，体温最高38.5℃，自行服用退烧药后缓解，但反复发作⋯⋯" maxlength="1000" show-word-limit />
          </el-form-item>
        </el-form>
        <div class="step-actions">
          <el-button size="large" @click="step = 3">上一步</el-button>
          <el-button type="primary" size="large" @click="step = 5">
            下一步
          </el-button>
        </div>
      </div>

      <!-- Step 5: 确认提交 -->
      <div v-if="step === 5" class="step-container">
        <h3 class="step-title">确认挂号信息</h3>
        <el-descriptions :column="1" border class="confirm-table">
          <el-descriptions-item label="就诊科室">{{ selectedDept?.deptName }}</el-descriptions-item>
          <el-descriptions-item label="就诊医生">{{ selectedDoctor?.doctorName }}</el-descriptions-item>
          <el-descriptions-item label="医生职称">{{ selectedDoctor?.title }}</el-descriptions-item>
          <el-descriptions-item label="专长">{{ selectedDoctor?.specialty }}</el-descriptions-item>
          <el-descriptions-item label="挂号类型">{{ form.regType === '专家' ? '专家号' : '普通号' }}</el-descriptions-item>
          <el-descriptions-item label="挂号费用">¥{{ form.regType === '专家' ? '50.00' : '25.00' }}</el-descriptions-item>
          <el-descriptions-item v-if="form.chiefComplaint" label="主诉">{{ form.chiefComplaint }}</el-descriptions-item>
          <el-descriptions-item v-if="form.presentIllness" label="现病史">{{ form.presentIllness }}</el-descriptions-item>
        </el-descriptions>
        <div class="step-actions">
          <el-button size="large" @click="step = 4">上一步</el-button>
          <el-button type="primary" size="large" :loading="submitting" @click="handleSubmit">
            确认挂号
          </el-button>
        </div>
      </div>

      <!-- 挂号成功 -->
      <div v-if="success" class="success-container">
        <el-result icon="success" title="挂号成功" sub-title="请按时到对应科室就诊，就诊时请出示身份证">
          <template #extra>
            <el-button type="primary" @click="$router.push('/patient-portal/registrations')">
              查看我的挂号
            </el-button>
            <el-button @click="resetForm">继续挂号</el-button>
          </template>
        </el-result>
      </div>
    </el-card>
  </div>
</template>

<script setup>
/**
 * SelfRegistration - 患者自助挂号页面组件
 * 功能：4步向导流程，让患者自行选择科室、医生、挂号类型并确认提交
 */
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDepartmentList } from '@/api/department'
import { getDoctorByDept } from '@/api/doctor'
import { selfRegistration } from '@/api/patient-portal'

const step = ref(1)
const submitting = ref(false)
const success = ref(false)
const departments = ref([])
const doctors = ref([])
const form = ref({ deptId: null, doctorId: null, regType: null, chiefComplaint: '', presentIllness: '' })

const selectedDept = computed(() => departments.value.find(d => d.id === form.value.deptId))
const selectedDoctor = computed(() => doctors.value.find(d => d.id === form.value.doctorId))

/** 选择科室后，自动加载该科室下的医生列表 */
const selectDept = (dept) => {
  form.value.deptId = dept.id
  form.value.doctorId = null
  form.value.regType = null
  loadDoctors()
}

/** 加载指定科室下的医生列表 */
const loadDoctors = async () => {
  if (!form.value.deptId) return
  try {
    const res = await getDoctorByDept(form.value.deptId)
    doctors.value = res.data || []
  } catch {
    doctors.value = []
  }
}

/** 提交挂号（带防重复提交保护） */
const handleSubmit = async () => {
  // 防止重复提交：正在提交中或已挂号成功则直接返回
  if (submitting.value || success.value) return
  submitting.value = true
  try {
    await selfRegistration({
      doctorId: form.value.doctorId,
      deptId: form.value.deptId,
      regType: form.value.regType,
      chiefComplaint: form.value.chiefComplaint,
      presentIllness: form.value.presentIllness
    })
    success.value = true
    ElMessage.success('您已挂号成功，请按时就诊')
  } catch {
    // request.js 已处理错误提示
  } finally {
    submitting.value = false
  }
}

/** 重置表单，继续挂号 */
const resetForm = () => {
  step.value = 1
  success.value = false
  form.value = { deptId: null, doctorId: null, regType: null, chiefComplaint: '', presentIllness: '' }
  doctors.value = []
}

/** 页面加载时获取科室列表 */
onMounted(async () => {
  try {
    const res = await getDepartmentList()
    departments.value = res.data || []
  } catch {
    departments.value = []
  }
})
</script>

<style scoped lang="scss">
.portal-card {
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

.steps-bar {
  margin: 24px 0 32px;
  padding: 0 40px;
}

.step-container {
  min-height: 300px;
  padding: 0 8px;
}

.step-title {
  font-size: 16px;
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 20px;
}

.option-card {
  border: 2px solid #e8ecf1;
  border-radius: 12px;
  padding: 20px 16px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  margin-bottom: 16px;

  &:hover {
    border-color: #5d87ff;
    background: #f8faff;
  }

  &.active {
    border-color: #5d87ff;
    background: #eef2ff;
  }

  .option-icon { margin-bottom: 10px; }
  .option-name { font-size: 15px; font-weight: 600; color: #2d3748; }
  .option-desc { font-size: 12px; color: #94a3b8; margin-top: 4px; }
}

.doctor-card {
  display: flex;
  align-items: center;
  text-align: left;
  gap: 12px;
  padding: 16px;

  .doctor-avatar {
    width: 44px;
    height: 44px;
    border-radius: 50%;
    background: linear-gradient(135deg, #5d87ff, #3b5fcf);
    color: #fff;
    font-size: 18px;
    font-weight: 600;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .doctor-info {
    flex: 1;
    .doctor-name { font-size: 15px; font-weight: 600; color: #2d3748; }
    .doctor-title { font-size: 12px; color: #5d87ff; margin-top: 2px; }
    .doctor-specialty { font-size: 12px; color: #94a3b8; margin-top: 2px; }
  }
}

.type-card {
  padding: 28px 16px;
  text-align: center;

  .type-name { font-size: 18px; font-weight: 700; color: #2d3748; }
  .type-fee { font-size: 28px; font-weight: 700; color: #5d87ff; margin: 8px 0; }
  .type-desc { font-size: 13px; color: #94a3b8; }
}

.step-hint {
  font-size: 13px;
  color: #94a3b8;
  margin: -12px 0 20px;
}

.symptom-form {
  max-width: 600px;
  margin: 0 auto;
}

.confirm-table {
  max-width: 500px;
  margin: 0 auto;
}

.step-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 28px;
  padding-bottom: 16px;
}

.empty-hint {
  text-align: center;
  color: #94a3b8;
  padding: 40px 0;
  font-size: 14px;
}

.success-container {
  padding: 20px 0;
}
</style>
