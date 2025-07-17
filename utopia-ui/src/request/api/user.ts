import { http } from '@/request/http'
import { type PageDTO, type PageVO } from '@/request/config'
import type { MakeOptional } from '@/type'
import { getEnFiled } from '@/request/api/index'
import type { ArticleVO } from '@/request/api/article'
import type { AlbumVO } from '@/request/api/album'
import type { DiaryVO } from '@/request/api/Diary'

export type UserVO = {
  avatar: string
  username: string
  email: string
  experience: number
  gender: string
  createTime: Date
  roleName: string
  roleIntroduction: string
  token: string
}

type BaseUser = {
  id: number
  roleId: number
  password: string
  avatar: string
  username: string
  email: string
  gender: string
  enabled: boolean
}

export type UserDTO = MakeOptional<BaseUser> & MakeOptional<PageDTO>

export type AdminUserVO = BaseUser & {
  experience: number
  roleName: string
  createTime: Date
  createUsername: string
  updateUsername: string
}

export const userAdminVO2DTO = (vo: AdminUserVO): UserDTO => {
  return {
    id: vo.id,
    roleId: vo.roleId,
    avatar: vo.avatar,
    username: vo.username,
    gender: vo.gender,
    email: vo.email,
    password: undefined,
    enabled: vo.enabled
  }
}

export const initUserVO = (): UserVO => {
  return {
    avatar: '',
    username: '',
    gender: '',
    email: '',
    experience: 0,
    createTime: new Date(),
    roleName: '',
    roleIntroduction: '',
    token: ''
  }
}

export const userVO2DTO = (vo: UserVO): UserDTO => {
  return {
    id: undefined,
    roleId: undefined,
    avatar: vo.avatar,
    username: vo.username,
    gender: vo.gender,
    email: vo.email,
    password: undefined,
    enabled: undefined
  }
}

export type SearchVO = {
  article: ArticleVO[]
  album: AlbumVO[]
  diary: DiaryVO[]
}

export const initSearchVO = (): SearchVO => {
  return {
    article: [],
    album: [],
    diary: []
  }
}

export const getCodeApi = () => {
  return http<string>({
    url: `/ui/user/code`,
    method: 'GET'
  })
}

export const exchangeSm2Api = () => {
  return http<string>({
    url: `/ui/user/exchange`,
    method: 'GET'
  })
}

export const getSearchApi = (content: string) => {
  return http<SearchVO>({
    url: `/ui/user/search/${content}`,
    method: 'GET'
  })
}

export const getTodayExperienceApi = () => {
  return http<number>({
    url: `/ui/user/today/experience`,
    method: 'GET'
  })
}

export const insertUserApi = (email: string, password: string) => {
  return http<boolean>({
    url: `/ui/user/insert`,
    method: 'POST',
    headers: {
      'content-type': 'application/x-www-form-urlencoded'
    },
    data: {
      email: getEnFiled(email),
      password: getEnFiled(password)
    }
  })
}

export const getUserApi = () => {
  return http<UserVO>({
    url: `/ui/user/info`,
    method: 'GET'
  })
}

export const updateUserApi = (user: UserDTO) => {
  user.password = getEnFiled(user.password)
  user.email = getEnFiled(user.email)
  return http<boolean>({
    url: `/ui/user/update`,
    method: 'PUT',
    data: user
  })
}

export const updateUserPassApi = (password: string, newPassword: string) => {
  return http<boolean>({
    url: `/ui/user/update/password`,
    method: 'PUT',
    headers: {
      'content-type': 'application/x-www-form-urlencoded'
    },
    data: {
      password: getEnFiled(password),
      newPassword: getEnFiled(newPassword)
    }
  })
}

export const pageUserAdminApi = (dto: UserDTO) => {
  return http<PageVO<AdminUserVO>>({
    url: `/admin/user/page`,
    method: 'GET',
    params: dto
  })
}

export const getUserDistributionAdminApi = () => {
  return http<{
    keys: string[]
    values: number[]
  }>({
    url: `/admin/user/distribution`,
    method: 'GET'
  })
}

export const insertUserAdminApi = (user: UserDTO) => {
  user.password = getEnFiled(user.password)
  user.email = getEnFiled(user.email)
  return http<boolean>({
    url: `/admin/user/insert`,
    method: 'POST',
    data: user
  })
}
