import { http } from '@/request/http'
import { type PageDTO, type PageVO } from '@/request/config'
import type { MakeOptional } from '@/type'

type BaseComment = {
  id: number
  sourceId: number
  floorId: number
  likeCount: number
  content: string[]
  replayContent: string[]
  createTime: Date
}

export type CommentDTO = MakeOptional<BaseComment> &
  MakeOptional<PageDTO> & {
    userId?: number
    parentUserId?: number
  }

export type CommentVO = BaseComment & {
  userId: number
  parentUserId: number
  username: string
  avatar: string
  roleName: string
  parentAvatar: string
  parentUsername: string
  parentRoleName: string
  children: CommentVO[]
}

export const addCommentApi = (data: FormData) => {
  return http<boolean>({
    url: `/ui/comment/insert`,
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: data
  })
}

export const pageCommentApi = (dto: CommentDTO) => {
  return http<PageVO<CommentVO>>({
    url: `/ui/comment/page`,
    method: 'GET',
    params: dto
  })
}

export const pageCommentAdminApi = (dto: CommentDTO) => {
  return http<PageVO<CommentVO>>({
    url: `/admin/comment/page`,
    method: 'GET',
    params: dto
  })
}

export const delCommentApi = (ids: number[]) => {
  return http<boolean>({
    url: `/admin/comment/delete`,
    method: 'DELETE',
    data: ids,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}
