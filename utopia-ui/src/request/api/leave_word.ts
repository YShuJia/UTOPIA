import { http } from '@/request/http'
import type { PageDTO, PageVO } from '@/request/config'
import type { MakeOptional } from '@/type'

export type LeaveWordVO = {
  id: number
  avatar: string
  content: string
  likeCount: number
  createTime: Date
}

export type AdminLeaveWordVO = LeaveWordVO & {
  id: number
  ip: string
}

export type LeaveWordDTO = MakeOptional<AdminLeaveWordVO> & MakeOptional<PageDTO>

export const pageLeaveWordApi = (dto: LeaveWordDTO) => {
  return http<PageVO<LeaveWordVO>>({
    url: `/ui/leave_word/page`,
    method: 'GET',
    params: dto
  })
}

export function addLeaveWordApi(treeHole: LeaveWordDTO) {
  return http<boolean>({
    url: '/ui/leave_word/insert',
    method: 'POST',
    data: treeHole
  })
}

export const pageLeaveWordAdminApi = (dto: LeaveWordDTO) => {
  return http<PageVO<AdminLeaveWordVO>>({
    url: `/admin/leave_word/page`,
    method: 'GET',
    params: dto
  })
}

export function delLeaveWordApi(ids: number[]) {
  return http<boolean>({
    url: `/admin/leave_word/delete`,
    method: 'DELETE',
    data: ids
  })
}
