import { defineStore } from 'pinia'
import type { ResultType } from '@/request/config'
import { useGlobalDialog } from '@/hooks'
import { getUserApi, initUserVO, type UserVO } from '@/request/api/user'
import { logoutApi } from '@/request/api'
import { clearToken, getToken } from '@/utils/tokenUtils'
import { routerTo } from '@/router'
import { RouteNameEnum } from '@/enum'

export const useUserStore = defineStore(
  'user',
  () => {
    const user = ref<UserVO>(initUserVO())
    const clear = () => {
      user.value = initUserVO()
    }

    const code = ref({
      expireTime: 180,
      isCountdown: false
    })

    const logout = async () => {
      await useGlobalDialog('确定要退出登录？').then((res) => {
        if (res) {
          logoutApi().then((res: ResultType<boolean>) => {
            routerTo(RouteNameEnum.HOME)
            setTimeout(() => {
              clearToken()
              user.value = initUserVO()
              window.location.reload()
            }, 2000)
          })
        }
      })
    }

    return {
      user,
      logout,
      clear,
      code
    }
  },
  {
    persist: {
      paths: ['code'],
      afterRestore: async (state) => {
        const token = getToken()
        if (token !== null) {
          await getUserApi().then((res: ResultType<UserVO>) => {
            state.store.user = res.data
          })
        }
      }
    }
  }
)
