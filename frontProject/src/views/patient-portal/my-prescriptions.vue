<template>
  <!-- 我的处方页面组件：展示当前患者的处方列表，支持分页和查看明细 -->
  <div class="portal-page">
    <el-card shadow="never" class="portal-card">
      <template #header>
        <div class="card-title">
          <el-icon><Document /></el-icon>
          <span>我的处方</span>
        </div>
      </template>
      <!-- 处方列表表格 -->
      <el-table :data="tableData" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="编号" width="70" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="totalAmount" label="总金额" width="100">
          <template #default="{ row }">¥{{ row.totalAmount }}</template>
        </el-table-column>
        <!-- 取药状态：已取药/未取药 -->
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">
              {{ row.status === 1 ? '已取药' : '未取药' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="开具时间" width="170" />
        <!-- 查看明细操作 -->
        <el-table-column label="操作" width="80" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleView(row)">明细</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 处方明细弹窗 -->
      <el-dialog v-model="detailVisible" title="处方明细" width="650px">
        <el-table :data="detailList" border>
          <el-table-column prop="drugName" label="药品名称" min-width="120" />
          <el-table-column prop="specification" label="规格" width="100" />
          <el-table-column prop="quantity" label="数量" width="70" />
          <el-table-column prop="unitPrice" label="单价" width="80">
            <template #default="{ row }">¥{{ row.unitPrice }}</template>
          </el-table-column>
          <el-table-column prop="amount" label="金额" width="80">
            <template #default="{ row }">¥{{ row.amount }}</template>
          </el-table-column>
          <el-table-column prop="usageMethod" label="用法用量" min-width="140" show-overflow-tooltip />
        </el-table>
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
 * MyPrescriptions - 患者门户：我的处方页面组件
 * 功能：分页展示当前患者的处方列表，支持查看处方明细（药品清单）
 */
import { ref, onMounted } from 'vue'
import { getMyPrescriptions, getMyPrescriptionDetails } from '@/api/patient-portal'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
/** 处方明细弹窗显示状态 */
const detailVisible = ref(false)
/** 当前查看的处方明细数据（药品列表） */
const detailList = ref([])

/**
 * 获取处方列表（分页）
 * 调用后端接口查询当前患者的处方数据
 */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMyPrescriptions({ page: page.value, size: size.value })
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

/**
 * 查看处方明细
 * 根据处方ID查询对应的药品明细列表
 * @param {Object} row - 选中的处方行数据
 */
const handleView = async (row) => {
  const res = await getMyPrescriptionDetails(row.id)
  detailList.value = res.data || []
  detailVisible.value = true
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
