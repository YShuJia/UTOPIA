<script lang="ts" setup>
import { useScrollStore } from '@/stores/scroll'
import { useSystemStore } from '@/stores/system'

const route = useRoute()
const scrollStore = useScrollStore()
const systemStore = useSystemStore()
// 内容 DOM（获取内容高度）
const scrollContentRef = ref<any>(null)
const scrollWrapperRef = ref<any>(null)

// 确保 UI 先挂载
const mounted = ref<boolean>(false)
onMounted(() => {
  scrollStore.scroll.contentRef = scrollContentRef.value
  scrollStore.scroll.scrollRef = scrollWrapperRef.value
  scrollStore.initScroll()
  mounted.value = true
  const headerRaf = document.querySelector('.header')
  if (headerRaf) {
    let hideTimeout: any
    headerRaf.addEventListener('mouseover', () => {
      systemStore.system.isShowHeader = true
      clearTimeout(hideTimeout)
    })
    headerRaf.addEventListener('mouseout', (event: any) => {
      // 判断是否真的离开了 .header 的边界
      hideTimeout = setTimeout(() => {
        systemStore.system.isShowHeader = false
      }, 3000)
    })
  }
})
</script>

<template>
  <el-container class="relative">
    <login-dialog />
    <el-container class="mx-auto items-center w-screen h-screen" direction="vertical">
      <el-header
        ref="headerRaf"
        :class="
          'header relative h-[60px] bg-gray-50/70 dark:bg-gray-950/70 ' +
          (systemStore.system.isShowHeader || systemStore.system.isShowMobile
            ? 'translate-y-0 opacity-100'
            : '-translate-y-full opacity-0')
        "
      >
        <header-bar />
      </el-header>
      <div
        ref="scrollWrapperRef"
        class="size-full hidden-scroll overflow-y-auto snap-none"
        @scroll="scrollStore.handleScroll"
      >
        <div ref="scrollContentRef" class="min-h-fit min-w-80">
          <router-view v-if="mounted" v-slot="{ Component }" name="ui">
            <keep-alive>
              <component :is="Component" :key="route.fullPath" />
            </keep-alive>
          </router-view>
          <footer-bar />
        </div>
      </div>
      <aircraft-scrollbar />
    </el-container>
  </el-container>
</template>

<style lang="scss">
.header {
  position: absolute;
  z-index: 99;
  width: 100vw;
  transition:
    opacity 0.5s linear,
    background-color 0.3s linear,
    transform 0.5s linear;
}
</style>
