import { defineStore } from 'pinia'
import { type ArticleDTO } from '@/request/api/article'

export const useTemporaryStore = defineStore(
  'temporary',
  () => {
    const article = ref<ArticleDTO>({
      id: undefined,
      labelId: undefined,
      title: undefined,
      cover: undefined,
      content: undefined,
      hasVideo: undefined,
      videoUrl: undefined,
      passwordTip: undefined,
      recommend: undefined,
      commentable: undefined,
      copyright: undefined,
      enabled: undefined,
      password: undefined,
      reviewed: undefined,
      urls: undefined
    })
    const initArticle = () => {
      article.value = {}
    }
    return {
      article,
      initArticle
    }
  },
  {
    persist: {
      key: 'temporary',
      paths: ['article']
    }
  }
)
