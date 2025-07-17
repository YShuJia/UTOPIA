<script lang="ts" setup>
import { ClassifyEnum, TableEnum } from '@/enum'
import {
  albumVO2DTO,
  delAlbumApi,
  pageAlbumAdminApi,
  updateAlbumApi,
  updateAlbumReviewedApi
} from '@/request/api/album'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'

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
    label: '照片',
    prop: 'urls',
    dataType: { component: 'img', type: 'array' }
  },
  {
    label: '标签名',
    prop: 'labelName'
  },
  {
    label: '简介',
    prop: 'introduction',
    minWidth: 300,
    overflowTooltip: true
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
    label: '创建人',
    prop: 'createUsername'
  },
  {
    label: '修改人',
    prop: 'updateUsername'
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
    :classify-key="ClassifyEnum.ALBUM"
    :columns="columns"
    :delete-api="delAlbumApi"
    :select-api="pageAlbumAdminApi"
    :table="TableEnum.ALBUM"
    :update-api="updateAlbumApi"
    :update-reviewed-api="updateAlbumReviewedApi"
    :vo-to-d-t-o="albumVO2DTO"
  />
</template>
