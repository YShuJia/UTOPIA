<script lang="ts" setup>
import { useGlobalDialog } from '@/hooks'
import { type ResultType } from '@/request/config'
import { TableEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'
import { getRoleTablesApi, type RoleDTO, updateRoleApi } from '@/request/api/role'

const systemStore = useSystemStore()

const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

// 表单
const formRef = ref<any>()
const form = defineModel<RoleDTO>('form', {
  default: {}
})

// 选中的表权限表字符
const tableForm = ref<{ [key in string]: string[] }>({})
// 选择 all 权限，则禁用其他权限，记录是否全选 select update delete insert
const selectAllDisableOther = ref<{ [key in string]: boolean }>({})

const rules = {
  name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
  introduction: [{ required: true, message: '简介不能为空', trigger: 'blur' }],
  experience: [{ required: true, message: '经验不能为空', trigger: 'blur' }],
  permission: [{ required: true, message: '权限不能为空', trigger: 'blur' }]
}

const tables = ref<string[]>([])
const getPermissionList = () => {
  getRoleTablesApi().then((res: ResultType<string[]>) => {
    tables.value = res.data
    for (const datum of res.data) {
      selectAllDisableOther.value[datum] = false
    }
  })
}

/** 提交按钮 */
function submitForm() {
  form.value.permission = createPermission()
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    await useGlobalDialog('确定修改 ID 为"' + form.value.id + '"的角色?').then((res: boolean) => {
      if (res) {
        updateRoleApi(form.value).then(() => {
          systemStore.dialog.role = false
          refresh()
        })
      }
    })
  })
}

const createPermission = (): string[] => {
  let permission: string[] = []
  for (const [key, value] of Object.entries(tableForm.value)) {
    if (value.length > 0) {
      permission.push(...value)
    }
  }
  return permission.sort()
}

const initPermission = () => {
  if (form.value.permission === undefined) {
    return
  }
  tableForm.value = {}
  selectAllDisableOther.value = {}
  for (const permissionElement of form.value.permission) {
    const array = permissionElement.split(':')
    const tableKey = array[0]
    // 确保 tableKey 是有效字符串
    if (tableKey == null || tableKey === '') {
      continue
    }
    if (tableForm.value[tableKey] === undefined) {
      tableForm.value[tableKey] = []
    }
    tableForm.value[tableKey].push(tableKey + ':' + array[1])
  }
}

onMounted(() => {
  getPermissionList()
})
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template :attribute="TableEnum.ROLE" title="修改角色" @open="initPermission">
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="昵称" prop="name">
        <el-input v-model="form.name" maxlength="16" placeholder="请输入（max 16）" />
      </el-form-item>
      <el-form-item label="简介" prop="introduction">
        <el-input
          v-model="form.introduction"
          :autosize="{ minRows: 5, maxRows: 5 }"
          maxlength="255"
          placeholder="请输入（max 255）"
          type="textarea"
        />
      </el-form-item>
      <el-container class="flex-wrap gap-5">
        <el-form-item label="经验值" prop="experience">
          <el-input-number
            v-model="form.experience"
            :max="2147483647"
            :min="128"
            class="min-w-52"
          />
        </el-form-item>
      </el-container>
      <el-container v-for="k in tables" direction="vertical">
        <el-form-item :label="`${k} 表权限 `" prop="permission">
          <el-checkbox-group v-model="tableForm[k]">
            <el-checkbox :label="k + ':admin'" :value="k + ':admin'" />
            <el-checkbox :label="k + ':insert'" :value="k + ':insert'" />
            <el-checkbox :label="k + ':delete'" :value="k + ':delete'" />
            <el-checkbox :label="k + ':select'" :value="k + ':select'" />
            <el-checkbox :label="k + ':update'" :value="k + ':update'" />
          </el-checkbox-group>
        </el-form-item>
      </el-container>
    </el-form>
    <el-container class="justify-between mt-5">
      <el-button type="info" @click="systemStore.dialog.role = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </dialog-template>
</template>
