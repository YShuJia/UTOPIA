import type { Code } from '@/request/code'

export type Method = 'GET' | 'DELETE' | 'OPTIONS' | 'POST' | 'PUT' | 'PATCH'

export type Config = {
  url: string
  method: Method
  data?: any
  params?: any
  headers?: any
}

export type ResultType<T> = {
  code: Code
  msg: string
  data: T
}

export type PageVO<T> = {
  pageSize: number
  pageNum: number
  total: number
  isEmpty: boolean
  list: Array<T>
}

export const initPageVO = () => {
  return {
    pageSize: 0,
    pageNum: 0,
    total: 0,
    isEmpty: false,
    list: []
  }
}

export type PageDTO = {
  //每页数量
  pageSize: number
  //当前页
  pageNum: number
  // 排序字段
  orderByColumns: string[]
}

export const initPageDTO = () => {
  return {
    pageSize: 10,
    pageNum: 1,
    orderByColumns: []
  }
}
