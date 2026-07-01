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
      <el-button type="success" icon="Plus" @click="handleAdd">新增处方</el-button>
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
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleView(row)">明细</el-button>
          <el-button v-if="row.status === 0" size="small" type="success" @click="handleComplete(row)">确认取药</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
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

    <!-- 新增处方弹窗 -->
    <el-dialog v-model="addVisible" title="新增处方" width="600px" :close-on-click-modal="false">
      <el-form :model="addForm" ref="addFormRef" label-width="90px">
        <el-form-item label="挂号记录">
          <el-select v-model="addForm.registrationId" placeholder="可选" clearable filterable style="width:100%" @change="onRegSelect">
            <el-option v-for="r in regOptions" :key="r.id" :label="`#${r.id} ${r.patientName} - ${r.deptName}`" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="患者" required>
          <el-select v-model="addForm.patientId" placeholder="选择患者" filterable style="width:100%">
            <el-option v-for="p in patientOptions" :key="p.id" :label="p.patientName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="医生" required>
          <el-select v-model="addForm.doctorId" placeholder="选择医生" filterable style="width:100%">
            <el-option v-for="d in doctorOptions" :key="d.id" :label="d.doctorName" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-divider>药品明细</el-divider>
        <el-row :gutter="8" style="margin-bottom:8px">
          <el-col :span="10">
            <el-select v-model="addDrug.drugId" filterable placeholder="搜索药品" style="width:100%" @change="onDrugSelect">
              <el-option v-for="d in drugOptions" :key="d.id" :label="`${d.drugName} (¥${d.unitPrice})`" :value="d.id" />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-input-number v-model="addDrug.quantity" :min="1" :max="99" placeholder="数量" controls-position="right" style="width:100%" />
          </el-col>
          <el-col :span="8">
            <el-input v-model="addDrug.usageMethod" placeholder="用法用量" />
          </el-col>
          <el-col :span="2">
            <el-button type="primary" :disabled="!addDrug.drugId || !addDrug.quantity" @click="addDrugItem">添加</el-button>
          </el-col>
        </el-row>
        <el-table :data="addForm.drugItems" border size="small" max-height="200" style="width:100%">
          <el-table-column prop="drugName" label="药品" width="130" />
          <el-table-column label="数量" width="50"><template #default="{row}">{{row.quantity}}</template></el-table-column>
          <el-table-column label="金额" width="70"><template #default="{row}">¥{{(row.unitPrice*row.quantity).toFixed(2)}}</template></el-table-column>
          <el-table-column prop="usageMethod" label="用法" />
          <el-table-column label="" width="50"><template #default="{$index}"><el-button size="small" type="danger" link @click="addForm.drugItems.splice($index,1)">删除</el-button></template></el-table-column>
        </el-table>
        <div v-if="addForm.drugItems.length>0" style="text-align:right;margin-top:8px;font-size:18px;font-weight:bold;color:#2563EB">
          合计：¥{{ addForm.drugItems.reduce((s,i)=>s+i.unitPrice*i.quantity,0).toFixed(2) }}
        </div>
      </el-form>
      <template #footer>
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" :disabled="!addForm.patientId||!addForm.doctorId||addForm.drugItems.length===0" @click="handleAddSubmit">创建处方</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 处方管理页面 - 支持处方查询、查看明细、确认取药操作
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPrescriptionList, getPrescriptionDetails, updatePrescriptionStatus, addPrescription, addPrescriptionDetail, deletePrescription } from '@/api/prescription'
import { getPatientList } from '@/api/patient'
import { getDoctorList } from '@/api/doctor'
import { getRegistrationList } from '@/api/registration'
import { getDrugList } from '@/api/drug'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const detailVisible = ref(false)
const detailList = ref([])

const searchParams = reactive({
  patientName: '',
  status: '',
  page: 1,
  size: 10
})

const addVisible = ref(false)
const addFormRef = ref(null)
const patientOptions = ref([])
const doctorOptions = ref([])
const regOptions = ref([])
const addForm = reactive({
  registrationId: '',
  patientId: '',
  doctorId: '',
  drugItems: []
})

const drugOptions = ref([])
const addDrug = reactive({ drugId: null, drugName: '', unitPrice: 0, quantity: 1, usageMethod: '' })

const onRegSelect = (regId) => {
  const reg = regOptions.value.find(r => r.id === regId)
  if (reg) {
    addForm.patientId = reg.patientId || ''
    addForm.doctorId = reg.doctorId || ''
  }
}

const onDrugSelect = (drugId) => {
  const d = drugOptions.value.find(x => x.id === drugId)
  if (d) { addDrug.drugName = d.drugName; addDrug.unitPrice = d.unitPrice }
}

const addDrugItem = () => {
  if (!addDrug.drugId || !addDrug.quantity) return
  addForm.drugItems.push({ drugId: addDrug.drugId, drugName: addDrug.drugName, unitPrice: addDrug.unitPrice, quantity: addDrug.quantity, usageMethod: addDrug.usageMethod })
  Object.assign(addDrug, { drugId: null, drugName: '', unitPrice: 0, quantity: 1, usageMethod: '' })
}

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

/** 打开新增处方弹窗 */
const handleAdd = async () => {
  Object.assign(addForm, { registrationId: '', patientId: '', doctorId: '', drugItems: [] })
  addVisible.value = true
  if (drugOptions.value.length === 0) {
    const res = await getDrugList({ page: 1, size: 100 })
    drugOptions.value = res.data.rows || []
  }
}

/** 提交新增处方 */
const handleAddSubmit = async () => {
  if (!addForm.patientId || !addForm.doctorId) {
    ElMessage.warning('请选择患者和医生')
    return
  }
  if (addForm.drugItems.length === 0) {
    ElMessage.warning('请至少添加一种药品')
    return
  }
  const res = await addPrescription(addForm)
  const prescriptionId = res.data
  for (const item of addForm.drugItems) {
    await addPrescriptionDetail({ prescriptionId, drugId: item.drugId, quantity: item.quantity, unitPrice: item.unitPrice, usageMethod: item.usageMethod })
  }
  ElMessage.success('处方创建成功')
  addVisible.value = false
  fetchData()
}

/** 删除处方（含二次确认） */
const handleDelete = async (row) => {
  await ElMessageBox.confirm('确认删除该处方？将同时删除所有药品明细', '警告', { type: 'warning' })
  await deletePrescription(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

/** 加载下拉选项 */
const loadOptions = async () => {
  const [pRes, dRes, rRes] = await Promise.all([
    getPatientList({ page: 1, size: 100 }),
    getDoctorList({ page: 1, size: 100 }),
    getRegistrationList({ page: 1, size: 200 })
  ])
  patientOptions.value = pRes.data.rows || []
  doctorOptions.value = dRes.data.rows || []
  regOptions.value = rRes.data.rows || []
}

/** 页面加载时初始化数据 */
onMounted(() => {
  fetchData()
  loadOptions()
})
</script>
