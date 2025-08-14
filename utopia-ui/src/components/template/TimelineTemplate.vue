<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { type DiaryVO, initDiaryVO } from '@/request/api/diary'

const systemStore = useSystemStore()

const { index, diary } = defineProps({
  index: {
    type: Number,
    required: true,
    default: 0
  },
  diary: {
    type: Object as PropType<DiaryVO>,
    default: () => {
      return initDiaryVO()
    }
  }
})
</script>

<template>
  <el-container :class="`px-2 max-md:pl-4 ${index % 2 === 0 ? 'max-md:flex-row-reverse' : ''}`">
    <el-container
      :class="`w-1/2 px-3 pt-7 pb-16 ${index % 2 !== 0 ? 'max-md:w-0 px-0' : 'max-md:w-full'}`"
    >
      <diary-box v-if="index % 2 === 0" :item="diary" />
    </el-container>
    <el-container class="relative items-center justify-center" direction="vertical">
      <span
        :class="`absolute -top-0.5 whitespace-nowrap ${index % 2 === 0 ? 'right-6 max-md:!left-6 ' : 'left-6'}`"
      >
        {{ diary.updateTime }}
      </span>
      <span class="flex min-w-5 max-w-5 rounded-full h-5 bg-amber-500"></span>
      <span class="border-2 w-0 h-full"></span>
    </el-container>
    <el-container
      :class="`w-1/2 px-3 pt-7 pb-16 ${index % 2 === 0 ? 'max-md:hidden px-0' : 'max-md:w-full'}`"
    >
      <diary-box v-if="index % 2 !== 0" :item="diary" />
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped></style>
