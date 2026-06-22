import request from '@/utils/request'

export function getDepartmentList(params) {
  return request.get('/department/list', { params })
}

export function getDepartmentById(id) {
  return request.get(`/department/${id}`)
}
