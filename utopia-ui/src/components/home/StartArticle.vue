<script lang="ts" setup>
import { type ArticleDTO, type ArticleVO, pageArticleApi } from '@/request/api/article'
import { RouteNameEnum } from '@/enum'
import { initPageVO, type PageVO } from '@/request/config'

const vo = ref<PageVO<ArticleVO>>(initPageVO())

const page = reactive<ArticleDTO>({
  pageNum: 1,
  pageSize: 8,
  recommend: true,
  urls: [],
  isUpdated: false
})

const getPage = () => {
  pageArticleApi(page).then((res) => {
    vo.value = res.data
  })
}

onMounted(() => {
  getPage()
})
</script>

<template>
  <el-container class="gap-8" direction="vertical">
    <el-container class="auto-column p-5">
      <article-box v-for="item in vo.list" :item="item" />
    </el-container>
    <el-empty v-if="!vo.list.length" />
    <route-to v-else :name="RouteNameEnum.ARCHIVES" />
  </el-container>
</template>

<style lang="scss" scoped></style>
