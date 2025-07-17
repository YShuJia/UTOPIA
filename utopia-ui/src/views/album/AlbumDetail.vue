<script lang="ts" setup>
import { type AlbumVO, getAlbumApi, initAlbumVO } from '@/request/api/album'

const route = useRoute()

const album = ref<AlbumVO>(initAlbumVO())
onMounted(async () => {
  const id = Number(route.params.id) || 0
  const { data } = await getAlbumApi(id)
  album.value = data
})
</script>

<template>
  <top-image-template :img="album.urls[album.urls.length - 1]">
    <template #title>
      <el-container class="items-center gap-2" direction="vertical">
        <span class="text-3xl line-clamp-1 flowing-text">{{ album.title }}</span>
        <span class="text-amber-500">{{ album.updateTime }}</span>
      </el-container>
    </template>
    <el-container class="gap-5" direction="vertical">
      <div class="hollow-circles p-5 flex flex-col gap-2">
        <span class="text-2xl font-bold">{{ album.title }}</span>
        <el-container class="gap-5 max-sm:gap-1">
          <svg-icon
            :message="'点赞' + album.likeCount"
            class="!gap-0.5 text-color-gray"
            name="praise"
            size="sm"
          />
          <svg-icon
            :message="'浏览' + album.viewCount"
            class="!gap-0.5 text-color-gray"
            name="footprint"
            size="sm"
          />
          <svg-icon
            :message="'评论' + album.commentCount"
            class="!gap-0.5 text-color-gray"
            name="message"
            size="sm"
          />
        </el-container>

        <span>{{ album.introduction }}</span>
        <svg-icon
          :message="album.updateTime"
          class="min-w-4 text-color-gray ml-auto"
          name="edit"
          size="sm"
        />
      </div>
      <el-container class="h-fit items-center" direction="vertical">
        <image-box
          v-for="item in album.urls"
          :src="item"
          class="sticky top-0 h-screen w-fit"
          fit="object-contain"
        />
      </el-container>
      <el-container class="inner-box pt-5 gap-5 items-center" direction="vertical">
        <like-button v-if="album.likeable" :count="album.likeCount" :source-id="album.id" />
        <comment v-if="album.commentable" :source-id="album.id" />
      </el-container>
    </el-container>
  </top-image-template>
</template>

<style lang="scss" scoped>
.hollow-circles {
  max-width: 90%;
  min-width: 300px;
  position: relative;
  background: #f59e0b;
}

.hollow-circles::after {
  content: '';
  position: absolute;
  height: 100%;
  width: 10px;
  top: 0;
  right: -10px;
  background-image:
    linear-gradient(to bottom, #f59e0b 10px, transparent 10px, transparent),
    radial-gradient(20px circle at 10px 20px, transparent 10px, #f59e0b 10px);
  background-size: 10px 30px;
}
</style>
