<script lang="ts" setup>
import { useGlobalDialog } from '@/hooks'
import { TableEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'

import { insertUserAdminApi, updateUserApi, type UserDTO } from '@/request/api/user'

const systemStore = useSystemStore()

const formRef = ref<any>()

const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

// 表单
const form = defineModel<UserDTO>('form', {
  default: {}
})

const rules = {
  username: [{ required: true, message: '昵称不能为空', trigger: 'blur' }],
  roleId: [{ required: true, message: '角色不能为空', trigger: 'blur' }],
  gender: [{ required: true, message: '性别不能为空', trigger: 'blur' }],
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
  ]
}

/** 提交按钮 */
const submitForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    if (form.value.id != undefined) {
      await useGlobalDialog('确定修改 ID 为"' + form.value.id + '"的用户?').then((res: boolean) => {
        if (res) {
          updateUserApi(form.value).then(() => {
            systemStore.dialog.user = false
            refresh()
          })
        }
      })
    } else {
      await insertUserAdminApi(form.value).then(() => {
        systemStore.dialog.user = false
        refresh()
      })
    }
  })
}
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template
    :attribute="TableEnum.USER"
    :title="form.id ? '修改用户' : '新增用户'"
    class="p-5"
  >
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="昵称" prop="username">
        <el-input v-model="form.username" maxlength="16" placeholder="请输入（max 16）" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" maxlength="16" placeholder="请输入（max 16）" />
      </el-form-item>
      <role-form v-model:role-id="form.roleId" />
      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="form.gender">
          <el-radio label="保密">保密</el-radio>
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" maxlength="32" placeholder="请输入（max 32）" />
      </el-form-item>
    </el-form>
    <el-container class="justify-between mt-5">
      <el-button type="info" @click="systemStore.dialog.user = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </dialog-template>
</template>
