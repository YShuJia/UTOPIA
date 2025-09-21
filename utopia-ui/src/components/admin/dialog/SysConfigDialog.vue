<script lang="ts" setup>
import { useGlobalDialog } from '@/hooks'
import { TableEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'
import {
  addSysConfigApi,
  listTableAdminApi,
  type SysConfigDTO,
  updateSysConfigApi
} from '@/request/api/sys_config'
import type { ResultType } from '@/request/config'

const systemStore = useSystemStore()

const formRef = ref<any>()

const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

// 表单
const form = defineModel<SysConfigDTO>('form', {
  default: {}
})

const rules = {
  name: [{ required: true, message: '昵称不能为空', trigger: 'blur' }],
  sysReplaceChar: [{ required: true, message: '角色不能为空', trigger: 'blur' }],
  sysMaxExp: [{ required: true, message: '最大经验不能为空', trigger: 'blur' }],
  sysRoleTable: [{ required: true, message: '角色权限表不能为空', trigger: 'blur' }],
  sysPasswordCount: [{ required: true, message: '试错次数不能为空', trigger: 'blur' }],
  sysPasswordTime: [{ required: true, message: '试错间隔时间不能为空', trigger: 'blur' }],
  sysPasswordBan: [{ required: true, message: '封禁账号、IP时间不能为空', trigger: 'blur' }],
  sysLimitCount: [{ required: true, message: '限流次数不能为空', trigger: 'blur' }],
  sysLimitTime: [{ required: true, message: '限流间隔时间不能为空', trigger: 'blur' }],
  sysLimitBan: [{ required: true, message: '限流封禁IP时间不能为空', trigger: 'blur' }],
  enabled: [{ required: true, message: '是否启用不能为空', trigger: 'blur' }]
}

/** 提交按钮 */
const submitForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    form.value.sysRoleTable?.sort()
    if (form.value.id != undefined) {
      await useGlobalDialog('确定修改 ID 为"' + form.value.id + '"的用户?').then((res: boolean) => {
        if (res) {
          updateSysConfigApi(form.value).then(() => {
            systemStore.dialog.sys_config = false
            refresh()
          })
        }
      })
    } else {
      await addSysConfigApi(form.value).then(() => {
        systemStore.dialog.sys_config = false
        refresh()
      })
    }
  })
}

const tableList = ref<string[]>([])
const getTableList = () => {
  listTableAdminApi().then((res: ResultType<string[]>) => {
    tableList.value = res.data
  })
}
onMounted(() => {
  getTableList()
})
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template
    :attribute="TableEnum.SYS_CONFIG"
    :title="form.id ? '修改用户' : '新增用户'"
    class="p-5"
  >
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-container class="gap-5 flex-wrap">
        <el-form-item label="配置名" prop="name">
          <el-input v-model="form.name" :maxlength="16" placeholder="请输入 （max 16）" />
        </el-form-item>
        <el-form-item label="敏感词替换符" prop="sysReplaceChar">
          <el-input v-model="form.sysReplaceChar" :maxlength="8" placeholder="请输入 （max 8）" />
        </el-form-item>
        <el-form-item label="每天可获取的最大经验值" prop="sysMaxExp">
          <el-input-number v-model="form.sysMaxExp" :max="65535" :min="1" placeholder="" />
        </el-form-item>
      </el-container>
      <el-container class="gap-5 flex-wrap">
        <el-form-item label="密码试错次数" prop="sysPasswordCount">
          <el-input-number v-model="form.sysPasswordCount" :max="255" :min="1" placeholder="" />
        </el-form-item>
        <el-form-item label="密码试错间隔时间 h" prop="sysPasswordTime">
          <el-input-number v-model="form.sysPasswordTime" :max="255" :min="1" placeholder="" />
        </el-form-item>
        <el-form-item label="密码试错封禁账号、IP时间 h" prop="sysPasswordBan">
          <el-input-number v-model="form.sysPasswordBan" :max="255" :min="1" placeholder="" />
        </el-form-item>
      </el-container>
      <el-container class="gap-5 flex-wrap">
        <el-form-item label="触发接口限流次数" prop="sysLimitCount">
          <el-input-number v-model="form.sysLimitCount" :max="255" :min="1" placeholder="" />
        </el-form-item>
        <el-form-item label="触发接口限流间隔时间 h" prop="sysLimitTime">
          <el-input-number v-model="form.sysLimitTime" :max="255" :min="1" placeholder="" />
        </el-form-item>
        <el-form-item label="触发限流封禁IP时间 h" prop="sysLimitBan">
          <el-input-number v-model="form.sysLimitBan" :max="255" :min="1" placeholder="" />
        </el-form-item>
      </el-container>
      <el-form-item label="生成角色权限的表配置" prop="sysRoleTable">
        <el-checkbox-group v-model="form.sysRoleTable">
          <el-checkbox v-for="t in tableList" :key="t" :label="t" :value="t" />
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <el-container class="justify-between mt-5">
      <el-button type="info" @click="systemStore.dialog.sys_config = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </dialog-template>
</template>
