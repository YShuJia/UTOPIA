<script lang="ts" setup>
import type { CommentDTO } from '@/request/api/comment'
import { type CommentVO, pageCommentApi } from '@/request/api/comment'
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import { useSocketStore } from '@/stores/socket'

const socketStore = useSocketStore()

const { sourceId } = defineProps({
  sourceId: {
    type: Number,
    default: undefined
  }
})

const comment = ref<
  CommentDTO & {
    isReply?: boolean
    parentName?: string
    replayContent?: string[]
  }
>({})

const page = reactive<CommentDTO>({
  pageSize: 10,
  pageNum: 0,
  sourceId: sourceId
})

const vo = reactive<PageVO<CommentVO>>(initPageVO())

const getList = () => {
  page.pageNum = (page.pageNum ?? 1) + 1
  page.sourceId = sourceId
  pageCommentApi(page).then((res: ResultType<PageVO<CommentVO>>) => {
    vo.list.push(...res.data.list)
    vo.isEmpty = res.data.isEmpty
  })
}

onMounted(() => {
  getList()
  // 初始化 评论源 sourceId
  comment.value.sourceId = sourceId
  // 接收 socket 广播的评论 判断是否要加入到当前的评论集合中（由 props 传入的 sourceId 决定）
  socketStore.initCommentSocket((res: CommentVO) => {
    // 情况 1. sourceId === undefined（没传 sourceId），评论的 res.sourceId === res.id 表示广播的评论是留言评论
    // 情况 2. sourceId !== undefined（props传入来源ID），评论的 res.sourceId === sourceId 表示广播的评论是该源的评论
    // 判断是否是当前 sourceId 的评论
    const isTargetSource =
      (sourceId === undefined && res.sourceId === res.id) ||
      (sourceId !== undefined && res.sourceId === sourceId)

    if (!isTargetSource) return

    // 处理楼层主评论，评论的 id === floorId，list为 [] 时用 push, 否则用 unshift
    if (res.id === res.floorId) {
      vo.list.length === 0 ? vo.list.push(res) : vo.list.unshift(res)
      return
    }
    // 处理子评论
    const parentIndex = vo.list.findIndex((item) => item.id === res.floorId)
    if (parentIndex > -1) {
      vo.list[parentIndex]!.children ??= []
      vo.list[parentIndex]!.children.unshift(res)
    }
  })
})
</script>

<template>
  <div class="flex flex-col pt-5 w-full">
    <el-affix :offset="0" class="w-full z-30" position="top">
      <editor v-model:comment="comment" />
    </el-affix>
    <el-container class="p-5" direction="vertical">
      <el-container v-for="item in vo.list" class="pb-5" direction="vertical">
        <comment-box v-model:comment="comment" :item="item" />
        <hr class="w-full h-0.5 mt-5 bg-gray-100" />
      </el-container>
      <loading-more :action="getList" :empty="vo.isEmpty" />
    </el-container>
  </div>
</template>
