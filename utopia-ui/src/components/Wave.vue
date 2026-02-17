<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()
onMounted(() => {
  systemStore.handleResize()
})
</script>

<template>
  <div class="w-full h-28">
    <svg
      :viewBox="systemStore.system.isLt1024 ? '0 0 240 100' : '0 0 500 100'"
      class="waves"
      preserveAspectRatio="none"
      shape-rendering="auto"
      xmlns="http://www.w3.org/2000/svg"
      xmlns:xlink="http://www.w3.org/1999/xlink"
    >
      <defs>
        <path
          id="gentle-wave"
          d="M-200 150
         C-100 80, -100 0, -50 0
         S 0 60, 50 60
         S 100 0, 150 0
         S 200 60, 250 60
         S 300 0, 350 0
         S 400 60, 450 60
         S 500 0, 550 0
         S 600 60, 650 60
         S 700 0, 750 0
         v100h-850z"
        />
      </defs>
      <g class="parallax">
        <use x="0" xlink:href="#gentle-wave" y="0" />
        <use x="0" xlink:href="#gentle-wave" y="10" />
        <use x="0" xlink:href="#gentle-wave" y="20" />
      </g>
    </svg>
  </div>
</template>

<style lang="scss" scoped>
@use 'sass:math';

.waves {
  position: relative;
  width: 100%;
  height: 100%;
  /*Fix for safari gap*/
  margin-bottom: -7px;
  max-height: 150px;
}

@for $i from 1 through 3 {
  .parallax > use:nth-child(#{$i}) {
    $duration: (math.random(12) + 4) + s;
    animation-name: move;
    animation-iteration-count: infinite;
    animation-timing-function: ease-out;
    animation-duration: calc($duration);
    fill: var(--el-bg-color);
    opacity: 0.85;
    @extend .use-theme !optional;
  }
}

@keyframes move {
  0% {
    transform: translate3d(-190px, 0, 0);
  }
  100% {
    transform: translate3d(10px, 0, 0);
  }
}
</style>
