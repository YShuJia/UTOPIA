<script lang="ts" setup>
import { TableEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'
import type { UploadType } from '@/components/editor/UploadImg.vue'
import { useGlobalDialog } from '@/hooks'
import { addToFormData, isEmpty } from '@/utils'
import { addDiaryApi, type DiaryDTO, updateDiaryApi } from '@/request/api/diary'

const systemStore = useSystemStore()

const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

// 表单
const form = defineModel<DiaryDTO>('form', {
  default: {}
})

const formRef = ref<any>()

const upload = ref<UploadType>({
  files: new FormData(),
  urls: []
})

const rules = {
  title: [{ required: true, message: '名字不能为空', trigger: 'blur' }],
  content: [{ required: true, message: '内容不能为空', trigger: 'blur' }]
}

/** 提交按钮 */
function submitForm() {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    if (form.value.id != undefined) {
      await useGlobalDialog('确定修改相册 ' + form.value.title + ' ?').then((res: boolean) => {
        if (res) {
          // 更新图片地址
          if (upload.value.urls != undefined) {
            form.value.urls = []
            for (let i = 0; i < upload.value.urls.length; i++) {
              form.value.urls.push(upload.value.urls[i].url)
            }
          }
          if (form.value.urls == undefined) {
            form.value.urls = []
          }
          form.value.updateStatus = false
          const formData = addToFormData(upload.value.files, form.value)
          updateDiaryApi(formData).then(() => {
            systemStore.dialog.diary = false
            upload.value.initUpload?.()
            refresh()
          })
        }
      })
    } else {
      if (isEmpty(upload.value.files.get('files'))) {
        ElMessage.error('请先上传照片！')
        return
      }
      const formData = addToFormData(upload.value.files, form.value)
      await addDiaryApi(formData).then(() => {
        upload.value.initUpload?.()
        systemStore.dialog.diary = false
        refresh()
      })
    }
  })
}
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template
    :attribute="TableEnum.DIARY"
    :title="form.id ? '修改日记' : '新增日记'"
    @close="upload.initUpload?.()"
    @open="upload.echoFiles?.(form.urls)"
  >
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="内容" prop="title">
        <el-input v-model="form.title" maxlength="32" placeholder="请输入（max 32）" />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input
          v-model="form.content"
          :rows="10"
          maxlength="512"
          placeholder="请输入（max 512）"
          type="textarea"
        />
      </el-form-item>
      <el-form-item label="图片" prop="files">
        <upload-img v-model:upload="upload" :limit="6" />
      </el-form-item>
    </el-form>
    <el-container class="justify-between mt-5">
      <el-button type="info" @click="systemStore.dialog.diary = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </dialog-template>
</template>

<style lang="scss" scoped></style>
