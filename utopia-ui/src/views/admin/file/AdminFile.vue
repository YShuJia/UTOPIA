<script lang="ts" setup>
import {
  delFileApi,
  fileVO2DTO,
  pageFileAdminApi,
  updateFileApi,
  updateFileReviewedApi
} from '@/request/api/file'
import { ClassifyEnum, TableEnum } from '@/enum'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'

const columns: ColumnsType[] = [
  {
    prop: '',
    type: 'selection',
    fixed: 'left'
  },
  {
    label: '资源名',
    prop: 'name',
    fixed: 'left'
  },
  {
    label: '预览',
    prop: 'url',
    dataType: {
      component: 'img',
      type: 'string'
    }
  },
  {
    label: '标签',
    prop: 'labelName'
  },
  {
    label: '大小KB',
    prop: 'size',
    sortable: true
  },
  {
    label: 'URL',
    prop: 'url'
  },
  {
    label: '标记',
    prop: 'tags'
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
    label: '启停状态',
    prop: 'enabled',
    dataType: { component: 'switch' },
    fixed: 'right'
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
    :classify-key="ClassifyEnum.FILE"
    :columns="columns"
    :delete-api="delFileApi"
    :select-api="pageFileAdminApi"
    :table="TableEnum.FILE"
    :update-api="updateFileApi"
    :update-reviewed-api="updateFileReviewedApi"
    :vo-to-d-t-o="fileVO2DTO"
  />
</template>
