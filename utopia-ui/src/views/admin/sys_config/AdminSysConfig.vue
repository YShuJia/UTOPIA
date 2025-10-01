<script lang="ts" setup>
import { TableEnum } from '@/enum'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'
import {
  delSysConfigApi,
  pageSysConfigAdminApi,
  sysConfigVO2DTO,
  updateSysConfigApi
} from '@/request/api/sys_config'

const columns: ColumnsType[] = [
  {
    prop: '',
    type: 'selection',
    fixed: 'left'
  },
  {
    label: '配置名',
    prop: 'name',
    fixed: 'left'
  },
  {
    label: '启停状态',
    prop: 'enabled',
    dataType: { component: 'switch' }
  },
  {
    label: '密码试错次数',
    prop: 'sysPasswordCount'
  },
  {
    label: '试错间隔时间',
    prop: 'sysPasswordTime'
  },
  {
    label: '试错封禁时间',
    prop: 'sysPasswordBan'
  },
  {
    label: '限流次数',
    prop: 'sysLimitCount'
  },
  {
    label: '限流间隔时间',
    prop: 'sysLimitTime'
  },
  {
    label: '限流封禁时间',
    prop: 'sysLimitBan'
  },
  {
    label: '敏感词替换',
    prop: 'sysReplaceChar'
  },
  {
    label: '每天可获取最大经验',
    prop: 'sysMaxExp'
  },
  {
    label: '权限认证配置表',
    prop: 'sysRoleTable'
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
    :columns="columns"
    :delete-api="delSysConfigApi"
    :select-api="pageSysConfigAdminApi"
    :table="TableEnum.SYS_CONFIG"
    :update-api="updateSysConfigApi"
    :vo-to-d-t-o="sysConfigVO2DTO"
  />
</template>

<style lang="scss"></style>
