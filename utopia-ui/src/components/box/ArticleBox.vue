<script lang="ts" setup>
import {useSystemStore} from '@/stores/system'
import type {ArticleVO} from '@/request/api/article'
import {RouteNameEnum} from '@/enum'
import {routerTo} from '@/router'

const systemStore = useSystemStore()

const { item } = defineProps({
  item: {
    type: Object as PropType<ArticleVO>,
    default: []
  }
})
</script>

<template>
  <a
    v-slide-in="systemStore.system.isOpenAnimation"
    class="flex flex-col use-box-large h-72 overflow-hidden"
    href="javascript:"
    @click="routerTo(RouteNameEnum.ARTICLE_DETAIL, { id: item.id })"
  >
    <div class="h-36 flex relative justify-center items-center overflow-hidden w-full">
      <svg-icon
        v-if="item.hasVideo"
        class="absolute rounded-lg z-10 bg-gray-100/70 py-1.5 px-5"
        name="play"
        size="5xl"
      />
      <image-box :src="item.cover" class="absolute use-hover-big" />
    </div>
    <el-container class="flex justify-between gap-1 p-3" direction="vertical">
      <svg-icon :message="item.updateTime" class="min-w-4 text-color-gray" name="edit" size="sm" />
      <el-container class="gap-1 justify-between">
        <svg-icon
          :message="'点赞' + item.likeCount"
          class="!gap-0.5 text-color-gray"
          name="praise"
          size="sm"
        />
        <svg-icon
          :message="'浏览' + item.viewCount"
          class="!gap-0.5 text-color-gray"
          name="footprint"
          size="sm"
        />
        <svg-icon
          :message="'评论' + item.commentCount"
          class="!gap-0.5 text-color-gray"
          name="message"
          size="sm"
        />
      </el-container>
      <span class="text-xl line-clamp-1">{{ item.title }}</span>
      <el-container class="flex-nowrap gap-1 items-center justify-between">
        <svg-icon
          :message="item.classifyName"
          class="px-1 py-0.5 use-btn-default text-color-gray bg-color-three"
          name="classify"
          size="sm"
          @click.stop="
            routerTo(RouteNameEnum.ARTICLE, {
              classifyId: item.classifyId,
              labelId: item.labelId
            })
          "
        />
        <svg-icon
          :message="item.labelName"
          class="px-1 py-0.5 use-btn-default text-color-gray bg-color-three"
          name="label"
          size="sm"
          @click.stop="
            routerTo(RouteNameEnum.ARTICLE, {
              classifyId: item.classifyId,
              labelId: item.labelId
            })
          "
        />
      </el-container>
    </el-container>
  </a>
</template>

<style lang="scss" scoped></style>
