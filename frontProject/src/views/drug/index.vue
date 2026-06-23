<template>
  <!-- 药品管理页面 -->
  <div class="page-container">
    <!-- 搜索栏区域：按药品名称筛选 -->
    <div class="search-bar">
      <el-input v-model="searchParams.drugName" placeholder="药品名称" clearable style="width: 180px;" />
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
      <el-button type="success" icon="Plus" @click="handleAdd">新增药品</el-button>
    </div>

    <!-- 药品列表表格 -->
    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="drugName" label="药品名称" min-width="140" />
      <el-table-column prop="drugCode" label="药品编码" width="100" />
      <el-table-column prop="specification" label="规格" width="130" />
      <el-table-column prop="unit" label="单位" width="60" />
      <el-table-column prop="manufacturer" label="生产厂家" min-width="140" show-overflow-tooltip />
      <el-table-column prop="unitPrice" label="单价" width="90">
        <template #default="{ row }">¥{{ row.unitPrice }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="80">
        <template #default="{ row }">
          <el-tag :type="row.stock > 100 ? 'success' : row.stock > 50 ? 'warning' : 'danger'" size="small">{{ row.stock }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
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

    <!-- 新增/编辑药品弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="药品名称" prop="drugName">
          <el-input v-model="formData.drugName" />
        </el-form-item>
        <el-form-item label="药品编码" prop="drugCode">
          <el-input v-model="formData.drugCode" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="formData.specification" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="formData.unit" />
        </el-form-item>
        <el-form-item label="生产厂家">
          <el-input v-model="formData.manufacturer" />
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="formData.unitPrice" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="formData.stock" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">停用</el-radio>
          </el-radio-group>
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
 * 药品管理页面 - 支持药品的查询、新增、编辑、删除操作
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDrugList, addDrug, updateDrug, deleteDrug } from '@/api/drug'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const searchParams = reactive({
  drugName: '',
  pageNum: 1,
  pageSize: 10
})

const formData = reactive({
  id: null,
  drugName: '',
  drugCode: '',
  specification: '',
  unit: '',
  manufacturer: '',
  unitPrice: 0,
  stock: 0,
  status: 1
})

const rules = {
  drugName: [{ required: true, message: '请输入药品名称', trigger: 'blur' }],
  drugCode: [{ required: true, message: '请输入药品编码', trigger: 'blur' }]
}

/** 获取药品列表数据（按名称分页查询） */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getDrugList(searchParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

/** 打开新增药品弹窗 */
const handleAdd = () => {
  dialogTitle.value = '新增药品'
  Object.assign(formData, { id: null, drugName: '', drugCode: '', specification: '', unit: '', manufacturer: '', unitPrice: 0, stock: 0, status: 1 })
  dialogVisible.value = true
}

/** 打开编辑药品弹窗 */
const handleEdit = (row) => {
  dialogTitle.value = '编辑药品'
  Object.assign(formData, row)
  dialogVisible.value = true
}

/** 提交表单（新增或更新药品信息） */
const handleSubmit = async () => {
  await formRef.value.validate()
  if (formData.id) {
    await updateDrug(formData.id, formData)
    ElMessage.success('修改成功')
  } else {
    await addDrug(formData)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

/** 删除药品（含二次确认） */
const handleDelete = async (row) => {
  await ElMessageBox.confirm('确认删除该药品？', '提示')
  await deleteDrug(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

/** 页面加载时初始化数据 */
onMounted(fetchData)
</script>
