<script lang="ts" setup>
import { useStyleStore } from '@/stores/style'
import {
  addArticleApi,
  type AdminArticleVO,
  articleVO2DTO,
  getArticleAdminApi,
  updateArticleApi,
  uploadArticleFileApi
} from '@/request/api/article'
import type { ResultType } from '@/request/config'
import { DropdownToolbar, MdEditor, MdModal, type ToolbarNames } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { useGlobalDialog, useGlobalNotification } from '@/hooks'
import { ClassifyEnum } from '@/enum'
import { hasOneNoEmpty, isEmpty } from '@/utils'
import { useSystemStore } from '@/stores/system'
import { useTemporaryStore } from '@/stores/temporary'
import { delMinioFilesApi } from '@/request/api'
import type { UploadFile, UploadProps } from 'element-plus'
import { extractFrameFromVideo } from '@/utils/fileUtils'

const temporaryStore = useTemporaryStore()
const systemStore = useSystemStore()

const route = useRoute()
const formRef = ref<any>()
const styleStore = useStyleStore()

const toolbars: ToolbarNames[] = [
  'bold',
  'underline',
  'italic',
  'strikeThrough',
  'title',
  'sub',
  'sup',
  'quote',
  'unorderedList',
  'orderedList',
  'task',
  'codeRow',
  'code',
  'link',
  'image',
  0,
  'table',
  'mermaid',
  'katex',
  '=',
  'save',
  'prettier',
  'pageFullscreen',
  'preview',
  'htmlPreview',
  'catalog'
]

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

const onUploadImg = async (files: File[], callback: (url: string[]) => void) => {
  const formData = new FormData()
  for (let i = 0; i < files.length; i++) {
    formData.append('files', files[i])
  }
  const { data } = await uploadArticleFileApi(formData)
  if (!isEmpty(data)) {
    temporaryStore.article.urls?.push(...data)
  }
  // 将文件路径返回给组件
  // callback(data.map((url) => url))
  data.map((url) => {
    insert(`<img width='60%' src='${url}' alt=''/>`)
  })
}

const onUploadVideo: UploadProps['onChange'] = async (
  uploadFile: UploadFile,
  uploadFiles: UploadFile[]
) => {
  // 创建视频封面
  let poster
  try {
    poster = await extractFrameFromVideo(uploadFile.raw!)
    const posterFormData = new FormData()
    posterFormData.append('files', poster)
    // 上传视频
    const formData = new FormData()
    formData.append('files', uploadFile.raw!)
    const results: Awaited<ResultType<string[]>>[] = await Promise.all([
      uploadArticleFileApi(formData),
      uploadArticleFileApi(posterFormData)
    ])
    console.log('上传成功', results)
    temporaryStore.article.urls = temporaryStore.article.urls ?? []
    temporaryStore.article.urls.push(...results[0].data)
    temporaryStore.article.urls.push(...results[1].data)
    /* poster='poster' 占位 提交时替换为封面图片 */
    insert(
      `<video width='100%' controls src='${results[0].data[0]}' poster='${results[1].data[0]}'></video>`
    )
  } catch (error: any) {
    useGlobalNotification({ message: error, type: 'error' })
  }
}

const editorRef = ref<any>()
onMounted(async () => {
  const id = Number(route.params.id) || 0
  // 判断是否有未完成的任务
  if (hasOneNoEmpty(temporaryStore.article, ['pageSize', 'pageNum', 'orderByColumns'])) {
    let text = '有未完成的任务是否继续上次的任务？'
    const hasFiles = temporaryStore.article.urls && temporaryStore.article.urls.length > 0
    if (hasFiles) {
      text += '取消将会删除文件：' + temporaryStore.article.urls?.join(',')
    }
    await useGlobalDialog(text).then((res: boolean) => {
      // 如果用户选择取消
      if (!res) {
        if (hasFiles) {
          delMinioFilesApi(temporaryStore.article.urls ?? [])
        }
        id > 0 ? getArticle(id) : temporaryStore.initArticle()
      }
    })
  } else {
    id > 0 ? getArticle(id) : temporaryStore.initArticle()
  }
})

// 视频下拉框显示
const visible = ref(false)
const onChange = (_visible: boolean) => {
  visible.value = _visible
}
const insert = (content: string) => {
  editorRef.value?.insert(() => {
    return {
      targetValue: content,
      select: true,
      deviationStart: 0,
      deviationEnd: 0
    }
  })
}

// 插入视频链接弹窗
const mVisible = ref(false)
// 以链接形式插入视频
const posterUrl = ref<string>('')
const videoUrl = ref<string>('')
const videoUrlHandler = () => {
  if (videoUrl.value == '' || posterUrl.value == '') {
    useGlobalNotification({ message: '请将表单填写完整！', type: 'error' })
    return
  }
  mVisible.value = false
  /* poster='poster' 占位 提交时替换为封面图片 */
  insert(
    `<video width='100%' controls poster='${posterUrl.value}' src='${videoUrl.value}'></video>`
  )
  videoUrl.value = ''
}

