<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { type FileDTO, type FileVO, pageFileSelectAdminApi } from '@/request/api/file'
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import { DialogEnum } from '@/enum'

const systemStore = useSystemStore()

const { type } = defineProps({
  type: {
    type: String as PropType<'wall' | 'avatar'>,
    default: 'wall'
  }
})

const dto = ref<FileDTO>({
  pageSize: 10,
  pageNum: 1
})

// 动态绑定值
const url = defineModel('url')

// 数据分页
const vo = ref<PageVO<FileVO>>(initPageVO())
const getList = () => {
  dto.value.labelId = 10000100002
  if (type === 'avatar') {
    dto.value.labelId = 10000100001
  }
  pageFileSelectAdminApi(dto.value).then((res: ResultType<PageVO<FileVO>>) => {
    vo.value = res.data
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <dialog-template :attribute="DialogEnum.MEDIA_FORM" class="p-5">
    <el-container class="auto-min-column">
      <image-box
        v-for="item in vo.list"
        :key="item.url"
        :class="`radius-lg cursor-pointer shadow ${type === 'wall' ? 'aspect-video' : 'aspect-square'}`"
        :src="item.url"
        @click="
          () => {
            url = item.url
            systemStore.dialog.media_form = false
          }
        "
      />
    </el-container>
    <template #footer>
      <page-box v-model:dto="dto" :change="getList" :total="vo.total" />
    </template>
  </dialog-template>
</template>

<style lang="scss" scoped></style>
