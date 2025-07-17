import { http } from '@/request/http'
import type { MakeOptional } from '@/type'
import { type PageDTO, type PageVO } from '@/request/config'

export type RoleVO = {
  name: string
  experience: number
  introduction: string
}

export type AdminSelectRoleVO = {
  id: number
  name: string
  permission: string[]
}

export type AdminRoleVO = RoleVO & {
  id: number
  permission: string[]
  enabled: boolean
  admin: boolean
  createTime: Date
}

export type RoleDTO = MakeOptional<AdminRoleVO> & MakeOptional<PageDTO>

export const roleVO2DOTO = (vo: AdminRoleVO): RoleDTO => {
  return {
    id: vo.id,
    name: vo.name,
    experience: vo.experience,
    permission: vo.permission,
    enabled: vo.enabled,
    createTime: undefined,
    introduction: vo.introduction
  }
}

export const getGtRoleApi = () => {
  return http<RoleVO>({
    url: `/ui/role/gtId`,
    method: 'GET'
  })
}

export const listRoleApi = () => {
  return http<RoleVO[]>({
    url: `/ui/role/list`,
    method: 'GET'
  })
}

export const listSelectDataRoleApi = () => {
  return http<AdminSelectRoleVO[]>({
    url: `/admin/role/select`,
    method: 'GET'
  })
}

export const pageRoleAdminApi = (dto: RoleDTO) => {
  return http<PageVO<AdminRoleVO>>({
    url: '/admin/role/page',
    method: 'GET',
    params: dto
  })
}

export const getRoleTablesApi = () => {
  return http<string[]>({
    url: '/admin/role/list/table',
    method: 'GET'
  })
}

export const updateRoleApi = (dto: RoleDTO) => {
  return http<boolean>({
    url: '/admin/role/update',
    method: 'PUT',
    data: dto
  })
}
