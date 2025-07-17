<script lang="ts" setup>
import { useGlobalDialog } from '@/hooks'

import { ClassifyEnum, TableEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'
import { addWebsiteApi, updateWebsiteApi, type WebsiteDTO } from '@/request/api/website'

const systemStore = useSystemStore()

// 表单
const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

// 表单
const form = defineModel<WebsiteDTO>('form', {
  default: {}
})

const formRef = ref<any>()

const rules = {
  title: [{ required: true, message: '网站标题不能为空', trigger: 'blur' }],
  url: [{ required: true, message: '网址不能为空', trigger: 'blur' }],
  avatar: [{ required: true, message: '头像不能为空', trigger: 'blur' }],
  labelId: [{ required: true, message: '类别不能为空', trigger: 'blur' }],
  recommend: [{ required: true, message: '推荐不能为空', trigger: 'blur' }],
  introduction: [{ required: true, message: '简介不能为空', trigger: 'blur' }]
}

/** 提交按钮 */
function submitForm() {
  formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      if (form.value.id != undefined) {
        await useGlobalDialog('确定修改 ID 为"' + form.value.id + '"的友链?').then(
          (res: boolean) => {
            if (res) {
              updateWebsiteApi(form.value).then(() => {
                systemStore.dialog.website = false
                refresh()
              })
            }
          }
        )
      } else {
        await addWebsiteApi(form.value).then(() => {
          systemStore.dialog.website = false
          refresh()
        })
      }
    }
  })
}
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template :attribute="TableEnum.WEBSITE" :title="form.id ? '修改友链' : '新增友链'">
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入" />
      </el-form-item>
      <classify-label-form v-model:label-id="form.labelId" :classify-key="ClassifyEnum.WEBSITE" />
      <el-form-item label="头像" prop="avatar">
        <el-input v-model="form.avatar" placeholder="请输入" />
      </el-form-item>
      <el-form-item label="封面" prop="cover">
        <el-input v-model="form.cover" placeholder="请输入" />
      </el-form-item>
      <el-form-item label="网站地址" prop="url">
        <el-input v-model="form.url" placeholder="请输入" />
      </el-form-item>
      <el-form-item label="是否推荐" prop="recommend">
        <el-radio-group v-model="form.recommend">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="简介" prop="introduction">
        <el-input
          v-model="form.introduction"
          :rows="10"
          maxlength="200"
          placeholder="请输入"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <el-container class="justify-between mt-5">
      <el-button type="info" @click="systemStore.dialog.website = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </dialog-template>
</template>

<style lang="scss"></style>
