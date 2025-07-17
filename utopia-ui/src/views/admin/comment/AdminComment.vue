<script lang="ts" setup>
import { useGlobalDialog } from '@/hooks'
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import { useSystemStore } from '@/stores/system'
import {
  type CommentDTO,
  type CommentVO,
  delCommentApi,
  pageCommentAdminApi
} from '@/request/api/comment'
import { useStyleStore } from '@/stores/style'

const systemStore = useSystemStore()
const styleStore = useStyleStore()
// 多选框 id
const ids = ref<number[]>([])
/** 查询列表 */
const vo = ref<PageVO<CommentVO>>(initPageVO())

const dto = ref<CommentDTO>({
  pageNum: 1,
  pageSize: 10
})

const getList = () => {
  pageCommentAdminApi(dto.value).then((res: ResultType<PageVO<CommentVO>>) => {
    vo.value = res.data
  })
}
// 多选框选中数据
const handleSelectionChange = (selection: CommentVO[]) => {
  ids.value = selection.map((item) => item.id)
}

/** 删除按钮操作 */
const handleDelete = async () => {
  await useGlobalDialog(
    '确定删除 ID 为 "' + ids.value + '" 的评论?（注：楼层被删除，其下的评论也会删除）'
  ).then((res: boolean) => {
    if (res) {
      delCommentApi(ids.value).then((res: ResultType<boolean>) => {
        if (res.data) {
          getList()
        }
      })
    }
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <el-container class="gap-5" direction="vertical">
    <el-container>
      <el-button
        :disabled="ids.length === 0"
        class="!px-2"
        plain
        type="danger"
        @click="handleDelete"
      >
        <svg-icon message="删除" name="delete" />
      </el-button>
    </el-container>

    <el-table
      :data="vo.list"
      :tooltip-effect="styleStore.background.bgType"
      border
      height="100%"
      row-key="id"
      table-layout="auto"
      @selection-change="handleSelectionChange"
    >
      <el-table-column fixed="left" type="selection" />
      <el-table-column fixed="left" label="ID" prop="id" />
      <el-table-column label="源ID" prop="sourceId" />
      <el-table-column label="评论内容" min-width="300" prop="content" show-overflow-tooltip>
        <template #default="scope">
          <el-container class="gap-1 items-center justify-center" direction="vertical">
            <template v-if="scope.row.content[0]">
              <span>{{ scope.row.content[0] }}</span>
            </template>
            <template v-if="scope.row.content[1]">
              <image-box
                :src="scope.row.content[1]"
                class="h-16 w-fit radius-sm"
                @click="systemStore.preview.open(scope.row.content[1])"
              />
            </template>
          </el-container>
        </template>
      </el-table-column>
      <el-table-column label="用户" prop="username" />
      <el-table-column label="父用户" prop="parentUsername" />
      <el-table-column label="点赞量" prop="likeCount" />
      <el-table-column label="创建时间" prop="createTime" />
    </el-table>
    <page-box v-model:dto="dto" :change="getList" :total="vo.total" />
  </el-container>
</template>
