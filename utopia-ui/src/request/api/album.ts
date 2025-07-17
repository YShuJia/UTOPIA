import { type PageDTO, type PageVO } from '@/request/config'
import { http } from '@/request/http'
import type { MakeOptional } from '@/type'

export type AlbumVO = {
  id: number
  title: string
  labelName: string
  urls: string[]
  likeCount: number
  viewCount: number
  commentCount: number
  introduction: string
  likeable: boolean
  commentable: boolean
  updateTime: Date
}

type BaseAlbum = {
  id: number
  labelId: number
  title: string
  urls: string[]
  commentCount: number
  introduction: string
  likeable: boolean
  recommend: boolean
  commentable: boolean
  enabled: boolean
  reviewed: number
}

export type AdminAlbumVO = BaseAlbum & {
  labelName: string
  likeCount: number
  viewCount: number
  updateTime: Date
  createTime: Date
  createUsername: string
  updateUsername: string
}

export type AlbumDTO = MakeOptional<PageDTO> &
  MakeOptional<BaseAlbum> & {
    updateStatus?: boolean
  }

export const albumVO2DTO = (vo: AdminAlbumVO): AlbumDTO => {
  return {
    id: vo.id,
    labelId: vo.labelId,
    title: vo.title,
    urls: vo.urls,
    introduction: vo.introduction,
    recommend: vo.recommend,
    likeable: vo.likeable,
    commentable: vo.commentable,
    commentCount: vo.commentCount,
    enabled: vo.enabled,
    reviewed: vo.reviewed
  }
}

export const initAlbumVO = (): AlbumVO => {
  return {
    id: 0,
    title: '',
    labelName: '',
    urls: [],
    introduction: '',
    likeable: false,
    commentable: false,
    updateTime: new Date(),
    likeCount: 0,
    viewCount: 0,
    commentCount: 0
  }
}

export const getAlbumApi = (id: number) => {
  return http<AlbumVO>({
    url: `/ui/album/${id}`,
    method: 'GET'
  })
}

export const pageAlbumApi = (dto: AlbumDTO) => {
  return http<PageVO<AlbumVO>>({
    url: `/ui/album/page`,
    method: 'GET',
    params: dto
  })
}

export const pageAlbumAdminApi = (dto: AlbumDTO) => {
  return http<PageVO<AdminAlbumVO>>({
    url: `/admin/album/page`,
    method: 'GET',
    params: dto
  })
}

export const addAlbumApi = (form: FormData) => {
  return http<boolean>({
    url: `/admin/album/insert`,
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: form
  })
}

export const updateAlbumApi = (data: FormData | AlbumDTO) => {
  return http<boolean>({
    url: `/admin/album/update`,
    method: 'PUT',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: data
  })
}

export const updateAlbumReviewedApi = (dto: AlbumDTO) => {
  return http<boolean>({
    url: '/admin/album/update/reviewed',
    method: 'PUT',
    data: dto
  })
}

export const delAlbumApi = (ids: number[]) => {
  return http<boolean>({
    url: `/admin/album/delete`,
    method: 'DELETE',
    data: ids
  })
}
