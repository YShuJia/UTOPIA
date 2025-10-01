<script lang="ts" setup>
import {useGlobalDialog} from '@/hooks'
import {useSystemStore} from '@/stores/system'
import {addSysConfigApi, updateSysConfigApi} from '@/request/api/sys_config'
import type {WebConfigDTO} from '@/request/api/web_config'
import type {InputInstance} from 'element-plus'
import {useTemporaryStore} from '@/stores/temporary'
import {initPageVO, type PageVO, type ResultType} from '@/request/config'
import {type FileVO, pageFileSelectAdminApi} from '@/request/api/file'

const store = useTemporaryStore()
const systemStore = useSystemStore()
const formRef = ref<any>()
// 表单
const form = ref<WebConfigDTO>({})

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
    if (form.value.id != undefined) {
      await useGlobalDialog('确定修改 ID 为"' + form.value.id + '"的用户?').then((res: boolean) => {
        if (res) {
          updateSysConfigApi(form.value).then(() => {
            systemStore.dialog.sys_config = false
          })
        }
      })
    } else {
      await addSysConfigApi(form.value).then(() => {
        systemStore.dialog.sys_config = false
      })
    }
  })
}

const tag = ref('')
const inputVisible = ref(false)
const InputRef = ref<InputInstance>()
const removeTag = (index: number) => {
  if (form.value.authorTag) {
    form.value.authorTag.splice(index, 1)
  } else {
    form.value.authorTag = []
  }
}

const createTag = () => {
  inputVisible.value = true
  nextTick(() => {
    InputRef.value!.input!.focus()
  })
}

const handleInputConfirm = () => {
  if (!tag.value) {
    return
  }
  if (form.value.authorTag) {
    form.value.authorTag.push(tag.value)
  } else {
    form.value.authorTag = []
    form.value.authorTag.push(tag.value)
  }
  inputVisible.value = false
  tag.value = ''
}

/* 联系方式配置 */
const contactName = ref<string[]>([])
const contactUrl = ref<string[]>([])
const contactType = ref<string[]>([])
const createContact = () => {
  contactName.value.push('')
  contactUrl.value.push('')
  contactType.value.push('')
}
const removeContact = (index: number) => {
  contactName.value.splice(index, 1)
  contactUrl.value.splice(index, 1)
  contactType.value.splice(index, 1)
}

/* 支付方式配置 */
const paymentName = ref<string[]>([])
const paymentUrl = ref<string[]>([])
const createPayment = () => {
  paymentName.value.push('')
  paymentUrl.value.push('')
}
const removePayment = (index: number) => {
  paymentName.value.splice(index, 1)
  paymentUrl.value.splice(index, 1)
}

/* 家乡坐标配置 */
const home = ref<string[]>([])

/* MBTI 配置 */
const mbti = ref<any>({
  name: '',
  E: 0,
  S: 0,
  T: 0,
  J: 0
})

