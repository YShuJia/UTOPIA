<script lang="ts" setup>
import { type ArchiveVO, listArchiveApi } from '@/request/api/article'
import type { ResultType } from '@/request/config'
import { useSystemStore } from '@/stores/system'
import { routerTo } from '@/router'
import { RouteNameEnum } from '@/enum'

const systemStore = useSystemStore()

const archiveList = ref<ArchiveVO[]>([])
const getArchiveList = () => {
  listArchiveApi().then((res: ResultType<ArchiveVO[]>) => {
    archiveList.value = res.data
  })
}

onMounted(() => {
  getArchiveList()
})
</script>

<template>
  <el-container class="inner-box pt-20 gap-10" direction="vertical">
    <el-main class="w-full aspect-[8/3] max-lg:aspect-[2/1] max-sm:aspect-[5/4]">
      <carousel-deploy v-slide-in="systemStore.system.isOpenAnimation" />
    </el-main>

    <el-container v-for="items in archiveList" :key="items.classifyName" direction="vertical">
      <el-main class="pb-0!">
        <svg-icon
          :message="items.classifyName"
          class="w-fit pb-1 border-b-4"
          name="classify"
          size="xl"
        />
      </el-main>
      <el-container class="p-5 auto-column">
        <a
          v-for="item in items.labelList"
          :key="item.labelId"
          v-slide-in="systemStore.system.isOpenAnimation"
          class="image-box transform-gpu use-box-large aspect-video overflow-hidden flex relative items-center justify-center"
          href="javascript:"
          @click="
            routerTo(RouteNameEnum.ARTICLE, { classifyId: items.classifyId, labelId: item.labelId })
          "
        >
          <image-box :src="item.labelCover" class="absolute" />
          <span class="flex z-10 flex-col items-center text-gray-50">
            <span
              class="bg-amber-500 radius-sm w-16 pt-1.5 pb-2 max-sm:text-sm text-center duration-300"
            >
              {{ item.articleCount }}
            </span>
            <span class="mt-2 line-clamp-1 px-8">{{ item.labelName }}</span>
          </span>
        </a>
      </el-container>
    </el-container>
  </el-container>
</template>

<style lang="scss">
.image-box {
  position: relative;
  background: linear-gradient(rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.3));
}

.image-box::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 1;
}
</style>
