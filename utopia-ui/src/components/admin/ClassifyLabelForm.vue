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
    for (let i = 0; i < res.data.length; i++) {
      labelMap.value.set(res.data[i].value, res.data[i].children)
      if (!res.data[i].children || !labelId.value) {
        continue
      }
      // 通过 labelId 初次赋值
      for (let j = 0; j < res.data[i].children.length; j++) {
        if (labelId.value === res.data[i].children[j].value) {
          classifyId.value = res.data[i].value
          labelId.value = res.data[i].children[j].value
        }
      }
    }
  })
}

onMounted(() => {
  getTree()
})
</script>

<template>
  <el-container class="flex-wrap gap-5 max-w-fit">
    <el-form-item v-if="requiredClassify" class="min-w-52" label="类别" prop="classifyId">
      <el-select v-model="classifyId">
        <el-option v-for="i in tree" :key="i.label" :label="i.label" :value="i.value" />
      </el-select>
    </el-form-item>
    <el-form-item v-if="requiredLabel" class="min-w-52" label="标签" prop="labelId">
      <el-select v-model="labelId">
        <el-option
          v-for="i in labelMap.get(classifyId)"
          :key="i.label"
          :label="i.label"
          :value="i.value"
        />
      </el-select>
    </el-form-item>
  </el-container>
</template>

<style lang="scss" scoped></style>
