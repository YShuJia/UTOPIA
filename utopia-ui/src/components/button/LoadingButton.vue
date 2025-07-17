<script lang="ts" setup>
const { message, action, type, shape, size, customClass } = defineProps({
  message: {
    type: String,
    default: '提交'
  },
  action: {
    type: Function as PropType<(...args: any[]) => any>,
    default: () => Promise.resolve()
  },
  type: {
    type: String as PropType<'primary' | 'success' | 'info' | 'warning' | 'danger'>,
    default: ''
  },
  shape: {
    type: String as PropType<'round' | 'circle' | 'plain' | 'text' | 'link'>,
    default: 'plain'
  },
  size: {
    type: String as PropType<'large' | 'default' | 'small'>,
    default: 'default'
  },
  customClass: {
    type: String,
    default: ''
  }
})

const loading = ref(false)
const click = async () => {
  if (loading.value) return
  loading.value = true
  // 同步函数执行完毕后，1.5s 后关闭 loading
  const timeout = setTimeout(async () => {
    loading.value = false
    const result = action()
    // 判断是否是 Promise
    if (result instanceof Promise) {
      try {
        await result
      } finally {
        loading.value = false
      }
    }
    clearTimeout(timeout)
  }, 1000)
}
</script>

<template>
  <el-button
    :[shape]="true"
    :class="customClass"
    :loading="loading"
    :size="size"
    :type="type"
    @click="click"
  >
    {{ loading ? '请稍等...' : message }}
  </el-button>
</template>

<style lang="scss" scoped></style>
