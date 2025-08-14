import { type PageDTO, type PageVO } from '@/request/config'
import { http } from '@/request/http'
import type { MakeOptional } from '@/type'

export type DiaryVO = {
  id: number
  content: string
  title: string
  urls: string[]
  year: number
  month: number
  likeCount: number
  viewCount: number
  commentCount: number
  commentable: boolean
  likeable: boolean
  updateTime: Date
  createUsername: string
}

export type AdminDiaryVO = DiaryVO & {
  recommend: boolean
  createTime: Date
  enabled: boolean
  reviewed: number
  createUsername: string
  updateUsername: string
}

export type DiaryDTO = MakeOptional<PageDTO> &
  MakeOptional<DiaryVO> & {
    recommend?: boolean
    enabled?: boolean
    reviewed?: number
    updateStatus?: boolean
  }

export const initDiaryVO = (): DiaryVO => {
  return {
    id: 0,
    content: '',
    title: '',
    year: 0,
    month: 0,
    urls: [''],
    likeCount: 0,
    viewCount: 0,
    commentCount: 0,
    commentable: false,
    likeable: false,
    updateTime: new Date(),
    createUsername: ''
  }
}

export const diaryVO2DTO = (vo: DiaryVO): DiaryDTO => {
  return {
    id: vo.id,
    title: vo.title,
    content: vo.content,
    year: vo.year,
    month: vo.month,
    urls: vo.urls,
    likeCount: vo.likeCount,
    viewCount: vo.viewCount,
    commentCount: vo.commentCount,
    commentable: vo.commentable,
    likeable: vo.likeable,
    updateTime: vo.updateTime
  }
}

export const getDiaryApi = (id: number) => {
  return http<DiaryVO>({
    url: `/ui/diary/${id}`,
    method: 'GET'
  })
}

export const pageDiaryApi = (dto: DiaryDTO) => {
  return http<PageVO<DiaryVO>>({
    url: '/ui/diary/page',
    method: 'GET',
    params: dto
  })
}

export const pageDiaryAdminApi = (dto: DiaryDTO) => {
  return http<PageVO<AdminDiaryVO>>({
    url: '/admin/diary/page',
    method: 'GET',
    params: dto
  })
}

export const addDiaryApi = (dto: DiaryDTO | FormData) => {
  return http({
    url: '/admin/diary/insert',
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: dto
  })
}

export const updateDiaryApi = (dto: DiaryDTO | FormData) => {
  return http<boolean>({
    url: '/admin/diary/update',
    method: 'PUT',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: dto
  })
}

export const updateDiaryReviewedApi = (dto: DiaryDTO) => {
  return http<boolean>({
    url: '/admin/diary/update/reviewed',
    method: 'PUT',
    data: dto
  })
}

export function delDiaryApi(ids: number[]) {
  return http<boolean>({
    url: '/admin/diary/delete',
    method: 'DELETE',
    data: ids
  })
}
