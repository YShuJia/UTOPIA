<script lang="ts" setup>
import { MdCatalog, MdPreview } from 'md-editor-v3'
import type { ResultType } from '@/request/config'
import 'md-editor-v3/lib/style.css'
import { type ArticleVO, getArticleApi, initArticleVO } from '@/request/api/article'
import { useStyleStore } from '@/stores/style'
import { useScrollStore } from '@/stores/scroll'
import { getLikeApi, updateLikeApi } from '@/request/api/like'

const route = useRoute()
const styleStore = useStyleStore()
const scrollStore = useScrollStore()

// 主题
const previewTheme = ['default', 'github', 'vuepress', 'mk-cute', 'smart-blue', 'cyanosis']
let preview = ref<string>('default')

const liked = ref(false)
const like = () => {
  getLikeApi(article.value.id).then((res: ResultType<boolean>) => {
    liked.value = res.data
  })
}

const updateLike = () => {
  updateLikeApi(article.value.id, liked.value).catch(() => {
    liked.value = !liked.value
  })
}

const article = ref<ArticleVO>(initArticleVO())
onMounted(() => {
  const id = Number(route.params.id) || 0
  getArticleApi(id).then((res: ResultType<ArticleVO>) => {
    article.value = res.data
    like()
  })
})
</script>

<template>
  <top-image-template :img="article.cover" class="">
    <template #title>
      <el-container class="items-center gap-2" direction="vertical">
        <span class="text-3xl line-clamp-1 flowing-text">{{ article.title }}</span>
        <span class="flex items-center gap-2">
          <svg-icon :message="article.likeCount" class="gap-0.5!" name="praise" />
          <svg-icon :message="article.viewCount" class="gap-0.5!" name="footprint" />
          <svg-icon :message="article.commentCount" class="gap-0.5!" name="message" />
        </span>
        <span class="text-amber-500">{{ article.updateTime }}</span>
      </el-container>
    </template>

    <div class="flex flex-col inner-min-box gap-10 justify-center relative">
      <md-preview
        v-model="article.content"
        :autoFoldThreshold="120"
        :preview-theme="preview"
        :theme="styleStore.background.bgType"
        class="pb-5 border-2! use-theme"
        editorId="my-preview"
        readonly="true"
      />
      <el-affix :offset="40" class="mx-auto" position="bottom">
        <el-container
          class="items-center bg-color-secondary use-box-large gap-5 py-2 px-4 rounded-full!"
        >
          <a href="javascript:">
            <el-dropdown>
              <svg-icon name="palette" size="3xl" />
              <template #dropdown>
                <span class="text-base! flex py-1 flex-col">
                  <a
                    v-for="(p, i) in previewTheme"
                    :key="i"
                    :class="
                      'py-1 px-5 flex use-link-default ' + (preview === p ? 'text-amber-500' : '')
                    "
                    href="javascript:"
                    @click="preview = p"
                  >
                    {{ p }}
                  </a>
                </span>
              </template>
            </el-dropdown>
          </a>
          <a href="javascript:">
            <el-dropdown>
              <svg-icon name="menu" size="3xl" />
              <template #dropdown>
                <md-catalog
                  :scrollElement="scrollStore.scroll.scrollRef"
                  :theme="styleStore.background.bgType"
                  editorId="my-preview"
                />
              </template>
            </el-dropdown>
          </a>
          <a href="javascript:">
            <label class="flex cursor-pointer items-center overflow-hidden">
              <input
                id="heart"
                v-model="liked"
                class="on hidden"
                type="checkbox"
                @change="updateLike"
              />
              <svg
                class="like-icon w-[35px] fill-gray-400"
                fill-rule="nonzero"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z"
                ></path>
              </svg>
            </label>
          </a>
        </el-container>
      </el-affix>
      <el-container class="px-5">
        <copyright :copyright="article.copyright" />
      </el-container>
      <comment v-if="article.commentable" :source-id="article.id" />
    </div>
  </top-image-template>
</template>

<style lang="scss" scoped>
.on:checked ~ .like-icon {
  fill: red;
  animation: enlarge 0.5s ease-out 1;
  transition: all 0.5s ease-out;
}

.on:checked ~ .like-count {
  transform: translateY(-40px);
}

@keyframes enlarge {
  0% {
    transform: scale(0.5);
  }
  100% {
    transform: scale(1.2);
  }
}
</style>
