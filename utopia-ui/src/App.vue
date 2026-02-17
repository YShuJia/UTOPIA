<script lang="ts" setup>
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { useSystemStore } from '@/stores/system'
import { useUserStore } from '@/stores/user'
import { useStyleStore } from '@/stores/style'
import { useSocketStore } from '@/stores/socket'
import { useScrollStore } from '@/stores/scroll'
import { useTemporaryStore } from '@/stores/temporary'
import { disableInfo, throttle } from '@/utils'
import { getWebConfigDefaultApi, type WebConfigVO } from '@/request/api/web_config'
import type { ResultType } from '@/request/config'

const route = useRoute()

// 初始化 pinia
const systemStore = useSystemStore()
const userStore = useUserStore()
const styleStore = useStyleStore()
const socketStore = useSocketStore()
const temporaryStore = useTemporaryStore()

const scrollStore = useScrollStore()

let timeout: any
const headerHandle = throttle(() => {
  if (!systemStore.system.isShowHeader) {
    systemStore.system.isShowHeader = true
    return
  }
  if (timeout) {
    systemStore.system.isShowHeader = true
    clearTimeout(timeout)
  }
  timeout = setTimeout(() => {
    systemStore.system.isShowHeader = false
    clearTimeout(timeout)
  }, 3000)
}, 1000)

onMounted(() => {
  // 获取网站配置
  getWebConfigDefaultApi().then((res: ResultType<WebConfigVO>) => {
    systemStore.webConfig = res.data
  })
  window.addEventListener('resize', systemStore.handleResize)
  window.addEventListener('click', (e) => {
    headerHandle()
    styleStore.handleClick(e)
  })
  /* 监听离开页面 */
  let timer: any
  document.addEventListener('visibilitychange', (r: any) => {
    switch (r.target.visibilityState) {
      case 'hidden':
        document.title = '😱😱😱不要走！再看看嘛！'
        systemStore.system.isLeave = true
        break
      case 'visible':
        document.title = '😁😁😁欢迎肥来！'
        systemStore.system.isLeave = false
        timer = setTimeout(() => {
          document.title = 'UTOPIA'
          clearTimeout(timer)
        }, 3000)
        break
    }
  })
  // 生产环境禁用调试
  if (import.meta.env.VITE_PROD === 'true') {
    disableInfo()
  }
})
</script>

<template>
  <el-config-provider :locale="zhCn">
    <!-- 图片预览 -->
    <el-dialog
      v-model="systemStore.dialog.preview"
      :show-close="false"
      center
      class="img-view !p-0 cursor-pointer radius-sm w-full"
      top="10vh"
      @close="systemStore.preview.init()"
    >
      <el-carousel :autoplay="false" :initial-index="systemStore.preview.index" trigger="click">
        <el-carousel-item v-for="item in systemStore.preview.urls" :key="item">
          <image-box :lazy="false" :src="item" class="" fit="object-contain" />
        </el-carousel-item>
      </el-carousel>
    </el-dialog>
    <confirm-dialog v-show="false" />
    <el-container class="use-theme">
      <router-view v-slot="{ Component }" name="root">
        <component :is="Component" />
      </router-view>
    </el-container>
    <search-dialog />
    <setting-dialog />
  </el-config-provider>
</template>

<style lang="scss">
html,
body {
  overflow: hidden;
  position: relative;
}

* {
  font-family: sans-serif, '微软雅黑', Dubai !important;
  font-weight: 520;
  text-decoration: none;
  letter-spacing: 1px;
  scroll-behavior: auto;
}

img {
  pointer-events: none;
  -moz-user-select: none;
  -ms-user-select: none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  -o-user-select: none;
  user-select: none;
  overflow-clip-margin: content-box;
  overflow: clip;
}

/* 图片预览 */
.img-view {
  overflow: hidden;
  margin-bottom: 0 !important;

  .el-dialog__header {
    display: none !important;
  }

  .el-dialog__body {
    min-width: 320px !important;

    .is-active {
      position: relative;
    }

    .el-carousel,
    .el-carousel__container,
    .el-carousel__item {
      height: fit-content !important;
      aspect-ratio: 16/9;
    }
  }
}
</style>
