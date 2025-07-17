import { http } from '@/request/http'
import { type PageDTO, type PageVO } from '@/request/config'
import type { MakeOptional } from '@/type'
import type { RouteNameEnum } from '@/enum'

export type RouterVO = {
  name: RouteNameEnum | string
  title: string
  admin: boolean
  type: 'H' | 'M' | 'B'
  frame: boolean
  path: string
  icon: string
  component: string
  children: RouterVO[]
}

export type AdminSelectRouterVO = {
  id: number
  title: string
  admin: boolean
}

export type BaseRouter = {
  id: number
  roleId: number
  sort: number
  parentId: number
  name: string
  title: string
  admin: boolean
  type: string
  frame: boolean
  path: string
  icon: string
  component: string
  enabled: boolean
}

export type RouterDTO = MakeOptional<BaseRouter> & MakeOptional<PageDTO>

export type AdminRouterVO = BaseRouter & {
  roleName: string
  children: AdminRouterVO[]
}

export const listRouterApi = () => {
  return http<RouterVO[]>({
    url: '/ui/router/list',
    method: 'GET'
  })
}

export const listSelectDataRouterApi = () => {
  return http<AdminSelectRouterVO[]>({
    url: `/admin/router/select`,
    method: 'GET'
  })
}

export const pageRouterAdminApi = (dto: RouterDTO) => {
  return http<PageVO<AdminRouterVO>>({
    url: '/admin/router/page',
    method: 'GET',
    params: dto
  })
}

// 新增
export function addRouterApi(data: RouterDTO) {
  return http<boolean>({
    url: '/admin/router/insert',
    method: 'POST',
    data: data
  })
}

// 修改
export function updateRouterApi(data: RouterDTO) {
  return http<boolean>({
    url: '/admin/router/update',
    method: 'PUT',
    data: data
  })
}

// 删除
export function delRouterApi(ids: number[]) {
  return http<boolean>({
    url: '/admin/router/delete',
    method: 'DELETE',
    data: ids
  })
}
