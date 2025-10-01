import type { MakeOptional } from '@/type'
import { http } from '@/request/http'
import type { PageVO } from '@/request/config'

export type WebConfigVO = {
  id: number
  name: string
  authorName: string
  authorAvatar: string
  authorTag: string[]
  authorContact: string[]
  authorPayment: string[]
  authorHome: string[]
  authorMbti: string[]
  authorSkill: string[]
  authorGame: string[]
  authorBook: string[]
  authorFootprint: string[]
  authorAbout: string
  siteTitle: string
  siteMotto: string
  siteRecord: string
  siteFavicon: string
  siteAbout: string
  siteCreateTime: Date
  enabled: boolean
  createdTime: Date
}

export type WebConfigDTO = MakeOptional<WebConfigVO>

export const sysConfigVO2DTO = (entity: WebConfigVO): WebConfigDTO => {
  return {
    id: entity.id,
    name: entity.name,
    authorName: entity.authorName,
    authorAvatar: entity.authorAvatar,
    authorTag: entity.authorTag,
    authorContact: entity.authorContact,
    authorPayment: entity.authorPayment,
    authorHome: entity.authorHome,
    authorMbti: entity.authorMbti,
    authorSkill: entity.authorSkill,
    authorGame: entity.authorGame,
    authorBook: entity.authorBook,
    authorFootprint: entity.authorFootprint,
    authorAbout: entity.authorAbout,
    siteTitle: entity.siteTitle,
    siteMotto: entity.siteMotto,
    siteRecord: entity.siteRecord,
    siteFavicon: entity.siteFavicon,
    siteAbout: entity.siteAbout,
    siteCreateTime: entity.siteCreateTime,
    enabled: entity.enabled
  }
}

export const pageWebConfigAdminApi = (dto: WebConfigDTO) => {
  return http<PageVO<WebConfigVO>>({
    url: `/admin/web-config/page`,
    method: 'GET',
    params: dto
  })
}

export const addWebConfigApi = (sys_config: WebConfigDTO) => {
  return http<boolean>({
    url: `/admin/web-config/insert`,
    method: 'POST',
    data: sys_config
  })
}

export const updateWebConfigApi = (data: WebConfigDTO) => {
  return http<boolean>({
    url: `/admin/web-config/update`,
    method: 'PUT',
    data: data
  })
}

export const delWebConfigApi = (ids: number[]) => {
  return http<boolean>({
    url: `/admin/web-config/delete`,
    method: 'DELETE',
    data: ids
  })
}
