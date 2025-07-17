import { Code } from '@/request/code'
import type { Method, ResultType } from '@/request/config'
import { SmUtils } from '@/utils/smUtils'
import { AxiosError, type AxiosResponse } from 'axios'
import { clearToken, hasToken } from '@/utils/tokenUtils'
import { useGlobalLoading } from '@/hooks'

export const parseResponse = (response: AxiosResponse<ResultType<any>>) => {
  return { code: response.data.code, msg: response.data.msg, data: response.data.data }
}

export const handleDecryption = (response: AxiosResponse<ResultType<any>>) => {
  if (response.data.code === Code.NEED_DECRYPTION) {
    const data = SmUtils.deSm2(SmUtils.keypair.privateKey, response.data.data)
    if (data) {
      response.data.data = JSON.parse(data)
    }
    response.data.code = Code.SUCCESS
  }
}

export const handleUnauthorized = (code: number) => {
  if (code === Code.UNAUTHORIZED && hasToken()) {
    clearToken()
    const time = setTimeout(() => {
      window.location.reload()
      clearTimeout(time)
    }, 1200)
  }
}

export const showResponseMessage = (res: ResultType<any>) => {
  const { code, msg, data } = res
  // 请求成功与否的提示信息
  if (typeof data === 'boolean') {
    data ? ElMessage.success(msg) : ElMessage.warning(msg)
  } else if (code === Code.SUCCESS || code === Code.NEED_DECRYPTION) {
    ElMessage.success(msg)
  } else if (code === Code.SERVER_ERROR) {
    ElMessage.error(msg)
  } else {
    ElMessage.warning(msg)
  }
}

const msg = new Map<Method, string>()
msg.set('GET', '正在加载数据，请稍等...')
msg.set('POST', '正在上传数据，请稍等...')
msg.set('PUT', '正在上传数据，请稍等...')
msg.set('DELETE', '正在删除，请稍等...')
export const showLoadingMessage = (method: Method) => {
  if (method !== undefined && method !== 'GET') {
    return useGlobalLoading({
      text: msg.get(method as Method),
      background: 'rgba(50,50,50,0.6)'
    })
  }
}

export const handleError = (err: AxiosError): ResultType<any> => {
  let message = err.message
  let code = err.response?.status || 400
  let statusText = err.response?.statusText || '服务器异常！'
  console.log(err)
  if (message === 'Network Error') {
    message = '后端接口连接异常'
  } else if (message.includes('timeout')) {
    message = '请求超时！'
  } else if (message.includes('Request failed with status code')) {
    message = '系统接口异常！'
  }
  ElMessage.error(message)
  return {
    code,
    msg: statusText,
    data: false
  }
}
