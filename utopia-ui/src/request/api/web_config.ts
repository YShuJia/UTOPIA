import { http } from '@/request/http'
import type { PageVO } from '@/request/config'

export type WebConfigVO = {
  id: number
  name: string
  authorName: string
  authorAvatar: string
  authorTag: string[]
  authorContact: Array<{ name: string; url: string; type: string }>
  authorPayment: Array<{ name: string; url: string }>
  authorHome: string[]
  authorMbti: { name: string; E: number; S: number; T: number; J: number }
  authorSkill: string[]
  authorGame: string[]
  authorBook: string[]
  authorFootprint: Array<{ name: string; position: string; time: Date }>
  authorAbout: string
  siteTitle: string
  siteMotto: string
  siteRecord: string
  siteFavicon: string
  siteAbout: string
  siteCreateTime: Date
  enabled: boolean
  createTime: Date
}

export type WebConfigDTO = WebConfigVO & {
  authorUrls: string[]
  siteUrls: string[]
  isUpdated: boolean
}

export const initWebConfigDTO = (): WebConfigDTO => {
  return {
    id: -1,
    name: '',
    authorName: '',
    authorAvatar: '',
    authorTag: [],
    authorContact: [],
    authorPayment: [],
    authorHome: [],
    authorMbti: { name: '', E: 0, S: 0, T: 0, J: 0 },
    authorSkill: [],
    authorGame: [],
    authorBook: [],
    authorFootprint: [],
    authorAbout: '',
    authorUrls: [],
    siteTitle: '',
    siteMotto: '',
    siteRecord: '',
    siteFavicon: '',
    siteAbout: '',
    siteCreateTime: new Date(),
    siteUrls: [],
    enabled: false,
    createTime: new Date(),
    isUpdated: false
  }
}

export const initWebConfigVO = (): WebConfigVO => {
  return {
    id: -1,
    name: '',
    authorName: '',
    authorAvatar: '',
    authorTag: [],
    authorContact: [],
    authorPayment: [],
    authorHome: [],
    authorMbti: { name: '', E: 0, S: 0, T: 0, J: 0 },
    authorSkill: [],
    authorGame: [],
    authorBook: [],
    authorFootprint: [],
    authorAbout: '',
    siteTitle: '',
    siteMotto: '',
    siteRecord: '',
    siteFavicon: '',
    siteAbout: '',
    siteCreateTime: new Date(),
    enabled: false,
    createTime: new Date()
  }
}

export const webConfigVO2DTO = (entity: WebConfigVO): WebConfigDTO => {
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
    siteCreateTime: new Date(entity.siteCreateTime),
    enabled: entity.enabled,
    createTime: entity.createTime,
    siteUrls: [],
    authorUrls: [],
    isUpdated: true
  }
}

export const getWebConfigDefaultApi = () => {
  return http<WebConfigVO>({
    url: `/ui/web-config/default`,
    method: 'GET'
  })
}

export const uploadWebConfigFileApi = (data: FormData) => {
  return http<string[]>({
    url: `/admin/web-config/upload/files`,
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: data
  })
}

export const getWebConfigAdminApi = (id: number) => {
  return http<WebConfigVO>({
    url: `/admin/web-config/${id}`,
    method: 'GET'
  })
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
