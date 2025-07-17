<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()
const defaultImg = systemStore.getRandomImg()
const { img, imgHeight, isCustomTop } = defineProps({
  img: {
    type: String,
    default: ''
  },
  imgHeight: {
    type: Number,
    default: 520
  },
  isCustomTop: {
    type: Boolean,
    default: false
  }
})

const mounted = ref<boolean>(false)
onMounted(() => {
  mounted.value = true
})
</script>

<template>
  <el-container class="w-screen" direction="vertical">
    <el-container
      v-if="!isCustomTop"
      :style="{
        height: `${imgHeight}px`,
        backgroundImage: 'url(' + (img !== '' ? img : defaultImg) + ')'
      }"
      class="sticky top-0 max-md:!h-[450px] max-sm:!h-96 z-0 bg-cover bg-center bg-no-repeat"
    >
      <el-container class="flex items-center justify-center">
        <slot class="z-10" name="title" />
      </el-container>
    </el-container>
    <slot v-else name="top" />
    <el-container class="size-full -mt-28 z-20" direction="vertical">
      <wave />
      <el-container class="use-theme pt-5" direction="vertical">
        <slot name="default" />
      </el-container>
    </el-container>
  </el-container>
</template>

<style lang="scss"></style>
