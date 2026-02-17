<script lang="ts" setup>
import type { ResultType } from '@/request/config'
import { type DiaryVO, getDiaryApi, initDiaryVO } from '@/request/api/diary'
import { useSystemStore } from '@/stores/system'

const route = useRoute()
const systemStore = useSystemStore()

const diary = ref<DiaryVO>(initDiaryVO())

const getDiary = (id: number) => {
  getDiaryApi(id).then((res: ResultType<DiaryVO>) => {
    diary.value = res.data
  })
}

onMounted(() => {
  const id = Number(route.params.id) || 0
  getDiary(id)
})

const checked = ref(false)
</script>

<template>
  <top-image-template>
    <el-container class="inner-min-box p-5 gap-5" direction="vertical">
      <label class="book flex cursor-pointer relative items-center">
        <input id="heart" v-model="checked" class="on hidden" type="checkbox" />
        <el-container
          class="cover bg-color justify-between p-5 gap-5 absolute size-full items-center origin-[0] duration-300 shadow z-10"
          direction="vertical"
        >
          <el-container class="items-center gap-5 pt-5 w-full overflow-hidden" direction="vertical">
            <span class="text-3xl line-clamp-1">{{ diary.title }}</span>
            <image-box :lazy="false" :src="diary.urls[0]" class="h-32 w-fit radius-sm" />
            <span class="flex items-center gap-3">
              <svg-icon :message="diary.likeCount" class="gap-0.5!" name="praise" />
              <svg-icon :message="diary.viewCount" class="gap-0.5!" name="footprint" />
              <svg-icon :message="diary.commentCount" class="gap-0.5!" name="message" />
            </span>
          </el-container>
          <el-container class="w-full max-h-fit items-center gap-2" direction="vertical">
            <svg-button message="康康" name="paper-plane" />
            <svg-icon :message="diary.updateTime" class="ml-auto" name="edit" />
          </el-container>
        </el-container>
        <el-container class="p-5 flex flex-col min-h-96 shadow origin-[0]">
          <span class="flex text-xl font-bold border-b-2 border-amber-500">
            {{ diary.title }}
          </span>
          <el-container class="gap-5" direction="vertical">
            <pre class="pt-2 leading-8 text-justify whitespace-break-spaces">
              {{ diary.content }}
            </pre>
            <el-container class="w-full h-fit auto-3-column">
              <div v-for="(url, index) in diary.urls">
                <image-box
                  :key="url"
                  :src="url"
                  class="radius-sm"
                  @click="systemStore.preview.open(diary.urls, index)"
                />
              </div>
            </el-container>
            <div class="flex gap-2 pt-2">
              <svg-icon
                :message="'点赞' + diary.likeCount"
                class="gap-0.5! text-color-gray"
                name="praise"
                size="sm"
              />
              <svg-icon
                :message="'浏览' + diary.viewCount"
                class="gap-0.5! text-color-gray"
                name="footprint"
                size="sm"
              />
              <svg-icon
                :message="'评论' + diary.commentCount"
                class="gap-0.5! text-color-gray"
                name="message"
                size="sm"
              />
            </div>
            <span class="flex mt-auto justify-end text-lg">—— {{ diary.createUsername }}</span>
            <like-button
              v-if="diary.likeable"
              :count="diary.likeCount"
              :source-id="diary.id"
              class="mx-auto"
            />
          </el-container>
        </el-container>
      </label>
      <el-container
        v-if="diary.commentable"
        :class="`overflow-hidden duration-500 ${checked ? 'max-h-[1000px]' : 'max-h-0'}`"
      >
        <comment :source-id="diary.id" />
      </el-container>
    </el-container>
  </top-image-template>
</template>

<style scoped>
.on:checked ~ .cover {
  transform: rotatey(-105deg);
}

.book {
  perspective: 4000px;
}
</style>
