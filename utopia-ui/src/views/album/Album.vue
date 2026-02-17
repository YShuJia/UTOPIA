<script lang="ts" setup>
import { type LabelVO, listLabelByClassifyApi } from '@/request/api/label'
import { ClassifyEnum } from '@/enum'
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import { useSystemStore } from '@/stores/system'
import type { AlbumDTO } from '@/request/api/album'
import { type AlbumVO, pageAlbumApi } from '@/request/api/album'

const systemStore = useSystemStore()

const labelList = ref<LabelVO[]>([])
const getLabelList = () => {
  listLabelByClassifyApi({
    key: ClassifyEnum.ALBUM
  }).then((res: ResultType<LabelVO[]>) => {
    labelList.value = res.data
    getAlbum(res!.data[0]!.id)
  })
}

const data = ref<PageVO<AlbumVO>>(initPageVO())
const page = reactive<AlbumDTO>({
  pageSize: 10,
  pageNum: 1
})

const getAlbum = (labelId: number) => {
  page.labelId = labelId
  pageAlbumApi(page).then((res: ResultType<PageVO<AlbumVO>>) => {
    data.value = res.data
  })
}

onMounted(() => {
  getLabelList()
})
</script>

<template>
  <top-image-template :is-custom-top="true">
    <template #top>
      <div class="parent sticky top-0 h-[520px] w-full">
        <el-container v-for="i in 7" :key="i" class="child">
          <image-box :src="systemStore.getRandomImg()" />
        </el-container>
      </div>
    </template>
    <el-container class="inner-box p-5 gap-5" direction="vertical">
      <el-container class="flex-wrap justify-center gap-10">
        <a
          v-for="(p, i) in labelList"
          :key="i"
          :class="
            'border-2 min-w-20 use-btn-large rounded-full! h-fit text-center py-2 px-4 ' +
            (p.id === page.labelId ? 'animate-pulse' : '')
          "
          href="javascript:"
          @click="getAlbum(p.id)"
        >
          {{ p.name }}
        </a>
      </el-container>
      <el-container class="auto-column">
        <album-box v-for="item in data.list" :item="item" />
      </el-container>
    </el-container>
  </top-image-template>
</template>

<style lang="scss" scoped>
.parent {
  aspect-ratio: 4/3;
  display: grid;
  grid-template-columns: 1fr 0.7fr 0.3fr 1fr;
  grid-template-rows: 20% 30% 40%;
  grid-template-areas:
    'one two two three'
    'four five five five'
    'six five five five';
}

.child {
  width: 120%;
  height: 120%;

  &:first-child {
    grid-area: one;
    clip-path: polygon(0% 0%, 78% 0%, 97% 100%, 0% 76%);
  }

  &:nth-child(2) {
    grid-area: two;
    clip-path: polygon(0% 0%, 88% 0%, 78% 100%, 19% 100%);
  }

  &:nth-child(3) {
    width: 120%;
    grid-area: three;
    clip-path: polygon(10% 0%, 100% 0%, 100% 80%, 0% 99%);
  }

  &:nth-child(4) {
    grid-area: four;
    clip-path: polygon(0% 0%, 90% 14.5%, 95% 63%, 90% 90%, 0% 100%);
  }

  &:nth-child(5) {
    grid-area: five;
    clip-path: polygon(
      5.6% 6.5%,
      40% 6.5%,
      100% 0%,
      100% 100%,
      52% 100%,
      52% 40%,
      5.6% 38.5%,
      8% 26%
    );
  }

  &:nth-child(6) {
    grid-area: six;
    clip-path: polygon(0% 16%, 90% 8.5%, 100% 100%, 0% 100%);
  }

  &:nth-child(7) {
    grid-row-start: 3;
    grid-row-end: 4;
    grid-column-start: 2;
    grid-column-end: 4;
    clip-path: polygon(11% 8.5%, 100% 11%, 100% 100%, 21% 100%);
  }
}
</style>
