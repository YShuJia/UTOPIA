<script lang="ts" setup>
import { useScrollStore } from '@/stores/scroll'
import { useSystemStore } from '@/stores/system'

const sys = useSystemStore()
let message = ref<string>('')
let msg = ref<string>(sys.webConfig.siteMotto)
let interval = ref<any>()
let timeout = ref<any>()
let flagAnimation = ref<boolean>(true)
let i = 0
const animation = () => {
  interval.value = setInterval(() => {
    clearTimeout(timeout.value)
    if (flagAnimation.value) {
      message.value += msg.value.charAt(i)
      i++
    } else {
      message.value = message.value.slice(0, message.value.length - 1)
      if (message.value.length === 0) {
        flagAnimation.value = !flagAnimation.value
        i = 0
        clearInterval(interval.value)
        timeout.value = setTimeout(() => {
          animation()
        }, 1500)
      }
    }
    if (message.value.length === msg.value.length && msg.value.length === i) {
      clearInterval(interval.value)
      flagAnimation.value = !flagAnimation.value
      timeout.value = setTimeout(() => {
        animation()
      }, 2000)
    }
  }, 200)
}

const scrollStore = useScrollStore()
let lookMoreHtml: any = null

const lookMore = () => {
  scrollStore.scrollTo(lookMoreHtml?.clientHeight || document.body.clientHeight)
}

onMounted(() => {
  lookMoreHtml = document.querySelector('.lookMore')
  timeout.value = setTimeout(() => {
    animation()
  }, 1000)
})
</script>

<template>
  <top-image-template :img-height="600" class="lookMore">
    <template #title>
      <el-container class="items-center relative justify-around size-full" direction="vertical">
        <el-container class="!max-h-14 pr-3 gap-1 bg-gray-900/50 rounded-2xl border-2 items-center">
          <span class="text-white ml-2 text-xl">{{ message }}</span>
          <span class="top_text h-1/2"></span>
        </el-container>
        <a class="absolute bottom-36 max-sm:bottom-32" href="javascript:" @click="lookMore">
          <svg-icon class="animate-bounce" name="downward" size="4xl" />
        </a>
      </el-container>
    </template>
  </top-image-template>
</template>

<style lang="scss" scoped>
.top_text {
  border-right: white solid 2px;
  animation: borderRight 1s linear infinite;
}

@keyframes borderRight {
  0% {
    opacity: 0;
  }
  20% {
    opacity: 0;
  }
  70% {
    opacity: 1;
  }
  100% {
    opacity: 1;
  }
}
</style>
