<script lang="ts" setup>
import {
  addRouterApi,
  type AdminSelectRouterVO,
  listSelectDataRouterApi,
  type RouterDTO,
  updateRouterApi
} from '@/request/api/router'
import { useGlobalDialog } from '@/hooks'
import { type ResultType } from '@/request/config'
import { useSystemStore } from '@/stores/system'
import { TableEnum } from '@/enum'

const systemStore = useSystemStore()

const { refresh } = defineProps({
  refresh: {
    type: Function,
    default: () => {}
  }
})

// 表单
const form = defineModel<RouterDTO>('form', {
  default: {}
})

// 表单实列
const formRef = ref<any>()

// UI、Admin 父级路由 做下拉框数据
const uiParents = ref<AdminSelectRouterVO[]>([])
const adminParents = ref<AdminSelectRouterVO[]>([])

const listSelectData = () => {
  listSelectDataRouterApi().then((res: ResultType<AdminSelectRouterVO[]>) => {
    for (let i = 0; i < res.data.length; i++) {
      if (res.data[i].admin) {
        adminParents.value.push(res.data[i])
      } else {
        uiParents.value.push(res.data[i])
      }
    }
  })
}

const rules = {
  title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
  name: [{ required: true, message: 'name不能为空', trigger: 'blur' }],
  sort: [{ required: true, message: '次序不能为空', trigger: 'blur' }],
  admin: [{ required: true, message: '位置不能为空', trigger: 'change' }],
  parentId: [{ required: true, message: '父路由ID不能为空', trigger: 'blur' }],
  frame: [{ required: true, message: '是否外链', trigger: 'blur' }],
  roleId: [{ required: true, message: '角色ID不能为空', trigger: 'blur' }],
  path: [{ required: true, message: '地址不能为空', trigger: 'blur' }],
  enabled: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '类型不能为空', trigger: 'blur' }]
}

// 新增数据层级、类型改变时修改表单值
const valueChange = () => {
  if (form.value.type === 'B') {
    if (form.value.admin) {
      form.value.parentId = adminParents.value[0].id
    } else {
      form.value.parentId = uiParents.value[0].id
    }
  } else {
    form.value.parentId = 0
  }
}

/** 提交按钮 */
const submitForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    if (form.value.id != undefined) {
      await useGlobalDialog('确定修改 ID 为"' + form.value.id + '"的路由?').then((res: boolean) => {
        if (res) {
          updateRouterApi(form.value).then(() => {
            systemStore.dialog.router = false
            refresh()
          })
        }
      })
    } else {
      addRouterApi(form.value).then(() => {
        systemStore.dialog.router = false
        refresh()
      })
    }
  })
}

onMounted(() => {
  listSelectData()
})
</script>

<template>
  <!-- 添加或修改对话框 -->
  <dialog-template
    :attribute="TableEnum.ROUTER"
    :title="form.id ? '修改路由' : '添加路由'"
    class="p-5"
  >
    <template #default>
      <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
        <el-container class="auto-column gap-y-0">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" maxlength="32" placeholder="请输入（max 32）" />
          </el-form-item>
          <role-form v-model:role-id="form.roleId" />
          <el-form-item label="路由Name" prop="name">
            <el-input v-model="form.name" maxlength="16" placeholder="路由 name（max 16）" />
          </el-form-item>
          <el-form-item label="路由类型" prop="type">
            <el-radio-group
              v-model="form.type"
              @change="form.type !== 'B' ? (form.parentId = 0) : (form.parentId = -1)"
            >
              <el-radio value="M">菜单</el-radio>
              <el-radio value="B">按钮</el-radio>
              <el-radio value="H">内链</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="位置" prop="admin">
            <el-radio-group v-model="form.admin" @change="valueChange">
              <el-radio :value="false">UI</el-radio>
              <el-radio :value="true">Admin</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="上级路由" prop="parentId">
            <template v-if="form.admin">
              <el-select v-model="form.parentId" :disabled="form.type !== 'B'">
                <el-option v-for="p in adminParents" :key="p.id" :label="p.title" :value="p.id" />
                <el-option :value="0" label="无" />
              </el-select>
            </template>
            <template v-else>
              <el-select v-model="form.parentId" :disabled="form.type !== 'B'">
                <el-option v-for="p in uiParents" :key="p.id" :label="p.title" :value="p.id" />
                <el-option :value="0" label="无" />
              </el-select>
            </template>
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="form.sort" :max="100" :min="0" class="!w-32" />
          </el-form-item>
          <el-form-item label="外链?" prop="frame">
            <el-radio-group v-model="form.frame">
              <el-radio :value="false">否</el-radio>
              <el-radio :value="true">是</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="路径" prop="path">
            <el-input v-model="form.path" maxlength="64" placeholder="路径（max 64）" />
          </el-form-item>
          <el-form-item label="状态" prop="enabled">
            <el-radio-group v-model="form.enabled">
              <el-radio :value="false">停用</el-radio>
              <el-radio :value="true">启用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-container>
        <SvgForm
          :disable="form.type === 'H'"
          :value="form.icon"
          @getValue="(value: string) => (form.icon = value)"
        />
        <el-form-item label="组件" prop="component">
          <el-input v-model="form.component" maxlength="64" placeholder="组件路径（max 64）" />
        </el-form-item>
      </el-form>
      <el-container class="justify-between mt-5">
        <el-button type="info" @click="systemStore.dialog.router = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </el-container>
    </template>
  </dialog-template>
</template>

<style lang="scss"></style>
