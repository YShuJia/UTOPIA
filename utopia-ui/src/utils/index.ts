export const disableInfo = () => {
  // 屏蔽 F12 键
  document.addEventListener('keydown', (e) => {
    if (e.code === 'F12') {
      e.preventDefault()
      e.stopPropagation()
      return false
    }
    // 屏蔽 Ctrl + Shift + I
    if (e.ctrlKey && e.shiftKey && e.code === 'KeyI') {
      e.preventDefault()
      e.stopPropagation()
      return false
    }

    // 屏蔽 Shift + F10
    if (e.shiftKey && e.code === 'F10') {
      e.preventDefault()
      e.stopPropagation()
      return false
    }
  })
  // 屏蔽右键单击
  document.addEventListener('contextmenu', (e) => {
    e.preventDefault()
    e.stopPropagation()
    return false
  })
}

export const throttle = (fn: any, delay: number) => {
  let canRun: boolean = true
  return (...args: any[]) => {
    if (!canRun) {
      return
    }
    canRun = false
    fn(...args)
    const timer = setTimeout(() => {
      canRun = true
      clearTimeout(timer)
    }, delay)
  }
}

export function debounce(fn: any, delay: number, immediate: boolean = false) {
  let timer: ReturnType<typeof setTimeout>
  return function (...args: any[]) {
    if (immediate && !timer) {
      fn.apply(args)
    }
    if (timer) {
      clearTimeout(timer)
    }
    timer = setTimeout(() => {
      if (!immediate) {
        fn.apply(args)
        clearTimeout(timer)
      }
    }, delay)
  }
}

export const createOpacityColor = (color: string, opacity: number): string => {
  opacity = Math.min(1, Math.max(0, opacity))
  // 去除空格以简化后续处理
  color = color.toLowerCase()
  let cleanedColor = color.trim().replace(/\s/g, '')
  if (/^#([A-Fa-f0-9]{8})$/.test(cleanedColor) || /^#([A-Fa-f0-9]{3}){1,2}$/.test(cleanedColor)) {
    // 处理十六进制颜色值
    return hexToRgba(cleanedColor, opacity)
  } else if (cleanedColor.includes('rgb')) {
    // 处理 rgb 颜色值
    const match = cleanedColor.match(/,\s*[\d|\W]\W\w+\)$/g)
    if (match && match.length > 0) {
      const transparency = Number(match[0].replace(',', '').replace(')', ''))
      if (transparency <= opacity) {
        return color
      }
    } else {
      return cleanedColor.replace(')', `,${opacity})`)
    }
  }
  return color
}

// 将十六进制颜色转换为RGBA格式
function hexToRgba(hex: string, opacity: number): string {
  // 将短格式 (#abc) 转换为长格式 (#aabbcc)
  let longHex = hex.replace(/^#?([a-f\d])([a-f\d])([a-f\d])$/i, (m, r, g, b) => {
    return '#' + r + r + g + g + b + b
  })
  // 移除开头的 # 号
  longHex = longHex.replace(/^#/, '')
  // 将每两个字符转换为十进制数
  const r = parseInt(longHex.slice(0, 2), 16)
  const g = parseInt(longHex.slice(2, 4), 16)
  const b = parseInt(longHex.slice(4, 6), 16)

  return `rgba(${r},${g},${b},${opacity})`
}

/**
 * 判断对象是否全部属性都有值
 * @param obj
 * @param excludes
 */
export function hasUndefined<T extends object>(obj: T, excludes: (keyof T)[] = []): boolean {
  function check(obj: any): boolean {
    if (typeof obj !== 'object' || obj === null) {
      return false
    }
    for (const key in obj) {
      if (Object.prototype.hasOwnProperty.call(obj, key)) {
        const value = obj[key]
        if (value === undefined && !excludes.includes(key as keyof T)) {
          return true
        } else if (typeof value === 'object') {
          if (check(value)) {
            return true
          }
        }
      }
    }
    return false
  }

  return check(obj)
}

/**
 * 判断对象是否至少有一个属性有值
 * @param obj
 * @param excludes
 */
