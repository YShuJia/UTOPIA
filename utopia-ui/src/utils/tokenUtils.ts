import { TOKEN_KEY, TOKEN_PREFIX } from '@/constant'
import { type AxiosResponseHeaders, type RawAxiosResponseHeaders } from 'axios'

export const getToken = () => {
  const token = localStorage.getItem(TOKEN_KEY)
  if (token !== null) {
    return TOKEN_PREFIX + token
  }
  return null
}

export const hasToken = (): boolean => {
  return localStorage.getItem(TOKEN_KEY) !== null
}

export const clearToken = () => {
  localStorage.removeItem(TOKEN_KEY)
}

export const setToken = (token: string | undefined | null) => {
  if (token !== null && token !== undefined) {
    if (token.startsWith(TOKEN_PREFIX)) {
      token = token.replace(TOKEN_PREFIX, '')
    }
    localStorage.setItem(TOKEN_KEY, token)
  }
}

export function refreshTokenByHeaders(headers: RawAxiosResponseHeaders | AxiosResponseHeaders) {
  let authHeader: string | undefined
  // 判断是否是 Headers 类型（即是否有 get 方法）
  if (typeof (headers as AxiosResponseHeaders).get === 'function') {
    authHeader = (headers as AxiosResponseHeaders).get(TOKEN_KEY)?.toString()
  } else {
    // 普通对象，使用小写 key 获取
    authHeader = (headers as RawAxiosResponseHeaders)[TOKEN_KEY.toLowerCase()]?.toString()
  }
  const token = authHeader?.startsWith(TOKEN_PREFIX) ? authHeader : null
  setToken(token)
}
