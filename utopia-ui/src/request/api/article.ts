import { http } from '@/request/http'
import { type PageDTO, type PageVO } from '@/request/config'
import type { MakeOptional } from '@/type'

type BaseArticle = {
  id: number
  labelId: number
  title: string
  cover: string
  content: string
  copyright: string
  hasVideo: boolean
  passwordTip: string
  commentable: boolean
  likeable: boolean
}

export type ArticleVO = BaseArticle & {
  labelName: string
  classifyId: number
  classifyName: string
  likeCount: number
  viewCount: number
  commentCount: number
  updateTime: Date
}

export type AdminArticleVO = BaseArticle & {
  password: string
  recommend: boolean
  enabled: boolean
  reviewed: number
  labelName: string
  likeCount: number
  viewCount: number
  commentCount: number
  createTime: Date
  updateTime: Date
  createUsername: string
  updateUsername: string
}

export type ArticleDTO = MakeOptional<BaseArticle> &
  MakeOptional<PageDTO> & {
    password?: string
    recommend?: boolean
    enabled?: boolean
    reviewed?: number
    updateStatus?: boolean
    urls: string[]
    isUpdated: boolean
  }

export type ArchiveVO = {
  classifyId: number
  classifyName: string
  labelList: ArticleLabelVO[]
}

type ArticleLabelVO = {
  labelId: number
  labelName: string
  labelCover: string
  articleCount: string
}

export const articleVO2DTO = (vo: AdminArticleVO): ArticleDTO => {
  return {
    id: vo.id,
    labelId: vo.labelId,
    title: vo.title,
    cover: vo.cover,
    content: vo.content,
    hasVideo: vo.hasVideo,
    passwordTip: vo.passwordTip,
    recommend: vo.recommend,
    commentable: vo.commentable,
    copyright: vo.copyright,
    enabled: vo.enabled,
    likeable: vo.likeable,
    reviewed: vo.reviewed,
    password: undefined,
    urls: [],
    isUpdated: true
  }
}

export const initArticleVO = (): ArticleVO => {
  return {
    id: 0,
    labelId: 0,
    labelName: '',
    classifyId: 0,
    classifyName: '',
    title: '',
    cover: '',
    content: '',
    likeCount: 0,
    viewCount: 0,
    commentCount: 0,
    hasVideo: false,
    passwordTip: '',
    commentable: false,
    likeable: false,
    copyright: '',
    updateTime: new Date()
  }
}

export const getArticleApi = (id: number) => {
  return http<ArticleVO>({
    url: `/ui/article/${id}`,
    method: 'GET'
  })
}

export const getArticleDeployApi = () => {
  return http<ArticleVO[]>({
    url: `/ui/article/deploy/list`,
    method: 'GET'
  })
}

export const pageArticleApi = (dto: ArticleDTO) => {
  return http<PageVO<ArticleVO>>({
    url: `/ui/article/page`,
    method: 'GET',
    params: dto
  })
}

// 获取归档列表
export const listArchiveApi = () => {
  return http<ArchiveVO[]>({
    url: `/ui/article/archive/list`,
    method: 'GET'
  })
}

export const uploadArticleFileApi = (data: FormData) => {
  return http<string[]>({
    url: `/admin/article/upload/files`,
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: data
  })
}

export const getArticleAdminApi = (id: number) => {
  return http<AdminArticleVO>({
    url: `/admin/article/${id}`,
    method: 'GET'
  })
}

export const pageArticleAdminApi = (dto: ArticleDTO) => {
  return http<PageVO<AdminArticleVO>>({
    url: `/admin/article/page`,
    method: 'GET',
    params: dto
  })
}

export const addArticleApi = (dto: ArticleDTO) => {
  return http<boolean>({
    url: '/admin/article/insert',
    method: 'POST',
    data: dto
  })
}

// 修改
export const updateArticleApi = (dto: ArticleDTO) => {
  return http<boolean>({
    url: '/admin/article/update',
    method: 'PUT',
    data: dto
  })
}

export const updateArticleReviewedApi = (dto: ArticleDTO) => {
  return http<boolean>({
    url: '/admin/article/update/reviewed',
    method: 'PUT',
    data: dto
  })
}

export const delArticleApi = (ids: number[]) => {
  return http<boolean>({
    url: '/admin/article/delete',
    method: 'DELETE',
    data: ids
  })
}
