<template>
  <!-- 患者管理页面 -->
  <div class="page-container">
    <!-- 搜索栏区域：按患者姓名筛选 -->
    <div class="search-bar">
      <el-input v-model="searchParams.patientName" placeholder="患者姓名" clearable style="width: 180px;" />
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
      <el-button type="success" icon="Plus" @click="handleAdd">新增患者</el-button>
    </div>

    <!-- 患者列表表格 -->
    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="patientName" label="姓名" width="100" />
      <el-table-column prop="gender" label="性别" width="70">
        <template #default="{ row }">
          <el-tag :type="row.gender === 1 ? 'primary' : 'success'" size="small" effect="plain">
            {{ row.gender === 1 ? '男' : '女' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="70" />
      <el-table-column prop="idCard" label="身份证号" width="180" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="address" label="地址" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="170" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
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

    <!-- 新增/编辑患者弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="姓名" prop="patientName">
          <el-input v-model="formData.patientName" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio :value="1">男</el-radio>
            <el-radio :value="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="formData.age" :min="0" :max="150" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="formData.idCard" maxlength="18" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" maxlength="11" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="formData.address" type="textarea" :rows="2" />
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
 * 患者管理页面 - 支持患者的查询、新增、编辑操作
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPatientList, addPatient, updatePatient, deletePatient } from '@/api/patient'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const searchParams = reactive({
  patientName: '',
  pageNum: 1,
  pageSize: 10
})

const formData = reactive({
  id: null,
  patientName: '',
  gender: 1,
  age: 0,
  idCard: '',
  phone: '',
  address: ''
})

const rules = {
  patientName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }]
}

/** 获取患者列表数据（按姓名分页查询） */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getPatientList(searchParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

/** 打开新增患者弹窗 */
const handleAdd = () => {
  dialogTitle.value = '新增患者'
  Object.assign(formData, { id: null, patientName: '', gender: 1, age: 0, idCard: '', phone: '', address: '' })
  dialogVisible.value = true
}

/** 打开编辑患者弹窗 */
const handleEdit = (row) => {
  dialogTitle.value = '编辑患者'
  Object.assign(formData, row)
  dialogVisible.value = true
}

/** 提交表单（新增或更新患者信息） */
const handleSubmit = async () => {
  await formRef.value.validate()
  if (formData.id) {
    await updatePatient(formData.id, formData)
    ElMessage.success('修改成功')
  } else {
    await addPatient(formData)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

/** 删除患者（含二次确认） */
const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确认删除患者「${row.patientName}」？`, '警告', { type: 'warning' })
  await deletePatient(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

/** 页面加载时初始化数据 */
onMounted(fetchData)
</script>
