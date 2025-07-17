import { http } from '@/request/http'

export type CpuVO = {
  // CPU核心数
  cpuNum: number
  // CPU总的使用率
  total: number
  // CPU系统使用率
  sysUsed: number
  // CPU用户使用率
  userUsed: number
  // CPU当前等待率
  wait: number
  // CPU当前空闲率
  free: number
}

export const initCpuVO = (): CpuVO => {
  return {
    cpuNum: 0,
    total: 0,
    sysUsed: 0,
    userUsed: 0,
    wait: 0,
    free: 0
  }
}

export type JvmVO = {
  // 当前JVM占用的内存总数(M)
  total: number
  // JVM最大可用内存总数(M)
  max: number
  // JVM空闲内存(M)
  free: number
  // JDK版本
  version: string
  // JDK路径
  home: string
  startTime: string

  runTime: string

  inputArgs: string
}

export const initJvmVO = (): JvmVO => {
  return {
    total: 0,
    max: 0,
    free: 0,
    version: '',
    home: '',
    startTime: '',
    runTime: '',
    inputArgs: ''
  }
}

export type MemVO = {
  // 内存总量
  total: number
  // 已用内存
  used: number
  // 剩余内存
  free: number
}

export const initMemVO = (): MemVO => {
  return {
    total: 0,
    used: 0,
    free: 0
  }
}

export type SysFileVO = {
  // 盘符路径
  dirName: string
  // 盘符类型
  sysTypeName: string
  // 文件类型
  typeName: string
  // 总大小
  total: string
  // 剩余大小
  free: string
  // 已经使用量
  used: string
  // 资源的使用率
  usage: number
}

export const initSysFileVO = (): SysFileVO => {
  return {
    dirName: '',
    sysTypeName: '',
    typeName: '',
    total: '',
    free: '',
    used: '',
    usage: 0
  }
}

export type SysVO = {
  // 服务器名称
  computerName: string
  // 服务器Ip
  computerIp: string
  // 项目路径
  userDir: string
  // 操作系统
  osName: string
  // 系统架构
  osArch: string
}

export const initSysVO = (): SysVO => {
  return {
    computerName: '',
    computerIp: '',
    userDir: '',
    osName: '',
    osArch: ''
  }
}

export type ServerVO = {
  // CPU信息
  cpuVO: CpuVO
  // 內存信息
  memVO: MemVO
  // JVM信息
  jvmVO: JvmVO
  // 服务器信息
  sysVO: SysVO
  // 磁盘信息
  sysFilesVO: SysFileVO[]
}

export const initServerVO = (): ServerVO => {
  return {
    cpuVO: initCpuVO(),
    memVO: initMemVO(),
    jvmVO: initJvmVO(),
    sysVO: initSysVO(),
    sysFilesVO: [initSysFileVO()]
  }
}

export type SystemCountVO = {
  // 角色数量
  role: number
  // 用户数量
  user: number
  album: number
  article: number
  comment: number
  leaveWord: number
  website: number
  like: number
  classify: number
  label: number
  file: number
  router: number
}

export const initSystemCountVO = (): SystemCountVO => {
  return {
    role: 0,
    user: 0,
    album: 0,
    article: 0,
    comment: 0,
    leaveWord: 0,
    website: 0,
    like: 0,
    classify: 0,
    label: 0,
    file: 0,
    router: 0
  }
}

export const getServerApi = () => {
  return http<ServerVO>({
    url: '/admin/server/info',
    method: 'GET'
  })
}

export const getSystemCountApi = () => {
  return http<SystemCountVO>({
    url: '/admin/home/count',
    method: 'GET'
  })
}
