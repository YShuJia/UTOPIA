import { defineStore } from 'pinia'
import { type ArticleDTO } from '@/request/api/article'
import type { WebConfigDTO } from '@/request/api/web_config'

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

    const webConfig = ref<WebConfigDTO>({
      id: undefined,
      name: undefined,
      authorName: undefined,
      authorAvatar: undefined,
      authorTag: undefined,
      authorContact: undefined,
      authorPayment: undefined,
      authorHome: [],
      authorMbti: undefined,
      authorSkill: undefined,
      authorGame: undefined,
      authorBook: undefined,
      authorFootprint: undefined,
      authorAbout: undefined,
      siteTitle: undefined,
      siteMotto: undefined,
      siteRecord: undefined,
      siteFavicon: undefined,
      siteAbout: undefined,
      siteCreateTime: undefined,
      enabled: undefined
    })

    const initWebConfig = () => {
      webConfig.value = {}
    }

    return {
      article,
      initArticle,
      webConfig,
      initWebConfig
    }
  },
  {
    persist: {
      key: 'temporary',
      paths: ['article']
    }
  }
)
