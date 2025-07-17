import { http } from '@/request/http'

export const getLikeApi = (sourceId: number) => {
  return http<boolean>({
    url: '/ui/like/liked',
    method: 'GET',
    params: {
      sourceId: sourceId
    }
  })
}

export const updateLikeApi = (sourceId: number, liked: boolean) => {
  return http<boolean>({
    url: '/ui/like/update',
    method: 'PUT',
    params: {
      sourceId: sourceId,
      liked: liked
    }
  })
}
