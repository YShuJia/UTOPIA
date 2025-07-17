import { defineStore } from 'pinia'
import ScrollTrigger from 'gsap/ScrollTrigger'
import Lenis from 'lenis'
import { gsap } from 'gsap'
import { debounce } from '@/utils'
import { useSystemStore } from '@/stores/system'

export type ScrollType = {
  scrollRef: HTMLElement
  contentRef: HTMLElement
  lenis: any
  scrollTop: number
  isDown: boolean
  isToTop: boolean
  isToBottom: boolean
}

export const useScrollStore = defineStore('scroll', () => {
  const systemStore = useSystemStore()

  const scroll = ref<ScrollType>({
    // 滚动条实列
    scrollRef: document.body,
    contentRef: document.body,
    // 相对视口距离
    scrollTop: 0,
    // 是否到达了顶部
    isToTop: true,
    // 是否向下滚动
    isDown: false,
    // lenis 实例
    lenis: null,
    // 到达底部
    isToBottom: false
  })

  let gaspFun: (time: number) => void
  const toggleScroll = (val: boolean) => {
    if (val) {
      if (!gaspFun) {
        gaspFun = (time: number) => {
          scroll.value.lenis?.raf(time * 1000)
        }
      }
      gsap.ticker.add(gaspFun)
    } else {
      if (gaspFun) {
        gsap.ticker.remove(gaspFun)
      }
    }
  }

  const initScroll = () => {
    if (
      !systemStore.system.isLt1024 &&
      systemStore.system.isOpenLenis &&
      scroll.value.scrollRef &&
      scroll.value.contentRef
    ) {
      destroyLenis()
      scroll.value.lenis = new Lenis({
        wrapper: scroll.value.scrollRef,
        content: scroll.value.contentRef,
        // 线性插值强度（介于 0 和 1 之间）默认 0.1
        lerp: 0.05,
        // 用于鼠标滚轮事件的乘数 默认 1
        wheelMultiplier: 0.5
      })
      scroll.value.lenis.on('scroll', (e: any) => {
        ScrollTrigger.update()
        scroll.value.isDown = e.direction > 0
        computerScroll(e.progress)
      })
      toggleScroll(true)
      gsap.ticker.lagSmoothing(0)
    } else if (systemStore.system.isLt1024 || !systemStore.system.isOpenLenis) {
      destroyLenis()
    }
  }

  const destroyLenis = () => {
    if (scroll.value.lenis) {
      toggleScroll(false)
      scroll.value.lenis.destroy()
      scroll.value.lenis = null
    }
  }

  watch(
    () => systemStore.system.isOpenLenis,
    () => {
      initScroll()
    },
    {
      immediate: true,
      deep: true
    }
  )

  const oldProgress = ref<number>(0)
  const computerScroll = (progress: number) => {
    // 比例乘以窗口高度，得到飞机应该滑动距离
    scroll.value.scrollTop = progress * window.innerHeight
    // 跟新滚动条状态
    scroll.value.isToTop = progress < 0.01
    scroll.value.isToBottom = progress > 0.99
    scroll.value.isDown = oldProgress.value < progress
    systemStore.system.isShowHeader = oldProgress.value > progress
    oldProgress.value = progress
    debouncedRotate180()
  }

  // 滚动结束后使飞机向上
  const debouncedRotate180 = debounce(() => {
    scroll.value.isDown = false
  }, 3000)

  // 关闭滚动阻尼时的滚动事件
  const handleScroll = (e: any) => {
    if (!scroll.value.lenis) {
      // 获取滚动内容总高度
      const contentHeight = scroll.value.contentRef?.clientHeight ?? window.innerHeight + 1
      // 计算比例 (滚动距离/(滚动内容总高度 - 可视区高度))
      let scale = e.target.scrollTop / (contentHeight - window.innerHeight)
      scale = scale > 1 ? 1 : scale
      computerScroll(scale)
    }
  }

  const scrollTo = (top: number = 0, duration: number = 5) => {
    if (scroll.value.lenis) {
      scroll.value.lenis.scrollTo(top, { duration: duration, lerp: 0.1 })
    } else if (scroll.value.scrollRef) {
      scroll.value.scrollRef.scrollTo({ behavior: 'smooth', top: top })
    }
  }

  return {
    scroll,
    destroyLenis,
    initScroll,
    handleScroll,
    toggleScroll,
    scrollTo
  }
})
