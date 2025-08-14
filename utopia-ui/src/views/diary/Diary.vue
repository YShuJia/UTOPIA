<script lang="ts" setup>
import type { CalendarDateType, CalendarInstance } from 'element-plus'
import { initPageDTO, initPageVO, type PageVO, type ResultType } from '@/request/config'
import { type DiaryDTO, type DiaryVO, pageDiaryApi } from '@/request/api/diary'

const route = useRoute()

const calendar = ref<CalendarInstance>()
const selectDate = (val: CalendarDateType) => {
  if (!calendar.value) {
    return
  }
  calendar.value.selectDate(val)
}

const vo = ref<PageVO<DiaryVO>>(initPageVO())

const page = ref<DiaryDTO>(initPageDTO())
const getList = () => {
  pageDiaryApi(page.value).then((res: ResultType<PageVO<DiaryVO>>) => {
    vo.value = res.data
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <top-image-template>
    <template #title>
      <el-container class="items-center gap-1" direction="vertical">
        <span class="text-3xl line-clamp-1 flowing-text">{{ route.meta.title }} </span>
        <span class="line-clamp-1">莫等闲，白了少年头，空悲切！</span>
      </el-container>
    </template>
    <el-container class="inner-box gap-10" direction="vertical">
      <el-container class="p-5 use-theme">
        <el-calendar ref="calendar" class="use-box-large bg-transparent">
          <template #date-cell="{ data }">
            <p
              :class="data.isSelected ? 'is-selected relative text-center' : 'relative text-center'"
            >
              {{ data.day.split('-').slice(2).join('-') }}
              <span class="absolute left-0 top-0">
                {{ data.isSelected ? '✔️' : '' }}
              </span>
            </p>
          </template>
          <template #header="{ date }">
            <el-container class="gap-5 items-center justify-center">
              <svg-icon
                class="cursor-pointer rotate-90"
                name="downward"
                @click="selectDate('prev-month')"
              />
              <span class="text-center">{{ date }}</span>
              <svg-icon
                class="cursor-pointer -rotate-90"
                name="downward"
                @click="selectDate('next-month')"
              />
            </el-container>
          </template>
        </el-calendar>
      </el-container>
      <el-container direction="vertical">
        <timeline-template
          v-for="(item, index) in vo.list"
          :key="item.id"
          :diary="item"
          :index="index"
        />
      </el-container>
    </el-container>
  </top-image-template>
</template>

<style lang="scss" scoped>
.note-text {
  &:before {
    content: '隐藏';
    visibility: hidden;
  }
}

.note-box {
  max-width: calc(100% - 52px);
  max-height: calc(100% - 40px);
}
</style>
