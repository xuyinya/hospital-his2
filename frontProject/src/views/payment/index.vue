<template>
  <!-- 收费管理页面 -->
  <div class="page-container">
    <!-- 搜索栏区域：按患者姓名和支付状态筛选 -->
    <div class="search-bar">
      <el-input v-model="searchParams.patientName" placeholder="患者姓名" clearable style="width: 180px;" />
      <el-select v-model="searchParams.status" placeholder="状态" clearable style="width: 120px;">
        <el-option label="未支付" :value="0" />
        <el-option label="已支付" :value="1" />
      </el-select>
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
      <el-button type="success" icon="Plus" @click="handleAdd">新增收费</el-button>
    </div>

    <!-- 收费列表表格 -->
    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="patientName" label="患者姓名" width="100" />
      <el-table-column prop="paymentType" label="收费类型" width="100">
        <template #default="{ row }">
          <el-tag size="small" effect="plain">{{ row.paymentType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalAmount" label="总金额" width="100">
        <template #default="{ row }">¥{{ row.totalAmount }}</template>
      </el-table-column>
      <el-table-column prop="paymentMethod" label="支付方式" width="90" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">{{ row.status === 1 ? '已支付' : '未支付' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="paymentTime" label="支付时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" size="small" type="success" @click="handlePay(row)">确认支付</el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(row)">删除</el-button>
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

    <!-- 新增收费弹窗 -->
    <el-dialog v-model="dialogVisible" title="新增收费" width="520px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="挂号ID" prop="registrationId">
          <el-input v-model="formData.registrationId" />
        </el-form-item>
        <el-form-item label="患者" prop="patientId">
          <el-select v-model="formData.patientId" placeholder="选择患者" filterable style="width: 100%">
            <el-option v-for="p in patientOptions" :key="p.id" :label="p.patientName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="收费类型" prop="paymentType">
          <el-select v-model="formData.paymentType" style="width: 100%">
            <el-option label="挂号费" value="挂号费" />
            <el-option label="药费" value="药费" />
            <el-option label="检查费" value="检查费" />
            <el-option label="检验费" value="检验费" />
            <el-option label="处置费" value="处置费" />
          </el-select>
        </el-form-item>
        <el-form-item label="总金额" prop="totalAmount">
          <el-input-number v-model="formData.totalAmount" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="formData.paymentMethod" style="width: 100%">
            <el-option label="现金" value="现金" />
            <el-option label="微信" value="微信" />
            <el-option label="支付宝" value="支付宝" />
            <el-option label="银行卡" value="银行卡" />
          </el-select>
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
 * 收费管理页面 - 支持收费的查询、新增、确认支付操作
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPaymentList, addPayment, updatePaymentStatus, deletePayment } from '@/api/payment'
import { getPatientList } from '@/api/patient'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref(null)
const patientOptions = ref([])

const searchParams = reactive({
  patientName: '',
  status: '',
  page: 1,
  size: 10
})

const formData = reactive({
  registrationId: '',
  patientId: '',
  paymentType: '',
  totalAmount: 0,
  paymentMethod: '现金'
})

const rules = {
  registrationId: [{ required: true, message: '请输入挂号ID', trigger: 'blur' }],
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
  paymentType: [{ required: true, message: '请选择收费类型', trigger: 'change' }],
  totalAmount: [{ required: true, message: '请输入总金额', trigger: 'blur' }]
}

/** 获取收费列表数据（按患者姓名和状态分页查询） */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getPaymentList(searchParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

/** 加载患者下拉选项 */
const fetchPatientOptions = async () => {
  const res = await getPatientList({ page: 1, size: 100 })
  patientOptions.value = res.data.rows || []
}

/** 打开新增收费弹窗 */
const handleAdd = () => {
  Object.assign(formData, { registrationId: '', patientId: '', paymentType: '', totalAmount: 0, paymentMethod: '现金' })
  dialogVisible.value = true
}

/** 提交新增收费表单 */
const handleSubmit = async () => {
  await formRef.value.validate()
  await addPayment(formData)
  ElMessage.success('新增成功')
  dialogVisible.value = false
  fetchData()
}

/** 确认支付（将收费状态设为"已支付"） */
const handlePay = async (row) => {
  await ElMessageBox.confirm('确认该笔收费已支付？', '提示')
  await updatePaymentStatus(row.id, 1)
  ElMessage.success('操作成功')
  fetchData()
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确认删除该收费记录？', '警告', { type: 'warning' })
  await deletePayment(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

/** 页面加载时初始化数据 */
onMounted(() => {
  fetchData()
  fetchPatientOptions()
})
</script>
