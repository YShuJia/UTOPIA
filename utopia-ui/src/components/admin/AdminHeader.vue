<script lang="ts" setup>
// 系统状态
import { useSystemStore } from '@/stores/system'
import { RouteNameEnum } from '@/enum'
import { useUserStore } from '@/stores/user'
import { useGlobalDialog } from '@/hooks'
import { routerPath, routerTo } from '@/router'

const userStore = useUserStore()
const systemStore = useSystemStore()

const route = useRoute()

const handleRemoveRoute = (p: [string, string]) => {
  systemStore.admin.routeMap.delete(p[0])
  if (p[1] === route.fullPath) {
    routerTo(RouteNameEnum.ADMIN_HOME)
  }
}

const logout = async () => {
  await useGlobalDialog('确定退出登录?').then((res: boolean) => {
    if (res) {
      userStore.logout()
      routerTo(RouteNameEnum.LOGIN)
    }
  })
}

const scrollbarRef = ref()
const clickHandle = (p: string, index: number) => {
  if (index < 4) {
    index = -index
  }
  scrollbarRef.value.scrollTo({
    left: 60 * index,
    behavior: 'smooth'
  })
  routerPath(p)
}
</script>

<template>
  <el-container class="size-full flex gap-5 justify-between items-center">
    <el-container class="items-center size-full gap-5 overflow-hidden">
      <el-link
        :underline="false"
        class="min-w-10 use-btn-default h-10 border-2 border-amber-500"
        @click="systemStore.admin.isShowAside = !systemStore.admin.isShowAside"
      >
        <svg-icon v-if="systemStore.admin.isShowAside" name="error" size="2xl" />
        <svg-icon v-else name="menu" size="2xl" />
      </el-link>
      <div ref="scrollbarRef" class="pb-2.5 h-full overflow-x-auto overflow-y-hidden">
        <el-container class="gap-5 mt-2.5 w-full">
          <el-tag
            v-for="(p, i) in systemStore.admin.routeMap"
            :key="p[1]"
            class="h-8!"
            closable
            @close="handleRemoveRoute(p)"
          >
            <a
              :id="`${p[0]}`"
              :class="
                'flex text-sm items-center h-8 ' + (route.fullPath === p[1] ? 'text-amber-500' : '')
              "
              @click="clickHandle(p[1], i)"
            >
              {{ p[0] }}
            </a>
          </el-tag>
        </el-container>
      </div>
    </el-container>
    <el-container class="z-40 min-w-28! flex items-center justify-end gap-5">
      <el-link
        :underline="false"
        class="min-w-10 h-10 border-2 use-btn-default border-amber-500"
        @click="
          () => {
            systemStore.dialog.setting = true
            systemStore.system.isShowMobile = false
          }
        "
      >
        <svg-icon name="setting" size="2xl" />
      </el-link>
      <el-popover>
        <template #reference>
          <el-link :underline="false" class="min-w-10 h-10 border-amber-500">
            <el-avatar :size="40" :src="userStore.user.avatar" shape="square" />
          </el-link>
        </template>
        <el-container direction="vertical">
          <a
            v-if="!userStore.user.username"
            class="text-center w-full use-link-default"
            @click="routerTo(RouteNameEnum.LOGIN)"
          >
            登 录
          </a>
          <a v-else class="text-center w-full use-link-default" @click="logout"> 退 出 </a>
        </el-container>
      </el-popover>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped></style>
