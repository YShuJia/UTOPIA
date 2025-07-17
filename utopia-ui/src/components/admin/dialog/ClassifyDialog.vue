<script lang="ts" setup>
import { useGlobalDialog } from '@/hooks'
import { addClassifyApi, type ClassifyDTO, updateClassifyApi } from '@/request/api/classify'
import { TableEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()

const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

// 表单
const form = defineModel<ClassifyDTO>('form', {
  default: {}
})

// 表单
const formRef = ref<any>()

const rules = {
  name: [{ required: true, message: '名字不能为空', trigger: 'blur' }],
  key: [{ required: true, message: 'key不能为空', trigger: 'blur' }],
  introduction: [{ required: true, message: '简介不能为空', trigger: 'blur' }]
}

/** 提交按钮 */
const submitForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    if (form.value.id != undefined) {
      await useGlobalDialog('确定修改 ID 为"' + form.value.id + '"的类别?').then((res: boolean) => {
        if (res) {
          updateClassifyApi(form.value).then(() => {
            systemStore.dialog.classify = false
            refresh()
          })
        }
      })
    } else {
      addClassifyApi(form.value).then(() => {
        systemStore.dialog.classify = false
        refresh()
      })
    }
  })
}
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template :attribute="TableEnum.CLASSIFY" :title="form.id ? '修改类别' : '新增类别'">
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" maxlength="16" placeholder="请输入（max 16）" />
      </el-form-item>
      <el-form-item label="类别 KEY" prop="key">
        <el-input v-model="form.key" maxlength="64" placeholder="请输入（max 64）">
          <template #prepend>sys:classify:</template>
        </el-input>
      </el-form-item>
      <el-form-item label="简介" maxlength="128" prop="introduction（max 128）">
        <el-input v-model="form.introduction" placeholder="请输入" />
      </el-form-item>
    </el-form>
    <el-container class="justify-between mt-5">
      <el-button type="info" @click="systemStore.dialog.classify = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </dialog-template>
</template>
