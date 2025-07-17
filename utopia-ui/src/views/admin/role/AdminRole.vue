<script lang="ts" setup>
import { TableEnum } from '@/enum'
import { pageRoleAdminApi, roleVO2DOTO, updateRoleApi } from '@/request/api/role'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'

const columns: ColumnsType[] = [
  {
    prop: '',
    type: 'selection',
    fixed: 'left'
  },
  {
    label: '角色名',
    prop: 'name',
    fixed: 'left'
  },
  {
    label: '经验值',
    prop: 'experience',
    sortable: true
  },
  {
    label: '管理员',
    prop: 'admin',
    dataType: {
      component: 'tag',
      labels: new Map([
        [true, '是'],
        [false, '否']
      ]),
      types: new Map([
        [true, 'success'],
        [false, 'info']
      ])
    }
  },
  {
    label: '权限',
    prop: 'permission',
    minWidth: 360,
    overflowTooltip: true
  },
  {
    label: '简介',
    prop: 'introduction',
    minWidth: 360,
    overflowTooltip: true
  },

  {
    label: '启停状态',
    prop: 'enabled',
    dataType: { component: 'switch' }
  },
  {
    label: '创建时间',
    prop: 'createTime'
  },
  {
    label: '操作',
    prop: '',
    fixed: 'right',
    dataType: { component: 'operation', type: 'dialog' }
  }
]
</script>

<template>
  <common-table
    :columns="columns"
    :select-api="pageRoleAdminApi"
    :show-add="false"
    :show-del="false"
    :table="TableEnum.ROLE"
    :update-api="updateRoleApi"
    :vo-to-d-t-o="roleVO2DOTO"
  />
</template>
