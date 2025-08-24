<script lang="ts" setup>
import { useScrollStore } from '@/stores/scroll'

const scrollStore = useScrollStore()

const { total, change} = defineProps({
  total: {
    type: Number,
    default: 0
  },
  change: {
    type: Function,
    default: () => {}
  },
})

const dto = defineModel('dto', {
  type: Object,
  default: {
    pageNum: 1,
    pageSize: 10
  }
})

const pageChange = (currentPage: number) => {
  dto.value.pageNum = currentPage
}

const changeHandler = () => {
  change()
  scrollStore.scrollTo()
}
</script>

<template>
  <el-pagination
    v-model:current-page="dto.pageNum"
    v-model:page-size="dto.pageSize"
    :page-sizes="[10, 30, 50, 100]"
    :total="total"
    background
    layout="total, sizes, prev, pager, next, jumper"
    @change="changeHandler"
    @currentChange="pageChange"
  />
</template>

<style lang="scss" scoped></style>
