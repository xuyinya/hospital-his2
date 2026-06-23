<template>
  <!-- 处方管理页面 -->
  <div class="page-container">
    <!-- 搜索栏区域：按患者姓名和处方状态筛选 -->
    <div class="search-bar">
      <el-input v-model="searchParams.patientName" placeholder="患者姓名" clearable style="width: 180px;" />
      <el-select v-model="searchParams.status" placeholder="状态" clearable style="width: 120px;">
        <el-option label="未取药" :value="0" />
        <el-option label="已取药" :value="1" />
      </el-select>
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
    </div>

    <!-- 处方列表表格 -->
    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="patientName" label="患者姓名" width="100" />
      <el-table-column prop="doctorName" label="医生" width="100" />
      <el-table-column prop="totalAmount" label="总金额" width="100">
        <template #default="{ row }">¥{{ row.totalAmount }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">{{ row.status === 1 ? '已取药' : '未取药' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleView(row)">查看明细</el-button>
          <el-button v-if="row.status === 0" size="small" type="success" @click="handleComplete(row)">确认取药</el-button>
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

    <!-- 处方明细弹窗 -->
    <el-dialog v-model="detailVisible" title="处方明细" width="700px">
      <el-table :data="detailList" border>
        <el-table-column prop="drugName" label="药品名称" min-width="120" />
        <el-table-column prop="specification" label="规格" width="120" />
        <el-table-column prop="quantity" label="数量" width="70" />
        <el-table-column prop="unitPrice" label="单价" width="90">
          <template #default="{ row }">¥{{ row.unitPrice }}</template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="90">
          <template #default="{ row }">¥{{ row.amount }}</template>
        </el-table-column>
        <el-table-column prop="usageMethod" label="用法用量" min-width="150" show-overflow-tooltip />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 处方管理页面 - 支持处方查询、查看明细、确认取药操作
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPrescriptionList, getPrescriptionDetails, updatePrescriptionStatus } from '@/api/prescription'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const detailVisible = ref(false)
const detailList = ref([])

const searchParams = reactive({
  patientName: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

/** 获取处方列表数据（按患者姓名和状态分页查询） */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getPrescriptionList(searchParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

/** 查看处方明细（弹窗显示药品列表） */
const handleView = async (row) => {
  const res = await getPrescriptionDetails(row.id)
  detailList.value = res.data || []
  detailVisible.value = true
}

/** 确认取药（将处方状态设为"已取药"） */
const handleComplete = async (row) => {
  await ElMessageBox.confirm('确认该处方已取药？', '提示')
  await updatePrescriptionStatus(row.id, 1)
  ElMessage.success('操作成功')
  fetchData()
}

/** 页面加载时初始化数据 */
onMounted(fetchData)
</script>
