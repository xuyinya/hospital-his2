<template>
  <!-- 医生管理页面 -->
  <div class="page-container">
    <!-- 搜索栏区域：按医生姓名和科室筛选 -->
    <div class="search-bar">
      <el-input v-model="searchParams.doctorName" placeholder="医生姓名" clearable style="width: 180px;" />
      <el-select v-model="searchParams.deptId" placeholder="科室" clearable style="width: 150px;">
        <el-option v-for="d in deptOptions" :key="d.id" :label="d.deptName" :value="d.id" />
      </el-select>
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
      <el-button type="success" icon="Plus" @click="handleAdd">新增医生</el-button>
    </div>

    <!-- 医生列表表格 -->
    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="doctorName" label="姓名" width="100" />
      <el-table-column prop="deptName" label="科室" width="120" />
      <el-table-column prop="title" label="职称" width="100">
        <template #default="{ row }">
          <el-tag :type="row.title?.includes('主任') ? 'danger' : row.title?.includes('副主任') ? 'warning' : 'info'" size="small">{{ row.title }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="specialty" label="专长" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '在职' : '离职' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
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

    <!-- 新增/编辑医生弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="姓名" prop="doctorName">
          <el-input v-model="formData.doctorName" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="登录用，如zhangming" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="科室" prop="deptId">
          <el-select v-model="formData.deptId" placeholder="选择科室" style="width: 100%">
            <el-option v-for="d in deptOptions" :key="d.id" :label="d.deptName" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-select v-model="formData.title" placeholder="选择职称" style="width: 100%">
            <el-option label="主任医师" value="主任医师" />
            <el-option label="副主任医师" value="副主任医师" />
            <el-option label="主治医师" value="主治医师" />
            <el-option label="住院医师" value="住院医师" />
          </el-select>
        </el-form-item>
        <el-form-item label="专长" prop="specialty">
          <el-input v-model="formData.specialty" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="formData.status" :active-value="1" :inactive-value="0" active-text="在职" inactive-text="离职" />
        </el-form-item>
        <el-alert v-if="!isEdit" title="默认密码：123456" type="info" :closable="false" show-icon style="margin-top:8px" />
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
 * 医生管理页面 - 支持医生的查询、新增、编辑、删除操作
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDoctorList, addDoctor, updateDoctor, deleteDoctor } from '@/api/doctor'
import { getDepartmentList } from '@/api/department'
import { getRegistrationList } from '@/api/registration'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const deptOptions = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const isEdit = ref(false)

const searchParams = reactive({
  doctorName: '',
  deptId: '',
  pageNum: 1,
  pageSize: 10
})

const formData = reactive({
  id: null,
  doctorName: '',
  username: '',
  deptId: '',
  title: '',
  specialty: '',
  status: 1
})
const rules = {
  doctorName: [{ required: true, message: '请输入医生姓名', trigger: 'blur' }],
  username: [{ required: true, message: '请输入登录用户名', trigger: 'blur' }],
  deptId: [{ required: true, message: '请选择科室', trigger: 'change' }],
  title: [{ required: true, message: '请选择职称', trigger: 'change' }]
}

/** 获取医生列表数据（按姓名和科室分页查询） */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getDoctorList(searchParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

/** 加载科室下拉选项 */
const fetchDeptOptions = async () => {
  const res = await getDepartmentList()
  deptOptions.value = res.data || []
}

/** 打开新增医生弹窗 */
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增医生'
  Object.assign(formData, { id: null, doctorName: '', username: '', deptId: '', title: '', specialty: '', status: 1 })
  dialogVisible.value = true
}

/** 打开编辑医生弹窗 */
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑医生'
  Object.assign(formData, { id: row.id, doctorName: row.doctorName, deptId: row.deptId, title: row.title, specialty: row.specialty, status: row.status })
  dialogVisible.value = true
}

/** 删除医生（有待诊患者时阻止，历史记录不阻止） */
const handleDelete = async (row) => {
  try {
    const pendingRes = await getRegistrationList({ doctorId: row.id, status: 0, pageNum: 1, pageSize: 1 })
    const pending = pendingRes.data?.total || 0
    if (pending > 0) {
      await ElMessageBox.alert(
        `医生「${row.doctorName}」还有 ${pending} 条待诊记录，无法删除。\n\n请先处理完待诊患者，或将患者转给其他医生后再删除。\n\n若暂时不需要该医生接诊，可先将其状态改为"离职"。`,
        '无法删除',
        { confirmButtonText: '知道了', type: 'warning' }
      )
      return
    }
    await ElMessageBox.confirm(`确认删除医生「${row.doctorName}」？`, '警告', { type: 'warning' })
    await deleteDoctor(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (e) {
    if (e !== 'cancel') throw e
  }
}

/** 提交表单（新增或更新医生信息） */
const handleSubmit = async () => {
  await formRef.value.validate()
  if (isEdit.value) {
    await updateDoctor(formData.id, formData)
    ElMessage.success('修改成功')
  } else {
    await addDoctor(formData)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

/** 页面加载时初始化数据 */
onMounted(() => {
  fetchData()
  fetchDeptOptions()
})
</script>
