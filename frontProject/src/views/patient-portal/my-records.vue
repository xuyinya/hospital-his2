<template>
  <!-- 我的病历页面组件：展示当前患者的病历列表，支持分页和查看详情 -->
  <div class="portal-page">
    <el-card shadow="never" class="portal-card">
      <template #header>
        <div class="card-title">
          <el-icon><Notebook /></el-icon>
          <span>我的病历</span>
        </div>
      </template>
      <!-- 病历列表表格 -->
      <el-table :data="tableData" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="编号" width="70" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="chiefComplaint" label="主诉" min-width="140" show-overflow-tooltip />
        <el-table-column prop="diagnosis" label="诊断" min-width="140" show-overflow-tooltip />
        <el-table-column prop="treatmentPlan" label="处理方案" min-width="140" show-overflow-tooltip />
        <el-table-column prop="recordTime" label="记录时间" width="170" />
        <!-- 查看详情操作 -->
        <el-table-column label="操作" width="80" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleView(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 病历详情弹窗 -->
      <el-dialog v-model="dialogVisible" title="病历详情" width="600px">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="医生">{{ currentRecord.doctorName }}</el-descriptions-item>
          <el-descriptions-item label="主诉">{{ currentRecord.chiefComplaint }}</el-descriptions-item>
          <el-descriptions-item label="现病史">{{ currentRecord.presentIllness }}</el-descriptions-item>
          <el-descriptions-item label="诊断">{{ currentRecord.diagnosis }}</el-descriptions-item>
          <el-descriptions-item label="处理方案">{{ currentRecord.treatmentPlan }}</el-descriptions-item>
          <el-descriptions-item label="记录时间">{{ currentRecord.recordTime }}</el-descriptions-item>
        </el-descriptions>
      </el-dialog>
      <!-- 分页组件 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="fetchData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
/**
 * MyRecords - 患者门户：我的病历页面组件
 * 功能：分页展示当前患者的病历列表，支持点击查看病历详情
 */
import { ref, onMounted } from 'vue'
import { getMyRecords } from '@/api/patient-portal'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
/** 病历详情弹窗显示状态 */
const dialogVisible = ref(false)
/** 当前查看的病历详情数据 */
const currentRecord = ref({})

/**
 * 获取病历列表（分页）
 * 调用后端接口查询当前患者的病历数据
 */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMyRecords({ page: page.value, size: size.value })
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

/**
 * 查看病历详情
 * @param {Object} row - 选中的病历行数据
 */
const handleView = (row) => {
  currentRecord.value = row
  dialogVisible.value = true
}

/** 组件挂载时加载第一页数据 */
onMounted(fetchData)
</script>

<style scoped lang="scss">
.portal-page {
  .portal-card {
    border-radius: 12px;
    border: 1px solid #e8ecf1;
    background: #fff;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);

    .card-title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 15px;
      font-weight: 600;
      color: #2d3748;
    }
  }
  .pagination {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
