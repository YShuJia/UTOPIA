<script lang="ts" setup>
import { ref } from 'vue'
import { addWebsiteApi, type WebsiteDTO } from '@/request/api/website'
import type { ResultType } from '@/request/config'
import { useUserStore } from '@/stores/user'
import { useSystemStore } from '@/stores/system'
import letterTop from '@/assets/img/letterTop.png'
import letterBottom from '@/assets/img/letterBottom.png'

const userStore = useUserStore()

const systemStore = useSystemStore()
const url = systemStore.getRandomImg()

const formRef = ref()

const form = ref<WebsiteDTO>({})
const rules = {
  title: [{ required: true, message: '名称不能为空！', trigger: 'blur' }],
  introduction: [{ required: true, message: '简介不能为空！', trigger: 'blur' }],
  url: [
    { required: true, message: '链接不能为空', trigger: 'blur' },
    {
      pattern: /^(http|https):\/\/([a-zA-Z0-9]+\.)+[a-z]+$/,
      message: '格式不正确！',
      trigger: 'blur'
    }
  ],
  avatar: [
    { required: true, message: '头像不能为空！', trigger: 'blur' },
    {
      pattern: /^(http|https):\/\/([a-zA-Z0-9]+\.)+[a-z]+$/,
      message: '格式不正确！',
      trigger: 'blur'
    }
  ],
  cover: [
    { required: true, message: '封面不能为空！', trigger: 'blur' },
    {
      pattern: /^(http|https):\/\/([a-zA-Z0-9]+\.)+[a-z]+$/,
      message: '格式不正确！',
      trigger: 'blur'
    }
  ]
}

const sendForm = () => {
  if (!userStore.user.username) {
    ElMessage.warning('请先登录！')
    return
  }
  formRef.value.validate(async (valid: boolean) => {
    // 表单验证
    if (valid) {
      addWebsiteApi(form.value).then((res: ResultType<boolean>) => {
        form.value = {}
      })
    }
  })
}
let showLetter = ref<boolean>(false)
</script>

<template>
  <el-container
    class="relative pb-1 items-center justify-end min-h-[500px] max-sm:min-h-96 max-xs:min-h-72"
    direction="vertical"
  >
    <el-image
      :src="letterTop"
      class="z-0 cursor-pointer w-[520px] !absolute bottom-[125px] max-sm:bottom-[102px] max-sm:w-[420px] max-xs:w-full max-xs:bottom-[78px]"
      fit="cover"
      @click="showLetter = !showLetter"
    />
    <div class="p-5 pb-0 z-10 w-[520px] max-sm:w-[420px] max-xs:w-full">
      <el-container
        :class="
          'relative radius-lg shadow use-transition-size overflow-hidden ' +
          (showLetter
            ? ' h-[805px] max-sm:h-[775px] max-xs:h-[660px]'
            : ' h-60 max-sm:h-48 max-xs:h-36')
        "
        direction="vertical"
      >
        <image-box :lazy="false" :src="url" class="absolute max-h-56 max-xs:max-h-40" />
        <el-container class="mt-56 use-theme max-xs:mt-40" direction="vertical">
          <span class="flex justify-center py-2">路漫漫其修远兮 </span>
          <el-form ref="formRef" :model="form" :rules="rules" class="px-4 flex flex-col gap-5">
            <el-form-item prop="title">
              <el-input v-model="form.title" :maxlength="15" placeholder="名称" />
            </el-form-item>
            <el-form-item prop="url">
              <el-input v-model="form.url" :maxlength="120" placeholder="链接" />
            </el-form-item>
            <el-form-item prop="avatar">
              <el-input v-model="form.avatar" :maxlength="120" placeholder="头像" />
            </el-form-item>
            <el-form-item prop="cover">
              <el-input v-model="form.cover" :maxlength="120" placeholder="封面" />
            </el-form-item>
            <el-form-item prop="introduction">
              <el-input v-model="form.introduction" :maxlength="60" placeholder="简介" />
            </el-form-item>
            <a class="py-1 use-btn-default" href="javascript:" @click="sendForm">提 交</a>
            <span class="flex justify-center">吾将上下而求索</span>
          </el-form>
        </el-container>
      </el-container>
    </div>
    <el-image
      :src="letterBottom"
      class="z-20 cursor-pointer w-[520px] !absolute bottom-0 max-sm:w-[420px] max-xs:w-full"
      fit="cover"
      @click="showLetter = !showLetter"
    />
  </el-container>
</template>

<style lang="scss" scoped></style>
