<template>
  <div class="page-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never">
          <template #header><span class="card-title">个人信息</span></template>
          <el-descriptions :column="1" border size="default">
            <el-descriptions-item label="用户名">{{ authStore.username }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ authStore.realName }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ {admin:'管理员',doctor:'医生',patient:'患者'}[authStore.role] || authStore.role }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <template #header><span class="card-title">修改密码</span></template>
          <el-form :model="pwdForm" ref="pwdFormRef" :rules="pwdRules" label-width="100px">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="saving" @click="handleChangePwd">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 管理员专属：系统用户管理 -->
    <el-card v-if="authStore.isAdmin" shadow="never" style="margin-top:20px">
      <template #header><span class="card-title">系统用户管理</span></template>
      <el-row :gutter="12" style="margin-bottom:12px">
        <el-col :span="6"><el-input v-model="newUser.username" placeholder="用户名" /></el-col>
        <el-col :span="4"><el-input v-model="newUser.realName" placeholder="姓名" /></el-col>
        <el-col :span="4"><el-input v-model="newUser.password" placeholder="密码" type="password" show-password /></el-col>
        <el-col :span="3">
          <el-select v-model="newUser.role" style="width:100%">
            <el-option label="管理员" value="admin" />
            <el-option label="医生" value="doctor" />
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-input-number v-if="newUser.role==='doctor'" v-model="newUser.doctorId" :min="1" placeholder="医生ID" controls-position="right" style="width:100%" />
        </el-col>
        <el-col :span="4"><el-button type="primary" @click="addSysUser">新增用户</el-button></el-col>
      </el-row>
      <el-alert v-if="newUser.role==='doctor'" title="医生ID需先在医生管理中添加医生获取" type="info" :closable="false" style="margin-bottom:8px" />
      <el-table :data="sysUsers" border stripe size="small" max-height="300">
        <el-table-column prop="id" label="ID" width="50" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="role" label="角色" width="80"><template #default="{row}">{{ {admin:'管理员',doctor:'医生'}[row.role] }}</template></el-table-column>
        <el-table-column prop="status" label="状态" width="70"><template #default="{row}"><el-tag :type="row.status===1?'success':'info'" size="small">{{row.status===1?'启用':'禁用'}}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{row}">
            <el-button v-if="row.username!==authStore.username" size="small" type="danger" @click="delSysUser(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { updatePassword } from '@/api/auth'
import request from '@/utils/request'

const authStore = useAuthStore()
const pwdFormRef = ref(null)
const saving = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })
const sysUsers = ref([])
const newUser = reactive({ username: '', realName: '', password: '', role: 'admin', doctorId: null })

const validateConfirm = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) callback(new Error('两次密码不一致'))
  else callback()
}
const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, min: 4, message: '至少4位', trigger: 'blur' }],
  confirmPassword: [{ required: true, validator: validateConfirm, trigger: 'blur' }]
}

const handleChangePwd = async () => {
  await pwdFormRef.value.validate()
  saving.value = true
  try {
    await updatePassword({ oldPassword: pwdForm.oldPassword, newPassword: pwdForm.newPassword })
    ElMessage.success('密码修改成功')
    Object.assign(pwdForm, { oldPassword: '', newPassword: '', confirmPassword: '' })
  } catch { }
  finally { saving.value = false }
}

const fetchSysUsers = async () => {
  if (!authStore.isAdmin) return
  const res = await request.get('/sys-user/list')
  sysUsers.value = res.data || []
}

const addSysUser = async () => {
  if (!newUser.username || !newUser.realName || !newUser.password) { ElMessage.warning('请填写完整信息'); return }
  if (newUser.role === 'doctor' && !newUser.doctorId) { ElMessage.warning('医生用户需填写医生ID'); return }
  await request.post('/sys-user', newUser)
  ElMessage.success('新增成功')
  Object.assign(newUser, { username: '', realName: '', password: '', role: 'admin', doctorId: null })
  fetchSysUsers()
}

const delSysUser = async (id) => {
  await ElMessageBox.confirm('确认删除该用户？', '警告', { type: 'warning' })
  await request.delete(`/sys-user/${id}`)
  ElMessage.success('删除成功')
  fetchSysUsers()
}

onMounted(fetchSysUsers)
</script>

<style scoped lang="scss">
.card-title { font-size: 15px; font-weight: 600; color: #2d3748; }
</style>
