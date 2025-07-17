<script lang="ts" setup>
import { type ArticleVO, getArticleDeployApi } from '@/request/api/article'
import type { ResultType } from '@/request/config'
import { RouteNameEnum } from '@/enum'
import { routerTo } from '@/router'

const list = ref<ArticleVO[]>([])
onMounted(() => {
  getArticleDeployApi().then((res: ResultType<ArticleVO[]>) => {
    list.value = res.data
  })
})
</script>

<template>
  <el-carousel
    :autoplay="false"
    :motion-blur="true"
    class="size-full use-box-large"
    height="100%"
    trigger="click"
  >
    <!--  el-carousel-item 不能设置 relative 定位 -->
    <el-carousel-item v-for="item in list" :key="item.id">
      <!--  懒加载可能会导致后续图片不加载  -->
      <image-box :src="item.cover" />
      <span class="absolute flex flex-col gap-2 top-10 left-10 max-xs:left-5">
        <span class="text-xl max-xs:text-lg hollow-text">{{ item.title }}</span>
        <svg-icon :message="item.updateTime" class="hollow-text" name="edit" />
      </span>
      <a
        class="absolute bg-gray-300/60 px-10 max-xs:px-5 max-xs:py-1 py-2 use-btn-default left-10 max-xs:left-5 bottom-10"
        href="javascript:"
        @click="routerTo(RouteNameEnum.ARTICLE_DETAIL, { id: item.id })"
      >
        立即前往
      </a>
    </el-carousel-item>
  </el-carousel>
</template>

<style lang="scss" scoped></style>
