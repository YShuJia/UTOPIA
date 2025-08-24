import dayjs from 'dayjs'
// 引入中文语言包
import 'dayjs/locale/zh-cn'
import duration from 'dayjs/plugin/duration'
import relativeTime from 'dayjs/plugin/relativeTime'

dayjs.locale('zh-cn')
dayjs.extend(duration)
dayjs.extend(relativeTime)

export const formatDate = (date: string | Date | number) => {
  return dayjs(date).format('YYYY-MM-DD')
}

export const formatDatetime = (date: string | Date | number) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

export const fromNow = (date: string | Date | number) => {
  return dayjs(date).fromNow()
}

// 计算日期差异并返回格式化字符串
export function getDateDiff(start: string | Date | number, end: string | Date | number) {
  // 创建起始日期和结束日期对象
  const startDate = dayjs(formatDatetime(start), 'YYYY-MM-DD HH:mm:ss')
  const endDate = dayjs(formatDatetime(end), 'YYYY-MM-DD HH:mm:ss')
  // 计算日期差异
  const diff = endDate.diff(startDate)
  // 提取差异的时间单位
  const duration = dayjs.duration(diff)

  return {
    year: duration.years(),
    month: duration.months(),
    day: duration.days(),
    hour: duration.hours(),
    second: duration.seconds(),
    minute: duration.minutes(),
    millisecond: duration.milliseconds()
  }
}

export function getTimeObj() {
  const time = dayjs(new Date())
  return {
    year: time.year(),
    month: time.month() + 1,
    day: time.day() + 1,
    hour: time.hour(),
    minute: time.minute(),
    second: time.second(),
    millisecond: time.millisecond()
  }
}

export function getDateObj() {
  const time = dayjs(new Date())
  return {
    hour: time.hour(),
    minute: time.minute(),
    second: time.second()
  }
}
