<script lang="ts" setup>
import { ClassifyEnum, TableEnum } from '@/enum'
import {
  delWebsiteApi,
  pageWebsiteAdminApi,
  updateWebsiteApi,
  updateWebsiteReviewedApi,
  websiteVO2DTO
} from '@/request/api/website'
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
    label: '头像',
    prop: 'avatar',
    dataType: { component: 'img', type: 'string' }
  },
  {
    label: '封面',
    prop: 'cover',
    minWidth: 120,
    dataType: { component: 'img', type: 'string' }
  },
  {
    label: '地址',
    prop: 'url'
  },
  {
    label: '标签名',
    prop: 'labelName'
  },
  {
    label: '简介',
    prop: 'introduction',
    minWidth: 500,
    overflowTooltip: true
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
    :classify-key="ClassifyEnum.WEBSITE"
    :columns="columns"
    :delete-api="delWebsiteApi"
    :select-api="pageWebsiteAdminApi"
    :table="TableEnum.WEBSITE"
    :update-api="updateWebsiteApi"
    :update-reviewed-api="updateWebsiteReviewedApi"
    :vo-to-d-t-o="websiteVO2DTO"
  />
</template>

<style lang="scss"></style>
