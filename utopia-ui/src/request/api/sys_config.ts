import type { MakeOptional } from '@/type'
import { http } from '@/request/http'
import type { PageVO } from '@/request/config'

export type SysConfigVO = {
  id: number
  name: number
  sysPasswordCount: number
  sysPasswordTime: number
  sysPasswordBan: number
  sysLimitCount: number
  sysLimitTime: number
  sysLimitBan: number
  sysReplaceChar: string
  sysMaxExp: number
  sysRoleTable: string[]
  enabled: number
  createdTime: Date
}

export type SysConfigDTO = MakeOptional<SysConfigVO>

export const sysConfigVO2DTO = (entity: SysConfigVO): SysConfigDTO => {
  return {
    id: entity.id,
    name: entity.name,
    sysPasswordCount: entity.sysPasswordCount,
    sysPasswordTime: entity.sysPasswordTime,
    sysPasswordBan: entity.sysPasswordBan,
    sysLimitCount: entity.sysLimitCount,
    sysLimitTime: entity.sysLimitTime,
    sysLimitBan: entity.sysLimitBan,
    sysReplaceChar: entity.sysReplaceChar,
    sysMaxExp: entity.sysMaxExp,
    sysRoleTable: entity.sysRoleTable,
    enabled: entity.enabled
  }
}

export const listTableAdminApi = () => {
  return http<string[]>({
    url: `/admin/sys-config/list/table`,
    method: 'GET'
  })
}

export const pageSysConfigAdminApi = (dto: SysConfigDTO) => {
  return http<PageVO<SysConfigVO>>({
    url: `/admin/sys-config/page`,
    method: 'GET',
    params: dto
  })
}

export const addSysConfigApi = (sys_config: SysConfigDTO) => {
  return http<boolean>({
    url: `/admin/sys-config/insert`,
    method: 'POST',
    data: sys_config
  })
}

export const updateSysConfigApi = (data: SysConfigDTO) => {
  return http<boolean>({
    url: `/admin/sys-config/update`,
    method: 'PUT',
    data: data
  })
}

export const delSysConfigApi = (ids: number[]) => {
  return http<boolean>({
    url: `/admin/sys-config/delete`,
    method: 'DELETE',
    data: ids
  })
}
