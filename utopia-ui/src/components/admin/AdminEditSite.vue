<script lang="ts" setup>
import { useTemporaryStore } from '@/stores/temporary'
import { uploadWebConfigFileApi } from '@/request/api/web_config'

const store = useTemporaryStore()

const rules = {
  siteTitle: [{ required: true, message: '网站名称不能为空', trigger: 'blur' }],
  siteMotto: [{ required: true, message: '网站座右铭不能为空', trigger: 'blur' }],
  siteRecord: [{ required: true, message: '网站备案信息不能为空', trigger: 'blur' }],
  siteFavicon: [{ required: true, message: '网站默认头像不能为空', trigger: 'blur' }],
  siteCreateTime: [{ required: true, message: '网站运行起始时间不能为空', trigger: 'blur' }],
  siteAbout: [{ required: true, message: '网站关于信息不能为空', trigger: 'blur' }]
}
</script>

<template>
  <el-container direction="vertical">
    <el-form ref="formRef" :model="store.webConfig" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="网站名称" prop="siteTitle">
        <el-input
          v-model="store.webConfig.siteTitle"
          :maxlength="32"
          placeholder="请输入 （max 32）"
        />
      </el-form-item>
      <el-form-item label="网站座右铭" prop="siteMotto">
        <el-input
          v-model="store.webConfig.siteMotto"
          :maxlength="32"
          placeholder="请输入 （max 32）"
        />
      </el-form-item>
      <el-form-item label="网站备案信息" prop="siteRecord">
        <el-input
          v-model="store.webConfig.siteRecord"
          :maxlength="64"
          placeholder="请输入 （max 64）"
        />
      </el-form-item>
      <el-form-item label="网站默认头像" prop="siteFavicon">
        <el-input
          v-model="store.webConfig.siteFavicon"
          :maxlength="128"
          placeholder="请输入 （max 128）"
        />
      </el-form-item>
      <el-form-item label="网站运行起始时间" prop="siteCreateTime">
        <el-date-picker
          v-model="store.webConfig.siteCreateTime"
          :maxlength="128"
          placeholder="请选择时间"
          type="datetime"
        />
      </el-form-item>
      <el-form-item label="关于网站" label-position="top" prop="siteAbout">
        <custom-editor
          v-model:text="store.webConfig.siteAbout"
          v-model:urls="store.webConfig.siteUrls"
          :upload="uploadWebConfigFileApi"
        />
      </el-form-item>
    </el-form>
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
