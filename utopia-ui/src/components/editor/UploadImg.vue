<script lang="ts" setup>
import type { UploadProps } from 'element-plus'
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()

const uploadRef = ref()

const { limit } = defineProps({
  limit: {
    type: Number,
    default: 1
  }
})

export type UploadType = {
  files: FormData
  uploadRef?: any
  // 回显时收集图片地址（只保存回显图片，新上传的图片保存到 files 中）
  urls?: any[]
  initUpload?: () => void
  // 回显图片调用传入的图片地址
  echoFiles?: (urls: string[] | undefined) => void
  //判断上传的文件是否为空
  isEmpty?: () => boolean
}

const upload = defineModel<UploadType>('upload', {
  default: {
    files: new FormData(),
    uploadRef: null as any,
    urls: [],
    initUpload: () => {},
    // 回显图片调用传入的图片地址
    echoFiles: (urls: string[] | undefined) => {},
    //判断上传的文件是否为空
    isEmpty: () => {}
  }
})

const handleChange: UploadProps['onChange'] = (uploadFile, uploadFiles) => {
  /* 注意： files 添加文件时要跳过 urls 中回显的图片 */
  upload.value.files.append('files', uploadFile.raw!)
  length.value = uploadFiles.length
}

// 移除图片，保存未移除的图片地址（修改已经上传的图片）
const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
  // 移除已上传的图片
  if (upload.value.urls != undefined) {
    for (let i = 0; i < upload.value.urls.length; i++) {
      if (upload.value.urls[i].url == uploadFile.url) {
        upload.value.urls?.splice(i, 1)
        break
      }
    }
  }

  if (upload.value.files.has('files')) {
    let files: any[] = upload.value.files.getAll('files')
    // 删除已上传的图片
    upload.value.files.delete('files')
    for (let i = 0; i < files.length; i++) {
      if (files[i].uid !== uploadFile.uid) {
        upload.value.files.append('files', files[i])
      }
    }
  }
  length.value = uploadFiles.length
}

// 图片预览
const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
  systemStore.preview.open(uploadFile.url!)
}
// 图片数量限制，隐藏文件上传
const length = ref(0)
watch(length, () => {
  const uploadDom: any = document.querySelector('.el-upload')
  if (length.value >= limit && uploadDom) {
    uploadDom.style.display = 'none'
  } else {
    uploadDom.style.display = 'flex'
  }
})

onMounted(() => {
  upload.value.uploadRef = uploadRef.value
  upload.value.initUpload = () => {
    upload.value.uploadRef?.clearFiles()
    upload.value.files = new FormData()
    upload.value.urls = []
    length.value = 0
  }
  upload.value.echoFiles = (urls: string[] | undefined) => {
    if (urls == undefined) {
      return
    }
    upload.value.urls = []
    for (let i = 0; i < urls.length; i++) {
      const str = {
        url: urls[i]
      }
      upload.value.urls.push(str)
    }
    length.value = urls.length + upload.value.files.getAll('files').length
  }
  upload.value.isEmpty = () => {
    return upload.value.files.getAll('files').length == 0 && upload.value.urls?.length == 0
  }
})
</script>

<template>
  <el-upload
    ref="uploadRef"
    :auto-upload="false"
    acc
    :file-list="upload.urls"
    :limit="limit"
    :on-change="handleChange"
    :on-preview="handlePictureCardPreview"
    :on-remove="handleRemove"
    list-type="picture-card"
    multiple
  >
    <svg-icon name="add" />
  </el-upload>
</template>

<style lang="scss" scoped></style>
