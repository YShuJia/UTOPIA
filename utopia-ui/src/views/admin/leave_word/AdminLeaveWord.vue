<script lang="ts" setup>
import { delLeaveWordApi, pageLeaveWordAdminApi } from '@/request/api/leave_word'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'
import { TableEnum } from '@/enum'

const columns: ColumnsType[] = [
  {
    prop: '',
    type: 'selection'
  },
  {
    label: 'ID',
    prop: 'id'
  },
  {
    label: '头像',
    prop: 'avatar',
    dataType: { component: 'img', type: 'string' }
  },
  {
    label: '留言内容',
    prop: 'content'
  },
  {
    label: 'IP地址',
    prop: 'ip'
  },
  {
    label: '创建时间',
    prop: 'createTime'
  }
]
</script>

<template>
  <common-table
    :columns="columns"
    :delete-api="delLeaveWordApi"
    :select-api="pageLeaveWordAdminApi"
    :table="TableEnum.LEAVE_WORD"
  />
</template>
