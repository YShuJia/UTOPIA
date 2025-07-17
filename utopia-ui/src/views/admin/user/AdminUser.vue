<script lang="ts" setup>
import { TableEnum } from '@/enum'

import { pageUserAdminApi, updateUserApi, userVO2DTO } from '@/request/api/user'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'

const columns: ColumnsType[] = [
  {
    prop: '',
    type: 'selection',
    fixed: 'left'
  },
  {
    label: '用户名',
    prop: 'username',
    fixed: 'left'
  },
  {
    label: '头像',
    prop: 'avatar',
    dataType: { component: 'img', type: 'string' }
  },
  {
    label: '角色名',
    prop: 'roleName'
  },
  {
    label: '邮箱',
    prop: 'email'
  },
  {
    label: '性别',
    prop: 'gender'
  },
  {
    label: '经验值',
    prop: 'experience'
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
    :select-api="pageUserAdminApi"
    :show-del="false"
    :table="TableEnum.USER"
    :update-api="updateUserApi"
    :vo-to-d-t-o="userVO2DTO"
  />
</template>
