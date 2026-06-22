import request from '@/utils/request'

export function login(data) {
  return request.post('/auth/login', data)
}

export function patientLogin(data) {
  return request.post('/auth/patient-login', data)
}

export function getUserInfo() {
  return request.get('/auth/info')
}
