<script lang="ts" setup>
import { RouteNameEnum, TableEnum } from '@/enum'
import type { ColumnsType } from '@/components/admin/CommonTable.vue'
import { routerTo } from '@/router'
import {
  delWebConfigApi,
  pageWebConfigAdminApi,
  updateWebConfigApi,
  webConfigVO2DTO
} from '@/request/api/web_config'

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
    label: '作者',
    prop: 'authorName'
  },
  {
    label: '作者头像',
    prop: 'authorAvatar',
    dataType: {
      component: 'img',
      type: 'string'
    }
  },
  {
    label: '网站名',
    prop: 'siteTitle'
  },
  {
    label: '网站默认头像',
    prop: 'siteFavicon',
    dataType: {
      component: 'img',
      type: 'string'
    }
  },
  {
    label: '网站座右铭',
    prop: 'siteMotto'
  },
  {
    label: '网站备案信息',
    prop: 'siteRecord'
  },
  {
    label: '网站运行时间',
    prop: 'siteCreateTime'
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
    dataType: { component: 'operation', type: RouteNameEnum.WEB_CONFIG_EDIT },
    fixed: 'right'
  }
]
</script>

<template>
  <common-table
    :columns="columns"
    :delete-api="delWebConfigApi"
    :select-api="pageWebConfigAdminApi"
    :show-add="false"
    :table="TableEnum.WEB_CONFIG"
    :update-api="updateWebConfigApi"
    :vo-to-d-t-o="webConfigVO2DTO"
  >
    <template #button>
      <el-button
        class="!px-2"
        plain
        type="primary"
        @click="routerTo(RouteNameEnum.WEB_CONFIG_EDIT, { id: -1 })"
      >
        <svg-icon message="新增" name="add" />
      </el-button>
    </template>
  </common-table>
</template>

<style lang="scss"></style>
