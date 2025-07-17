<script lang="ts" setup>
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import { RouteNameEnum } from '@/enum'
import { pageWebsiteApi, type WebsiteDTO, type WebsiteVO } from '@/request/api/website'

const vo = ref<PageVO<WebsiteVO>>(initPageVO())
const page = reactive<WebsiteDTO>({
  pageNum: 1,
  pageSize: 8,
  recommend: true
})

const getAlbum = () => {
  pageWebsiteApi(page).then((res: ResultType<PageVO<WebsiteVO>>) => {
    vo.value = res.data
  })
}

onMounted(() => {
  getAlbum()
})
</script>

<template>
  <el-container class="gap-8" direction="vertical">
    <el-container class="auto-column p-2.5">
      <friend-box v-for="item in vo.list" :item="item" />
    </el-container>
    <el-empty v-if="!vo.list.length" />
    <route-to v-else :name="RouteNameEnum.FRIEND" />
  </el-container>
</template>

<style lang="scss" scoped></style>
