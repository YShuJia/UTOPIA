<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { type DialogEnum, TableEnum } from '@/enum'
import { useSlots } from 'vue'
const slots = useSlots()

const systemStore = useSystemStore()

const { attribute, title, open } = defineProps({
  attribute: {
    type: String as unknown as PropType<TableEnum | DialogEnum>,
    required: true
  },
  title: {
    type: String,
    default: ''
  },
  open: {
    type: Function as PropType<() => any>,
    default: () => {}
  }
})
</script>

<template>
  <el-dialog
    v-model="systemStore.dialog[attribute]"
    :show-close="false"
    class="overflow-hidden p-0 min-w-72! w-full max-w-[1024px] use-box-large"
    top="5vh"
    @close="systemStore.dialog[attribute] = false"
    @open="open"
  >
    <el-scrollbar class="flex flex-col max-h-[80vh] p-5">
      <span v-if="title != ''" class="flex text-xl justify-center pb-8">{{ title }}</span>
      <slot name="default" />
    </el-scrollbar>
    <el-container v-if="slots.footer" class="px-5 pb-5 overflow-auto">
      <slot name="footer" />
    </el-container>
  </el-dialog>
</template>

<style lang="scss"></style>
