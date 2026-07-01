<template>
  <!-- 检查管理页面 -->
  <div class="page-container">
    <!-- 搜索栏区域：按患者姓名和检查状态筛选 -->
    <div class="search-bar">
      <el-input v-model="searchParams.patientName" placeholder="患者姓名" clearable style="width: 180px;" />
      <el-select v-model="searchParams.status" placeholder="状态" clearable style="width: 120px;">
        <el-option label="待检查" :value="0" />
        <el-option label="已完成" :value="1" />
      </el-select>
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
      <el-button type="success" icon="Plus" @click="handleAdd">新增检查</el-button>
    </div>

    <!-- 检查列表表格 -->
    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="patientName" label="患者姓名" width="100" />
      <el-table-column prop="examType" label="检查类型" width="100" />
      <el-table-column prop="examName" label="检查项目" min-width="150" />
      <el-table-column prop="result" label="检查结果" min-width="150" show-overflow-tooltip />
      <el-table-column prop="fee" label="费用" width="90">
        <template #default="{ row }">¥{{ row.fee }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">{{ row.status === 1 ? '已完成' : '待检查' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="examTime" label="检查时间" width="170" />
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 0" size="small" type="success" @click="handleResult(row)">录入结果</el-button>
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

    <!-- 新增/编辑检查弹窗 -->
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
        <el-form-item label="检查类型" prop="examType">
          <el-input v-model="formData.examType" />
        </el-form-item>
        <el-form-item label="检查项目" prop="examName">
          <el-input v-model="formData.examName" />
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

    <!-- 录入检查结果弹窗 -->
    <el-dialog v-model="resultVisible" title="录入检查结果" width="520px">
      <el-input v-model="resultText" type="textarea" :rows="4" placeholder="请输入检查结果" />
      <template #footer>
        <el-button @click="resultVisible = false">取消</el-button>
        <el-button type="primary" @click="submitResult">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 检查管理页面 - 支持检查的查询、新增、编辑、录入结果操作
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getExaminationList, addExamination, updateExamination, updateExaminationResult, deleteExamination } from '@/api/examination'
import { getPatientList } from '@/api/patient'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const resultVisible = ref(false)
const resultText = ref('')
const currentId = ref(null)
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
  examType: '',
  examName: '',
  fee: 0
})

const rules = {
  registrationId: [{ required: true, message: '请输入挂号ID', trigger: 'blur' }],
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
  examType: [{ required: true, message: '请输入检查类型', trigger: 'blur' }],
  examName: [{ required: true, message: '请输入检查项目', trigger: 'blur' }]
}

/** 获取检查列表数据（按患者姓名和状态分页查询） */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getExaminationList(searchParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

/** 加载患者下拉选项 */
const fetchPatientOptions = async () => {
  const res = await getPatientList({ pageNum: 1, pageSize: 100 })
  patientOptions.value = res.data.rows || []
}

/** 打开新增检查弹窗 */
const handleAdd = () => {
  dialogTitle.value = '新增检查'
  Object.assign(formData, { id: null, registrationId: '', patientId: '', examType: '', examName: '', fee: 0 })
  dialogVisible.value = true
}

/** 打开编辑检查弹窗 */
const handleEdit = (row) => {
  dialogTitle.value = '编辑检查'
  Object.assign(formData, row)
  dialogVisible.value = true
}

/** 提交表单（新增或更新检查信息） */
const handleSubmit = async () => {
  await formRef.value.validate()
  if (formData.id) {
    await updateExamination(formData.id, formData)
    ElMessage.success('修改成功')
  } else {
    await addExamination(formData)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

/** 打开录入检查结果弹窗 */
const handleResult = (row) => {
  currentId.value = row.id
  resultText.value = row.result || ''
  resultVisible.value = true
}

/** 提交检查结果并标记为已完成 */
const submitResult = async () => {
  await updateExaminationResult(currentId.value, resultText.value)
  ElMessage.success('录入成功')
  resultVisible.value = false
  fetchData()
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确认删除该检查记录？', '警告', { type: 'warning' })
  await deleteExamination(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

/** 页面加载时初始化数据 */
onMounted(() => {
  fetchData()
  fetchPatientOptions()
})
</script>
