import { http } from '@/request/http'
import { type PageDTO, type PageVO } from '@/request/config'
import type { MakeOptional } from '@/type'

export type WebsiteVO = {
  title: string
  avatar: string
  introduction: string
  url: string
  cover: string
  createTime: Date
}

type BaseWebsite = {
  id: number
  userId: number
  labelId: number
  title: string
  avatar: string
  introduction: string
  url: string
  cover: string
  recommend: boolean
  enabled: boolean
  reviewed: number
}

export type WebsiteDTO = MakeOptional<BaseWebsite> & MakeOptional<PageDTO>

export type AdminWebsiteVO = BaseWebsite & {
  labelName: string
  createTime: Date
  updateTime: Date
  createUsername: string
  updateUsername: string
}

export const websiteVO2DTO = (vo: AdminWebsiteVO): WebsiteDTO => {
  return {
    id: vo.id,
    userId: vo.userId,
    labelId: vo.labelId,
    title: vo.title,
    avatar: vo.avatar,
    introduction: vo.introduction,
    url: vo.url,
    cover: vo.cover,
    recommend: vo.recommend,
    enabled: vo.enabled,
    reviewed: vo.reviewed
  }
}

export const addWebsiteApi = (data: WebsiteDTO) => {
  return http<boolean>({
    url: `/admin/website/insert`,
    method: 'POST',
    data: data
  })
}

export const pageWebsiteApi = (dto: WebsiteDTO) => {
  return http<PageVO<WebsiteVO>>({
    url: `/ui/website/page`,
    method: 'GET',
    params: dto
  })
}

export const pageWebsiteAdminApi = (dto: WebsiteDTO) => {
  return http<PageVO<AdminWebsiteVO>>({
    url: `/admin/website/page`,
    method: 'GET',
    params: dto
  })
}

export const updateWebsiteApi = (data: WebsiteDTO) => {
  return http<boolean>({
    url: `/admin/website/update`,
    method: 'PUT',
    data: data
  })
}

export const updateWebsiteReviewedApi = (dto: WebsiteDTO) => {
  return http<boolean>({
    url: `/admin/website/update/reviewed`,
    method: 'PUT',
    data: dto
  })
}

export const delWebsiteApi = (ids: number[]) => {
  return http<boolean>({
    url: `/admin/website/delete`,
    method: 'DELETE',
    data: ids
  })
}
