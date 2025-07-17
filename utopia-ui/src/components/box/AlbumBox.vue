<script lang="ts" setup>
import { type AlbumVO } from '@/request/api/album'
import { useSystemStore } from '@/stores/system'
import { RouteNameEnum } from '@/enum'
import { routerTo } from '@/router'

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
    class="flex flex-col card relative items-center"
  >
    <el-container class="category bg-color-three px-2 py-1 gap-1 justify-between w-11/12">
      <svg-icon :message="item.title" name="picture" />
      <svg-icon :message="item.labelName" class="text-color-gray" name="label" size="sm" />
    </el-container>
    <a
      :data-title="item.title"
      class="card_container z-10 h-52 w-full overflow-hidden relative radius-lg"
      href="javascript:"
      @click="routerTo(RouteNameEnum.ALBUM_DETAIL, { id: item.id })"
    >
      <image-box :src="item.urls[0]" />
    </a>
    <el-container class="named bg-color-three px-2 py-1 gap-1 w-11/12" direction="vertical">
      <el-container class="gap-1 justify-between">
        <svg-icon :message="'点赞' + item.likeCount" name="praise" size="sm" />
        <svg-icon :message="'浏览' + item.viewCount" name="footprint" size="sm" />
        <svg-icon :message="'评论' + item.commentCount" name="message" size="sm" />
      </el-container>
      <el-container class="items-center gap-2 justify-between">
        <svg-icon :message="item.updateTime" name="edit" size="sm" />
        <span class="text-sm text-color-gray">图片{{ item.urls.length }}</span>
      </el-container>
    </el-container>
  </a>
</template>

<style lang="scss" scoped>
.card {
  .category {
    border-radius: 0.6rem 0.6rem 0 0;
    transition: transform 0.5s cubic-bezier(1, 0, 0, 1);
  }

  .named {
    border-radius: 0 0 0.6rem 0.6rem;
    transition: transform 0.5s cubic-bezier(1, 0, 0, 1);
  }

  .card_container {
    &::before {
      content: '';
      position: absolute;
      z-index: 10;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 0;
      aspect-ratio: 1;
      background-color: hsl(240, 15%, 9%, 0.2);
      border-radius: 50%;
      transform-origin: center;
      transition: all 0.5s cubic-bezier(1, 0, 0, 1) 0.2s;
    }

    &::after {
      content: '查看详情';
      position: absolute;
      z-index: 10;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      display: flex;
      justify-content: center;
      align-items: center;

      padding: 0;
      box-sizing: border-box;
      width: 0;
      aspect-ratio: 1;
      background-color: hsl(240, 15%, 9%, 0.5);

      font-size: 0;
      font-weight: bold;
      color: wheat;
      text-align: center;

      border-radius: 50%;
      transform-origin: center;

      transition: all 0.5s cubic-bezier(1, 0, 0, 1) 0s;
    }
  }

  &:hover {
    .category {
      transform: translateY(100%);
    }

    .named {
      transform: translateY(-100%);
    }

    .card_container::before {
      width: 120%;
      transition: all 1s cubic-bezier(1, 0, 0, 1) 0s;
    }

    .card_container::after {
      padding: 0.9rem 1rem;
      width: 4rem;
      font-size: 0.75rem;
      transition: all 0.3s ease-in-out 0.4s;
    }
  }
}
</style>
