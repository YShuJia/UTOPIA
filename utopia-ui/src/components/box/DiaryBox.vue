<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { type DiaryVO, initDiaryVO } from '@/request/api/Diary'
import { routerTo } from '@/router'
import { RouteNameEnum } from '@/enum'

const systemStore = useSystemStore()

const { item } = defineProps({
  item: {
    type: Object as PropType<DiaryVO>,
    default: () => {
      return initDiaryVO()
    }
  }
})
</script>

<template>
  <a class="flex relative w-full" href="javascript:">
    <el-main class="note-box size-full use-theme absolute left-0 top-6 shadow" />
    <el-main class="note-box size-full use-theme absolute left-3 top-3 shadow" />
    <el-container class="pl-8 pr-5 py-6 z-10" direction="vertical">
      <span class="flex text-xl font-bold border-b-2 border-amber-500"> {{ item.title }} </span>
      <el-container class="gap-5" direction="vertical">
        <span class="pt-2 leading-8 text-justify">
          {{ item.content }}
        </span>
        <div class="w-full h-fit auto-3-column">
          <div v-for="(url, index) in item.urls">
            <image-box
              :key="url"
              :lazy="false"
              :src="url"
              class="radius-sm"
              @click="systemStore.preview.open(item.urls, index)"
            />
          </div>
        </div>
        <div class="flex gap-2">
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
        </div>
        <span class="flex mt-auto justify-end text-lg">—— {{ item.createUsername }}</span>
        <svg-button
          class="ml-auto"
          message="去康康"
          name="paper-plane"
          size="sm"
          @click="routerTo(RouteNameEnum.DIARY_DETAIL, { id: item.id })"
        />
      </el-container>
    </el-container>
  </a>
</template>

<style lang="scss" scoped>
.note-box {
  max-width: calc(100% - 12px);
  max-height: calc(100% - 12px);
}
</style>
