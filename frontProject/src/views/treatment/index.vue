<template>
  <div class="page-container">
    <div class="search-bar">
      <el-input v-model="searchParams.patientName" placeholder="患者姓名" clearable style="width: 180px;" />
      <el-select v-model="searchParams.status" placeholder="状态" clearable style="width: 120px;">
        <el-option label="待处置" :value="0" />
        <el-option label="已完成" :value="1" />
      </el-select>
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
      <el-button type="success" icon="Plus" @click="handleAdd">新增处置</el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="patientName" label="患者姓名" width="100" />
      <el-table-column prop="treatmentName" label="处置名称" min-width="150" />
      <el-table-column prop="treatmentDesc" label="处置描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="fee" label="费用" width="90">
        <template #default="{ row }">¥{{ row.fee }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">{{ row.status === 1 ? '已完成' : '待处置' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="treatmentTime" label="处置时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 0" size="small" type="success" @click="handleComplete(row)">完成</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="挂号ID" prop="registrationId">
          <el-input v-model="formData.registrationId" />
        </el-form-item>
        <el-form-item label="患者" prop="patientId">
          <el-select v-model="formData.patientId" placeholder="选择患者" filterable style="width: 100%">
            <el-option v-for="p in patientOptions" :key="p.id" :label="p.patientName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="处置名称" prop="treatmentName">
          <el-input v-model="formData.treatmentName" />
        </el-form-item>
        <el-form-item label="处置描述">
          <el-input v-model="formData.treatmentDesc" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="费用">
          <el-input-number v-model="formData.fee" :precision="2" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getTreatmentList, addTreatment, updateTreatment, updateTreatmentStatus } from '@/api/treatment'
import { getPatientList } from '@/api/patient'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const patientOptions = ref([])

const searchParams = reactive({
  patientName: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

const formData = reactive({
  id: null,
  registrationId: '',
  patientId: '',
  treatmentName: '',
  treatmentDesc: '',
  fee: 0
})

const rules = {
  registrationId: [{ required: true, message: '请输入挂号ID', trigger: 'blur' }],
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
  treatmentName: [{ required: true, message: '请输入处置名称', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getTreatmentList(searchParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

const fetchPatientOptions = async () => {
  const res = await getPatientList({ pageNum: 1, pageSize: 100 })
  patientOptions.value = res.data.rows || []
}

const handleAdd = () => {
  dialogTitle.value = '新增处置'
  Object.assign(formData, { id: null, registrationId: '', patientId: '', treatmentName: '', treatmentDesc: '', fee: 0 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑处置'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (formData.id) {
    await updateTreatment(formData.id, formData)
    ElMessage.success('修改成功')
  } else {
    await addTreatment(formData)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

const handleComplete = async (row) => {
  await ElMessageBox.confirm('确认完成该处置？', '提示')
  await updateTreatmentStatus(row.id, 1)
  ElMessage.success('操作成功')
  fetchData()
}

onMounted(() => {
  fetchData()
  fetchPatientOptions()
})
</script>
