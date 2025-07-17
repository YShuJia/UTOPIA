<script lang="ts" setup>
// 控制移动端导航栏显示 默认隐藏
import { useSystemStore } from '@/stores/system'
import { RouteNameEnum } from '@/enum'
import { getUiRouter } from '@/router'
import { useUserStore } from '@/stores/user'
import type { RouterType } from '@/type'

const route = useRoute()
const router = useRouter()
// 系统状态
const systemStore = useSystemStore()
const userStore = useUserStore()
const routerData: RouterType[] = getUiRouter()
</script>

<template>
  <el-container class="size-full min-w-80 gap-5 justify-between">
    <el-container class="max-w-fit gap-5 max-sm:mr-0.5 items-center z-50">
      <a class="size-10 use-btn-default !hidden max-lg:!flex border-2 border-amber-500">
        <label
          class="burger flex flex-col size-full items-center justify-center relative cursor-pointer"
          for="burger"
        >
          <input
            id="burger"
            v-model="systemStore.system.isShowMobile"
            class="hidden"
            type="checkbox"
          />
          <el-container
            class="box absolute p-1 size-full !items-center justify-between"
            direction="vertical"
          >
            <span></span>
            <span></span>
            <span></span>
          </el-container>
        </label>
      </a>
      <a
        href="javascript:"
        @click="
          () => {
            systemStore.system.isShowMobile = false
            router.push({ name: RouteNameEnum.HOME })
          }
        "
      >
        <span class="text-2xl font-bold">UTOPIA</span>
      </a>
    </el-container>
    <el-container class="z-50 gap-6 items-center mr-auto max-lg:!hidden">
      <route-drop :routerData="routerData" />
    </el-container>
    <el-container class="z-50 gap-4 max-sm:gap-2 items-center justify-end">
      <el-link
        :underline="false"
        class="min-w-10 h-10 border-2 use-btn-default border-amber-500"
        @click="
          () => {
            systemStore.dialog.search = true
            systemStore.system.isShowMobile = false
          }
        "
      >
        <svg-icon name="search" size="2xl" />
      </el-link>
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
      <el-dropdown :teleported="false">
        <el-link :underline="false" class="min-w-10 h-10">
          <image-box :src="userStore.user.avatar" class="!size-10 radius-sm" />
        </el-link>
        <template #dropdown>
          <el-container class="min-w-40 px-5 py-2" direction="vertical">
            <svg-icon
              v-if="!userStore.user.username"
              class="use-link-default justify-center p-1.5"
              message="登 录"
              name="login"
              @click="systemStore.dialog.login = true"
            />
            <el-container v-else direction="vertical">
              <svg-icon
                class="use-link-default justify-center p-1.5"
                message="退 出"
                name="logout"
                @click="userStore.logout()"
              />
              <svg-icon
                class="use-link-default justify-center p-1.5"
                message="个人信息"
                name="people"
                @click="router.push({ name: RouteNameEnum.USER_INFO })"
              />
            </el-container>
          </el-container>
        </template>
      </el-dropdown>
    </el-container>
  </el-container>
  <!-- 移动端导航栏 -->
  <el-main
    :class="
      'w-full max-lg:!block bg-gray-950/70 backdrop-blur-sm h-screen flex-col !pt-20 fixed top-0 left-0 z-40 duration-500 ' +
      (systemStore.system.isShowMobile ? ' translate-x-0' : ' -translate-x-full')
    "
    @click="systemStore.system.isShowMobile = !systemStore.system.isShowMobile"
  >
    <el-container
      v-for="(item, index) in routerData"
      :key="index"
      class="w-full mb-2 text-gray-50 relative"
      direction="vertical"
    >
      <template v-if="item.meta.type === 'M'">
        <el-container direction="vertical">
          <a
            :href="item.meta.frame ? item.path : 'javascript:'"
            class="use-link-default"
            @click="item.components ? router.push({ name: item.name }) : ''"
          >
            <svg-icon :message="item.meta.title" :name="item.meta.icon" />
          </a>
          <el-container
            class="flex flex-col pt-1.5 ml-1 border-l border-amber-500"
            direction="vertical"
          >
            <a
              v-for="(child, index) in item.children"
              :key="index"
              :class="
                'hover:translate-x-5 duration-300 hover-color' +
                (route.name === child.name ? ' !text-amber-500' : '')
              "
              :href="child.meta.frame ? child.path : 'javascript:'"
              @click="child.components ? router.push({ name: child.name }) : ''"
            >
              <svg-icon :message="child.meta.title" :name="child.meta.icon" class="py-1.5 px-4" />
            </a>
          </el-container>
        </el-container>
      </template>
      <template v-else-if="item.meta.type === 'B'">
        <a
          :class="'use-link-default ' + (route.name === item.name ? ' !text-amber-500' : '')"
          :href="item.meta.frame ? item.path : 'javascript:'"
          @click="item.components ? router.push({ name: item.name }) : ''"
        >
          <svg-icon :message="item.meta.title" :name="item.meta.icon" />
        </a>
      </template>
    </el-container>
  </el-main>
</template>

<style lang="scss" scoped>
.burger {
  span {
    display: flex;
    height: 3px;
    width: 100%;
    background: var(--el-text-color-primary);
    border-radius: 3px;
    transition: all 0.3s ease-in-out;

    &:nth-of-type(1) {
      transform-origin: left bottom;
    }

    &:nth-of-type(3) {
      transform-origin: left top;
    }
  }

  input:checked ~ .box span {
    &:nth-of-type(1) {
      width: calc(141.4% - 4px);
      margin: 0 auto;
      transform: rotate(45deg);
    }

    &:nth-of-type(2) {
      width: 0;
      opacity: 0;
    }

    &:nth-of-type(3) {
      width: calc(141.4% - 4px);
      margin: 0 auto;
      transform: rotate(-45deg);
    }
  }
}
</style>
