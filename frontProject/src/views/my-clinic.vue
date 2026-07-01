<template>
  <!-- 医生接诊工作台：展示待诊/已诊患者列表，支持接诊、写病历、开处方 -->
  <div class="clinic-page">
    <div class="page-header">
      <h2>接诊管理</h2>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-select v-model="statusFilter" placeholder="全部状态" clearable style="width: 130px;" @change="handleFilter">
        <el-option label="全部" :value="''" />
        <el-option label="待诊" :value="0" />
        <el-option label="已诊" :value="1" />
        <el-option label="已取消" :value="2" />
      </el-select>
      <el-input v-model="searchName" placeholder="患者姓名" clearable style="width: 160px;" @keyup.enter="handleFilter" />
      <el-button type="primary" icon="Search" @click="handleFilter">查询</el-button>
      <el-button icon="Refresh" @click="resetFilter">重置</el-button>
    </div>

    <!-- 患者列表 -->
    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="单号" width="70" />
      <el-table-column prop="patientName" label="患者" width="90" />
      <el-table-column prop="gender" label="性别" width="60">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="60" />
      <el-table-column prop="phone" label="手机号" width="120" />
      <el-table-column prop="deptName" label="科室" width="100" />
      <el-table-column prop="regType" label="类型" width="70">
        <template #default="{ row }">
          <el-tag :type="row.regType === '专家' ? 'warning' : 'info'" size="small">{{ row.regType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="regFee" label="费用" width="65">
        <template #default="{ row }">¥{{ row.regFee }}</template>
      </el-table-column>
      <el-table-column prop="regTime" label="挂号时间" width="165" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="{0:'warning',1:'success',2:'info'}[row.status]" size="small">
            {{ {0:'待诊',1:'已诊',2:'已取消'}[row.status] || '未知' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" size="small" type="primary" @click="startConsultation(row)">
            接诊
          </el-button>
          <el-button v-else size="small" type="default" @click="viewConsultation(row)">
            查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        :page-sizes="[10, 20, 50]"
        @size-change="fetchData"
        @current-change="fetchData"
      />
    </div>

    <!-- ========== 接诊对话框 ========== -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="860px"
      :close-on-click-modal="false"
      destroy-on-close
      @close="handleDialogClose"
    >
      <!-- 患者信息卡片 -->
      <el-card shadow="never" class="patient-info-card">
        <div class="patient-info-header">
          <div class="patient-avatar">{{ currentPatient?.patientName?.charAt(0) }}</div>
          <div class="patient-detail">
            <div class="patient-name">{{ currentPatient?.patientName }}</div>
            <div class="patient-meta">
              <span>{{ currentPatient?.gender === 1 ? '男' : '女' }}</span>
              <span class="divider">|</span>
              <span>{{ currentPatient?.age }}岁</span>
              <span class="divider">|</span>
              <span>{{ currentPatient?.phone }}</span>
            </div>
          </div>
          <div class="patient-reg-info">
            <el-tag size="small">{{ currentPatient?.deptName }}</el-tag>
            <el-tag size="small" :type="currentPatient?.regType === '专家' ? 'warning' : 'info'" class="reg-tag">
              {{ currentPatient?.regType }}号
            </el-tag>
            <div class="reg-time">挂号时间：{{ currentPatient?.regTime }}</div>
          </div>
        </div>
      </el-card>

      <!-- 患者自述症状 -->
      <el-card v-if="currentPatient?.chiefComplaint" shadow="never" class="form-section symptom-section">
        <template #header>
          <span class="section-title">患者自述</span>
        </template>
        <div class="symptom-content">
          <div class="symptom-item">
            <span class="symptom-label">主诉：</span>
            <span class="symptom-text">{{ currentPatient.chiefComplaint }}</span>
          </div>
          <div v-if="currentPatient?.presentIllness" class="symptom-item">
            <span class="symptom-label">现病史：</span>
            <span class="symptom-text">{{ currentPatient.presentIllness }}</span>
          </div>
        </div>
      </el-card>

      <!-- 病历表单 -->
      <el-card shadow="never" class="form-section">
        <template #header>
          <span class="section-title">病历记录</span>
        </template>
        <el-form :model="recordForm" label-width="100px" size="default">
          <el-form-item label="主诉" required>
            <el-input v-model="recordForm.chiefComplaint" type="textarea" :rows="2" placeholder="患者的主要症状和就诊原因" />
          </el-form-item>
          <el-form-item label="现病史">
            <el-input v-model="recordForm.presentIllness" type="textarea" :rows="3" placeholder="发病情况、症状演变、诊疗经过等" />
          </el-form-item>
          <el-form-item label="诊断" required>
            <el-input v-model="recordForm.diagnosis" type="textarea" :rows="2" placeholder="诊断结论" />
          </el-form-item>
          <el-form-item label="治疗方案">
            <el-input v-model="recordForm.treatmentPlan" type="textarea" :rows="2" placeholder="治疗建议、用药方案等" />
          </el-form-item>
        </el-form>
        <div class="form-actions">
          <el-button type="primary" :loading="savingRecord" @click="saveMedicalRecord">
            保存病历
          </el-button>
        </div>
      </el-card>

      <!-- 处方区域 -->
      <el-card shadow="never" class="form-section">
        <template #header>
          <span class="section-title">处方开药</span>
        </template>

        <!-- 药品添加行 -->
        <el-row :gutter="12" class="drug-add-row">
          <el-col :span="8">
            <el-select v-model="newDrug.drugId" filterable placeholder="搜索药品" style="width:100%" @change="onDrugSelect">
              <el-option
                v-for="d in drugOptions"
                :key="d.id"
                :label="`${d.drugName} (${d.specification})`"
                :value="d.id"
              />
            </el-select>
          </el-col>
          <el-col :span="3">
            <el-input-number v-model="newDrug.quantity" :min="1" :max="999" placeholder="数量" style="width:100%" controls-position="right" />
          </el-col>
          <el-col :span="3">
            <el-input :model-value="'¥' + (newDrug.unitPrice || '0.00')" disabled placeholder="单价" />
          </el-col>
          <el-col :span="8">
            <el-input v-model="newDrug.usageMethod" placeholder="用法用量，如口服每日3次每次1粒" />
          </el-col>
          <el-col :span="2">
            <el-button type="primary" :disabled="!newDrug.drugId || !newDrug.quantity" @click="addDrugItem">添加</el-button>
          </el-col>
        </el-row>

        <!-- 已选药品表格 -->
        <el-table :data="drugItems" border stripe size="small" style="width:100%; margin-top:12px;" max-height="240">
          <el-table-column prop="drugName" label="药品" min-width="140" />
          <el-table-column prop="specification" label="规格" width="100" />
          <el-table-column prop="unitPrice" label="单价" width="80">
            <template #default="{ row }">¥{{ row.unitPrice }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="60" />
          <el-table-column label="金额" width="80">
            <template #default="{ row }">¥{{ (row.unitPrice * row.quantity).toFixed(2) }}</template>
          </el-table-column>
          <el-table-column prop="usageMethod" label="用法" min-width="150" />
          <el-table-column label="操作" width="60" fixed="right">
            <template #default="{ $index }">
              <el-button size="small" type="danger" link @click="removeDrugItem($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 合计 -->
        <div v-if="drugItems.length > 0" class="drug-total">
          合计金额：<span class="total-amount">¥{{ drugTotal.toFixed(2) }}</span>
        </div>

        <div class="form-actions">
          <el-button type="success" :disabled="drugItems.length === 0" :loading="savingPrescription" @click="savePrescription">
            保存处方
          </el-button>
          <span v-if="prescriptionSaved" class="saved-hint">
            <el-icon><Check /></el-icon> 处方已保存
          </span>
        </div>
      </el-card>

      <!-- 底部操作 -->
      <template #footer>
        <div class="dialog-footer">
          <el-button size="large" @click="dialogVisible = false">取消</el-button>
          <el-button
            size="large"
            type="primary"
            :loading="completing"
            :disabled="!recordForm.chiefComplaint || !recordForm.diagnosis"
            @click="completeConsultation"
          >
            {{ isDone ? '已完成的诊疗' : '完成诊疗' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * MyClinic - 医生接诊工作台页面组件
 *
 * 功能：展示当前医生名下的患者列表，点击"接诊"打开接诊对话框，
 * 在对话框中可查看患者信息、填写病历、开处方，最后完成诊疗。
 *
 * 使用到的后端接口（全部已存在）：
 * - GET  /api/registration/list           → 获取挂号列表（JWT自动过滤医生）
 * - PUT  /api/registration/{id}/status    → 更新挂号状态
 * - POST /api/medical-record              → 新增病历
 * - GET  /api/medical-record/registration/{regId} → 按挂号查询病历
 * - POST /api/prescription                → 新增处方（返回处方ID）
 * - POST /api/prescription/detail         → 新增处方明细
 * - GET  /api/drug/list                   → 获取药品列表
 */
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Check } from '@element-plus/icons-vue'
import { getRegistrationList, updateRegistrationStatus } from '@/api/registration'
import { addMedicalRecord, getMedicalRecordByRegistration } from '@/api/medicalRecord'
import { addPrescription, addPrescriptionDetail, getPrescriptionList, getPrescriptionDetails } from '@/api/prescription'
import { getDrugList } from '@/api/drug'

// ========== 列表状态 ==========
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const statusFilter = ref('')   // 默认显示全部
const searchName = ref('')

/** 获取患者列表 */
const fetchData = async () => {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (statusFilter.value !== null && statusFilter.value !== undefined && statusFilter.value !== '') {
      params.status = statusFilter.value
    }
    if (searchName.value) {
      params.patientName = searchName.value
    }
    const res = await getRegistrationList(params)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } catch {
    // request.js 已处理错误提示
  } finally {
    loading.value = false
  }
}

/** 筛选或搜索时重置到第一页 */
const handleFilter = () => {
  pageNum.value = 1
  fetchData()
}

/** 重置搜索条件 */
const resetFilter = () => {
  statusFilter.value = ''
  searchName.value = ''
  pageNum.value = 1
  fetchData()
}

// ========== 接诊对话框状态 ==========
const dialogVisible = ref(false)
const dialogTitle = ref('')
const currentPatient = ref(null)           // 当前接诊的挂号记录
const isDone = ref(false)                  // 是否已是已完成状态（查看模式）
const recordId = ref(null)                 // 已有的病历ID（编辑时）
const prescriptionSaved = ref(false)       // 处方是否已保存
const savedPrescriptionId = ref(null)      // 已保存的处方ID

// 提交状态
const savingRecord = ref(false)
const savingPrescription = ref(false)
const completing = ref(false)

/** 病历表单数据 */
const recordForm = reactive({
  chiefComplaint: '',
  presentIllness: '',
  diagnosis: '',
  treatmentPlan: ''
})

/** 药品选项列表 */
const drugOptions = ref([])

/** 新药品输入 */
const newDrug = reactive({
  drugId: null,
  drugName: '',
  specification: '',
  unitPrice: 0,
  quantity: 1,
  usageMethod: ''
})

/** 已添加的药品明细列表 */
const drugItems = ref([])

/** 处方合计金额 */
const drugTotal = computed(() => {
  return drugItems.value.reduce((sum, item) => sum + (item.unitPrice * item.quantity), 0)
})

/** 选中药品后自动填充信息 */
const onDrugSelect = (drugId) => {
  const drug = drugOptions.value.find(d => d.id === drugId)
  if (drug) {
    newDrug.drugName = drug.drugName
    newDrug.specification = drug.specification
    newDrug.unitPrice = drug.unitPrice
  }
}

/** 添加药品到列表中 */
const addDrugItem = () => {
  if (!newDrug.drugId || !newDrug.quantity) return
  // 检查是否已添加过该药品
  const exists = drugItems.value.find(item => item.drugId === newDrug.drugId)
  if (exists) {
    ElMessage.warning('该药品已添加，请修改数量或删除后重新添加')
    return
  }
  drugItems.value.push({
    drugId: newDrug.drugId,
    drugName: newDrug.drugName,
    specification: newDrug.specification,
    unitPrice: newDrug.unitPrice,
    quantity: newDrug.quantity,
    usageMethod: newDrug.usageMethod
  })
  // 重置输入
  newDrug.drugId = null
  newDrug.drugName = ''
  newDrug.specification = ''
  newDrug.unitPrice = 0
  newDrug.quantity = 1
  newDrug.usageMethod = ''
}

/** 删除药品项 */
const removeDrugItem = (index) => {
  drugItems.value.splice(index, 1)
}

// ========== 对话操作 ==========

/**
 * 点击"接诊"按钮，打开接诊对话框
 * 加载药品列表、检查是否有已有病历和处方
 */
const startConsultation = async (row) => {
  currentPatient.value = row
  isDone.value = false
  prescriptionSaved.value = false
  savedPrescriptionId.value = null
  recordId.value = null
  dialogTitle.value = `接诊 - ${row.patientName}`

  // 清空表单，用患者自述预填主诉和现病史
  recordForm.chiefComplaint = row.chiefComplaint || ''
  recordForm.presentIllness = row.presentIllness || ''
  recordForm.diagnosis = ''
  recordForm.treatmentPlan = ''
  drugItems.value = []

  // 加载药品列表
  await loadDrugs()

  // 检查是否已有病历
  await checkExistingRecord(row.id)

  dialogVisible.value = true
}

/**
 * 点击"查看"按钮，打开已完成诊疗的对话框（只读模式）
 */
const viewConsultation = async (row) => {
  currentPatient.value = row
  isDone.value = true
  prescriptionSaved.value = true
  dialogTitle.value = `诊疗记录 - ${row.patientName}`

  // 清空表单
  recordForm.chiefComplaint = ''
  recordForm.presentIllness = ''
  recordForm.diagnosis = ''
  recordForm.treatmentPlan = ''
  drugItems.value = []

  // 加载药品列表
  await loadDrugs()

  // 检查是否已有病历和处方
  await checkExistingRecord(row.id)
  await checkExistingPrescription(row.id)

  dialogVisible.value = true
}

/** 加载药品列表 */
const loadDrugs = async () => {
  try {
    const res = await getDrugList({ pageNum: 1, pageSize: 999 })
    drugOptions.value = res.data.rows || []
  } catch {
    drugOptions.value = []
  }
}

/** 检查是否有已有病历 */
const checkExistingRecord = async (registrationId) => {
  try {
    const res = await getMedicalRecordByRegistration(registrationId)
    if (res.data) {
      recordId.value = res.data.id
      recordForm.chiefComplaint = res.data.chiefComplaint || ''
      recordForm.presentIllness = res.data.presentIllness || ''
      recordForm.diagnosis = res.data.diagnosis || ''
      recordForm.treatmentPlan = res.data.treatmentPlan || ''
    }
  } catch {
    // 没有病历是正常情况
  }
}

/** 检查是否有已有处方 */
const checkExistingPrescription = async (registrationId) => {
  try {
    const res = await getPrescriptionList({ pageNum: 1, pageSize: 10, registrationId })
    const rows = res.data.rows || []
    if (rows.length > 0) {
      savedPrescriptionId.value = rows[0].id
      // 加载处方明细
      const detailRes = await getPrescriptionDetails(rows[0].id)
      drugItems.value = detailRes.data || []
    }
  } catch {
    // 没有处方是正常情况
  }
}

/** 保存病历 */
const saveMedicalRecord = async () => {
  if (!recordForm.chiefComplaint || !recordForm.diagnosis) {
    ElMessage.warning('请填写主诉和诊断')
    return
  }
  savingRecord.value = true
  try {
    const data = {
      registrationId: currentPatient.value.id,
      patientId: currentPatient.value.patientId,
      doctorId: currentPatient.value.doctorId,
      chiefComplaint: recordForm.chiefComplaint,
      presentIllness: recordForm.presentIllness,
      diagnosis: recordForm.diagnosis,
      treatmentPlan: recordForm.treatmentPlan
    }
    if (recordId.value) {
      // 已有病历则更新
      const { updateMedicalRecord } = await import('@/api/medicalRecord')
      await updateMedicalRecord(recordId.value, data)
      ElMessage.success('病历已更新')
    } else {
      await addMedicalRecord(data)
      recordId.value = recordId.value // 不需要更新，后面会重新查询
      ElMessage.success('病历已保存')
    }
    // 重新获取病历ID
    await checkExistingRecord(currentPatient.value.id)
  } catch {
    // request.js 已处理错误提示
  } finally {
    savingRecord.value = false
  }
}

/** 保存处方 */
const savePrescription = async () => {
  if (drugItems.value.length === 0) {
    ElMessage.warning('请先添加药品')
    return
  }
  savingPrescription.value = true
  try {
    let prescriptionId = savedPrescriptionId.value

    // 创建处方（如果没有已保存的处方）
    if (!prescriptionId) {
      const res = await addPrescription({
        registrationId: currentPatient.value.id,
        patientId: currentPatient.value.patientId,
        doctorId: currentPatient.value.doctorId
      })
      prescriptionId = res.data  // 后端返回新创建的处方ID
      savedPrescriptionId.value = prescriptionId
    }

    // 逐条添加明细
    for (const item of drugItems.value) {
      // 跳过已保存的明细（如果有id字段）
      if (item.id) continue
      await addPrescriptionDetail({
        prescriptionId: prescriptionId,
        drugId: item.drugId,
        quantity: item.quantity,
        unitPrice: item.unitPrice,
        usageMethod: item.usageMethod || ''
      })
    }

    prescriptionSaved.value = true
    ElMessage.success('处方已保存')
  } catch {
    // request.js 已处理错误提示
  } finally {
    savingPrescription.value = false
  }
}

/** 完成诊疗：保存病历 + 处方 + 更新状态 */
const completeConsultation = async () => {
  if (!recordForm.chiefComplaint || !recordForm.diagnosis) {
    ElMessage.warning('请先填写主诉和诊断')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确认完成对患者「${currentPatient.value.patientName}」的诊疗？`,
      '提示',
      { confirmButtonText: '确认完成', cancelButtonText: '取消', type: 'info' }
    )
  } catch {
    return  // 用户取消
  }

  completing.value = true
  try {
    // 1. 保存病历
    const recordData = {
      registrationId: currentPatient.value.id,
      patientId: currentPatient.value.patientId,
      doctorId: currentPatient.value.doctorId,
      chiefComplaint: recordForm.chiefComplaint,
      presentIllness: recordForm.presentIllness,
      diagnosis: recordForm.diagnosis,
      treatmentPlan: recordForm.treatmentPlan
    }
    if (recordId.value) {
      const { updateMedicalRecord } = await import('@/api/medicalRecord')
      await updateMedicalRecord(recordId.value, recordData)
    } else {
      await addMedicalRecord(recordData)
    }

    // 2. 如有药品且处方未保存，保存处方
    if (drugItems.value.length > 0 && !prescriptionSaved.value) {
      let prescriptionId = savedPrescriptionId.value
      if (!prescriptionId) {
        const res = await addPrescription({
          registrationId: currentPatient.value.id,
          patientId: currentPatient.value.patientId,
          doctorId: currentPatient.value.doctorId
        })
        prescriptionId = res.data
      }
      for (const item of drugItems.value) {
        if (item.id) continue
        await addPrescriptionDetail({
          prescriptionId: prescriptionId,
          drugId: item.drugId,
          quantity: item.quantity,
          unitPrice: item.unitPrice,
          usageMethod: item.usageMethod || ''
        })
      }
    }

    // 3. 更新挂号状态为已诊
    await updateRegistrationStatus(currentPatient.value.id, 1)

    ElMessage.success('诊疗已完成')
    dialogVisible.value = false
    fetchData()
  } catch {
    // request.js 已处理错误提示
  } finally {
    completing.value = false
  }
}

/** 关闭对话框时清理 */
const handleDialogClose = () => {
  currentPatient.value = null
  recordId.value = null
  prescriptionSaved.value = false
  savedPrescriptionId.value = null
  drugItems.value = []
}

// 页面加载时获取数据
onMounted(fetchData)
</script>

<style scoped lang="scss">
.clinic-page {
  .page-header {
    margin-bottom: 16px;
    h2 {
      font-size: 18px;
      font-weight: 600;
      color: #2d3748;
      margin: 0;
    }
  }
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

// ========== 接诊对话框样式 ==========

.patient-info-card {
  margin-bottom: 16px;
  border-radius: 10px;
  border: 1px solid #e8ecf1;

  .patient-info-header {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .patient-avatar {
    width: 52px;
    height: 52px;
    border-radius: 50%;
    background: linear-gradient(135deg, #5d87ff, #3b5fcf);
    color: #fff;
    font-size: 22px;
    font-weight: 600;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .patient-detail {
    flex: 1;
    .patient-name { font-size: 17px; font-weight: 600; color: #2d3748; }
    .patient-meta {
      font-size: 13px;
      color: #64748b;
      margin-top: 4px;
      .divider { margin: 0 8px; color: #cbd5e1; }
    }
  }

  .patient-reg-info {
    text-align: right;
    .reg-tag { margin-left: 6px; }
    .reg-time { font-size: 12px; color: #94a3b8; margin-top: 4px; }
  }
}

.form-section {
  margin-bottom: 16px;
  border-radius: 10px;
  border: 1px solid #e8ecf1;

  .section-title {
    font-size: 14px;
    font-weight: 600;
    color: #2d3748;
  }
}

.symptom-section {
  background: #fffbeb;
  border-color: #fde68a !important;

  .symptom-content {
    .symptom-item {
      margin-bottom: 8px;
      line-height: 1.6;
      &:last-child { margin-bottom: 0; }
    }
    .symptom-label {
      font-weight: 600;
      color: #92400e;
    }
    .symptom-text {
      color: #78350f;
    }
  }
}

.form-actions {
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.drug-add-row {
  display: flex;
  align-items: flex-start;
}

.drug-total {
  text-align: right;
  margin-top: 8px;
  font-size: 14px;
  color: #64748b;
  .total-amount {
    font-size: 18px;
    font-weight: 700;
    color: #5d87ff;
  }
}

.saved-hint {
  color: #67c23a;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
