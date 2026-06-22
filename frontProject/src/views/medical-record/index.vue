<template>
  <div class="page-container">
    <div class="search-bar">
      <el-input v-model="searchParams.patientName" placeholder="患者姓名" clearable style="width: 180px;" />
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
      <el-button type="success" icon="Plus" @click="handleAdd">新增病历</el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="patientName" label="患者姓名" width="100" />
      <el-table-column prop="doctorName" label="医生" width="100" />
      <el-table-column prop="chiefComplaint" label="主诉" min-width="150" show-overflow-tooltip />
      <el-table-column prop="diagnosis" label="诊断" min-width="150" show-overflow-tooltip />
      <el-table-column prop="recordTime" label="记录时间" width="170" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleView(row)">查看</el-button>
          <el-button size="small" type="warning" @click="handleEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="searchParams.pageNum"
        v-model:page-size="searchParams.pageSize"
        :page-sizes="[10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData"
        @current-change="fetchData"
      />
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="挂号ID" prop="registrationId">
              <el-input v-model="formData.registrationId" :disabled="isView" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMedicalRecordList, addMedicalRecord, updateMedicalRecord } from '@/api/medicalRecord'
import { getPatientList } from '@/api/patient'
import { getDoctorList } from '@/api/doctor'

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
  pageNum: 1,
  pageSize: 10
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
  registrationId: [{ required: true, message: '请输入挂号ID', trigger: 'blur' }],
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }]
}

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

const fetchOptions = async () => {
  const [pRes, dRes] = await Promise.all([
    getPatientList({ pageNum: 1, pageSize: 100 }),
    getDoctorList({ pageNum: 1, pageSize: 100 })
  ])
  patientOptions.value = pRes.data.rows || []
  doctorOptions.value = dRes.data.rows || []
}

const handleAdd = () => {
  dialogTitle.value = '新增病历'
  isView.value = false
  Object.assign(formData, { id: null, registrationId: '', patientId: '', doctorId: '', chiefComplaint: '', presentIllness: '', diagnosis: '', treatmentPlan: '' })
  dialogVisible.value = true
}

const handleView = (row) => {
  dialogTitle.value = '查看病历'
  isView.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑病历'
  isView.value = false
  Object.assign(formData, row)
  dialogVisible.value = true
}

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

onMounted(() => {
  fetchData()
  fetchOptions()
})
</script>
