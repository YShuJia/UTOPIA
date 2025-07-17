<script lang="ts" setup>
import { useUserStore } from '@/stores/user'
import {
  initUserVO,
  updateUserApi,
  updateUserPassApi,
  type UserDTO,
  userVO2DTO
} from '@/request/api/user'
import { useGlobalDialog } from '@/hooks'
import type { ResultType } from '@/request/config'
import { useSystemStore } from '@/stores/system'
import { clearToken } from '@/utils/tokenUtils'
import { logoutApi } from '@/request/api'

const userStore = useUserStore()
const systemStore = useSystemStore()

const rules = {
  avatar: [{ required: true, message: '头像不能为空', trigger: 'blur' }],
  username: [{ required: true, message: '昵称不能为空', trigger: 'blur' }],
  gender: [{ required: true, message: '性别不能为空', trigger: 'blur' }],
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    {
      pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
      message: '邮箱格式不正确',
      trigger: 'blur'
    }
  ]
}
const formRef = ref()
const form = ref<UserDTO>({})

// 修改密码
const pass = ref({
  oldPass: '',
  password: '',
  confirmPass: ''
})

/** 提交按钮 */
const submitForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      await useGlobalDialog('确定修改信息？').then((res: boolean) => {
        if (!res) {
          return
        }
        updateUserApi(form.value)
      })
    }
  })
}

const passSubmitForm = async () => {
  if (pass.value.oldPass == '') {
    ElMessage.warning('请输入旧密码！')
    return
  }
  if (pass.value.password == '' || pass.value.confirmPass == '') {
    ElMessage.warning('请输入新密码！')
    return
  }
  if (pass.value.confirmPass != pass.value.password) {
    ElMessage.warning('两次密码不一致！')
    return
  }
  await useGlobalDialog('确定修改密码？').then((res: boolean) => {
    if (!res) {
      return
    }
    updateUserPassApi(pass.value.oldPass, pass.value.password).then((res: ResultType<boolean>) => {
      logoutApi().then((res: ResultType<boolean>) => {
        setTimeout(() => {
          clearToken()
          userStore.user = initUserVO()
          window.location.reload()
        }, 1500)
      })
    })
  })
}

onMounted(() => {
  form.value = userVO2DTO(userStore.user)
})
</script>

<template>
  <el-container class="gap-12 p-5" direction="vertical">
    <el-form ref="formRef" :model="form" :rules="rules" class="bg-color-three p-5 radius-lg">
      <el-container class="justify-between border-b-2 pb-1 mb-5">
        <span class="text-xl">修改信息</span>
        <svg-button class="ml-auto" @click="submitForm" />
      </el-container>
      <el-container class="gap-5 flex-col">
        <el-form-item label="头像" prop="avatar">
          <el-avatar
            :src="form.avatar"
            class="cursor-pointer"
            shape="square"
            size="large"
            @click="systemStore.dialog.media_form = true"
          />
        </el-form-item>
        <el-form-item label="昵称" prop="username">
          <el-input v-model="form.username" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="保密" value="保密" />
            <el-radio label="男" value="男" />
            <el-radio label="女" value="女" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入" />
        </el-form-item>
      </el-container>
    </el-form>

    <el-form ref="passFormRef" :model="pass" :rules="rules" class="bg-color-three p-5 radius-lg">
      <el-container class="justify-between border-b-2 pb-1 mb-5">
        <span class="text-xl">修改密码</span>
        <svg-button class="ml-auto" @click="passSubmitForm" />
      </el-container>
      <el-container class="gap-5" direction="vertical">
        <el-form-item label="旧密码" prop="oldPass">
          <el-input v-model="pass.oldPass" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input v-model="pass.password" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPass">
          <el-input v-model="pass.confirmPass" placeholder="请输入" />
        </el-form-item>
      </el-container>
    </el-form>
    <file-form v-model:url="form.avatar" type="avatar" />
  </el-container>
</template>

<style lang="scss" scoped></style>
