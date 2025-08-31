<script lang="ts" setup>
import type { WebsiteDTO } from '@/request/api/website'
import { pageWebsiteApi, type WebsiteVO } from '@/request/api/website'
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()
const url = systemStore.getRandomImg()

let recommendVO = reactive<PageVO<WebsiteVO>>(initPageVO())
const recommendPage = reactive<WebsiteDTO>({
  pageSize: 12,
  pageNum: 0,
  enabled: true,
  reviewed: 1
})
const getRecommendList = () => {
  recommendPage.recommend = true
  recommendPage.pageNum = (recommendPage.pageNum ?? 1) + 1
  pageWebsiteApi(recommendPage).then((res: ResultType<PageVO<WebsiteVO>>) => {
    recommendVO.list.push(...res.data.list)
    recommendVO.isEmpty = res.data.isEmpty
  })
}

let vo = reactive<PageVO<WebsiteVO>>(initPageVO())
const page = reactive<WebsiteDTO>({
  pageSize: 12,
  pageNum: 0,
  enabled: true,
  reviewed: 1
})
const getList = () => {
  page.recommend = false
  page.pageNum = (page.pageNum ?? 1) + 1
  pageWebsiteApi(page).then((res: ResultType<PageVO<WebsiteVO>>) => {
    vo.list.push(...res.data.list)
    vo.isEmpty = res.data.isEmpty
  })
}

onMounted(() => {
  getList()
  getRecommendList()
})
</script>

<template>
  <el-container class="inner-box gap-5 pt-20" direction="vertical">
    <el-main v-slide-in="systemStore.system.isOpenAnimation">
      <el-container class="h-96 use-box-large overflow-hidden items-center justify-center relative">
        <image-box :src="url" />
        <span class="absolute text-2xl hollow-text">有朋自远方来，不亦说乎！</span>
      </el-container>
    </el-main>
    <el-main class="my-10">
      <site-form />
    </el-main>
    <el-container class="w-full relative" direction="vertical">
      <website-info />
    </el-container>
    <template v-if="recommendVO.list.length">
      <el-container direction="vertical">
        <el-main class="!pb-0">
          <svg-icon class="w-fit pb-1.5 border-b-4" message="推荐" name="heat" size="xl" />
        </el-main>
        <el-container class="p-2.5 auto-column !gap-3">
          <friend-box v-for="item in recommendVO.list" :item="item" />
        </el-container>
        <loading-more :action="getRecommendList" :empty="recommendVO.isEmpty" />
      </el-container>
    </template>
    <el-container direction="vertical">
      <el-main class="!pb-0">
        <svg-icon class="w-fit pb-1.5 border-b-4" message="友情链接" name="friend-link" size="xl" />
      </el-main>
      <el-container class="p-2.5 auto-column !gap-3">
        <friend-box v-for="item in vo.list" :item="item" />
      </el-container>
      <loading-more :action="getList" :empty="vo.isEmpty" />
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped></style>
