<script lang="ts" setup>
import { useStyleStore } from '@/stores/style'
import { DropdownToolbar, MdEditor, MdModal, type ToolbarNames } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { useGlobalNotification } from '@/hooks'
import { isEmpty } from '@/utils'
import type { UploadFile, UploadProps } from 'element-plus'
import { extractFrameFromVideo } from '@/utils/fileUtils'
import type { ResultType } from '@/request/config'
import { useTemporaryStore } from '@/stores/temporary'

const formRef = ref<any>()
const styleStore = useStyleStore()

const toolbars: ToolbarNames[] = [
  0,
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
  1,
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

const editorRef = ref<any>()
// 视频下拉框显示
const visible = ref(false)
const onChange = (_visible: boolean) => {
  visible.value = _visible
}

const { upload } = defineProps({
  upload: {
    type: Function as PropType<(formData: FormData) => Promise<ResultType<string[]>>>,
    required: true
  }
})

const text = defineModel<string>('text', {
  default() {
    return ''
  }
})

const urls = defineModel<string[]>('urls', {
  default() {
    return []
  }
})

const onUploadImg = async (files: File[]) => {
  const formData = new FormData()
  for (const file of files) {
    formData.append('files', file)
  }
  const { data } = await upload(formData)
  if (!isEmpty(data)) {
    urls.value = urls.value ?? []
    urls.value.push(...data)
    console.log(urls.value, useTemporaryStore().article.urls)
  }
  // 将文件路径返回给组件
  // callback(data.map((url) => url))
  data.map((url) => {
    insert(`<img width='60%' src='${url}' alt=''/>`)
  })
}

const onUploadVideo: UploadProps['onChange'] = async (uploadFile: UploadFile) => {
  // 创建视频封面
  let poster
  try {
    poster = await extractFrameFromVideo(uploadFile.raw!)
    const formData = new FormData()
    formData.append('files', poster)
    // 上传视频
    formData.append('files', uploadFile.raw!)
    const results = await upload(formData)
    urls.value = urls.value ?? []
    urls.value.push(...results.data)
    /* poster='poster' 占位 提交时替换为封面图片 */
    insert(
      `<video width='100%' controls poster='${results.data[0]}' src='${results.data[1]}'></video>`
    )
  } catch (error: any) {
    useGlobalNotification({ message: error, type: 'error' })
  }
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
</script>

<template>
  <div class="w-full">
    <md-editor
      ref="editorRef"
      v-model="text"
      :theme="styleStore.background.bgType"
      :toolbars="toolbars"
      class="min-w-fit mt-1 min-h-[640px]"
      input-box-witdh="45%"
      show-toolbar-name
      @uploadImg="onUploadImg"
    >
      <template #defToolbars>
        <dropdown-toolbar title="remark">
          <template #default>
            <div class="block size-6 overflow-hidden pb-1">?</div>
            <el-tooltip :effect="'light'" placement="top">
              <span class="cursor-pointer text-xs text-nowrap">说明</span>
              <template #content>
                <el-container class="gap-5" direction="vertical">
                  <el-container class="gap-1" direction="vertical">
                    <span class="text-sm">图片标记：</span>
                    <span class="text-sm">&emsp;&lt;img width='50%' src='' alt='' /></span>
                  </el-container>
                  <el-container class="gap-1" direction="vertical">
                    <span class="text-sm">视频标记：</span>
                    <span class="text-sm"
                      >&emsp;&lt;video width='100%' controls poster='poster'
                      src=''>&lt;/video></span
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
          </template>
        </dropdown-toolbar>
        <dropdown-toolbar :onChange="onChange" :visible="visible" title="video">
          <template #default>
            <svg
              height="24"
              p-id="5733"
              t="1752590348358"
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
            <div class="text-xs text-nowrap">视频</div>
            <md-modal :visible="mVisible" title="添加视频" @onClose="() => (mVisible = false)">
              <el-form ref="formRef" class="flex flex-col gap-5">
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
                class="size-full hover:bg-gray-100 dark:hover:bg-zinc-900"
                multiple
              >
                <a class="text-xs flex h-6! px-1 items-center justify-center">上传视频</a>
              </el-upload>
              <a
                class="text-xs w-full flex items-center h-6! justify-center hover:bg-gray-100 dark:hover:bg-zinc-900"
                @click="mVisible = true"
                >插入视频
              </a>
            </el-container>
          </template>
        </dropdown-toolbar>
      </template>
    </md-editor>
  </div>
</template>

<style lang="scss" scoped></style>
