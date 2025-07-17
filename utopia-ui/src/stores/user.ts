import { defineStore } from 'pinia'
import type { ResultType } from '@/request/config'
import icon from '@/assets/img/icon.png'
import { useGlobalDialog } from '@/hooks'
import { getUserApi, initUserVO, type UserVO } from '@/request/api/user'
import { logoutApi } from '@/request/api'
import { clearToken, getToken } from '@/utils/tokenUtils'

export const useUserStore = defineStore(
  'user',
  () => {
    const user = ref<UserVO>(initUserVO())
    user.value.avatar = icon
    const clear = () => {
      user.value = initUserVO()
      user.value.avatar = icon
    }

    const logout = async () => {
      await useGlobalDialog('确定要退出登录？').then((res) => {
        if (res) {
          logoutApi().then((res: ResultType<boolean>) => {
            setTimeout(() => {
              clearToken()
              user.value = initUserVO()
              user.value.avatar = icon
              window.location.reload()
            }, 1500)
          })
        }
      })
    }

    return {
      user,
      logout,
      clear
    }
  },
  {
    persist: {
      paths: [],
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
