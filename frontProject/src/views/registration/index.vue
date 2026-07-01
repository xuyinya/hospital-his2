<template>
  <!-- 挂号管理页面 -->
  <div class="page-container">
    <!-- 搜索栏区域：按患者姓名和挂号状态筛选 -->
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

    <!-- 挂号列表表格 -->
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
      <el-table-column label="操作" width="210" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 0" size="small" type="success" @click="handleComplete(row)">完成</el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页控件 -->
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

    <!-- 新增/编辑挂号弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="身份证号">
          <el-input v-model="formData.idCard" placeholder="输入身份证号自动查询" maxlength="18" @input="onIdCardInput" />
        </el-form-item>
        <template v-if="matchedPatient">
          <el-alert :title="`已匹配：${matchedPatient.patientName}（${matchedPatient.gender===1?'男':'女'} ${matchedPatient.age}岁）`" type="success" :closable="false" style="margin-bottom:12px" />
        </template>
        <template v-if="formData.idCard && !matchedPatient">
          <el-divider>新患者信息</el-divider>
          <el-form-item label="姓名" required>
            <el-input v-model="formData.newPatientName" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="formData.newGender">
              <el-radio :value="1">男</el-radio>
              <el-radio :value="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input-number v-model="formData.newAge" :min="0" :max="150" controls-position="right" style="width:100%" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="formData.newPhone" maxlength="11" />
          </el-form-item>
        </template>
        <template v-if="!formData.idCard">
          <el-divider>或选择已有患者</el-divider>
          <el-form-item label="患者">
            <el-select v-model="formData.patientId" placeholder="选择已有患者" filterable style="width:100%">
              <el-option v-for="p in patientOptions" :key="p.id" :label="`${p.patientName}（${p.idCard||''}）`" :value="p.id" />
            </el-select>
          </el-form-item>
        </template>
        <el-divider>挂号信息</el-divider>
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
/**
 * 挂号管理页面 - 支持挂号的查询、新增、编辑、完成就诊、取消挂号操作
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRegistrationList, addRegistration, updateRegistration, updateRegistrationStatus, deleteRegistration } from '@/api/registration'
import { getPatientList, addPatient } from '@/api/patient'
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
  regFee: 0,
  idCard: '',
  newPatientName: '',
  newGender: 1,
  newAge: 30,
  newPhone: ''
})

const matchedPatient = ref(null)

const onIdCardInput = () => {
  const id = formData.idCard
  matchedPatient.value = null
  formData.patientId = ''
  if (id.length >= 15) {
    const found = patientOptions.value.find(p => p.idCard === id)
    if (found) {
      matchedPatient.value = found
      formData.patientId = found.id
    }
  }
}

const rules = {
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  deptId: [{ required: true, message: '请选择科室', trigger: 'change' }]
}

const patientOptions = ref([])
const deptOptions = ref([])
const doctorOptions = ref([])

/** 根据状态值返回对应的文本显示 */
const getStatusText = (status) => ({ 0: '待诊', 1: '已诊', 2: '已取消' }[status] || '未知')
/** 根据状态值返回对应的标签类型 */
const getStatusType = (status) => ({ 0: 'warning', 1: 'success', 2: 'info' }[status] || '')

/** 获取挂号列表数据（按搜索条件分页查询） */
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

/** 加载下拉选项数据（患者列表和科室列表） */
const fetchOptions = async () => {
  const [pRes, dRes] = await Promise.all([
    getPatientList({ pageNum: 1, pageSize: 100 }),
    getDepartmentList()
  ])
  patientOptions.value = pRes.data.rows || []
  deptOptions.value = dRes.data || []
}

/** 科室选择变化时加载对应医生列表 */
const onDeptChange = async (deptId) => {
  formData.doctorId = ''
  if (deptId) {
    const res = await getDoctorByDept(deptId)
    doctorOptions.value = res.data || []
  } else {
    doctorOptions.value = []
  }
}

/** 打开新增挂号弹窗 */
const handleAdd = () => {
  dialogTitle.value = '新增挂号'
  matchedPatient.value = null
  Object.assign(formData, { id: null, patientId: '', doctorId: '', deptId: '', regType: '普通', regFee: 0, idCard: '', newPatientName: '', newGender: 1, newAge: 30, newPhone: '' })
  dialogVisible.value = true
}

/** 打开编辑挂号弹窗并加载当前科室所对应的医生 */
const handleEdit = (row) => {
  dialogTitle.value = '编辑挂号'
  matchedPatient.value = null
  Object.assign(formData, { idCard: '', newPatientName: '', newGender: 1, newAge: 30, newPhone: '' }, row)
  onDeptChange(row.deptId)
  dialogVisible.value = true
}

/** 提交表单（新增或更新挂号） */
const handleSubmit = async () => {
  await formRef.value.validate()
  if (formData.id) {
    await updateRegistration(formData.id, formData)
    ElMessage.success('修改成功')
  } else {
    let pid = formData.patientId
    // 新患者：先创建患者再挂号
    if (!pid && formData.idCard && formData.newPatientName) {
      const pRes = await addPatient({
        patientName: formData.newPatientName,
        gender: formData.newGender,
        age: formData.newAge,
        idCard: formData.idCard,
        phone: formData.newPhone
      })
      pid = pRes.data?.id || pRes.id
      if (!pid) {
        // 尝试按身份证号查找刚创建的患者
        const list = await getPatientList({ pageNum: 1, pageSize: 200 })
        const p = (list.data.rows || []).find(x => x.idCard === formData.idCard)
        pid = p?.id
      }
      if (!pid) { ElMessage.error('创建患者失败'); return }
    }
    if (!pid) { ElMessage.warning('请选择或录入患者信息'); return }
    await addRegistration({ ...formData, patientId: pid })
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

/** 完成就诊（状态改为"已诊"） */
const handleComplete = async (row) => {
  await ElMessageBox.confirm('确认完成该挂号？', '提示')
  await updateRegistrationStatus(row.id, 1)
  ElMessage.success('操作成功')
  fetchData()
}

/** 取消挂号（状态改为"已取消"） */
const handleCancel = async (row) => {
  await ElMessageBox.confirm('确认取消该挂号？', '提示')
  await updateRegistrationStatus(row.id, 2)
  ElMessage.success('操作成功')
  fetchData()
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确认删除该挂号记录？`, '警告', { type: 'warning' })
  await deleteRegistration(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

/** 页面加载时初始化数据 */
onMounted(() => {
  fetchData()
  fetchOptions()
})
</script>
