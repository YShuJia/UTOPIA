<script lang="ts" setup>
import { sizeMap, type SvgSizeType } from '@/type'

export type SvgNameType =
  | 'add'
  | 'archives'
  | 'authority'
  | 'bilibili'
  | 'classify'
  | 'cloud'
  | 'computer-monitor'
  | 'computer'
  | 'delete'
  | 'diary'
  | 'document-set'
  | 'downward'
  | 'edit'
  | 'email-box'
  | 'email'
  | 'emoji'
  | 'error'
  | 'folder'
  | 'footprint'
  | 'friend-link'
  | 'gitee'
  | 'github'
  | 'heat'
  | 'home'
  | 'horn'
  | 'id-card'
  | 'info'
  | 'lab'
  | 'label'
  | 'lock'
  | 'login'
  | 'logout'
  | 'menu'
  | 'message'
  | 'moon'
  | 'more'
  | 'mouse'
  | 'note'
  | 'palette'
  | 'paper-plane'
  | 'people-link'
  | 'people'
  | 'picture'
  | 'plane'
  | 'play'
  | 'praise'
  | 'qq'
  | 'redis'
  | 'refresh'
  | 'resources'
  | 'router-link'
  | 'search'
  | 'server-monitor'
  | 'setting'
  | 'success'
  | 'sun'
  | 'system-set'
  | 'time-machine'
  | 'timeline'
  | 'user-set'
  | 'video'
  | 'wechat'

const { name, message, size } = defineProps({
  name: {
    type: [String, undefined] as PropType<SvgNameType | string>,
    default: ''
  },
  message: {
    type: null as unknown as PropType<any>,
    default: ''
  },
  size: {
    type: String as PropType<SvgSizeType>,
    default: 'base'
  }
})
</script>

<template>
  <a :class="`flex items-center ${message === '' ? '' : 'gap-1.5'}`">
    <svg
      v-if="name != ''"
      :style="{
        width: sizeMap.get(size),
        height: sizeMap.get(size),
        minWidth: sizeMap.get(size),
        maxWidth: sizeMap.get(size)
      }"
      aria-hidden="true"
    >
      <use :xlink:href="`#svg-${name}`" />
    </svg>
    <span :style="{ fontSize: sizeMap.get(size) }" class="line-clamp-1">{{ message }}</span>
  </a>
</template>

<style lang="scss" scoped>
a {
  span {
    &,
    &:deep(*) {
      outline: unset !important;
    }

    z-index: inherit;
    color: inherit;
    transition: inherit;
  }

  svg {
    outline: unset !important;
    z-index: inherit;
    color: inherit;
  }
}
</style>
