<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { getAdminRouter } from '@/router'
import { useScrollStore } from '@/stores/scroll'

const route = useRoute()
const systemStore = useSystemStore()
const scrollStore = useScrollStore()

// router 数据
const data = getAdminRouter()
onMounted(() => {
  // 关闭 lenis 滚动条
  scrollStore.destroyLenis()
})
</script>
<template>
  <div class="w-screen h-screen flex">
    <el-aside class="w-fit">
      <el-menu
        :collapse="!systemStore.admin.isShowAside"
        :default-active="route.fullPath"
        class="h-screen hidden-scroll overflow-y-auto bg-color"
      >
        <a class="flex px-5 py-3 line-clamp-1" href="javascript:">
          <span class="text-3xl line-clamp-1">UTOPIA</span>
        </a>
        <router-menu :data="data" />
      </el-menu>
    </el-aside>

    <el-container class="overflow-hidden" direction="vertical">
      <el-header class="border-b">
        <admin-header />
      </el-header>
      <div class="px-5 pb-5 overflow-hidden">
        <div class="content-box py-5 overflow-visible">
          <transition appear name="admin">
            <div :key="route.fullPath" class="size-full">
              <router-view v-slot="{ Component }" name="admin">
                <component :is="Component" />
              </router-view>
            </div>
          </transition>
        </div>
      </div>
    </el-container>
  </div>
</template>

<style lang="scss">
.content-box {
  height: calc(100vh - 60px);
}

.el-scrollbar__bar {
  z-index: 100 !important;
}
</style>
