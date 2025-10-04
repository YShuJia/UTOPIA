<script lang="ts" setup>
import type { CalendarDateType, CalendarInstance } from 'element-plus'
import { RouteNameEnum } from '@/enum'
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import { type DiaryDTO, type DiaryVO, pageDiaryApi } from '@/request/api/diary'
import { formatDate } from '@/utils/timeUtils'

const calendar = ref<CalendarInstance>()
const selectDate = (val: CalendarDateType) => {
  if (!calendar.value) {
    return
  }
  calendar.value.selectDate(val)
}

const date = ref(new Date())

const vo = ref<PageVO<DiaryVO>>(initPageVO())
const page = ref<DiaryDTO>({
  pageNum: 1,
  pageSize: 8,
  recommend: true
})

const getList = () => {
  pageDiaryApi(page.value).then((res: ResultType<PageVO<DiaryVO>>) => {
    vo.value = res.data
  })
}

watchEffect(() => {
  page.value.createTime = formatDate(date.value)
  getList()
})

onMounted(() => {
  getList()
})
</script>

<template>
  <el-container class="gap-14" direction="vertical">
    <el-container class="max-xl:flex-col">
      <el-container class="p-5 gap-10 use-theme" direction="vertical">
        <el-calendar ref="calendar" v-model="date" class="use-box-large bg-transparent">
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
        <el-container class="gap-8" direction="vertical">
          <span class="text-3xl hollow-text">莫等闲</span>
          <span class="text-5xl">白了少年头</span>
          <span class="text-3xl hollow-text">空悲切!</span>
        </el-container>
      </el-container>
      <el-container class="min-w-[70%] px-5 py-2">
        <diary-box :item="vo.list[0]" />
      </el-container>
    </el-container>
    <route-to :name="RouteNameEnum.DIARY" />
  </el-container>
</template>

<style lang="scss" scoped></style>
