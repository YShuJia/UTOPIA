<script lang="ts" setup>
import { ClassifyEnum, TableEnum } from '@/enum'
import { addAlbumApi, type AlbumDTO, updateAlbumApi } from '@/request/api/album'
import { useSystemStore } from '@/stores/system'
import type { UploadType } from '@/components/editor/UploadImg.vue'
import { useGlobalDialog } from '@/hooks'
import { addToFormData, isEmpty } from '@/utils'

const systemStore = useSystemStore()

const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

// 表单
const form = defineModel<AlbumDTO>('form', {
  default: {}
})

const formRef = ref<any>()

const upload = ref<UploadType>({
  files: new FormData(),
  urls: []
})

const rules = {
  title: [{ required: true, message: '名字不能为空', trigger: 'blur' }],
  roleId: [{ required: true, message: '名字不能为空', trigger: 'blur' }],
  labelId: [{ required: true, message: '类别不能为空', trigger: 'blur' }],
  introduction: [{ required: true, message: '简介不能为空', trigger: 'blur' }]
}

/** 提交按钮 */
function submitForm() {
  if (upload.value.isEmpty?.()) {
    ElMessage.warning('请先上传照片！')
    return
  }
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    if (form.value.id != undefined) {
      await useGlobalDialog('确定修改 ID 为"' + form.value.id + '"的相册?').then((res: boolean) => {
        if (res) {
          // 更新图片地址
          if (upload.value.urls != undefined) {
            form.value.urls = []
            for (let i = 0; i < upload.value.urls.length; i++) {
              form.value.urls.push(upload.value.urls[i].url)
            }
          }
          form.value.updateStatus = false
          const formData = addToFormData(upload.value.files, form.value)
          updateAlbumApi(formData).then(() => {
            systemStore.dialog.album = false
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
      await addAlbumApi(formData).then(() => {
        upload.value.initUpload?.()
        systemStore.dialog.album = false
        refresh()
      })
    }
  })
}
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template
    :attribute="TableEnum.ALBUM"
    :title="form.id ? '修改相册' : '新增相册'"
    @close="upload.initUpload?.()"
    @open="upload.echoFiles?.(form.urls)"
  >
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" maxlength="32" placeholder="请输入（max 32）" />
      </el-form-item>
      <classify-label-form v-model:label-id="form.labelId" :classifyKey="ClassifyEnum.ALBUM" />
      <el-form-item label="文件" prop="files">
        <upload-img v-model:upload="upload" :limit="9" />
      </el-form-item>
      <el-form-item label="简介" prop="introduction">
        <el-input
          v-model="form.introduction"
          :rows="10"
          maxlength="128"
          placeholder="请输入（max 128）"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <el-container class="justify-between mt-5">
      <el-button type="info" @click="systemStore.dialog.album = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </dialog-template>
</template>

<style lang="scss" scoped></style>
