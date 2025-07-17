<script lang="ts" setup>
import wechat_pay from '@/assets/img/wechat_pay.png'
import zhi_pay from '@/assets/img/zhi_pay.png'
</script>

<template>
  <el-container class="gap-1 use-box-large" direction="vertical">
    <span>请作者喝咖啡！(●'◡'●)</span>
    <span class="text-sm">请备注你的昵称、头像地址，或者网站地址，感谢支持！</span>
    <el-container class="gap-5">
      <image-box :src="wechat_pay" class="rounded-lg !size-24" />
      <image-box :src="zhi_pay" class="rounded-lg !size-24" />
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped></style>
