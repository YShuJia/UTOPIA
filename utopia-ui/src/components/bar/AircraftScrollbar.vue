<script lang="ts" setup>
import { useScrollStore } from '@/stores/scroll'

const scrollStore = useScrollStore()
const toTopOrBottom = () => {
  if (scrollStore.scroll.isDown) {
    scrollStore.scrollTo(scrollStore.scroll.contentRef.clientHeight)
  } else {
    scrollStore.scrollTo(0)
  }
}

const transformStyle = computed(() => {
  return {
    transform: `translateY(${scrollStore.scroll.scrollTop}px) ${!scrollStore.scroll.isDown ? 'rotate(0deg)' : 'rotate(-180deg)'}`,
    willChange: 'transform'
  }
})
</script>

<template>
  <el-container
    :style="transformStyle"
    class="-top-28 toTop_box duration-100 absolute z-40 right-5 max-sm:right-2"
  >
    <el-container class="items-center" direction="vertical">
      <span @click="toTopOrBottom">
        <svg-icon class="cursor-pointer" name="plane" size="4xl" />
      </span>
      <span class="rotate-180 flex min-h-16 w-8 relative flex-col justify-center items-center">
        <svg-icon class="absolute flow -bottom-2" name="cloud" size="xl" />
        <svg-icon class="absolute flow delay-500 -bottom-2" name="cloud" size="xl" />
        <svg-icon class="absolute flow delay-1000 -bottom-2" name="cloud" size="xl" />
      </span>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
/*回到顶部*/
.toTop_box {
  transform-origin: center 15%;

  .delay-500 {
    animation-delay: 300ms !important;
  }

  .delay-1000 {
    animation-delay: 600ms !important;
  }

  .flow {
    animation: flow_frames 0.9s linear infinite;
    @keyframes flow_frames {
      from {
        transform: translateY(0);
        scale: 1;
        opacity: 1;
      }
      to {
        transform: translateY(-80px);
        scale: 0.5;
        opacity: 0;
      }
    }
  }
}
</style>
