<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { type DialogEnum, TableEnum } from '@/enum'

const systemStore = useSystemStore()

const { attribute, title } = defineProps({
  attribute: {
    type: String as unknown as PropType<TableEnum | DialogEnum>,
    required: true
  },
  title: {
    type: String,
    default: ''
  }
})
</script>

<template>
  <el-dialog
    v-model="systemStore.dialog[attribute]"
    :show-close="false"
    class="overflow-hidden p-0 !min-w-72 w-full max-w-[1024px] use-box-large"
    top="5vh"
    @close="systemStore.dialog[attribute] = false"
  >
    <el-scrollbar class="flex flex-col max-h-[80vh] p-5">
      <span v-if="title != ''" class="flex text-xl justify-center pb-8">{{ title }}</span>
      <slot name="default" />
    </el-scrollbar>
    <slot name="footer" />
  </el-dialog>
</template>

<style lang="scss"></style>
