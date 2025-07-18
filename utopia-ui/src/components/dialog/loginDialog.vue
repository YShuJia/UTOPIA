<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { useUserStore } from '@/stores/user'
import { getCodeApi, insertUserApi, type UserVO } from '@/request/api/user'
import type { ResultType } from '@/request/config'
import { loginApi } from '@/request/api'
import { setToken } from '@/utils/tokenUtils'

const systemStore = useSystemStore()
const userStore = useUserStore()

const rules = {
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    {
      pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
      message: '邮箱格式不正确',
      trigger: 'blur'
    }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 8, max: 16, message: '密码长度应在8到16位之间', trigger: 'blur' },
    {
      pattern: /^(?=.*[A-Za-z])(?=.*\d).+$/,
      message: '密码必须包含英文和数字',
      trigger: 'blur'
    }
  ],
  code: [{ required: true, message: '验证码不能为空', trigger: 'blur' }]
}

// 表单数据
const formRef = ref()
const loginForm = ref({
  email: '',
  password: '',
  code: ''
})

const code = ref('')

// 登录注册切换状态
const isLogin = ref(true)

const leftClass =
  'flex w-2/5 max-sm:w-1/3 duration-500 z-50 py-10 pb-6 px-2 bg-amber-500 justify-between items-center '
const rightClass =
  'flex bg-color w-3/5 duration-500 max-sm:w-2/3 z-40 pt-10 pb-5 px-2 justify-between items-center '

const getCode = () => {
  getCodeApi().then((res: ResultType<string>) => {
    code.value = res.data
  })
}

const sendForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    // 表单验证失败
    if (!valid) {
      return
    }
    if (isLogin.value) {
      await loginApi(loginForm.value.email, loginForm.value.password, loginForm.value.code).then(
        (res: ResultType<UserVO>) => {
          userStore.user = res.data
          setToken(res.data.token)
          systemStore.dialog.login = false
          setTimeout(() => {
            window.location.reload()
          }, 1000)
        }
      )
    } else {
      await insertUserApi(loginForm.value.email, loginForm.value.password)
    }
  })
}
</script>

<template>
  <el-dialog
    v-model="systemStore.dialog.login"
    :show-close="false"
    class="overflow-hidden p-0 use-box-large mt-32 min-w-80 !max-w-[520px]"
    top="10vh"
    @close="systemStore.dialog.login = false"
    @open="getCode"
  >
    <el-container class="z-50 h-[400px] overflow-hidden relative">
      <el-container
        :class="isLogin ? leftClass : leftClass + ' translate-x-[150%] max-sm:translate-x-[200%]'"
        direction="vertical"
      >
        <span class="text-xl max-sm:text-sm">
          {{ isLogin ? '没有账号？' : '已有账号？' }}
        </span>
        <a class="py-1 flex justify-center w-full" href="javascript:" @click="isLogin = !isLogin">
          {{ isLogin ? '去注册?' : '去登录?' }}
        </a>
      </el-container>
      <el-container
        :class="
          isLogin ? rightClass : rightClass + ' -translate-x-[66.66%] max-sm:-translate-x-[50%]'
        "
        direction="vertical"
      >
        <span class="pb-5 text-xl">{{ isLogin ? '登 录' : '注 册' }}</span>
        <el-form ref="formRef" :model="loginForm" :rules="rules" class="w-full flex flex-col gap-3">
          <el-form-item class="w-full" label="邮 箱" label-position="top" prop="email">
            <el-input v-model="loginForm.email" class="text-lg" placeholder="邮 箱" size="large">
              <template #prefix>
                <svg-icon name="email" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item class="w-full" label="密 码" label-position="top" prop="password">
            <el-input
              v-model="loginForm.password"
              class="text-lg"
              placeholder="密 码"
              show-password
              size="large"
              type="password"
            >
              <template #prefix>
                <svg-icon name="lock" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item class="w-full" label="验证码" label-position="top" prop="code">
            <el-input
              v-model="loginForm.code"
              class="relative text-lg"
              placeholder="验证码"
              size="large"
            >
              <template #prefix>
                <svg-icon name="lock" />
              </template>
              <template #suffix>
                <code-box :value="code" class="absolute right-0.5" @click="getCode" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item class="w-full">
            <a class="mt-5 border p-1 w-full use-btn-default" href="javascript:" @click="sendForm">
              {{ isLogin ? '登 录' : '注 册' }}
            </a>
          </el-form-item>
        </el-form>
      </el-container>
    </el-container>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
