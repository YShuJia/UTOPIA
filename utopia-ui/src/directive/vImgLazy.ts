import type { DirectiveBinding } from 'vue'
import defaultImg from '@/assets/img/default.webp'

// 用于存储 img 元素和对应的真实图片地址的映射
const placeholderMap = new WeakMap()

// 创建 IntersectionObserver 实例，监听元素是否进入视口
const ob = new IntersectionObserver((entries) => {
  for (const entry of entries) {
    if (entry.isIntersecting || entry.boundingClientRect.y <= window.innerHeight * 2) {
      const el = entry.target as HTMLImageElement
      el.src = placeholderMap.get(el)
      el.onerror = () => {
        el.src = defaultImg
      }
      // 加载完成后可以取消观察，防止重复触发
      ob.unobserve(el)
      placeholderMap.delete(el)
    }
  }
})

export default {
  mounted(el: HTMLImageElement, binding: DirectiveBinding) {
    // 检查是否是 <img> 元素
    if (!(el instanceof HTMLImageElement)) {
      return
    }

    // 获取用户传入的图片真实路径
    const realSrc = binding.value
    if (!realSrc) {
      return
    }

    // 设置占位图（可选）
    el.src = binding.arg || defaultImg

    // 缓存真实图片地址
    placeholderMap.set(el, realSrc)

    // 开始监听该图片是否进入视口
    ob.observe(el)
  },
  unmounted(el: HTMLImageElement) {
    ob.unobserve(el)
    placeholderMap.delete(el)
  }
}
