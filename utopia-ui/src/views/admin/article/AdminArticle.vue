<script lang="ts" setup>
import {
  articleVO2DTO,
  delArticleApi,
  pageArticleAdminApi,
  updateArticleApi,
  updateArticleReviewedApi
} from '@/request/api/article'
import { ClassifyEnum, RouteNameEnum, TableEnum } from '@/enum'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'
import { routerTo } from '@/router'

const columns: ColumnsType[] = [
  {
    prop: '',
    type: 'selection',
    fixed: 'left'
  },
  {
    label: '标题',
    prop: 'title',
    fixed: 'left'
  },
  {
    label: '图片',
    prop: 'cover',
    dataType: { component: 'img', type: 'string' }
  },
  {
    label: '标签',
    prop: 'labelName'
  },
  {
    label: '密码提示',
    prop: 'passwordTip'
  },
  {
    label: '评论',
    prop: 'commentCount',
    sortable: true
  },
  {
    label: '浏览',
    prop: 'viewCount',
    sortable: true
  },
  {
    label: '点赞',
    prop: 'likeCount',
    sortable: true
  },
  {
    label: '包含视频',
    prop: 'hasVideo',
    dataType: { component: 'switch', activeText: '是', inactiveText: '否' }
  },
  {
    label: '评论状态',
    prop: 'commentable',
    dataType: { component: 'switch' }
  },
  {
    label: '点赞状态',
    prop: 'likeable',
    dataType: { component: 'switch' }
  },
  {
    label: '推荐状态',
    prop: 'recommend',
    dataType: { component: 'switch' }
  },
  {
    label: '启停状态',
    prop: 'enabled',
    dataType: { component: 'switch' }
  },
  {
    label: '审核状态',
    prop: 'reviewed',
    dataType: { component: 'tag' }
  },
  {
    label: '修改人',
    prop: 'updateUsername'
  },
  {
    label: '修改时间',
    prop: 'updateTime'
  },
  {
    label: '创建人',
    prop: 'createUsername'
  },
  {
    label: '创建时间',
    prop: 'createTime'
  },
  {
    label: '操作',
    prop: '',
    dataType: {
      component: 'operation',
      type: RouteNameEnum.ARTICLE_EDIT
    },
    fixed: 'right'
  }
]
</script>

<template>
  <common-table
    :classify-key="ClassifyEnum.ARTICLE"
    :columns="columns"
    :delete-api="delArticleApi"
    :select-api="pageArticleAdminApi"
    :show-add="false"
    :table="TableEnum.ARTICLE"
    :update-api="updateArticleApi"
    :update-reviewed-api="updateArticleReviewedApi"
    :vo-to-d-t-o="articleVO2DTO"
  >
    <template #button>
      <el-button
        class="!px-2"
        plain
        type="primary"
        @click="routerTo(RouteNameEnum.ARTICLE_EDIT, { id: -1 })"
      >
        <svg-icon message="新增" name="add" />
      </el-button>
    </template>
  </common-table>
</template>

<style lang="scss" scoped></style>
