// 将属性变为可选
import { RouteNameEnum } from '@/enum'

export type MakeOptional<T> = {
  [K in keyof T]?: T[K]
}

// 工具类型：从枚举中提取所有值
export type EnumValues<T> = T[keyof T]

export type RouterType = {
  path: string
  name: RouteNameEnum | string
  components: { [key in 'ui' | 'admin']?: any } | undefined
  meta: {
    title: string
    icon: string
    name: string
    type: 'H' | 'M' | 'B'
    admin: boolean
    frame: boolean
  }
  children: RouterType[]
}

export type SystemType = {
  // 是否离开页面
  isLeave: boolean
  // 是否开启滚动阻尼
  isOpenLenis: boolean
  // 是否开启点击效果
  isOpenClick: boolean
  // 显示移动端导航
  isShowMobile: boolean
  // 显示头部导航栏
  isShowHeader: boolean
  // 小于1024
  isLt1024: boolean
  // 小于768
  isLt768: boolean
  // 是移动端
  isMobile: boolean
  // 是否开启动画
  isOpenAnimation: boolean
  isAutoAnimation: boolean
  // 浏览器信息
  browserName: string
  // 浏览器版本信息
  browserVersion: string
  // 操作系统信息
  osName: string
  // 操作系统版本信息
  osVersion: string
}

/* 管理端 store 数据类型 */
export type AdminType = {
  // 管理端面包屑
  routeMap: Map<string, string>
  // 显示头部导航栏
  isShowAside: boolean
}

/* 背景 store 数据类型 */
export type BgType = {
  // 主要背景颜色
  bgColor: string
  // 次要背景颜色
  bgSecondColor: string
  bgType: 'dark' | 'light'
  bgIsAuto: boolean
}

/* svg大小 和 文字大小映射 */
export type SvgSizeType = 'xs' | 'sm' | 'base' | 'lg' | 'xl' | '2xl' | '3xl' | '4xl' | '5xl' | '6xl'

const _sizeMap = new Map<SvgSizeType, string>([
  ['xs', '12px'], // 12px
  ['sm', '14px'], // 14px
  ['base', '16px'], // 16px
  ['lg', '18px'], // 18px
  ['xl', '20px'], // 20px
  ['2xl', '24px'], // 24px
  ['3xl', '30px'], // 30px
  ['4xl', '36px'], // 36px
  ['5xl', '48px'], // 48px
  ['6xl', '60px'] // 60px
])

// 定义一个只读视图
export const sizeMap = {
  get(key: SvgSizeType): string {
    return _sizeMap.get(key) ?? ''
  }
}
