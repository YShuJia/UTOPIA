/* 交换密钥 */
import { http } from '@/request/http'
import type { UserVO } from '@/request/api/user'
import { SmUtils } from '@/utils/smUtils'
import { useSystemStore } from '@/stores/system'

export const loginApi = (username: string, password: string) => {
  return http<UserVO>({
    url: `/login`,
    method: 'POST',
    headers: {
      'content-type': 'application/x-www-form-urlencoded'
    },
    data: {
      username: getEnFiled(username),
      password: getEnFiled(password),
    }
  })
}

export const logoutApi = () => {
  return http<boolean>({
    url: `/logout`,
    method: 'POST'
  })
}

export const delMinioFilesApi = (urls: string[]) => {
  return http<boolean>({
    url: `/minio/delete`,
    method: 'DELETE',
    data: urls
  })
}

export const getEnFiled = (filed: string | undefined) => {
  if (filed !== undefined && filed !== '') {
    filed = SmUtils.enSm2(useSystemStore().backendSm2.publicKey, filed)
  }
  return filed
}
