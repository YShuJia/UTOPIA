import axios, { AxiosError, type AxiosResponse } from 'axios'
import type { Config, ResultType } from '@/request/config'
import { SmUtils } from '@/utils/smUtils'
import { PUBLIC_KEY, TOKEN_KEY } from '@/constant'
import { serializeParams } from '@/utils'
import { getToken, refreshTokenByHeaders } from '@/utils/tokenUtils'
import {
  handleDecryption,
  handleError,
  handleUnauthorized,
  parseResponse,
  showLoadingMessage,
  showResponseMessage
} from '@/utils/httpUtils'
import { Code } from '@/request/code'
// 创建axios实例
const service = axios.create({
  // 请求地址
  baseURL: import.meta.env.VITE_API_PREFIX,
  // 超时 180s
  timeout: import.meta.env.VITE_API_TIMEOUT,
  headers: {
    'Content-type': 'application/json;charset=utf-8',
    'X-Requested-With': 'XMLHttpRequest'
  }
})

service.interceptors.request.use(
  (config) => {
    // 添加公钥
    config.headers[PUBLIC_KEY] = SmUtils.keypair.publicKey
    // 添加 token
    config.headers[TOKEN_KEY] = getToken()
    // get 请求参数序列化
    if (config.method?.toUpperCase() === 'GET' && config.params) {
      let url = config.url + '?' + serializeParams(config.params)
      config.params = {}
      config.url = url
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  async (response: AxiosResponse<ResultType<any>, Config>) => {
    // 解析返回数据
    const { code } = parseResponse(response)
    // 数据解密
    handleDecryption(response)
    // 登录过期
    handleUnauthorized(code)
    // 刷新 token
    refreshTokenByHeaders(response.headers)
    return response
  },
  (err: AxiosError<any>) => {
    return Promise.reject(handleError(err))
  }
)

export const http = <T>(config: Config): Promise<ResultType<T>> => {
  const load = showLoadingMessage(config.method)
  return new Promise<ResultType<T>>((resolve, reject) => {
    service
      .request<ResultType<T>>(config)
      .then((res: AxiosResponse<ResultType<T>>) => {
        const result = res.data
        const method = config.method.toUpperCase()
        const time = setTimeout(() => {
          load?.close()
          if (method !== 'GET' || result.code !== Code.SUCCESS) {
            showResponseMessage(result)
          }
          clearTimeout(time)
        }, 500)
        result.code === Code.SUCCESS ? resolve(result) : reject(result)
      })
      .catch((err: ResultType<any>) => {
        console.log(err)
        const time = setTimeout(() => {
          load?.close()
          if (config.method.toUpperCase() !== 'GET') {
            showResponseMessage(err)
          }
          clearTimeout(time)
        }, 500)
        reject(err)
      })
  })
}
