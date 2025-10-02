<script lang="ts" setup>
import {
  addArticleApi,
  type AdminArticleVO,
  articleVO2DTO,
  getArticleAdminApi,
  updateArticleApi,
  uploadArticleFileApi
} from '@/request/api/article'
import type { ResultType } from '@/request/config'
import 'md-editor-v3/lib/style.css'
import { useGlobalDialog } from '@/hooks'
import { ClassifyEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'
import { useTemporaryStore } from '@/stores/temporary'
import { delMinioFilesApi } from '@/request/api'

const temporaryStore = useTemporaryStore()
const systemStore = useSystemStore()

const route = useRoute()
const formRef = ref<any>()

const rules = {
  title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
  labelId: [{ required: true, message: '标签不能为空', trigger: 'blur' }],
  cover: [{ required: true, message: '封面不能为空', trigger: 'blur' }],
  content: [{ required: true, message: '内容不能为空', trigger: 'blur' }],
  hasVideo: [{ required: true, message: '不能为空', trigger: 'blur' }],
  isComment: [{ required: true, message: '不能为空', trigger: 'blur' }],
  recommend: [{ required: true, message: '不能为空', trigger: 'blur' }],
  enabled: [{ required: true, message: '状态不能为空', trigger: 'blur' }]
}

// 获取文章
const getArticle = (id: number) => {
  getArticleAdminApi(id).then((res: ResultType<AdminArticleVO>) => {
    temporaryStore.article = articleVO2DTO(res.data)
  })
}

const initArticle = () => {
  temporaryStore.article = { urls: [], isUpdated: false }
}

onMounted(async () => {
  const id = Number(route.params.id) || 0
  // 判断是否有未完成的任务
  if (temporaryStore.article.isUpdated) {
    let text = '有未完成的任务是否继续上次的任务？'
    if (temporaryStore.article.urls.length > 0) {
      text += '取消将会删除文件：' + temporaryStore.article.urls?.join(',')
    }
    await useGlobalDialog(text).then((res: boolean) => {
      // 如果用户选择取消
      if (!res) {
        if (temporaryStore.article.urls.length > 0) {
          delMinioFilesApi(temporaryStore.article.urls)
        }
        id > 0 ? getArticle(id) : initArticle()
      }
    })
  } else {
    id > 0 ? getArticle(id) : initArticle()
  }
})

/** 提交按钮 */
const submitForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    if (temporaryStore.article.id != undefined) {
      await useGlobalDialog('确定修改 ID 为"' + temporaryStore.article.id + '"的文章?').then(
        (res: boolean) => {
          if (res) {
            temporaryStore.article.updateStatus = false
            updateArticleApi(temporaryStore.article).then(() => {
              initArticle()
            })
          }
        }
      )
    } else {
      addArticleApi(temporaryStore.article).then((res: ResultType<boolean>) => {
        if (res.data) {
          initArticle()
        }
      })
    }
  })
}
</script>

<template>
  <div class="size-full overflow-auto">
    <el-affix :offset="80">
      <el-button class="mb-5" type="primary" @click="submitForm">提 交</el-button>
    </el-affix>
    <el-form
      ref="formRef"
      :model="temporaryStore.article"
      :rules="rules"
      class="size-full flex flex-col gap-5"
    >
      <el-form-item label="封面" prop="cover">
        <el-button
          v-if="temporaryStore.article.cover == undefined"
          @click="systemStore.dialog.media_form = true"
        >
          选择封面
        </el-button>
        <el-image
          v-else
          :src="temporaryStore.article.cover"
          class="cursor-pointer max-w-40 aspect-video radius-sm"
          @click="systemStore.dialog.media_form = true"
        />
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model="temporaryStore.article.title" placeholder="请输入标题" />
      </el-form-item>
      <classify-label-form
        v-model:label-id="temporaryStore.article.labelId"
        :classifyKey="ClassifyEnum.ARTICLE"
      />
      <el-form-item label="含有视频?" prop="hasVideo">
        <el-radio-group v-model="temporaryStore.article.hasVideo">
          <el-radio :value="false">否</el-radio>
          <el-radio :value="true">是</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="视频链接" prop="videoUrl">
        <el-input
          v-model="temporaryStore.article.videoUrl"
          :disabled="!temporaryStore.article.hasVideo"
          placeholder="请输入视频链接"
        />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="temporaryStore.article.password" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item label="密码提示" prop="passwordTip">
        <el-input v-model="temporaryStore.article.passwordTip" placeholder="请输入密码提示" />
      </el-form-item>
      <el-form-item label="版权所属" prop="copyright">
        <el-input v-model="temporaryStore.article.copyright" />
      </el-form-item>

      <el-form-item label="文章内容">
        <custom-editor
          v-model:text="temporaryStore.article.content"
          v-model:urls="temporaryStore.article.urls"
          :upload="uploadArticleFileApi"
        />
      </el-form-item>
    </el-form>
  </div>

  <file-form v-model:url="temporaryStore.article.cover" type="wall" />
</template>

<style lang="scss" scoped></style>
