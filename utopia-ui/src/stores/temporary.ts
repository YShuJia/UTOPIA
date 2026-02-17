import { defineStore } from 'pinia'
import { type ArticleDTO } from '@/request/api/article'
import { initWebConfigDTO, type WebConfigDTO } from '@/request/api/web_config'

export const useTemporaryStore = defineStore(
  'temporary',
  () => {
    const article = ref<ArticleDTO>({
      urls: [],
      isUpdated: false
    })

    const webConfig = ref<WebConfigDTO>(initWebConfigDTO())

    return {
      article,
      webConfig
    }
  },
  {
    persist: {
      key: 'temporary',
      pick: ['article', 'webConfig']
    }
  }
)
