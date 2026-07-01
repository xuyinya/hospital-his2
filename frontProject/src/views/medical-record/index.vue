<template>
  <!-- 病历管理页面 -->
  <div class="page-container">
    <!-- 搜索栏区域：按患者姓名筛选 -->
    <div class="search-bar">
      <el-input v-model="searchParams.patientName" placeholder="患者姓名" clearable style="width: 180px;" />
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
      <el-button type="success" icon="Plus" @click="handleAdd">新增病历</el-button>
    </div>

    <!-- 病历列表表格 -->
    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="patientName" label="患者姓名" width="100" />
      <el-table-column prop="doctorName" label="医生" width="100" />
      <el-table-column prop="chiefComplaint" label="主诉" min-width="150" show-overflow-tooltip />
      <el-table-column prop="diagnosis" label="诊断" min-width="150" show-overflow-tooltip />
      <el-table-column prop="recordTime" label="记录时间" width="170" />
      <el-table-column label="操作" width="210" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleView(row)">查看</el-button>
          <el-button size="small" type="warning" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页控件 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="searchParams.page"
        v-model:page-size="searchParams.size"
        :page-sizes="[10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData"
        @current-change="fetchData"
      />
    </div>

    <!-- 新增/查看/编辑病历弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="挂号记录">
              <el-select v-model="formData.registrationId" placeholder="选择挂号记录" filterable :disabled="isView" style="width:100%" @change="onRegSelect">
                <el-option v-for="r in regOptions" :key="r.id" :label="`#${r.id} ${r.patientName} - ${r.deptName}`" :value="r.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="患者" prop="patientId">
              <el-select v-model="formData.patientId" placeholder="选择患者" filterable :disabled="isView" style="width: 100%">
                <el-option v-for="p in patientOptions" :key="p.id" :label="p.patientName" :value="p.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="医生" prop="doctorId">
          <el-select v-model="formData.doctorId" placeholder="选择医生" filterable :disabled="isView" style="width: 100%">
            <el-option v-for="d in doctorOptions" :key="d.id" :label="d.doctorName" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="主诉">
          <el-input v-model="formData.chiefComplaint" type="textarea" :rows="2" :disabled="isView" />
        </el-form-item>
        <el-form-item label="现病史">
          <el-input v-model="formData.presentIllness" type="textarea" :rows="3" :disabled="isView" />
        </el-form-item>
        <el-form-item label="诊断">
          <el-input v-model="formData.diagnosis" type="textarea" :rows="2" :disabled="isView" />
        </el-form-item>
        <el-form-item label="处理方案">
          <el-input v-model="formData.treatmentPlan" type="textarea" :rows="2" :disabled="isView" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ isView ? '关闭' : '取消' }}</el-button>
        <el-button v-if="!isView" type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 病历管理页面 - 支持病历的查询、新增、查看、编辑操作
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMedicalRecordList, addMedicalRecord, updateMedicalRecord, deleteMedicalRecord } from '@/api/medicalRecord'
import { getPatientList } from '@/api/patient'
import { getDoctorList } from '@/api/doctor'
import { getRegistrationList } from '@/api/registration'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isView = ref(false)
const formRef = ref(null)
const patientOptions = ref([])
const doctorOptions = ref([])

const searchParams = reactive({
  patientName: '',
  page: 1,
  size: 10
})

const formData = reactive({
  id: null,
  registrationId: '',
  patientId: '',
  doctorId: '',
  chiefComplaint: '',
  presentIllness: '',
  diagnosis: '',
  treatmentPlan: ''
})

const rules = {
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }]
}

/** 获取病历列表数据（按患者姓名分页查询） */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMedicalRecordList(searchParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

const regOptions = ref([])

/** 选择挂号记录后自动填充患者和医生 */
const onRegSelect = (regId) => {
  const reg = regOptions.value.find(r => r.id === regId)
  if (reg) {
    formData.patientId = reg.patientId || ''
    formData.doctorId = reg.doctorId || ''
  }
}

/** 加载下拉选项数据（患者列表、医生列表、挂号列表） */
const fetchOptions = async () => {
  const [pRes, dRes, rRes] = await Promise.all([
    getPatientList({ page: 1, size: 100 }),
    getDoctorList({ page: 1, size: 100 }),
    getRegistrationList({ page: 1, size: 200 })
  ])
  patientOptions.value = pRes.data.rows || []
  doctorOptions.value = dRes.data.rows || []
  regOptions.value = rRes.data.rows || []
}

/** 打开新增病历弹窗 */
const handleAdd = () => {
  dialogTitle.value = '新增病历'
  isView.value = false
  Object.assign(formData, { id: null, registrationId: '', patientId: '', doctorId: '', chiefComplaint: '', presentIllness: '', diagnosis: '', treatmentPlan: '' })
  dialogVisible.value = true
}

/** 打开查看病历弹窗（所有字段只读） */
const handleView = (row) => {
  dialogTitle.value = '查看病历'
  isView.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

/** 打开编辑病历弹窗 */
const handleEdit = (row) => {
  dialogTitle.value = '编辑病历'
  isView.value = false
  Object.assign(formData, row)
  dialogVisible.value = true
}

/** 提交表单（新增或更新病历） */
const handleSubmit = async () => {
  await formRef.value.validate()
  if (formData.id) {
    await updateMedicalRecord(formData.id, formData)
    ElMessage.success('修改成功')
  } else {
    await addMedicalRecord(formData)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

/** 删除病历（含二次确认） */
const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确认删除该病历？`, '警告', { type: 'warning' })
  await deleteMedicalRecord(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

/** 页面加载时初始化数据 */
onMounted(() => {
  fetchData()
  fetchOptions()
})
</script>
