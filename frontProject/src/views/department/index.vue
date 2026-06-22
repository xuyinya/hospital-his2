<template>
  <div class="page-container">
    <div class="search-bar">
      <el-input v-model="searchParams.deptName" placeholder="科室名称" clearable style="width: 180px;" />
      <el-button type="primary" icon="Search" @click="fetchData">查询</el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="deptName" label="科室名称" min-width="150" />
      <el-table-column prop="deptCode" label="科室编码" width="100" />
      <el-table-column prop="location" label="科室位置" min-width="150" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getDepartmentList } from '@/api/department'

const loading = ref(false)
const tableData = ref([])

const searchParams = reactive({
  deptName: ''
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getDepartmentList(searchParams)
    tableData.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(fetchData)
</script>
