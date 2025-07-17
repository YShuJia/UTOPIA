<template>
  <el-container class="px-5 py-10" direction="vertical">
    <span class="w-full h-fit flex flex-wrap items-center justify-between">
      <router-link class="text-xl mr-1" to="/">
        <span class="text-2xl font-bold flowing-text">UTOPIA</span>
      </router-link>
      <span class="flowing-text line-clamp-1 max-w-full">路漫漫其修远兮，吾将上下而求索。</span>
    </span>
    <span class="flex flex-wrap pt-2 items-center">
      <span>本站已运行：</span>
      <span class="flex items-center">
        <span class="time">{{ time.year }}</span>
        <span class="time">年</span>
        <span class="time">{{ time.month }}</span>
        <span class="time">月</span>
        <span class="time">{{ time.day }}</span>
        <span class="time">日</span>
        <span class="time">{{ time.hour }}</span>
        <span class="time">时</span>
        <span class="time">{{ time.minute }}</span>
        <span class="time">分</span>
        <span class="time">{{ time.second }}</span>
        <span class="time">秒</span>
      </span>
    </span>
    <hr class="w-full my-1" />
    <span class="flex flex-wrap items-center">
      <span class="mr-1 my-1">Copyright © 2024 @YShuJia</span>
      <a class="use-link-default" href="https://beian.miit.gov.cn/">黔ICP备2024020134号-1</a>
    </span>
  </el-container>
</template>

<script lang="ts" setup>
import { getDateDiff } from '@/utils/timeUtils'

let time = ref<any>(getDateDiff('2025-04-23 8:00:00', new Date()))
let interval = ref<any>()
onMounted(() => {
  time.value.second = (time.value.second + 1) % 60
  interval.value = setInterval(() => {
    if (time.value.second === 0) {
      time.value.minute = (time.value.minute + 1) % 60
      if (time.value.minute === 0) {
        time.value = getDateDiff('2024-04-23 8:00:00', new Date())
      }
    }
    time.value.second = (time.value.second + 1) % 60
  }, 1000)
})

onBeforeMount(() => {
  clearInterval(interval.value)
})
</script>

<style lang="scss" scoped>
.time:nth-child(odd) {
  font-size: 20px;
  color: orange;
}

.time:nth-child(even) {
  margin: 0 2px;
}
</style>
