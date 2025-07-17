import { http } from '@/request/http'
import { type PageDTO, type PageVO } from '@/request/config'
import type { MakeOptional } from '@/type'
import type { ClassifyDTO } from '@/request/api/classify'

export type LabelVO = {
  id: number
  name: string
  classifyName: string
  introduction: string
}

type BaseLabel = {
  id: number
  name: string
  cover: string
  introduction: string
  classifyId: number
  enabled: boolean
  reviewed: number
}

export type LabelDTO = MakeOptional<BaseLabel> & MakeOptional<PageDTO>

export type AdminLabelVO = BaseLabel & {
  classifyName: string
}

export const labelVO2DTO = (vo: AdminLabelVO): LabelDTO => {
  return {
    id: vo.id,
    cover: vo.cover,
    classifyId: vo.classifyId,
    name: vo.name,
    introduction: vo.introduction,
    enabled: vo.enabled,
    reviewed: vo.reviewed
  }
}

export const initLabelVO = (): LabelVO => {
  return {
    id: 0,
    name: '',
    classifyName: '',
    introduction: ''
  }
}

export const listLabelByClassifyApi = (dto: ClassifyDTO) => {
  return http<LabelVO[]>({
    url: `/ui/label/classify`,
    method: 'GET',
    params: dto
  })
}

export const pageLabelAdminApi = (dto: LabelDTO) => {
  return http<PageVO<AdminLabelVO>>({
    url: `/admin/label/page`,
    method: 'GET',
    params: dto
  })
}

export function addLabelApi(data: LabelDTO) {
  return http<boolean>({
    url: '/admin/label/insert',
    method: 'POST',
    data: data
  })
}

// 修改
export function updateLabelApi(data: LabelDTO) {
  return http<boolean>({
    url: '/admin/label/update',
    method: 'PUT',
    data: data
  })
}

export function updateLabelReviewedApi(dto: LabelDTO) {
  return http<boolean>({
    url: '/admin/label/update/reviewed',
    method: 'PUT',
    data: dto
  })
}

// 删除
export function delLabelApi(ids: number[]) {
  return http<boolean>({
    url: '/admin/label/delete',
    method: 'DELETE',
    data: ids
  })
}
