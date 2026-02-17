<script lang="ts" setup>
const { empty, action } = defineProps({
  empty: {
    type: Boolean,
    default: false
  },
  action: {
    type: Function as PropType<(...args: any[]) => any>
  }
})
</script>

<template>
  <el-container class="justify-center p-5">
    <loading-button
      v-if="!empty"
      :action="action"
      custom-class="border-4!  border-white!"
      message="加载更多"
      shape="round"
      size="large"
    />
    <el-empty v-else :image-size="100" description="已经到达尽头了..." />
  </el-container>
</template>

<style lang="scss" scoped></style>
