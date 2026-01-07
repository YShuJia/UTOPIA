import { defineStore } from 'pinia'
import ScrollTrigger from 'gsap/ScrollTrigger'
import Lenis from 'lenis'
import { gsap } from 'gsap'
import { debounce, throttle } from '@/utils'
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
      // 先移除，再添加 gsap ticker
      gsap.ticker.remove(gaspFun)
      gsap.ticker.add(gaspFun)
    } else {
      if (gaspFun) {
        gsap.ticker.remove(gaspFun)
      }
    }
  }

  // 滚动结束后使飞机向上
  const debouncedRotate180 = debounce(() => {
    scroll.value.isDown = false
  }, 3000)

  let scheduled = false
  const scheduleUpdate = (progress: number) => {
    if (!scheduled) {
      requestAnimationFrame(() => {
        computerScroll(progress)
        scheduled = false
      })
      scheduled = true
    }
  }

  // 初始化
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
        lerp: 0.1,
        // 用于鼠标滚轮事件的乘数 默认 1
        wheelMultiplier: 0.8
      })
      scroll.value.lenis.on('scroll', (e: any) => {
        ScrollTrigger.update()
        scroll.value.isDown = e.direction > 0
        scheduleUpdate(e.progress)
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
    const newScrollTop = progress * systemStore.system.innerHeight
    const newIsToTop = progress < 0.01
    const newIsToBottom = progress > 0.99
    const newIsDown = oldProgress.value < progress
    // 只有变化时才更新
    if (scroll.value.scrollTop !== newScrollTop) {
      scroll.value.scrollTop = newScrollTop
    }
    if (scroll.value.isToTop !== newIsToTop) {
      scroll.value.isToTop = newIsToTop
    }
    if (scroll.value.isToBottom !== newIsToBottom) {
      scroll.value.isToBottom = newIsToBottom
    }
    if (scroll.value.isDown !== newIsDown) {
      scroll.value.isDown = newIsDown
    }
    // header 显示逻辑
    if (oldProgress.value > progress !== systemStore.system.isShowHeader) {
      systemStore.system.isShowHeader = oldProgress.value > progress
    }
    oldProgress.value = progress
    debouncedRotate180()
  }

  // 关闭滚动阻尼时的滚动事件
  const handleScroll = throttle((e: any) => {
    if (!systemStore.system.isOpenLenis) {
      // 获取滚动内容总高度
      const contentHeight =
        scroll.value.contentRef?.clientHeight ?? systemStore.system.innerHeight + 1
      // 计算比例 (滚动距离/(滚动内容总高度 - 可视区高度))
      let scale = e.target.scrollTop / (contentHeight - systemStore.system.innerHeight)
      scale = scale > 1 ? 1 : scale
      scheduleUpdate(scale)
    }
  }, 50)

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
