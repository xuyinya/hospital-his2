<template>
  <div class="page-container">
    <div class="search-bar">
      <el-input v-model="searchParams.patientName" placeholder="患者姓名" clearable style="width: 180px;" />
      <el-select v-model="searchParams.status" placeholder="状态" clearable style="width: 120px;">
        <el-option label="待诊" :value="0" />
        <el-option label="已诊" :value="1" />
        <el-option label="已取消" :value="2" />
      </el-select>
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
      <el-button type="success" icon="Plus" @click="handleAdd">新增挂号</el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="patientName" label="患者姓名" width="100" />
      <el-table-column prop="doctorName" label="医生" width="100" />
      <el-table-column prop="deptName" label="科室" width="100" />
      <el-table-column prop="regType" label="挂号类型" width="90">
        <template #default="{ row }">
          <el-tag :type="row.regType === '专家' ? 'warning' : 'info'" size="small">{{ row.regType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="regFee" label="挂号费" width="90">
        <template #default="{ row }">¥{{ row.regFee }}</template>
      </el-table-column>
      <el-table-column prop="regTime" label="挂号时间" width="170" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" size="small">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 0" size="small" type="success" @click="handleComplete(row)">完成</el-button>
          <el-button v-if="row.status === 0" size="small" type="danger" @click="handleCancel(row)">取消</el-button>
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
        <el-form-item label="患者" prop="patientId">
          <el-select v-model="formData.patientId" placeholder="选择患者" filterable style="width: 100%">
            <el-option v-for="p in patientOptions" :key="p.id" :label="p.patientName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="科室" prop="deptId">
          <el-select v-model="formData.deptId" placeholder="选择科室" @change="onDeptChange" style="width: 100%">
            <el-option v-for="d in deptOptions" :key="d.id" :label="d.deptName" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="医生" prop="doctorId">
          <el-select v-model="formData.doctorId" placeholder="选择医生" style="width: 100%">
            <el-option v-for="d in doctorOptions" :key="d.id" :label="d.doctorName" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="挂号类型" prop="regType">
          <el-radio-group v-model="formData.regType">
            <el-radio value="普通">普通</el-radio>
            <el-radio value="专家">专家</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="挂号费" prop="regFee">
          <el-input-number v-model="formData.regFee" :precision="2" :min="0" />
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
import { getRegistrationList, addRegistration, updateRegistration, updateRegistrationStatus } from '@/api/registration'
import { getPatientList } from '@/api/patient'
import { getDepartmentList } from '@/api/department'
import { getDoctorByDept } from '@/api/doctor'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const searchParams = reactive({
  patientName: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

const formData = reactive({
  id: null,
  patientId: '',
  doctorId: '',
  deptId: '',
  regType: '普通',
  regFee: 0
})

const rules = {
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  deptId: [{ required: true, message: '请选择科室', trigger: 'change' }]
}

const patientOptions = ref([])
const deptOptions = ref([])
const doctorOptions = ref([])

const getStatusText = (status) => ({ 0: '待诊', 1: '已诊', 2: '已取消' }[status] || '未知')
const getStatusType = (status) => ({ 0: 'warning', 1: 'success', 2: 'info' }[status] || '')

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getRegistrationList(searchParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

const fetchOptions = async () => {
  const [pRes, dRes] = await Promise.all([
    getPatientList({ pageNum: 1, pageSize: 100 }),
    getDepartmentList()
  ])
  patientOptions.value = pRes.data.rows || []
  deptOptions.value = dRes.data || []
}

const onDeptChange = async (deptId) => {
  formData.doctorId = ''
  if (deptId) {
    const res = await getDoctorByDept(deptId)
    doctorOptions.value = res.data || []
  } else {
    doctorOptions.value = []
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增挂号'
  Object.assign(formData, { id: null, patientId: '', doctorId: '', deptId: '', regType: '普通', regFee: 0 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑挂号'
  Object.assign(formData, row)
  onDeptChange(row.deptId)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (formData.id) {
    await updateRegistration(formData.id, formData)
    ElMessage.success('修改成功')
  } else {
    await addRegistration(formData)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

const handleComplete = async (row) => {
  await ElMessageBox.confirm('确认完成该挂号？', '提示')
  await updateRegistrationStatus(row.id, 1)
  ElMessage.success('操作成功')
  fetchData()
}

const handleCancel = async (row) => {
  await ElMessageBox.confirm('确认取消该挂号？', '提示')
  await updateRegistrationStatus(row.id, 2)
  ElMessage.success('操作成功')
  fetchData()
}

onMounted(() => {
  fetchData()
  fetchOptions()
})
</script>
