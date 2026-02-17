<script lang="ts" setup>
import 'md-editor-v3/lib/style.css'
import { addCommentApi, type CommentDTO } from '@/request/api/comment'
import type { ResultType } from '@/request/config'
import { addToFormData } from '@/utils'
import type { UploadType } from '@/components/editor/UploadImg.vue'

// 是否是文字输入
let isTextarea = ref<boolean>(true)
const commentList = ref<string[]>([''])

const comment = defineModel<
  CommentDTO & {
    isReply?: boolean
    parentName?: string
    replayContent?: string[]
  }
>('comment', {
  default: {}
})

const upload = ref<UploadType>({
  files: new FormData()
})

const initComment = () => {
  // 重置评论数据, 保留 sourceId
  commentList.value = ['']
  comment.value.content = ['']
  comment.value.isReply = false
  comment.value.parentName = undefined
  comment.value.floorId = undefined
  comment.value.userId = undefined
  comment.value.parentUserId = undefined
  comment.value.replayContent = undefined
  upload.value.initUpload?.()
}

const addComment = () => {
  if (!upload.value.files.get('files') && commentList.value[0] === '') {
    ElMessage.warning('请输入评论内容')
    return
  }
  // 去除首尾字符
  commentList.value[0] = commentList.value![0]!.trim()
  // 添加评论数据
  comment.value.content = commentList.value
  const formData = addToFormData(upload.value.files, comment.value)
  addCommentApi(formData)
    .then((res: ResultType<boolean>) => {
      initComment()
      isTextarea.value = true
    })
    .catch(() => {
      initComment()
    })
}
</script>

<template>
  <el-container class="px-5 py-2 gap-2 bg-color" direction="vertical">
    <span class="flex items-center">
      <svg-icon class="text-amber-500!" message="评 论" name="edit" size="lg" />
    </span>
    <el-container
      class="relative h-30 border-2 border-dotted p-2 border-amber-500 overflow-hidden"
      direction="vertical"
    >
      <textarea
        v-model="commentList[0]"
        :class="
          'bg-transparent resize-none h-full hidden-scroll size-full duration-300 ' +
          (isTextarea ? '' : ' -translate-x-full')
        "
        :maxlength="128"
        :placeholder="
          comment.isReply ? '@' + comment.parentName + '：' + comment.replayContent : '非礼勿言...'
        "
      />
      <el-container
        :class="'duration-300 absolute ' + (isTextarea ? '-translate-x-[120%]' : 'translate-x-0')"
      >
        <upload-img v-model:upload="upload" :limit="1" />
      </el-container>
    </el-container>
    <span class="flex h-fit items-center justify-between bottom-0">
      <span class="flex items-center gap-3">
        <emoji @get-emoji="(emoji: string) => (commentList[0] += emoji)" />
        <a href="javascript:" @click="isTextarea = !isTextarea">
          <svg-icon name="picture" size="2xl" />
        </a>
      </span>
      <span class="flex items-center gap-3 w-fit">
        <svg-button message="清除" name="delete" size="sm" @click="initComment" />
        <svg-button size="sm" @click="addComment" />
      </span>
    </span>
  </el-container>
</template>
