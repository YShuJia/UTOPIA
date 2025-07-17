<script lang="ts" setup>
import { RouteNameEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()

onMounted(() => {})
</script>

<template>
  <el-container class="gap-8" direction="vertical">
    <el-container class="gap-5 p-5 max-2xl:flex-col">
      <connect-author v-slide-in="systemStore.system.isOpenAnimation" />
      <carousel-deploy v-slide-in="systemStore.system.isOpenAnimation" class="h-[350px]" />
    </el-container>
    <route-to :name="RouteNameEnum.ABOUT" message="查看详情" />
  </el-container>
</template>

<style lang="scss" scoped></style>
