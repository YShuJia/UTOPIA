import { defineStore } from 'pinia'
import type { PageVO, ResultType } from '@/request/config'
import { type FileVO, pageFileWallApi } from '@/request/api/file'
import default_img from '@/assets/img/default.webp'
import type { AdminType, SystemType } from '@/type'
import { DialogEnum, TableEnum } from '@/enum'
import { browserName, browserVersion, isMobile, osName, osVersion } from 'vue-device-detect'
import { useScrollStore } from '@/stores/scroll'
import { throttle } from '@/utils'
import { exchangeSm2Api } from '@/request/api/user'
import { initWebConfigVO, type WebConfigVO } from '@/request/api/web_config'

export const useSystemStore = defineStore(
  'system',
  () => {
    // 系统信息
    const system = ref<SystemType>({
      isLeave: false,
      isOpenLenis: false,
      isOpenClick: true,
      isShowMobile: false,
      isShowHeader: true,
      isLt1024: false,
      isLt768: false,
      isMobile: Boolean(isMobile),
      isOpenAnimation: false,
      isAutoAnimation: false,
      browserName: browserName,
      browserVersion: browserVersion,
      osName: osName,
      osVersion: osVersion,
      innerWidth: window.innerWidth,
      innerHeight: window.innerHeight
    })

    // 后台信息
    const admin = ref<AdminType>({
      isShowAside: true,
      routeMap: new Map<string, string>()
    })

    // 监听窗口大小
    const handleResize = throttle(() => {
      const w = window.innerWidth
      system.value.innerWidth = w
      system.value.innerHeight = window.innerHeight
      system.value.isLt1024 = w < 1024
      system.value.isLt768 = w < 768
      system.value.isMobile = w < 1024
      // 动画控制
      if (system.value.isAutoAnimation) {
        system.value.isOpenAnimation = w > 1024
      }
      // 隐藏移动端导航
      if (w > 1024) {
        system.value.isShowMobile = false
      }
      useScrollStore().initScroll()
    }, 500)

    // 系统弹窗状态管理
    const dialog = ref<{ [key in 'preview' | DialogEnum | TableEnum]: boolean }>({
      preview: false,
      [DialogEnum.SEARCH]: false,
      [DialogEnum.SETTING]: false,
      [TableEnum.ROUTER]: false,
      [TableEnum.FILE]: false,
      [TableEnum.CLASSIFY]: false,
      [TableEnum.LABEL]: false,
      [TableEnum.USER]: false,
      [TableEnum.ALBUM]: false,
      [TableEnum.ROLE]: false,
      [TableEnum.DIARY]: false,
      [TableEnum.ARTICLE]: false,
      [TableEnum.COMMENT]: false,
      [TableEnum.WEBSITE]: false,
      [TableEnum.SYS_CONFIG]: false,
      [TableEnum.WEB_CONFIG]: false,
      [TableEnum.LEAVE_WORD]: false,
      [DialogEnum.MEDIA_FORM]: false,
      [DialogEnum.LOGIN]: false
    })

    const preview = {
      open: (urls: string | string[] | undefined, index: number = 0) => {
        dialog.value.preview = true
        if (urls === undefined) {
          preview.init()
        } else {
          preview.urls.value = typeof urls === 'string' ? [urls] : urls
        }
        preview.index.value = index
      },
      init: () => {
        dialog.value.preview = false
        preview.urls.value = []
        preview.index.value = 0
      },
      urls: ref<string[]>([]),
      index: ref<number>(0)
    }

    // 后端sm2公钥
    const backendSm2 = ref<{ publicKey: string }>({
      publicKey: ''
    })

    // 随机媒体列表
    const file = ref<FileVO[]>([])
    const getRandomImg = () => {
      if (file.value !== null && file.value.length > 0) {
        const random = Math.floor(Math.random() * file.value.length)
        return file.value![random]!.url
      }
      return default_img
    }

    const webConfig = ref<WebConfigVO>(initWebConfigVO())

    return {
      system,
      admin,
      preview,
      file,
      handleResize,
      dialog,
      backendSm2,
      webConfig,
      getRandomImg
    }
  },
  {
    persist: {
      pick: [
        'system.isOpenLenis',
        'system.isOpenClick',
        'system.isOpenAnimation',
        'system.isAutoAnimation',
        'admin.isShowAside',
        'admin.isAbsoluteAside'
      ],
      afterHydrate: ({ store }) => {
        exchangeSm2Api().then((res: ResultType<string>) => {
          store.backendSm2.publicKey = res.data
        })

        pageFileWallApi({ pageNum: 1, pageSize: 100 }).then((res: ResultType<PageVO<FileVO>>) => {
          store.file = res.data.list
        })
      }
    }
  }
)
