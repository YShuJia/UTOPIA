<script lang="ts" setup>
import { type AdminTreeVO, treeClassifyApi } from '@/request/api/classify'
import type { ClassifyEnum } from '@/enum'
import type { ResultType } from '@/request/config'

const { classifyKey, requiredClassify, requiredLabel } = defineProps({
  classifyKey: {
    type: String as PropType<ClassifyEnum>,
    default: 'sys:classify'
  },
  requiredClassify: {
    type: Boolean,
    default: true
  },
  requiredLabel: {
    type: Boolean,
    default: true
  }
})

const siftLabel = () => {
  if (!tree.value) {
    return
  }
  for (let i = 0; i < tree.value.length; i++) {
    labelMap.value.set(tree.value[i].value, tree.value[i].children)
    if (!tree.value[i].children || !labelId.value) {
      continue
    }
    // 通过 labelId 初次赋值
    for (let j = 0; j < tree.value[i].children?.length; j++) {
      if (labelId.value === tree.value[i].children[j].value) {
        classifyId.value = tree.value[i].value
      }
    }
  }
}

const classifyId = defineModel<number>('classifyId')
const labelId = defineModel<number>('labelId')

// 获取树形结构
const tree = ref<AdminTreeVO[]>()
const labelMap = ref<Map<number | undefined, AdminTreeVO[]>>(new Map())
labelMap.value.set(-1, [])

const getTree = () => {
  if (!classifyKey) {
    return
  }
  treeClassifyApi(classifyKey).then((res: ResultType<AdminTreeVO[]>) => {
    tree.value = res.data
    siftLabel()
  })
}

watch(
  () => labelId.value,
  () => {
    siftLabel()
  }
)

const labels = computed(() => {
  return labelMap.value.get(classifyId.value) || []
})

onMounted(() => {
  getTree()
})
</script>

<template>
  <el-container class="flex-wrap gap-5 max-w-fit">
    <el-form-item v-if="requiredClassify" class="min-w-52" label="类别" prop="classifyId">
      <el-select v-model="classifyId">
        <el-option v-for="i in tree" :key="i.value" :label="i.label" :value="i.value" />
      </el-select>
    </el-form-item>
    <el-form-item v-if="requiredLabel" class="min-w-52" label="标签" prop="labelId">
      <el-select v-model="labelId">
        <el-option v-for="i in labels" :key="i.value" :label="i.label" :value="i.value" />
      </el-select>
    </el-form-item>
  </el-container>
</template>

<style lang="scss" scoped></style>
