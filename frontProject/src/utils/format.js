/**
 * 工具函数模块 - 格式化工具
 *
 * 提供日期时间格式化、金额格式化、性别格式化、状态映射等通用工具函数，
 * 供各业务组件在展示数据时使用。
 */

/**
 * 格式化日期时间（精确到秒）
 * 将日期对象或字符串转换为 "yyyy-MM-dd HH:mm:ss" 格式
 * @param {string|Date} date 日期值
 * @returns {string} 格式化后的日期时间字符串
 */
export function formatDateTime(date) {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

/**
 * 格式化日期（精确到日）
 * 将日期对象或字符串转换为 "yyyy-MM-dd" 格式
 * @param {string|Date} date 日期值
 * @returns {string} 格式化后的日期字符串
 */
export function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

/**
 * 格式化金额
 * 将数值保留两位小数，如 10 => "10.00"
 * @param {number} amount 金额数值
 * @returns {string} 格式化后的金额字符串
 */
export function formatMoney(amount) {
  if (amount === null || amount === undefined) return '0.00'
  return Number(amount).toFixed(2)
}

/**
 * 性别格式化
 * 将数字格式的性别转换为中文显示
 * @param {number} gender 性别（1=男，其他=女）
 * @returns {string} "男" 或 "女"
 */
export function formatGender(gender) {
  return gender === 1 ? '男' : '女'
}

/**
 * 状态格式化
 * 根据状态码和映射表返回对应的中文状态名称
 * @param {number} status 状态码
 * @param {object} map 状态映射表（如 {0: '待诊', 1: '已诊'}）
 * @returns {string} 状态的中文名称
 */
export function formatStatus(status, map = {}) {
  return map[status] || '未知'
}

/**
 * 挂号状态映射表
 * 0=待诊, 1=已诊, 2=已取消
 */
export const regStatusMap = {
  0: '待诊',
  1: '已诊',
  2: '已取消'
}

/**
 * 处方状态映射表
 * 0=未取药, 1=已取药
 */
export const prescriptionStatusMap = {
  0: '未取药',
  1: '已取药'
}

/**
 * 支付状态映射表
 * 0=未支付, 1=已支付
 */
export const paymentStatusMap = {
  0: '未支付',
  1: '已支付'
}
