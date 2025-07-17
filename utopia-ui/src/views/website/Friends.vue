<script lang="ts" setup>
import type { WebsiteDTO } from '@/request/api/website'
import { pageWebsiteApi, type WebsiteVO } from '@/request/api/website'
import type { PageVO, ResultType } from '@/request/config'
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()
const url = systemStore.getRandomImg()

const page = reactive<WebsiteDTO>({
  pageSize: 20,
  pageNum: 1
})

let siteList = ref<WebsiteVO[]>([])
let isEmpty = ref<boolean>(false)
const getList = () => {
  page.recommend = false
  pageWebsiteApi(page).then((res: ResultType<PageVO<WebsiteVO>>) => {
    siteList.value = res.data.list
    isEmpty.value = res.data.isEmpty
  })
}

let recommendList = ref<WebsiteVO[]>([])
const getRecommendList = () => {
  page.recommend = true
  pageWebsiteApi(page).then((res: ResultType<PageVO<WebsiteVO>>) => {
    recommendList.value = res.data.list
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
    <template v-if="recommendList.length">
      <el-container direction="vertical">
        <el-main class="!pb-0">
          <svg-icon class="w-fit pb-1.5 border-b-4" message="推荐" name="heat" size="xl" />
        </el-main>
        <el-container class="p-2.5 auto-column !gap-3">
          <friend-box v-for="item in recommendList" :item="item" />
        </el-container>
      </el-container>
    </template>
    <el-container direction="vertical">
      <el-main class="!pb-0">
        <svg-icon class="w-fit pb-1.5 border-b-4" message="友情链接" name="friend-link" size="xl" />
      </el-main>
      <el-container class="p-2.5 auto-column !gap-3">
        <friend-box v-for="item in siteList" :item="item" />
      </el-container>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped></style>
