<script lang="ts" setup>
import * as echarts from 'echarts/core'
import { GridComponent, TooltipComponent } from 'echarts/components'
import { BarChart } from 'echarts/charts'
import { CanvasRenderer } from 'echarts/renderers'
import { getUserDistributionAdminApi } from '@/request/api/user'
import { getSystemCountApi, initSystemCountVO, type SystemCountVO } from '@/request/api/admin'

echarts.use([TooltipComponent, GridComponent, BarChart, CanvasRenderer])

const option: any = {
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '0.2%',
    right: '0.5%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: [
    {
      type: 'category',
      data: [],
      axisTick: {
        alignWithLabel: true
      },
      nameTextStyle: {
        fontSize: 16
      },
      offset: 20,
      axisLabel: {
        //interval设置成 0 强制显示所有标签
        interval: 0,
        //可以通过旋转解决标签显示不下的问题
        rotate: 20,
        // 居中显示
        align: 'center'
      }
    }
  ],
  yAxis: [
    {
      type: 'value',
      interval: 1,
      name: '数量',
      nameTextStyle: {
        fontSize: 16
      },
      axisLabel: {
        shadowOffsetX: 10
      }
    }
  ],
  series: [
    {
      name: 'Direct',
      type: 'bar',
      barWidth: '60%',
      data: [],
      label: {
        show: true,
        position: 'top'
      }
    }
  ]
}

const getUserDistributionAdmin = () => {
  getUserDistributionAdminApi().then((res) => {
    option.xAxis[0].data.push(...res.data['keys'])
    option.series[0].data.push(...res.data['values'])
    const chartDom = document.getElementById('main')!
    const myChart = echarts.init(chartDom)
    option && myChart.setOption(option)
  })
}

const systemCount = ref<SystemCountVO>(initSystemCountVO())

const getCount = () => {
  getSystemCountApi().then((res) => {
    systemCount.value = res.data
  })
}

onMounted(() => {
  getUserDistributionAdmin()
  getCount()
})
</script>

<template>
  <div class="flex flex-col overflow-auto h-full gap-2">
    <el-container class="gap-5 max-h-fit flex-wrap  text-gray-800">
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-amber-300 radius-sm">
        <svg-icon :message="'用户 ' + systemCount.user + ' 个'" name="people" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-cyan-300 radius-sm">
        <svg-icon :message="'角色 ' + systemCount.role + ' 个'" name="authority" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-emerald-300 radius-sm">
        <svg-icon :message="'类别 ' + systemCount.classify + ' 个'" name="classify" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-fuchsia-300 radius-sm">
        <svg-icon :message="'标签 ' + systemCount.label + ' 个'" name="label" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-indigo-300 radius-sm">
        <svg-icon :message="'资源 ' + systemCount.file + ' 个'" name="resources" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-lime-300 radius-sm">
        <svg-icon :message="'路由 ' + systemCount.router + ' 个'" name="router-link" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-orange-300 radius-sm">
        <svg-icon :message="'网站 ' + systemCount.website + ' 个'" name="friend-link" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-purple-300 radius-sm">
        <svg-icon :message="'文章 ' + systemCount.article + ' 篇'" name="note" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-red-300 radius-sm">
        <svg-icon :message="'日记 ' + systemCount.article + ' 篇'" name="note" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-teal-300 radius-sm">
        <svg-icon :message="'相册 ' + systemCount.album + ' 个'" name="picture" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-violet-300 radius-sm">
        <svg-icon :message="'留言 ' + systemCount.leaveWord + ' 条'" name="email" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-yellow-300 radius-sm">
        <svg-icon :message="'评论 ' + systemCount.comment + ' 条'" name="message" />
      </span>
      <span class="flex min-w-52 h-fit px-5 py-8 text-lg bg-zinc-300 radius-sm">
        <svg-icon :message="'点赞 ' + systemCount.like + ' 个'" name="praise" />
      </span>
    </el-container>
    <div id="main" class="w-full min-h-96"></div>
  </div>
</template>

<style lang="scss" scoped></style>
