<script lang="ts" setup>
import { getDateDiff } from '@/utils/timeUtils'
import { useSystemStore } from '@/stores/system'

const sys = useSystemStore()

let time = ref<any>(getDateDiff(sys.webConfig.siteCreateTime, new Date()))
let interval = ref<any>()
onMounted(() => {
  time.value.second = (time.value.second + 1) % 60
  interval.value = setInterval(() => {
    if (time.value.second === 0) {
      time.value.minute = (time.value.minute + 1) % 60
      if (time.value.minute === 0) {
        time.value = getDateDiff(sys.webConfig.siteCreateTime, new Date())
      }
    }
    time.value.second = (time.value.second + 1) % 60
  }, 1000)
})

onBeforeMount(() => {
  clearInterval(interval.value)
})
</script>
<template>
  <el-container class="px-5 py-10" direction="vertical">
    <span class="w-full h-fit flex flex-wrap items-center justify-between">
      <router-link class="text-xl mr-1" to="/">
        <span class="text-2xl font-bold flowing-text">{{ sys.webConfig.siteTitle }}</span>
      </router-link>
      <span class="flowing-text line-clamp-1 max-w-full">{{ sys.webConfig.siteMotto }}</span>
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
    <a class="use-link-default" href="https://beian.miit.gov.cn/" target="_blank">
      {{ sys.webConfig.siteRecord }}
    </a>
  </el-container>
</template>

<style lang="scss" scoped>
.time:nth-child(odd) {
  font-size: 20px;
  color: orange;
}

.time:nth-child(even) {
  margin: 0 2px;
}
</style>
