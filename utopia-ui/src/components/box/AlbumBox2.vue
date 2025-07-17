<script lang="ts" setup>
import { type AlbumVO } from '@/request/api/album'
import { useSystemStore } from '@/stores/system'
import { routerTo } from '@/router'
import { RouteNameEnum } from '@/enum'

const systemStore = useSystemStore()

const { item } = defineProps({
  item: {
    type: Object as PropType<AlbumVO>,
    required: true
  }
})
</script>

<template>
  <a
    :key="item.id"
    v-slide-in="systemStore.system.isOpenAnimation"
    class="flex flex-col overflow-hidden use-hover-box-large"
    @click="routerTo(RouteNameEnum.ALBUM_DETAIL, { id: item.id })"
  >
    <el-container class="h-40 relative items-start overflow-hidden">
      <div class="left-label absolute right-0 top-0 flex w-1/2 h-10 bg-color z-10"></div>
      <el-container class="w-full h-10 p-2 gap-10 justify-between z-10">
        <svg-icon :message="item.title" class="w-full hollow-text" name="picture" />
        <svg-icon :message="item.labelName" class="w-full text-color-gray" name="label" />
      </el-container>
      <image-box :src="item.urls[0]" class="absolute z-0" />
    </el-container>
    <el-container class="p-3" direction="vertical">
      <svg-icon :message="item.updateTime" class="text-color-gray" name="edit" size="sm" />
      <span class="line-clamp-2 pt-1">{{ item.introduction }}</span>
      <el-container class="items-center justify-around">
        <el-container class="max-w-fit items-center justify-center" direction="vertical">
          <span class="big-text">{{ item.likeCount }}</span>
          <svg-icon class="text-color-gray" message="点赞" name="praise" size="sm" />
        </el-container>
        <span class="flex w-[1px] h-4/5 bg-gray-400"></span>
        <el-container class="max-w-fit items-center justify-center" direction="vertical">
          <span class="big-text">{{ item.viewCount }}</span>
          <svg-icon class="text-color-gray" message="浏览" name="footprint" size="sm" />
        </el-container>
        <span class="flex w-[1px] h-4/5 bg-gray-400"></span>
        <el-container class="max-w-fit items-center justify-center" direction="vertical">
          <span class="big-text">{{ item.commentCount }}</span>
          <svg-icon class="text-color-gray" message="评论" name="message" size="sm" />
        </el-container>
      </el-container>
    </el-container>
  </a>
</template>

<style lang="scss" scoped>
.left-label {
  border-bottom-left-radius: 15px;
  transform: skew(40deg);
  box-shadow: 15px -10px 0 0 var(--el-bg-color);

  &::before {
    transform: skew(-10deg);
    z-index: inherit;
    content: '';
    position: absolute;
    width: 40px;
    height: 40px;
    top: 0;
    left: -40px;
    border-top-right-radius: 15px;
    box-shadow: 15px -20px 0 0 var(--el-bg-color);
  }
}
</style>
