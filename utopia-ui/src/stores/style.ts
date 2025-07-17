import { defineStore } from 'pinia'
import { ref } from 'vue'
import { pop } from '@/utils/clickUtils'
import { ClickEnum } from '@/enum'
import type { BgType } from '@/type'

export const useStyleStore = defineStore(
  'style',
  () => {
    const font = ref<any>({
      fontSize: '1rem',
      fontStyle: 'OpenSans',
      fontLightColor: getComputedStyle(document.documentElement).getPropertyValue('--text-light'),
      fontDarkColor: getComputedStyle(document.documentElement).getPropertyValue('--text-dark')
    })

    const mouse = ref({
      mouseStyle: 'default',
      mouseClickType: ClickEnum.CHAR,
      isMouseClick: true
    })

    const initBackground = (): BgType => {
      return {
        bgColor: getComputedStyle(document.documentElement).getPropertyValue('--el-bg-color'),
        bgSecondColor: getComputedStyle(document.documentElement).getPropertyValue('--el-bg-color'),
        bgType: 'light',
        bgIsAuto: true
      }
    }

    const resetBackground = () => {
      background.value = initBackground()
    }

    const background = ref<BgType>(initBackground())

    const shadow = ref({
      size: Number(
        getComputedStyle(document.documentElement)
          .getPropertyValue('--shadow-size')
          .replace('px', '')
      )
    })
    const radius = ref({
      lg: Number(
        getComputedStyle(document.documentElement).getPropertyValue('--radius-lg').replace('px', '')
      ),
      sm: Number(
        getComputedStyle(document.documentElement).getPropertyValue('--radius-sm').replace('px', '')
      )
    })

    /* 自动设置主题色 */
    const refreshBgColor = () => {
      // 自动切换
      if (background.value.bgIsAuto) {
        const hour = new Date().getHours()
        if (hour >= 6 && hour < 18) {
          background.value.bgType = 'light'
        } else {
          background.value.bgType = 'dark'
        }
      }
      const html = document.querySelector('html')
      if (html) {
        background.value.bgColor = getComputedStyle(document.documentElement).getPropertyValue(
          '--el-bg-color'
        )
        background.value.bgSecondColor = getComputedStyle(
          document.documentElement
        ).getPropertyValue('--el-bg-color')
        html.className = background.value.bgType.toString()
      }
    }

    // 页面点击效果
    const handleClick = (e: MouseEvent) => {
      if (mouse.value.isMouseClick) {
        pop(e, mouse.value.mouseClickType)
      }
    }

    return {
      font,
      mouse,
      background,
      shadow,
      radius,
      handleClick,
      refreshBgColor,
      resetBackground
    }
  },
  {
    persist: {
      key: 'style',
      storage: localStorage,
      afterRestore: ({ store }) => {
        /* 监听主题色变化 */
        watch(
          () => store.background,
          () => {
            store.refreshBgColor()
          },
          {
            deep: true
          }
        )

        watch(
          () => store.radius,
          () => {
            document.documentElement.style.setProperty('--radius-lg', store.radius.lg + 'px')
            document.documentElement.style.setProperty('--radius-sm', store.radius.sm + 'px')
          },
          {
            immediate: true,
            deep: true
          }
        )

        watch(
          () => store.shadow,
          () => {
            document.documentElement.style.setProperty('--shadow-size', store.shadow.size + 'px')
          },
          {
            immediate: true,
            deep: true
          }
        )
      }
    }
  }
)
