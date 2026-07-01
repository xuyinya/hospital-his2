<template>
  <!-- 我的挂号记录页面组件：展示当前患者的挂号列表，支持分页 -->
  <div class="portal-page">
    <el-card shadow="never" class="portal-card">
      <template #header>
        <div class="card-title">
          <el-icon><Tickets /></el-icon>
          <span>我的挂号记录</span>
        </div>
      </template>
      <!-- 挂号记录表格 -->
      <el-table :data="tableData" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="单号" width="70" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="deptName" label="科室" width="100" />
        <!-- 挂号类型：专家/普通 -->
        <el-table-column prop="regType" label="类型" width="70">
          <template #default="{ row }">
            <el-tag :type="row.regType === '专家' ? 'warning' : 'info'" size="small">{{ row.regType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="regFee" label="费用" width="80">
          <template #default="{ row }">¥{{ row.regFee }}</template>
        </el-table-column>
        <el-table-column prop="regTime" label="挂号时间" width="170" />
        <!-- 挂号状态：诊断中/已完成/已取消 -->
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="{0:'warning',1:'success',2:'info'}[row.status]" size="small">
              {{ {0:'诊断中',1:'已完成',2:'已取消'}[row.status] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页组件 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
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
 * MyRegistrations - 患者门户：我的挂号记录页面组件
 * 功能：分页展示当前患者的挂号列表，显示医生、科室、类型、费用、时间、状态
 */
import { ref, onMounted } from 'vue'
import { getMyRegistrations } from '@/api/patient-portal'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

/**
 * 获取挂号记录列表（分页）
 * 调用后端接口查询当前患者的挂号数据
 */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMyRegistrations({ pageNum: pageNum.value, pageSize: pageSize.value })
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
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
