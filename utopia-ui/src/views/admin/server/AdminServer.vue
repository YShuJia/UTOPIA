<script lang="ts" setup>
import { getServerApi, initServerVO, type ServerVO } from '@/request/api/admin'
import type { ResultType } from '@/request/config'

const server = ref<ServerVO>(initServerVO())

const getServer = async () => {
  await getServerApi().then((res: ResultType<ServerVO>) => {
    server.value = res.data
  })
}

onMounted(() => {
  getServer()
})
</script>

<template>
  <el-container class="h-full">
    <div class="flex flex-wrap h-full gap-5 pr-5 overflow-auto">
      <el-card class="min-w-96 flex-1">
        <template #header>
          <span>CPU 信息</span>
        </template>
        <el-container class="w-full gap-4" direction="vertical">
          <el-row class="border-b pb-0.5">
            <el-col :span="12">核心数：</el-col>
            <el-col :span="12">{{ server.cpuVO.cpuNum }}</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">系统使用率：</el-col>
            <el-col :span="12">{{ server.cpuVO.sysUsed }}%</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">用户使用率：</el-col>
            <el-col :span="12">{{ server.cpuVO.userUsed }}%</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">当前等待率：</el-col>
            <el-col :span="12">{{ server.cpuVO.wait }}%</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">当前空闲率：</el-col>
            <el-col :span="12">{{ server.cpuVO.free }}%</el-col>
          </el-row>
        </el-container>
      </el-card>
      <el-card class="min-w-96 flex-1">
        <template #header>
          <span>内存信息</span>
        </template>
        <el-container class="w-full gap-4" direction="vertical">
          <el-row class="border-b pb-0.5">
            <el-col :span="12">总内存：</el-col>
            <el-col :span="12">{{ server.memVO.total }}GB</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">已用内存：</el-col>
            <el-col :span="12">{{ server.memVO.used }}GB</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">剩余内存：</el-col>
            <el-col :span="12">{{ server.memVO.free }}GB</el-col>
          </el-row>
        </el-container>
      </el-card>
      <el-card class="min-w-96 flex-1">
        <template #header>
          <span>服务器信息</span>
        </template>
        <el-container class="w-full gap-4" direction="vertical">
          <el-row class="border-b pb-0.5">
            <el-col :span="12">服务器名称：</el-col>
            <el-col :span="12">{{ server.sysVO.computerName }}</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">服务器IP：</el-col>
            <el-col :span="12">{{ server.sysVO.computerIp }}</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">项目路径：</el-col>
            <el-col :span="12" class="break-words">{{ server.sysVO.userDir }}</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">操作系统：</el-col>
            <el-col :span="12">{{ server.sysVO.osName }}</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="12">系统架构：</el-col>
            <el-col :span="12">{{ server.sysVO.osArch }}</el-col>
          </el-row>
        </el-container>
      </el-card>
      <el-card class="min-w-96 flex-1">
        <template #header>
          <span>JVM 信息</span>
        </template>
        <el-container class="w-full gap-4" direction="vertical">
          <el-row class="border-b pb-0.5">
            <el-col :span="8">已用内存：</el-col>
            <el-col :span="16">{{ server.jvmVO.total }}MB</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="8">最大内存：</el-col>
            <el-col :span="16">{{ server.jvmVO.max }}MB</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="8">空闲内存：</el-col>
            <el-col :span="16">{{ server.jvmVO.free }}MB</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="8">JDK 版本：</el-col>
            <el-col :span="16">{{ server.jvmVO.version }}</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="8">JDK 路径：</el-col>
            <el-col :span="16" class="break-words">{{ server.jvmVO.home }}</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="8">启动时间：</el-col>
            <el-col :span="16" class="break-words">{{ server.jvmVO.startTime }}</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="8">运行时间：</el-col>
            <el-col :span="16" class="break-words">{{ server.jvmVO.runTime }}</el-col>
          </el-row>
          <el-row class="border-b pb-0.5">
            <el-col :span="8">运行参数：</el-col>
            <el-col :span="16" class="break-words">{{ server.jvmVO.inputArgs }}</el-col>
          </el-row>
        </el-container>
      </el-card>
      <el-card class="w-full">
        <template #header>
          <span>盘符信息</span>
        </template>
        <el-table :data="server.sysFilesVO">
          <el-table-column label="盘符路径" prop="dirName" />
          <el-table-column label="文件类型" prop="typeName" />
          <el-table-column label="盘符类型" prop="sysTypeName" />
          <el-table-column label="总大小" prop="total" />
          <el-table-column label="剩余大小" prop="free" />
          <el-table-column label="已使用" prop="used" />
          <el-table-column label="使用率%" prop="usage" />
        </el-table>
      </el-card>
    </div>
  </el-container>
</template>

<style lang="scss" scoped></style>
