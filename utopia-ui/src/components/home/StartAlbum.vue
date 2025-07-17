<script lang="ts" setup>
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import type { AlbumDTO } from '@/request/api/album'
import { type AlbumVO, pageAlbumApi } from '@/request/api/album'
import { RouteNameEnum } from '@/enum'

const vo = ref<PageVO<AlbumVO>>(initPageVO())
const page = reactive<AlbumDTO>({
  pageNum: 1,
  pageSize: 8,
  recommend: true
})

const getAlbum = () => {
  pageAlbumApi(page).then((res: ResultType<PageVO<AlbumVO>>) => {
    vo.value = res.data
  })
}

onMounted(() => {
  getAlbum()
})
</script>

<template>
  <el-container class="gap-8" direction="vertical">
    <el-container class="auto-column p-5">
      <album-box-2 v-for="item in vo.list" :item="item" />
    </el-container>
    <el-empty v-if="!vo.list.length" />
    <route-to v-else :name="RouteNameEnum.ALBUM" />
  </el-container>
</template>

<style lang="scss" scoped></style>
