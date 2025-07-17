<script lang="ts" setup>
import {
  classifyVO2DTO,
  delClassifyApi,
  pageClassifyApi,
  updateClassifyApi,
  updateClassifyReviewedApi
} from '@/request/api/classify'
import { TableEnum } from '@/enum'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'

const columns: ColumnsType[] = [
  {
    prop: '',
    type: 'selection',
    fixed: 'left'
  },
  {
    label: 'ID',
    prop: 'id',
    fixed: 'left'
  },
  {
    label: '类别名',
    prop: 'name',
    fixed: 'left'
  },
  {
    label: '键',
    prop: 'key'
  },
  {
    label: '简介',
    prop: 'introduction',
    minWidth: 500,
    overflowTooltip: true
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
    :columns="columns"
    :delete-api="delClassifyApi"
    :select-api="pageClassifyApi"
    :table="TableEnum.CLASSIFY"
    :update-api="updateClassifyApi"
    :update-reviewed-api="updateClassifyReviewedApi"
    :vo-to-d-t-o="classifyVO2DTO"
  />
</template>
