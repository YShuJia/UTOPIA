<script lang="ts" setup>
import { useGlobalDialog } from '@/hooks'
import { addLabelApi, type LabelDTO, updateLabelApi } from '@/request/api/label'
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
const form = defineModel<LabelDTO>('form', {
  default: {}
})

const formRef = ref<any>()

const rules = {
  name: [{ required: true, message: '名字不能为空', trigger: 'blur' }],
  cover: [{ required: true, message: '封面不能为空', trigger: 'blur' }],
  classifyId: [{ required: true, message: '类别不能为空', trigger: 'blur' }],
  introduction: [{ required: true, message: '简介不能为空', trigger: 'blur' }]
}

/** 提交按钮 */
function submitForm() {
  formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      if (form.value.id != undefined) {
        await useGlobalDialog('确定修改 ID 为"' + form.value.id + '"的标签?').then(
          (res: boolean) => {
            if (res) {
              updateLabelApi(form.value).then(() => {
                systemStore.dialog.label = false
                refresh()
              })
            }
          }
        )
      } else {
        await addLabelApi(form.value).then(() => {
          systemStore.dialog.label = false
          refresh()
        })
      }
    }
  })
}
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template :attribute="TableEnum.LABEL" :title="form.id ? '修改标签' : '新增标签'">
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" maxlength="16" placeholder="请输入（max 16）" />
      </el-form-item>
      <classify-label-form v-model:classify-id="form.classifyId" :required-label="false" />
      <el-form-item label="封面" prop="cover">
        <el-button v-if="form.cover === undefined" @click="systemStore.dialog.media_form = true">
          选择封面
        </el-button>
        <el-image
          v-else
          :src="form.cover"
          class="cursor-pointer max-w-64 aspect-video radius-sm"
          @click="systemStore.dialog.media_form = true"
        />
        <file-form v-model:url="form.cover" type="wall" />
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
      <el-button type="info" @click="systemStore.dialog.label = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </dialog-template>
</template>
