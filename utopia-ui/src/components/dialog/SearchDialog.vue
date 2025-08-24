<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { DialogEnum, RouteNameEnum } from '@/enum'
import type { RouteLocationRaw } from 'vue-router'
import { getSearchApi, initSearchVO, type SearchVO } from '@/request/api/user'

const systemStore = useSystemStore()
const router = useRouter()

const search = ref<string>('')
const data = ref<SearchVO>(initSearchVO())
const getSearch = () => {
  if (search.value === '') {
    data.value = initSearchVO()
    ElMessage.warning('请输入搜索内容！')
    return
  }
  getSearchApi(search.value).then((res) => {
    data.value = res.data
  })
}

const getLight = (text: string) => {
  // 创建正则表达式，用于匹配关键字（忽略大小写）
  const regex = new RegExp(`(${search.value})`, 'gi')
  return text.replace(regex, (match) => {
    // 匹配到的关键字用 <span> 包裹并添加样式
    return `<span style="height: fit-content; background-color: yellow; font-weight: bolder;">${match}</span>`
  })
}

const to = (to: RouteLocationRaw) => {
  systemStore.dialog.search = false
  router.push(to)
}
</script>

<template>
  <dialog-template :attribute="DialogEnum.SEARCH" class="p-5" title="搜索">
    <el-input v-model="search" placeholder="请输入搜索内容" size="large" @keydown.enter="getSearch">
      <template #suffix>
        <a class="h-full flex items-center px-3" @click="getSearch">
          <svg-icon name="search" />
        </a>
      </template>
    </el-input>
    <el-container v-if="data.article.length" class="mt-2" direction="vertical">
      <span class="text-xl">博客文章</span>
      <a
        v-for="p in data.article"
        class="py-1 px-5 use-link-default"
        @click="
          to({
            name: RouteNameEnum.ARTICLE_DETAIL,
            params: {
              id: p.id
            }
          })
        "
        v-html="getLight(p.title)"
      />
    </el-container>
    <el-container v-if="data.album.length" class="mt-2" direction="vertical">
      <span class="text-xl">如梦令</span>
      <a
        v-for="p in data.album"
        class="py-1 px-5 use-link-default"
        @click="
          to({
            name: RouteNameEnum.ALBUM_DETAIL,
            params: {
              id: p.id
            }
          })
        "
        v-html="getLight(p.title)"
      />
    </el-container>
    <el-container v-if="data.diary.length" class="mt-2" direction="vertical">
      <span class="text-xl">青玉案</span>
      <a
        v-for="p in data.diary"
        class="py-1 px-5 use-link-default"
        @click="
          to({
            name: RouteNameEnum.DIARY_DETAIL,
            params: {
              id: p.id
            }
          })
        "
        v-html="getLight(p.title)"
      />
    </el-container>
  </dialog-template>
</template>

<style lang="scss" scoped></style>
