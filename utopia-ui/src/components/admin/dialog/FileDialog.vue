<script lang="ts" setup>
import { useGlobalDialog } from '@/hooks'
import { addFileApi, type FileDTO, updateFileApi } from '@/request/api/file'
import { useSystemStore } from '@/stores/system'
import { ClassifyEnum, TableEnum } from '@/enum'
import { addToFormData } from '@/utils'
import type { UploadType } from '@/components/editor/UploadImg.vue'

const systemStore = useSystemStore()

const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

const form = defineModel<FileDTO>('form', {
  default: {}
})

const formRef = ref<any>()

const upload = ref<UploadType>({
  files: new FormData(),
  urls: [],
  initUpload: () => {},
  echoFiles(urls) {}
})

const rules = {
  name: [{ required: true, message: '文件名不能为空', trigger: 'blur' }],
  labelId: [{ required: true, message: '标签不能为空', trigger: 'blur' }],
  tags: [{ required: true, message: '标记不能为空', trigger: 'blur' }]
}

/** 提交按钮 */
const submitForm = () => {
  if (upload.value.isEmpty?.()) {
    ElMessage.warning('请选择文件！')
    return
  }
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    if (form.value.id != undefined) {
      await useGlobalDialog('确定修改 ' + form.value.name + ' ?').then((res: boolean) => {
        if (res) {
          const formData = addToFormData(upload.value.files, form.value)
          updateFileApi(formData).then(() => {
            systemStore.dialog.file = false
            refresh()
          })
        }
      })
    } else {
      const formData = addToFormData(upload.value.files, form.value)
      addFileApi(formData).then(() => {
        upload.value.initUpload?.()
        systemStore.dialog.file = false
        refresh()
      })
    }
  })
}

const color = ['昼', '夜', '红色', '橙色', '黄色', '绿色', '青色', '蓝色', '紫色', '粉色']
const season = ['春季', '夏季', '秋季', '冬季']
const label = ['女性', '男性', '影视', '游戏', '科技', '自然', '动物']
const mood = [
  '快乐',
  '乐观',
  '治愈',
  '悲伤',
  '忧郁',
  '孤独',
  '平静',
  '愤怒',
  '失望',
  '惊恐',
  '嫌弃',
  '激动'
]

const checkList = ref<string[]>([])
const tagsChange = () => {
  if (!checkList.value.length) {
    return
  }
  if (checkList.value.length >= 12) {
    ElMessage.warning('最多选择12个标记！')
  }
  form.value.tags = checkList.value.sort()
}

const open = () => {
  checkList.value = form.value.tags ?? []
  if (form.value.url) {
    upload.value.echoFiles?.([form.value.url])
  }
}
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template
    :attribute="TableEnum.FILE"
    class="p-5"
    @close="upload.initUpload?.()"
    @open="open"
  >
    <template #default>
      <el-container class="gap-5" direction="vertical">
        <span class="text-center">{{ form.id ? '修改资源' : '添加资源' }}</span>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          class="gap-5 flex flex-col"
          label-position="left"
        >
          <el-form-item label="资源名" prop="name">
            <el-input v-model="form.name" maxlength="32" placeholder="请输入（max 32）" />
          </el-form-item>
          <classify-label-form v-model:label-id="form.labelId" :classify-key="ClassifyEnum.FILE" />
          <el-form-item label="文件">
            <upload-img v-model:upload="upload" :limit="100" />
          </el-form-item>
          <el-form-item label="标记" prop="tags">
            <el-checkbox-group
              v-model="checkList"
              :max="12"
              class="flex flex-col"
              @change="tagsChange"
            >
              <el-container class="gap-3">
                <el-checkbox v-for="p in season" :key="p" :label="p" :value="p" />
              </el-container>
              <el-container class="gap-3">
                <el-checkbox v-for="p in mood" :key="p" :label="p" :value="p" />
              </el-container>
              <el-container class="gap-3">
                <el-checkbox v-for="p in label" :key="p" :label="p" :value="p" />
              </el-container>
              <el-container class="gap-3">
                <el-checkbox v-for="p in color" :key="p" :label="p" :value="p" />
              </el-container>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
        <el-container class="justify-between">
          <el-button type="info" @click="systemStore.dialog.file = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </el-container>
      </el-container>
    </template>
  </dialog-template>
</template>