/** 提交按钮 */
const submitForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    // insertPoster()
    if (temporaryStore.article.id != undefined) {
      await useGlobalDialog('确定修改 ID 为"' + temporaryStore.article.id + '"的文章?').then(
        (res: boolean) => {
          if (res) {
            temporaryStore.article.updateStatus = false
            updateArticleApi(temporaryStore.article).then(() => {
              temporaryStore.initArticle()
            })
          }
        }
      )
    } else {
      addArticleApi(temporaryStore.article).then((res: ResultType<boolean>) => {
        if (res.data) {
          temporaryStore.initArticle()
        }
      })
    }
  })
}
</script>

<template>
  <div class="size-full overflow-auto">
    <el-button class="max-w-20 mb-5" type="primary" @click="submitForm">提 交</el-button>
    <el-form ref="formRef" :model="temporaryStore.article" :rules="rules" class="size-full">
      <el-container class="flex-wrap gap-5">
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
      </el-container>
      <div class="py-5 overflow-x-auto w-full">
        <el-tooltip :effect="'light'" placement="top">
          <span class="cursor-pointer text-amber-500">文章编辑格式！</span>
          <template #content>
            <el-container class="gap-5" direction="vertical">
              <el-container class="gap-1" direction="vertical">
                <span class="text-sm">图片标记：</span>
                <span class="text-sm">&emsp;&lt;img width='50%' src='' alt='' /></span>
              </el-container>
              <el-container class="gap-1" direction="vertical">
                <span class="text-sm">视频标记：</span>
                <span class="text-sm"
                  >&emsp;&lt;video width='100%' controls poster='poster' src=''>&lt;/video></span
                >
              </el-container>
              <el-container class="gap-1" direction="vertical">
                <span class="text-sm">图文环绕请使用：</span>
                <span class="text-sm">
                  &emsp;&lt;p><br />
                  &emsp;&emsp;&lt;img width='50%' src='' class='img-left or img-right'
                  alt=''>&lt;/img><br />
                  &emsp;&emsp;&lt;span>&lt;/span><br />
                  &emsp;&lt;/p>
                </span>
              </el-container>
            </el-container>
          </template>
        </el-tooltip>
        <md-editor
          ref="editorRef"
          v-model="temporaryStore.article.content"
          :theme="styleStore.background.bgType"
          :toolbars="toolbars"
          class="min-w-fit mt-1 min-h-[640px]"
          input-box-witdh="45%"
          show-toolbar-name
          @uploadImg="onUploadImg"
        >
          <template #defToolbars>
            <dropdown-toolbar :onChange="onChange" :visible="visible" title="video">
              <template #overlay>
                <el-container
                  class="items-center border dark:border-gray-800 rounded overflow-hidden"
                  direction="vertical"
                >
                  <el-upload
                    ref="uploadRef"
                    :auto-upload="false"
                    :on-change="onUploadVideo"
                    :show-file-list="false"
                    accept="video/*"
                    class="size-full hover:bg-gray-100 dark:hover:bg-zinc-900 !h-6"
                    multiple
                  >
                    <a class="text-xs flex size-full px-1 items-start">上传视频</a>
                  </el-upload>
                  <a
                    class="text-xs w-full flex items-center !h-6 justify-center hover:bg-gray-100 dark:hover:bg-zinc-900"
                    @click="mVisible = true"
                    >插入视频
                  </a>
                </el-container>
              </template>
              <template #default>
                <el-container class="items-center justify-center pt-1 pb-0.5" direction="vertical">
                  <svg
                    class="icon"
                    height="22"
                    p-id="5733"
                    t="1752590348358"
                    version="1.1"
                    viewBox="0 0 1024 1024"
                    width="22"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      d="M740 210q1 4 5 6t8 1l90-27q5-2 12-2 17 0 29.5 12.5T897 231v300q0 6-2 12-5 17-20.5 25.5T843 572l-89-27h-3q-5 0-8.5 3.5T739 557q0 28-14 52t-38 38q-24 14-52 14H232q-29 0-53-14t-38-38q-14-24-14-52V207q0-28 14-52t38-38q24-14 53-14h403q28 0 52 14t38 38q14 24 14 52l1 3zM349 474q0 12 7 23 9 15 26 18.5t32-5.5l144-91q8-5 13-13 9-15 5-32t-18-26l-144-91q-10-7-22-7-18 0-30.5 12.5T349 293v181z"
                      fill="#666666"
                      p-id="5734"
                    ></path>
                  </svg>
                  <span class="text-xs text-nowrap">视频</span>
                </el-container>
                <md-modal :visible="mVisible" title="添加视频" @onClose="() => (mVisible = false)">
                  <el-form
                    ref="formRef"
                    :model="temporaryStore.article"
                    class="flex flex-col gap-5"
                  >
                    <el-input v-model="posterUrl" placeholder="请输入视频封面链接" />
                    <el-input v-model="videoUrl" placeholder="请输入视频链接" />
                    <a
                      class="flex text-sm text-color-gray hover:text-gray-950 hover:dark:text-white size-full border radius-sm p-[5px] justify-center"
                      @click="videoUrlHandler"
                    >
                      确定
                    </a>
                  </el-form>
                </md-modal>
              </template>
            </dropdown-toolbar>
          </template>
        </md-editor>
      </div>
    </el-form>
  </div>

  <file-form v-model:url="temporaryStore.article.cover" type="wall" />
</template>

<style lang="scss" scoped></style>
