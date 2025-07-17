<script lang="ts" setup>
import {
  delLabelApi,
  labelVO2DTO,
  pageLabelAdminApi,
  updateLabelApi,
  updateLabelReviewedApi
} from '@/request/api/label'
import { ClassifyEnum, TableEnum } from '@/enum'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'

const columns: ColumnsType[] = [
  {
    prop: '',
    type: 'selection',
    fixed: 'left'
  },
  {
    label: '标签名',
    prop: 'name',
    fixed: 'left'
  },
  {
    label: '封面',
    prop: 'cover',
    dataType: { component: 'img', type: 'string' }
  },
  {
    label: '类别名',
    prop: 'classifyName'
  },
  {
    label: '简介',
    prop: 'introduction',
    minWidth: 560,
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
    :classify-key="ClassifyEnum.NULL"
    :columns="columns"
    :delete-api="delLabelApi"
    :select-api="pageLabelAdminApi"
    :table="TableEnum.LABEL"
    :update-api="updateLabelApi"
    :update-reviewed-api="updateLabelReviewedApi"
    :vo-to-d-t-o="labelVO2DTO"
  />
</template>
