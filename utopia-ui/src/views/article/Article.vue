<script lang="ts" setup>
import type { ArticleDTO } from '@/request/api/article'
import { type ArticleVO, pageArticleApi } from '@/request/api/article'
import { ref } from 'vue'
import { initLabelVO, type LabelVO, listLabelByClassifyApi } from '@/request/api/label'
import { type ClassifyVO, getClassifyApi, initClassifyVO } from '@/request/api/classify'
import { initPageVO, type PageVO, type ResultType } from '@/request/config'

const colors = [
  'rgb(241, 147, 156)',
  'rgb(232,31,103)',
  'rgb(254, 204, 17)',
  'rgb(129, 95, 37)',
  'rgb(18, 170, 156)',
  'rgb(99, 187, 208)',
  'rgb(24,159,224)',
  'rgb(97, 100, 159)'
]

const route = useRoute()

// 获取参数
const params = reactive({
  classifyId: 0,
  labelId: 0
})

// 当前选中的 label
const selectLabel = ref<LabelVO>(initLabelVO())
const updateSelectedLabel = (p: LabelVO) => {
  if (p.id !== selectLabel.value.id) {
    selectLabel.value = p
    page.labelId = p.id
    article.list = []
    page.pageNum = 0
    getList()
  }
}

const page = reactive<ArticleDTO>({
  pageSize: 10,
  pageNum: 0,
  labelId: 0,
  urls: [],
  isUpdated: false
})

const article = reactive<PageVO<ArticleVO>>(initPageVO())
const getList = () => {
  page.pageNum = (page.pageNum ?? 1) + 1
  pageArticleApi(page).then((res) => {
    article.list.push(...res.data.list)
    article.isEmpty = res.data.isEmpty
  })
}

const labelList = ref<LabelVO[]>([])
const getLabelList = (classifyId: number) => {
  listLabelByClassifyApi({ id: classifyId }).then((res) => {
    labelList.value = res.data
    for (let i = 0; i < labelList.value.length; i++) {
      if (params.labelId === labelList.value[i].id) {
        selectLabel.value = labelList.value[i]
        page.labelId = selectLabel.value.id
      }
    }
    getList()
  })
}

const classify = ref<ClassifyVO>(initClassifyVO())
const getClassify = (classifyId: number) => {
  getClassifyApi(classifyId).then((res: ResultType<ClassifyVO>) => {
    classify.value = res.data
  })
}

onMounted(() => {
  if (route.params.classifyId && route.params.labelId) {
    params.classifyId = Number(route.params.classifyId)
    params.labelId = Number(route.params.labelId)
    getClassify(params.classifyId)
    getLabelList(params.classifyId)
  }
})
</script>

<template>
  <top-image-template>
    <template #title>
      <el-container class="items-center gap-2" direction="vertical">
        <span class="text-4xl max-md:text-2xl flowing-text">{{ classify.name }}</span>
        <span>标签数量：{{ labelList.length }}</span>
      </el-container>
    </template>
    <el-container class="inner-box gap-5" direction="vertical">
      <el-container class="flex-wrap justify-center p-5 gap-5">
        <a
          v-for="(p, i) in labelList"
          :key="i"
          :class="
            'border-2 py-3 px-4 use-link-default rounded-full ' +
            (p.id === page.labelId ? 'animate-pulse' : '')
          "
          :style="{ backgroundColor: colors[i % colors.length] }"
          href="javascript:"
          @click="updateSelectedLabel(p)"
        >
          {{ p.name }}
        </a>
      </el-container>
      <span class="flex px-5 justify-center">简介：{{ selectLabel.introduction }}</span>
      <el-container class="p-5 auto-column">
        <article-box v-for="item in article.list" :item="item" />
      </el-container>
      <loading-more :action="getList" :empty="article.isEmpty" />
    </el-container>
  </top-image-template>
</template>

<style lang="scss"></style>
