<script lang="ts" setup>
import { type AdminSelectRoleVO, listSelectDataRoleApi } from '@/request/api/role'
import { type ResultType } from '@/request/config'

const roleId = defineModel<number>('roleId', {
  default: 0
})

// 角色数据
const vo = ref<AdminSelectRoleVO[]>([])
const getList = () => {
  listSelectDataRoleApi().then((res: ResultType<AdminSelectRoleVO[]>) => {
    vo.value = res.data
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <el-form-item label="权限" prop="roleId">
    <el-select v-model="roleId">
      <template v-for="i in vo" :key="i.id">
        <el-tooltip :effect="'light'" placement="top">
          <el-option :label="i.name" :value="i.id" />
          <template #content>
            <span class="text-lg">{{ i.permission }}</span>
          </template>
        </el-tooltip>
      </template>
    </el-select>
  </el-form-item>
</template>
<style lang="scss" scoped></style>
