/**
 * 首页统计 API 模块
 *
 * 封装首页仪表盘相关的接口：
 * - 获取统计概览数据（GET /api/dashboard/stats）
 */

import request from '@/utils/request'

/**
 * 获取首页统计概览数据
 * 返回今日挂号量、就诊量、收入等仪表盘指标
 * @returns {Promise} 统计指标数据
 */
export function getDashboardStats() {
  return request.get('/dashboard/stats')
}
