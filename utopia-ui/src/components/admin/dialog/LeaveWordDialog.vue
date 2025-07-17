<script lang="ts" setup>
import { TableEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'
import { addLeaveWordApi, type LeaveWordDTO } from '@/request/api/leave_word'

const systemStore = useSystemStore()

// 表单
const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

// 表单
const form = defineModel<LeaveWordDTO>('form', {
  default: {}
})

const formRef = ref<any>()

const rules = {
  content: [{ required: true, message: '留言内容不能为空', trigger: 'blur' }]
}

/** 提交按钮 */
function submitForm() {
  formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      await addLeaveWordApi(form.value).then(() => {
        systemStore.dialog.leave_word = false
        refresh()
      })
    }
  })
}
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template :attribute="TableEnum.LEAVE_WORD" title="新增留言">
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="留言内容" prop="content">
        <el-input v-model="form.content" :maxlength="32" placeholder="请输入（max 32）" />
      </el-form-item>
    </el-form>
    <el-container class="justify-between mt-5">
      <el-button type="info" @click="systemStore.dialog.leave_word = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </dialog-template>
</template>

<style lang="scss"></style>
