<script lang="ts" setup>
import { useSystemStore } from '@/stores/system'
import { listRoleApi, type RoleVO } from '@/request/api/role'
import type { ResultType } from '@/request/config'

const sys = useSystemStore()
const url = sys.getRandomImg()

const role = ref<RoleVO[]>([])
const getRoleList = () => {
  listRoleApi().then((res: ResultType<RoleVO[]>) => {
    role.value = res.data
    console.log(role.value)
  })
}

const colors = [
  'rgb(179,103,111)',
  'rgb(242,131,171)',
  'rgb(254, 204, 17)',
  'rgb(227,210,182)',
  'rgb(18, 170, 156)',
  'rgb(99, 187, 208)',
  'rgb(102, 169, 201)',
  'rgb(97, 100, 159)'
]

onMounted(() => {
  getRoleList()
})
</script>

<template>
  <div class="relative flex flex-col gap-10 w-screen">
    <el-container
      :style="{ backgroundImage: `url(${url})` }"
      class="h-96 bg-no-repeat bg-cover bg-center items-center justify-center"
    />
    <el-container
      class="pt-16 use-box-large bg-color pb-5 max-sm:rounded-none max-sm:shadow-none gap-10 items-center inner-box z-10 !-mt-36"
      direction="vertical"
    >
      <el-container class="items-center gap-2" direction="vertical">
        <image-box :src="sys.webConfig.authorAvatar" class="!size-52 rounded-full" />
        <span class="text-3xl z-10">{{ sys.webConfig.authorName }}</span>
      </el-container>
      <el-container class="w-full px-5 flex-wrap justify-between gap-5 max-sm:gap-1">
        <span
          v-for="(p, i) in sys.webConfig.authorTag"
          :key="i"
          :style="{
            backgroundColor: colors[Math.floor(Math.random() * colors.length)],
            top: Math.random() * 300 + 'px'
          }"
          class="p-3 w-fit rounded-full animate-pulse"
        >
          {{ p }}
        </span>
      </el-container>
    </el-container>
    <el-container class="inner-box flex-wrap gap-5 justify-between">
      <connect-author />
      <sponsor-author class="p-5" />
    </el-container>
    <el-container
      class="use-box-large inner-box max-sm:rounded-none max-sm:shadow-none gap-5 p-5"
      direction="vertical"
    >
      <span>
        <span>
          &emsp;&emsp;一直想要开发一个属于自己的博客网站，但是一直在网站的UI设计上纠结，自己在这上面并没有什么灵感。
          尝试开发了几个页面，但是 UI 始终达不到预期效果。一次偶然的机会，看到了
        </span>
        <a class="!inline text-red-500 use-link-default" href="https://poetize.cn/" target="_blank">
          &nbsp;POETIZE&nbsp;
        </a>
        <span>大佬的博客， 之后陆陆续续又接触了不少同行&nbsp;</span>
        <a class="text-red-500 use-link-default" href="https://liuyuyang.net/" target="_blank"
          >宇阳</a
        >、
        <a class="text-red-500 use-link-default" href="http://mrzym.top/#/home" target="_blank">
          小张的个人博客 </a
        >、
        <a class="text-red-500 use-link-default" href="https://huohuo90.com/home" target="_blank">
          YIKE 时光&nbsp;
        </a>
        <span> 等。这些博客在 UI 上给了我一些灵感。 </span>
      </span>
      <span>
        &emsp;&emsp;如今建此站，一方面是源于对此的热爱和兴趣，
        另一方面，是记录自己的学习与生活，在这日新月异的互联网上，留下一点点的足迹。正所谓 “
        莫等闲，白了少年头，空悲切！”, 希望自己在这不断飞逝的时光中能够有收获。
      </span>
    </el-container>
    <el-container
      class="use-box-large inner-box max-sm:rounded-none max-sm:shadow-none gap-5 p-5"
      direction="vertical"
    >
      <svg-icon message="系统段位信息" name="setting" size="xl" />
      <el-container class="gap-5" direction="vertical">
        <span>每日最多可获取 300 点经验值</span>
        <el-row class="border-b-2 pb-0.5">
          <el-col :span="16">1、阅读一篇文章</el-col>
          <el-col :span="8">5 点经验值</el-col>
        </el-row>
        <el-row class="border-b-2 pb-0.5">
          <el-col :span="16">2、发布一条留言</el-col>
          <el-col :span="8">10 点经验值</el-col>
        </el-row>
        <el-row class="border-b-2 pb-0.5">
          <el-col :span="16">3、发布一条评论</el-col>
          <el-col :span="8">10 点经验值</el-col>
        </el-row>
        <el-row class="border-b-2 pb-0.5">
          <el-col :span="16">4、发布一条友人链接</el-col>
          <el-col :span="8">20 点经验值</el-col>
        </el-row>
      </el-container>
      <el-container class="gap-5" direction="vertical">
        <span>段位对应的经验值</span>
        <el-container class="gap-5" direction="vertical">
          <el-container v-for="p in role" class="border-b-2 pb-0.5 gap-x-5 gap-y-1 flex-wrap">
            <span class="w-40">段位：{{ p.name }}</span>
            <span class="w-32">经验：{{ p.experience }}</span>
            <span class="w-full" v-html="'特权：' + p.introduction"></span>
          </el-container>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
.animate-rotate {
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  0% {
    transform: rotateZ(0deg);
  }
  100% {
    transform: rotateZ(360deg);
  }
}
</style>