/* 技能特长 */
const skill = ref<string[]>([])
const skillVO = ref<PageVO<FileVO>>(initPageVO())
const getList = () => {
  pageFileSelectAdminApi({
    labelId: 10000100003,
    pageNum: 1,
    pageSize: 999
  }).then((res: ResultType<PageVO<FileVO>>) => {
    skillVO.value = res.data
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <el-container class="overflow-y-auto" direction="vertical">
    <el-form ref="formRef" :model="form" :rules="rules" class="flex flex-col gap-5">
      <el-container class="gap-3 flex-wrap">
        <el-form-item label="配置名" prop="name">
          <el-input v-model="form.name" :maxlength="16" placeholder="请输入 （max 16）" />
        </el-form-item>
        <el-form-item label="作者名称" prop="authorName">
          <el-input v-model="form.authorName" :maxlength="32" placeholder="请输入 （max 32）" />
        </el-form-item>
        <el-form-item label="作者头像" prop="authorAvatar">
          <el-input v-model="form.authorAvatar" :maxlength="128" placeholder="请输入 （max 128）" />
        </el-form-item>
      </el-container>
      <el-container class="gap-3" direction="vertical">
        <el-form-item label="个性化标签" prop="authorTag">
          <el-button type="primary" @click="createTag">新建标签</el-button>
        </el-form-item>
        <el-container class="gap-2 px-5 flex-wrap">
          <el-tag
            v-for="(tag, i) in form.authorTag"
            :key="tag"
            :disable-transitions="false"
            closable
            size="large"
            @close="removeTag(i)"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-if="inputVisible"
            ref="InputRef"
            v-model="tag"
            class="min-w-32"
            @blur="handleInputConfirm"
            @keyup.enter="handleInputConfirm"
          />
        </el-container>
      </el-container>
      <el-container class="gap-3" direction="vertical">
        <el-form-item label="联系方式配置" prop="authorContact">
          <el-button class="button-new-tag" type="primary" @click="createContact">
            新建联系方式
          </el-button>
        </el-form-item>
        <el-container class="gap-2 px-5" direction="vertical">
          <el-container v-for="i in contactName.length" :key="i" class="gap-2 flex-wrap">
            <el-form-item :label="'联系方式' + i">
              <el-input
                v-model="contactName[i - 1]"
                :maxlength="32"
                label="联系方式名称"
                placeholder="请输入 （max 32）"
              />
            </el-form-item>
            <el-form-item :label="'联系方式链接' + i">
              <el-input
                v-model="contactUrl[i - 1]"
                :maxlength="32"
                placeholder="请输入 （max 32）"
              />
            </el-form-item>
            <el-form-item :label="'类型' + i">
              <el-select v-model="contactType[i - 1]" class="min-w-32">
                <el-option label="账号" value="account" />
                <el-option label="链接" value="link" />
                <el-option label="图片地址" value="img" />
              </el-select>
            </el-form-item>
            <el-button class="button-new-tag" type="danger" @click="removeContact(i - 1)">
              删除
            </el-button>
          </el-container>
        </el-container>
      </el-container>
      <el-container class="gap-3" direction="vertical">
        <el-form-item label="支付方式配置" prop="authorContact">
          <el-button class="button-new-tag" type="primary" @click="createPayment">
            新建支付方式
          </el-button>
        </el-form-item>
        <el-container class="gap-2 px-5" direction="vertical">
          <el-container v-for="i in paymentName.length" :key="i" class="gap-2 flex-wrap">
            <el-form-item :label="'支付方式' + i">
              <el-input
                v-model="paymentName[i - 1]"
                :maxlength="32"
                placeholder="请输入 （max 32）"
              />
            </el-form-item>
            <el-form-item :label="'收款码链接' + i">
              <el-input
                v-model="paymentUrl[i - 1]"
                :maxlength="32"
                placeholder="请输入 （max 32）"
              />
            </el-form-item>
            <el-button class="button-new-tag" type="danger" @click="removePayment(i - 1)">
              删除
            </el-button>
          </el-container>
        </el-container>
      </el-container>
      <el-form-item label="家乡坐标配置" label-position="top" prop="authorHome">
        <el-container class="gap-2 px-5 py-2 flex-wrap">
          <el-form-item label="经度">
            <el-input v-model="home[0]" class="w-40 mr-2" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="纬度">
            <el-input v-model="home[1]" class="w-40" placeholder="请输入" />
          </el-form-item>
        </el-container>
      </el-form-item>
      <el-form-item label="MBTI人格配置" label-position="top" prop="authorContact">
        <el-container class="gap-2 px-5 py-2 flex-wrap">
          <el-form-item label="E">
            <el-input-number
              v-model="mbti.E"
              :max="100"
              :min="0"
              placeholder="请输入 （max 100）"
            />
          </el-form-item>
          <el-form-item label="S">
            <el-input-number
              v-model="mbti.S"
              :max="100"
              :min="0"
              placeholder="请输入 （max 100）"
            />
          </el-form-item>
          <el-form-item label="T">
            <el-input-number
              v-model="mbti.T"
              :max="100"
              :min="0"
              placeholder="请输入 （max 100）"
            />
          </el-form-item>
          <el-form-item label="J">
            <el-input-number
              v-model="mbti.J"
              :max="100"
              :min="0"
              placeholder="请输入 （max 100）"
            />
          </el-form-item>
        </el-container>
      </el-form-item>
      <el-form-item label="技能配置" label-position="top" prop="authorContact">
        <el-checkbox-group v-model="skill">
          <el-checkbox-button v-for="p in skillVO.list" :key="p.url" :value="p.url">
            <img :src="p.url" alt="" class="min-w-8 radius-lg cursor-pointer" />
          </el-checkbox-button>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <el-container class="justify-between mt-5">
      <el-button type="info" @click="systemStore.dialog.sys_config = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </el-container>
  </el-container>
</template>

<style lang="scss">
.el-checkbox-button__inner {
  border: none !important;
  border-radius: 10px !important;
}

.el-checkbox-group {
  gap: 10px !important;
}
</style>
