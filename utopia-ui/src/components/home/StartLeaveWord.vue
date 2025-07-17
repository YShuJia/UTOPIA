<script lang="ts" setup>
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import type { LeaveWordDTO, LeaveWordVO } from '@/request/api/leave_word'
import { pageLeaveWordApi } from '@/request/api/leave_word'
import { RouteNameEnum } from '@/enum'

const page = reactive<LeaveWordDTO>({
  pageNum: 1,
  pageSize: 12
})

const data = ref<PageVO<LeaveWordVO>>(initPageVO())
const getList = () => {
  pageLeaveWordApi(page).then((res: ResultType<PageVO<LeaveWordVO>>) => {
    data.value = res.data
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <el-container class="gap-8" direction="vertical">
    <el-container class="auto-column p-5">
      <leave-word-box v-for="item in data.list" :item="item" />
    </el-container>
    <el-empty v-if="!data.list.length" />
    <route-to v-else :name="RouteNameEnum.LEAVE_WORD" />
  </el-container>
</template>

<style lang="scss" scoped></style>
