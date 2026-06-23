/**
 * Vue Router 路由配置
 *
 * 功能：定义应用的所有路由规则，包括员工管理端和患者门户端两套路由体系。
 * 通过导航守卫实现：
 * - 未登录用户自动跳转到登录页
 * - 已登录用户根据角色（admin/doctor/patient）限制可访问的页面
 * - 患者只能访问 /patient-portal 路径下的页面
 * - 员工不能访问 /patient-portal 路径
 */

import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import MainLayout from '@/layout/MainLayout.vue'
import PatientLayout from '@/layout/PatientLayout.vue'

// 员工端路由（所有业务模块，供管理员和医生使用）
const staffRoutes = [
  {
    path: 'dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/index.vue'),
    meta: { title: '首页', icon: 'HomeFilled', roles: ['admin', 'doctor'] }
  },
  {
    path: 'registration',
    name: 'Registration',
    component: () => import('@/views/registration/index.vue'),
    meta: { title: '挂号管理', icon: 'Tickets', roles: ['admin'] }
  },
  {
    path: 'doctor',
    name: 'Doctor',
    component: () => import('@/views/doctor/index.vue'),
    meta: { title: '医生管理', icon: 'User', roles: ['admin'] }
  },
  {
    path: 'patient',
    name: 'Patient',
    component: () => import('@/views/patient/index.vue'),
    meta: { title: '患者管理', icon: 'UserFilled', roles: ['admin', 'doctor'] }
  },
  {
    path: 'drug',
    name: 'Drug',
    component: () => import('@/views/drug/index.vue'),
    meta: { title: '药品管理', icon: 'FirstAidKit', roles: ['admin'] }
  },
  {
    path: 'medical-record',
    name: 'MedicalRecord',
    component: () => import('@/views/medical-record/index.vue'),
    meta: { title: '病历管理', icon: 'Notebook', roles: ['admin', 'doctor'] }
  },
  {
    path: 'prescription',
    name: 'Prescription',
    component: () => import('@/views/prescription/index.vue'),
    meta: { title: '处方管理', icon: 'Document', roles: ['admin', 'doctor'] }
  },
  {
    path: 'examination',
    name: 'Examination',
    component: () => import('@/views/examination/index.vue'),
    meta: { title: '检查管理', icon: 'Monitor', roles: ['admin', 'doctor'] }
  },
  {
    path: 'laboratory',
    name: 'Laboratory',
    component: () => import('@/views/laboratory/index.vue'),
    meta: { title: '检验管理', icon: 'DataAnalysis', roles: ['admin', 'doctor'] }
  },
  {
    path: 'treatment',
    name: 'Treatment',
    component: () => import('@/views/treatment/index.vue'),
    meta: { title: '处置管理', icon: 'Cpu', roles: ['admin', 'doctor'] }
  },
  {
    path: 'payment',
    name: 'Payment',
    component: () => import('@/views/payment/index.vue'),
    meta: { title: '收费管理', icon: 'Money', roles: ['admin'] }
  },
  {
    path: 'department',
    name: 'Department',
    component: () => import('@/views/department/index.vue'),
    meta: { title: '科室管理', icon: 'OfficeBuilding', roles: ['admin'] }
  }
]

// 患者门户路由（患者自助服务页面）
const patientRoutes = [
  {
    path: '',
    redirect: '/patient-portal'
  },
  {
    path: '/patient-portal',
    component: PatientLayout,
    redirect: '/patient-portal/index',
    children: [
      {
        path: 'index',
        name: 'PatientHome',
        component: () => import('@/views/patient-portal/index.vue'),
        meta: { title: '患者首页' }
      },
      {
        path: 'registrations',
        name: 'PatientRegistrations',
        component: () => import('@/views/patient-portal/my-registrations.vue'),
        meta: { title: '我的挂号' }
      },
      {
        path: 'records',
        name: 'PatientRecords',
        component: () => import('@/views/patient-portal/my-records.vue'),
        meta: { title: '我的病历' }
      },
      {
        path: 'prescriptions',
        name: 'PatientPrescriptions',
        component: () => import('@/views/patient-portal/my-prescriptions.vue'),
        meta: { title: '我的处方' }
      }
    ]
  }
]

// 路由表：登录页 + 员工端路由 + 患者门户路由
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { noAuth: true } // 标记为免认证页面
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: staffRoutes
  },
  ...patientRoutes
]

// 创建路由实例，使用 HTML5 History 模式
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 角色白名单：定义每个角色可以访问的员工端路由路径
const routeRoleMap = {
  admin: staffRoutes.map(r => '/' + r.path),
  doctor: staffRoutes.filter(r => r.meta.roles.includes('doctor')).map(r => '/' + r.path)
}

/**
 * 全局前置导航守卫
 *
 * 处理逻辑：
 * 1. 访问登录页等免认证页面时，若已登录则重定向到对应首页
 * 2. 未登录用户重定向到 /login
 * 3. 患者用户只能访问 /patient-portal 路径
 * 4. 员工用户不能访问 /patient-portal 路径
 * 5. 根据角色检查页面访问权限
 */
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // 登录页放行
  if (to.meta.noAuth) {
    if (authStore.isLoggedIn) {
      return next(authStore.isPatient ? '/patient-portal' : '/dashboard')
    }
    return next()
  }

  // 未登录 → 登录页
  if (!authStore.isLoggedIn) {
    return next('/login')
  }

  // 患者只能访问 patient-portal 路径
  if (authStore.isPatient) {
    if (to.path.startsWith('/patient-portal')) {
      return next()
    }
    return next('/patient-portal')
  }

  // 员工不能访问 patient-portal
  if (to.path.startsWith('/patient-portal')) {
    return next('/dashboard')
  }

  // 角色检查：当前角色必须有对应路由权限
  const allowedPaths = routeRoleMap[authStore.role]
  if (!allowedPaths) {
    // 未知角色 → 登出并跳转登录页
    authStore.logout()
    return next('/login')
  }
  if (!allowedPaths.includes(to.path)) {
    return next('/dashboard')
  }

  next()
})

export default router
