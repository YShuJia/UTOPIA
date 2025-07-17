import { http } from '@/request/http'
import { type PageDTO, type PageVO } from '@/request/config'
import type { MakeOptional } from '@/type'

export type FileVO = {
  url: string
}

type BaseFile = {
  id: number
  labelId: number
  name: string
  url: string
  tags: string[]
  size: number
  createTime: Date
  updateTime: Date
  enabled: boolean
  reviewed: number
  createUsername: string
  updateUsername: string
}

export type FileDTO = MakeOptional<BaseFile> & MakeOptional<PageDTO>

export type AdminFileVO = BaseFile & {
  labelName: string
}

export const fileVO2DTO = (vo: AdminFileVO): FileDTO => {
  return {
    id: vo.id,
    labelId: vo.labelId,
    name: vo.name,
    url: vo.url,
    tags: vo.tags,
    size: vo.size,
    enabled: vo.enabled,
    reviewed: vo.reviewed
  }
}

export const pageFileWallApi = (dto: FileDTO) => {
  return http<PageVO<FileVO>>({
    url: `/ui/file/page/wall`,
    method: 'GET',
    params: dto
  })
}

export const pageFileAvatarApi = (dto: FileDTO) => {
  return http<PageVO<FileVO>>({
    url: `/ui/file/page/avatar`,
    method: 'GET',
    params: dto
  })
}

export const pageFileAdminApi = (dto: FileDTO) => {
  return http<PageVO<AdminFileVO>>({
    url: `/admin/file/page`,
    method: 'GET',
    params: dto
  })
}

export const pageFileSelectAdminApi = (dto: FileDTO) => {
  return http<PageVO<FileVO>>({
    url: `/admin/file/page/select`,
    method: 'GET',
    params: dto
  })
}

export function addFileApi(dto: FormData) {
  return http<boolean>({
    url: '/admin/file/insert',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    method: 'POST',
    data: dto
  })
}

// 修改
export function updateFileApi(dto: FileDTO | FormData) {
  return http<boolean>({
    url: '/admin/file/update',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    method: 'PUT',
    data: dto
  })
}

export function updateFileReviewedApi(dto: FileDTO) {
  return http<boolean>({
    url: '/admin/file/update/reviewed',
    method: 'PUT',
    data: dto
  })
}

// 删除
export function delFileApi(ids: number[]) {
  return http<boolean>({
    url: '/admin/file/delete',
    method: 'DELETE',
    data: ids
  })
}
