<script lang="ts" setup>
import { sizeMap, type SvgSizeType } from '@/type'
import type { SvgNameType } from '@/components/SvgIcon.vue'

const { message, size, name, rotate } = defineProps({
  message: {
    type: String,
    default: '发送'
  },
  name: {
    type: String as PropType<SvgNameType>,
    default: 'paper-plane'
  },
  size: {
    type: String as PropType<SvgSizeType>,
    default: 'base'
  },
  rotate: {
    type: Number,
    default: 0
  }
})
</script>

<template>
  <a class="p-1.5 w-fit">
    <a
      class="a-box py-1 radius-sm px-1.5 flex items-center justify-center mimicry-button overflow-hidden relative"
    >
      <div class="svg-wrapper absolute left-2 transition-[left] duration-300">
        <svg-icon :data-rotate="rotate" :name="name" :size="size" class="svg" />
      </div>
      <div :style="{ width: sizeMap.get(size) }"></div>
      <span :style="{ fontSize: sizeMap.get(size) }" class="pl-2 w-full">{{ message }}</span>
    </a>
  </a>
</template>

<style lang="scss" scoped>
.a-box {
  transition:
    scale 0.3s,
    background-color 0.3s;

  span {
    transition: transform 0.5s ease-in-out;
    letter-spacing: 2px;
  }

  svg {
    transform-origin: center center;
    transition: transform 0.5s ease-in-out;
  }

  &:hover {
    .svg-wrapper {
      top: 50%;
      left: 50%;
      animation: fly 0.6s ease-in-out infinite alternate;
    }

    .svg {
      transform-origin: center center;
      transform: translate(-50%, -50%) rotate(45deg) scale(1.1);
    }

    span {
      transform: translateX(120%);
    }
  }

  &:active {
    transform: scale(0.95);
  }
}

@keyframes fly {
  from {
    transform: translateY(-25%);
  }
  to {
    transform: translateY(25%);
  }
}
</style>
