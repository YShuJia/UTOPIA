<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { useStyleStore } from '@/stores/style'
import { DialogEnum } from '@/enum'

const systemStore = useSystemStore()
const styleStore = useStyleStore()
const theme = ref<'dark' | 'light' | 'auto'>('light')
const options = [
  {
    label: '昼',
    value: 'light',
    name: 'sun'
  },
  {
    label: '夜',
    value: 'dark',
    name: 'moon'
  },
  {
    label: '自动',
    value: 'auto',
    name: 'system-set'
  }
]

const bgChange = () => {
  if (theme.value === 'auto') {
    styleStore.background.bgIsAuto = true
  } else {
    styleStore.background.bgType = theme.value
    styleStore.background.bgIsAuto = false
  }
}

const animation = ref<boolean | 'auto'>(true)
const animationOptions = [
  {
    label: '开',
    value: true
  },
  {
    label: '关',
    value: false
  },
  {
    label: '自动',
    value: 'auto'
  }
]
const animationChange = () => {
  if (animation.value === 'auto') {
    systemStore.system.isOpenAnimation = systemStore.system.isMobile
    systemStore.system.isAutoAnimation = true
  } else {
    systemStore.system.isOpenAnimation = animation.value
    systemStore.system.isAutoAnimation = false
  }
}

onMounted(() => {
  if (styleStore.background.bgIsAuto) {
    theme.value = 'auto'
  } else {
    theme.value = styleStore.background.bgType
  }
  if (systemStore.system.isAutoAnimation) {
    animation.value = 'auto'
  } else {
    animation.value = systemStore.system.isOpenAnimation
  }
})
</script>

<template>
  <dialog-template :attribute="DialogEnum.SETTING" class="p-5">
    <el-container class="gap-2" direction="vertical">
      <svg-icon class="m-auto pb-5" message="系统设置" name="setting" size="xl" />
      <el-container class="justify-between items-center">
        <span>主题色</span>
        <el-segmented v-model="theme" :options="options" @change="bgChange">
          <template #default="scope: any">
            <svg-icon :message="scope.item.label" :name="scope.item.name" />
          </template>
        </el-segmented>
      </el-container>
      <hr />
      <el-container direction="vertical">
        <el-container class="justify-between items-center">
          <span>大圆角</span>
          <el-slider
            v-model="styleStore.radius.lg"
            :max="30"
            :min="2"
            :step="2"
            class="w-40! mr-3"
            show-stops
          />
        </el-container>
        <span class="ml-auto text-xs">容器、盒子等组件圆角</span>
      </el-container>
      <hr />
      <el-container direction="vertical">
        <el-container class="justify-between items-center">
          <span>小圆角</span>
          <el-slider
            v-model="styleStore.radius.sm"
            :max="10"
            :min="2"
            :step="1"
            class="w-40! mr-3"
            show-stops
          />
        </el-container>
        <span class="ml-auto text-xs">按钮、链接等组件圆角</span>
      </el-container>
      <hr />
      <el-container direction="vertical">
        <el-container class="justify-between items-center">
          <span>阴影</span>
          <el-slider
            v-model="styleStore.shadow.size"
            :max="8"
            :min="1"
            :step="0.5"
            class="w-40! mr-3"
            show-stops
          />
        </el-container>
        <span class="ml-auto text-xs">页面组件阴影大小</span>
      </el-container>
      <hr />
      <el-container direction="vertical">
        <el-container class="justify-between items-center">
          <span>过渡动画</span>
          <el-segmented v-model="animation" :options="animationOptions" @change="animationChange">
            <template #default="scope: any">
              <span>{{ scope.item.label }}</span>
            </template>
          </el-segmented>
        </el-container>
        <span class="ml-auto text-xs">（自动时在电脑端开启）如出现卡顿请关闭该效果</span>
      </el-container>
      <hr />
      <el-container direction="vertical">
        <el-container class="justify-between items-center">
          <span>点击效果</span>
          <el-switch
            v-model="styleStore.mouse.isMouseClick"
            active-text="开"
            inactive-text="关"
            inline-prompt
            size="large"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
          />
        </el-container>
        <span class="ml-auto text-xs">如出现卡顿请关闭该效果</span>
      </el-container>
      <hr />
      <el-container direction="vertical">
        <el-container class="justify-between items-center">
          <span>滚动阻尼</span>
          <el-switch
            v-model="systemStore.system.isOpenLenis"
            active-text="开"
            inactive-text="关"
            inline-prompt
            size="large"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
          />
        </el-container>
        <span class="ml-auto text-xs">如出现卡顿请关闭该效果</span>
      </el-container>
    </el-container>
  </dialog-template>
</template>

<style lang="scss" scoped></style>
