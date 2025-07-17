import { ElMessageBox, ElNotification } from 'element-plus'

type NotificationConfig = {
  title?: string
  message?: string
  type?: 'success' | 'warning' | 'info' | 'error'
}

export async function useGlobalDialog(
  message: string = '确定要执行该操作？',
  title: string = '警告！'
) {
  return await ElMessageBox({
    title: title,
    type: 'warning',
    message: h('span', { style: 'word-break: break-all;' }, message),
    showCancelButton: true,
    closeOnClickModal: false,
    confirmButtonText: '确定',
    showClose: false,
    cancelButtonText: '取消',
    beforeClose: (action, instance, done) => {
      if (action === 'confirm') {
        instance.confirmButtonLoading = true
        instance.confirmButtonText = 'Loading...'
        setTimeout(() => {
          done()
          instance.confirmButtonLoading = false
        }, 2000)
      } else {
        done()
      }
    }
  })
    .then((action: string) => {
      return action === 'confirm'
    })
    .catch(() => {
      useGlobalNotification({ message: '已取消操作！', type: 'info' })
      return false
    })
}

export const useGlobalNotification = (config: NotificationConfig) => {
  const { title, message, type } = config

  ElNotification({
    title: title || '提示',
    message: message || '操作成功！',
    type: type || 'success'
  })
}

export const useGlobalLoading = (
  options: {
    text?: string
    background?: string
    spinner?: string | boolean
  } = {}
) => {
  return ElLoading.service({
    // 锁定屏幕，防止用户在加载过程中进行其他操作
    lock: true,
    // 自定义加载提示文本
    text: options.text || '加载中...',
    // 自定义背景颜色
    background: options.background || 'rgba(255,255,255,1)',
    // 自定义加载动画（可选）
    spinner: options.spinner,
    // 全屏模式
    fullscreen: true
  })
}
