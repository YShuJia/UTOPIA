<script lang="ts" setup>
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>

<template>
  <el-container class="inner-min-box gap-5 pt-16" direction="vertical">
    <user-auth />
    <user-update />
  </el-container>
</template>

<style lang="scss" scoped></style>
