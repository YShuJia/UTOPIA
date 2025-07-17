import { http } from '@/request/http'
import type { MakeOptional } from '@/type'
import { type PageDTO, type PageVO } from '@/request/config'
import type { ClassifyEnum } from '@/enum'

export type ClassifyVO = {
  name: string
  key: string
  introduction: string
}

type BaseClassify = {
  id: number
  name: string
  key: string
  introduction: string
  enabled: boolean
  reviewed: number
}

export type ClassifyDTO = MakeOptional<BaseClassify> & MakeOptional<PageDTO>

export type AdminClassifyVO = BaseClassify & {
  createTime: Date
  updateTime: Date
  createUsername: string
  updateUsername: string
}

export type AdminTreeVO = {
  value: number
  label: string
  children: AdminTreeVO[]
}

export const classifyVO2DTO = (vo: AdminClassifyVO): ClassifyDTO => {
  return {
    id: vo.id,
    name: vo.name,
    introduction: vo.introduction,
    key: vo.key,
    enabled: vo.enabled,
    reviewed: vo.reviewed
  }
}

export const initClassifyVO = (): ClassifyVO => {
  return {
    name: '',
    introduction: '',
    key: ''
  }
}

export const getClassifyApi = (id: number) => {
  return http<ClassifyVO>({
    url: `/ui/classify/${id}`,
    method: 'GET'
  })
}

export const treeClassifyApi = (key: ClassifyEnum) => {
  return http<AdminTreeVO[]>({
    url: `/admin/classify/tree/${key}`,
    method: 'GET'
  })
}

// 查询
export const pageClassifyApi = (dto: ClassifyDTO) => {
  return http<PageVO<AdminClassifyVO>>({
    url: `/admin/classify/page`,
    method: 'GET',
    params: dto
  })
}

// 新增
export function addClassifyApi(data: ClassifyDTO) {
  return http<boolean>({
    url: '/admin/classify/insert',
    method: 'POST',
    data: data
  })
}

// 修改
export function updateClassifyApi(data: ClassifyDTO) {
  return http<boolean>({
    url: '/admin/classify/update',
    method: 'PUT',
    data: data
  })
}

export function updateClassifyReviewedApi(data: ClassifyDTO) {
  return http<boolean>({
    url: '/admin/classify/update/reviewed',
    method: 'PUT',
    data: data
  })
}

// 删除
export function delClassifyApi(ids: number[]) {
  return http<boolean>({
    url: '/admin/classify/delete',
    method: 'DELETE',
    data: ids
  })
}
