<template>
  <div class="portal-page">
    <el-card shadow="never" class="portal-card">
      <template #header><div class="card-title"><el-icon><User /></el-icon><span>个人主页</span></div></template>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-descriptions :column="1" border size="default" title="个人信息">
            <el-descriptions-item label="姓名">{{ authStore.realName }}</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ authStore.idCard }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ authStore.phone }}</el-descriptions-item>
          </el-descriptions>
        </el-col>
        <el-col :span="12">
          <el-form :model="pwdForm" ref="pwdFormRef" :rules="pwdRules" label-width="100px">
            <h4 style="margin-bottom:16px;color:#2d3748">修改密码</h4>
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
              <el-button type="primary" :loading="saving" @click="handleSubmit">保存</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { updatePassword } from '@/api/auth'

const authStore = useAuthStore()
const pwdFormRef = ref(null)
const saving = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })
const validateConfirm = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) callback(new Error('两次密码不一致'))
  else callback()
}
const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, min: 4, message: '至少4位', trigger: 'blur' }],
  confirmPassword: [{ required: true, validator: validateConfirm, trigger: 'blur' }]
}
const handleSubmit = async () => {
  await pwdFormRef.value.validate()
  saving.value = true
  try {
    await updatePassword({ oldPassword: pwdForm.oldPassword, newPassword: pwdForm.newPassword })
    ElMessage.success('密码修改成功')
    Object.assign(pwdForm, { oldPassword: '', newPassword: '', confirmPassword: '' })
  } catch { }
  finally { saving.value = false }
}
</script>
