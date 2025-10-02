<script lang="ts" setup>
import { useTemporaryStore } from '@/stores/temporary'
import { useGlobalDialog } from '@/hooks'
import {
  addWebConfigApi,
  getWebConfigAdminApi,
  initWebConfigDTO,
  updateWebConfigApi,
  type WebConfigVO,
  webConfigVO2DTO
} from '@/request/api/web_config'
import { delMinioFilesApi } from '@/request/api'
import type { ResultType } from '@/request/config'

const store = useTemporaryStore()
const route = useRoute()
const formRef = ref<any>()

const rules = {
  name: [
    {
      required: true,
      message: '请输入配置名',
      trigger: 'blur'
    }
  ]
}

/** 提交按钮 */
const submitForm = async () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    if (store.webConfig.id > 0) {
      await useGlobalDialog('确定修改 ID 为"' + store.webConfig.id + '"的用户?').then(
        (res: boolean) => {
          if (res) {
            updateWebConfigApi(store.webConfig).then(() => {})
          }
        }
      )
    } else {
      await addWebConfigApi(store.webConfig).then(() => {})
    }
  })
}

const getWebConfig = (id: number) => {
  getWebConfigAdminApi(id).then((res: ResultType<WebConfigVO>) => {
    store.webConfig = webConfigVO2DTO(res.data)
  })
}

const initWebConfig = () => {
  store.webConfig = initWebConfigDTO()
}

onMounted(async () => {
  const id = Number(route.params.id) || 0
  // 判断是否有未完成的任务
  if (store.webConfig.isUpdated) {
    let text = '有未完成的任务是否继续上次的任务？'
    const fileUrls: string[] = []
    fileUrls.push(...store.webConfig.authorUrls)
    fileUrls.push(...store.webConfig.siteUrls)
    if (fileUrls.length > 0) {
      text += '取消将会删除文件：' + fileUrls.join(',')
    }
    await useGlobalDialog(text).then((res: boolean) => {
      // 如果用户选择取消
      if (!res) {
        if (fileUrls.length > 0) {
          delMinioFilesApi(fileUrls)
        }
        id > 0 ? getWebConfig(id) : initWebConfig()
      }
    })
  } else {
    id > 0 ? getWebConfig(id) : initWebConfig()
  }
})
</script>

<template>
  <el-container class="overflow-y-auto h-full" direction="vertical">
    <el-container class="gap-5" direction="vertical">
      <el-form ref="formRef" :model="store.webConfig" :rules="rules" class="flex flex-col gap-5">
        <el-form-item label="配置名" prop="name">
          <el-input
            v-model="store.webConfig.name"
            :maxlength="16"
            placeholder="请输入 （max 16）"
          />
        </el-form-item>
      </el-form>
      <admin-edit-site />
      <admin-edit-author class="mt-5" />
      <el-container class="mb-5 gap-10">
        <el-button type="info">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </el-container>
    </el-container>
  </el-container>
</template>
