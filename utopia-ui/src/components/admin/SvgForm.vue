<script lang="ts" setup>
/* 加载所有svg图标 */
const modules: any = import.meta.glob('@/assets/svg/*.svg', { eager: true })
const list = ref<string[]>([])
for (const path in modules) {
  list.value.push(path.replace('/src/assets/svg/', '').replace('.svg', ''))
}
const icon = ref<string>('')
const emit = defineEmits(['getValue'])

const { value, disable } = defineProps({
  value: {
    type: String,
    default: ''
  },
  disable: {
    type: Boolean,
    default: false
  }
})

watch(
  () => value,
  (val) => {
    icon.value = val
  },
  {
    immediate: true
  }
)
</script>

<template>
  <el-form-item label="图标">
    <el-select
      v-model="icon"
      :disabled="disable"
      placeholder="请选择图标"
      @change="emit('getValue', icon)"
    >
      <el-container class="auto-icon-column p-2">
        <el-option
          v-for="p in list"
          :key="p"
          :value="p"
          class="flex items-center rounded min-w-9 justify-center"
        >
          <svg-icon :name="p" size="2xl" />
        </el-option>
      </el-container>
    </el-select>
  </el-form-item>
</template>

<style lang="scss" scoped></style>