export function hasOneNoUndefined<T extends object>(obj: T, excludes: (keyof T)[] = []): boolean {
  function check(obj: any): boolean {
    if (typeof obj !== 'object' || obj === null) {
      return false
    }
    for (const key in obj) {
      if (Object.prototype.hasOwnProperty.call(obj, key)) {
        const value = obj[key]
        if (value !== undefined && !excludes.includes(key as keyof T)) {
          return true
        } else if (typeof value === 'object') {
          if (check(value)) {
            return true
          }
        }
      }
    }
    return false
  }

  return check(obj)
}

export function hasOneNoEmpty<T extends object>(obj: T, excludes: (keyof T)[] = []): boolean {
  function check(obj: any): boolean {
    if (typeof obj !== 'object' || obj === null) {
      return false
    }
    for (const key in obj) {
      if (Object.prototype.hasOwnProperty.call(obj, key)) {
        const v = obj[key]
        const empty = isEmpty(v)
        if (!empty && !excludes.includes(key as keyof T)) {
          return true
        } else if (typeof v === 'object') {
          if (check(v)) {
            return true
          }
        }
      }
    }
    return false
  }

  return check(obj)
}

/**
 * 合并两个对象，如果 newObj 中有值，则覆盖 originalObj 中的值，否则保留 originalObj 中的值
 * @param originalObj
 * @param newObj
 */
export function mergeObjects<T extends object>(originalObj: T, newObj: T): T {
  const result: Partial<T> = { ...originalObj } // 创建一个浅拷贝以避免修改原始对象

  for (const key of Object.keys(newObj) as (keyof T)[]) {
    const value1 = originalObj[key]
    const value2 = newObj[key]

    if (value2 !== undefined) {
      if (value1 === undefined) {
        result[key] = value2
      } else {
        result[key] = value1 // 如果 obj1 中有值，则保留该值
      }
    }
    // 如果 value2 是 undefined 并且 value1 也是 undefined，则不做任何操作
  }
  return result as T
}

/**
 * 判断对象是否为空
 * @param obj
 */
export function isEmpty(obj: any): boolean {
  // 处理 null 和 undefined
  if (obj == null) {
    return true
  }
  switch (typeof obj) {
    // 检查字符串是否为空
    case 'string':
      return obj.length === 0
    case 'object':
      if (Array.isArray(obj)) {
        return obj.length === 0
      } else if (obj instanceof Set || obj instanceof Map) {
        return obj.size === 0
      } else if (obj.constructor === Object) {
        return Object.keys(obj).length === 0
      }
      // 对于其他对象类型（如Date, RegExp等），返回false
      return false
    default:
      return false
  }
}

function isObject(item: any): item is object {
  return typeof item === 'object' && item !== null && !Array.isArray(item)
}

function appendToFormData(formData: FormData, prefix: string, value: any): void {
  if (isObject(value)) {
    for (const [key, nestedValue] of Object.entries(value)) {
      const newPrefix = prefix ? `${prefix}.${key}` : key
      appendToFormData(formData, newPrefix, nestedValue)
    }
  } else if (Array.isArray(value)) {
    value.forEach((item, index) => {
      const newPrefix = `${prefix}[${index}]`
      appendToFormData(formData, newPrefix, item)
    })
  } else if (value !== undefined) {
    formData.append(prefix, value)
  }
}

export const addToFormData = (formData: FormData, obj: Record<string, any>): FormData => {
  for (const [key, value] of Object.entries(obj)) {
    appendToFormData(formData, key, value)
  }
  return formData
}
/**
 * 将对象转换为 URL 编码的查询字符串
 * 支持嵌套对象和数组
 */
export function serializeParams(params: Record<string, any>): string {
  if (params === null || typeof params !== 'object') {
    return params
  }
  const result: string[] = []

  function appendParam(result: string[], keyPath: string, value: any): void {
    if (value === null || value === undefined || value === '') {
      return
    }
    // 处理数组
    if (Array.isArray(value)) {
      value.forEach((item) => {
        appendParam(result, `${keyPath}[]`, item)
      })
    }
    // 处理对象（非数组）
    else if (typeof value === 'object') {
      for (const subKey of Object.keys(value)) {
        appendParam(result, `${keyPath}[${subKey}]`, value[subKey])
      }
    } else {
      // 普通值
      result.push(`${encodeURIComponent(keyPath)}=${encodeURIComponent(value)}`)
    }
  }

  for (const key of Object.keys(params)) {
    const value = params[key]
    // 递归处理单个参数
    appendParam(result, key, value)
  }
  return result.join('&')
}
