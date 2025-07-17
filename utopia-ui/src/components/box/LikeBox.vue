<script lang="ts" setup>
const { likeCount, viewCount, commentCount } = defineProps({
  likeCount: {
    type: Number,
    default: 0
  },
  viewCount: {
    type: Number,
    default: 0
  },
  commentCount: {
    type: Number,
    default: 0
  }
})
</script>

<template>
  <el-container class="flex-wrap">
    <roll-box :like-count="likeCount" />
  </el-container>
</template>

<style lang="scss" scoped></style>
