<script lang="ts" setup>
import { ClassifyEnum, TableEnum } from '@/enum'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'
import {
  delDiaryApi,
  diaryVO2DTO,
  pageDiaryAdminApi,
  updateDiaryApi,
  updateDiaryReviewedApi
} from '@/request/api/Diary'

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
    label: '内容',
    prop: 'content',
    minWidth: 300,
    overflowTooltip: true
  },
  {
    label: '图片',
    prop: 'urls',
    dataType: { component: 'img', type: 'array' }
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
    dataType: { component: 'operation', type: 'dialog' },
    fixed: 'right'
  }
]
</script>

<template>
  <common-table
    :classify-key="ClassifyEnum.DIARY"
    :columns="columns"
    :delete-api="delDiaryApi"
    :select-api="pageDiaryAdminApi"
    :table="TableEnum.DIARY"
    :update-api="updateDiaryApi"
    :update-reviewed-api="updateDiaryReviewedApi"
    :vo-to-d-t-o="diaryVO2DTO"
  />
</template>
