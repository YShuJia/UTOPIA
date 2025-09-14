import type { DirectiveBinding } from 'vue'
// 默认占位图
import defaultImg from '@/assets/img/default.webp'
// 使用 WeakMap 避免内存泄漏
const srcMap = new WeakMap<HTMLImageElement, string>()

// 创建 IntersectionObserver 实例
const observer = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        const img = entry.target as HTMLImageElement
        const realSrc = srcMap.get(img)

        if (realSrc) {
          // 设置真实图片地址
          img.src = realSrc
          // 加载成功
          img.onload = () => {
            img.classList.remove('img-loading')
            img.classList.add('img-loaded')
          }
          // 加载失败，回退到默认图
          img.onerror = () => {
            img.src = defaultImg
            img.classList.remove('img-loading')
            img.classList.add('img-error')
          }
          // 停止观察，防止重复触发
          observer.unobserve(img)
          srcMap.delete(img)
        }
      }
    })
  },
  {
    // 提前 100px 开始加载，提升用户体验
    rootMargin: '100px',
    // 只要露出 1% 就触发加载
    threshold: 0.01
  }
)
// 自定义指令定义
const ImgLazy = {
  mounted(el: HTMLImageElement, binding: DirectiveBinding<string>) {
    // 确保是 <img> 元素
    if (!(el instanceof HTMLImageElement)) {
      return
    }
    const realSrc = binding.value
    if (!realSrc) {
      return
    }
    // 设置加载中状态类
    el.classList.add('img-loading')
    // 支持通过 binding.arg 传入自定义占位图
    el.src = binding.arg || defaultImg
    // 启用浏览器原生懒加载作为兜底（兼容性好）
    el.loading = 'lazy'
    // 缓存真实图片地址
    srcMap.set(el, realSrc)
    // 开始监听元素是否进入视口
    observer.observe(el)
  },
  /**
   * 当元素被销毁时调用
   */
  unmounted(el: HTMLImageElement) {
    // 停止观察
    observer.unobserve(el)
    // 清理缓存
    srcMap.delete(el)
  }
}

export default ImgLazy
