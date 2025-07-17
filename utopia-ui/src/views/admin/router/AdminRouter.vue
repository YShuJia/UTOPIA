<script lang="ts" setup>
import {
  delRouterApi,
  pageRouterAdminApi,
  type RouterVO,
  updateRouterApi
} from '@/request/api/router'
import { TableEnum } from '@/enum'
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
    label: '排序',
    prop: 'sort',
    sortable: true
  },
  {
    label: '权限',
    prop: 'roleName'
  },
  {
    label: 'name',
    prop: 'name'
  },
  {
    label: '前缀',
    prop: 'icon',
    dataType: {
      component: 'svg'
    }
  },
  {
    label: '类型',
    prop: 'type',
    dataType: {
      component: 'tag',
      labels: new Map([
        ['M', '菜单'],
        ['B', '按钮'],
        ['H', '内链']
      ]),
      types: new Map([
        ['M', 'success'],
        ['B', 'warning'],
        ['H', 'info']
      ])
    }
  },
  {
    label: '外链',
    prop: 'frame',
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
    label: '位置',
    prop: 'admin',
    dataType: {
      component: 'tag',
      labels: new Map([
        [true, '后台'],
        [false, '前台']
      ]),
      types: new Map([
        [true, 'info'],
        [false, 'success']
      ])
    }
  },
  {
    label: '路径',
    prop: 'path'
  },
  {
    label: '组件路径',
    prop: 'component'
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
    :delete-api="delRouterApi"
    :page-size="100"
    :select-api="pageRouterAdminApi"
    :table="TableEnum.ROUTER"
    :update-api="updateRouterApi"
    :vo-to-d-t-o="
      (row: RouterVO) => {
        return row
      }
    "
  />
</template>

<style lang="scss"></style>
