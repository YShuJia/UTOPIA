<script lang="ts" setup>
import { routerTo } from '@/router'
import { RouteNameEnum } from '@/enum'

const { copyright } = defineProps({
  copyright: {
    type: String,
    required: true
  }
})
</script>

<template>
  <el-container
    class="p-5 mt-3 use-box-large border-l-4 border-amber-500 gap-1"
    direction="vertical"
  >
    <span>文章版权归属：{{ copyright }}</span>
    <span class="flex items-center">
      <span>1. 本站作者：</span>
      <a
        class="flex text-cyan-500 use-link-default"
        href="javascript:"
        @click="routerTo(RouteNameEnum.ABOUT)"
        >@yshujia</a
      >
    </span>
    <span>
      2.
      本网站所发布的内容，包括但不限于文字、图片、音频、视频、文章、软件、程序等可能来源于网路，仅供学习和参考，如有侵权请联系作者（yshujia2001@163.com）进行删除处理。
    </span>
    <span>3. 本网站一切内容不代表本站立场，并不代表本站赞同其观点和对其真实性负责。 </span>
    <span class="flex items-center">
      <span>4. 版权&许可请详阅</span>
      <a class="flex pt-0.5 text-cyan-500 use-link-default" href="javascript:">版权声明。</a>
    </span>
  </el-container>
</template>
