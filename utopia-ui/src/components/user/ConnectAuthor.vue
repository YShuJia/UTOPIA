<script lang="ts" setup>
import wechat from '@/assets/img/wechat.png'
import QQ from '@/assets/img/qq.png'
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()
</script>

<template>
  <el-container class="gap-5 flex-wrap">
    <el-container
      class="p-5 gap-2 use-box-large bg-gradient-to-r from-teal-100 to-orange-100"
      direction="vertical"
    >
      <span class="text-black">UTOPIA 项目地址</span>
      <el-container class="items-end gap-2">
        <a
          class="child flex w-fit h-fit p-2.5 rounded-full transition-all duration-500"
          href="https://gitee.com/yshujia-utopia/UTOPIA"
          target="_blank"
        >
          <button class="button border-none bg-transparent transition-all duration-500">
            <svg-icon name="gitee" size="5xl" />
          </button>
        </a>
        <a
          class="child flex w-fit h-fit p-2.5 rounded-full transition-all duration-500"
          href="https://github.com/YShuJia/UTOPIA"
          target="_blank"
        >
          <button class="button border-none bg-transparent transition-all duration-500">
            <svg-icon name="github" size="5xl" />
          </button>
        </a>
        <a
          class="child flex w-fit h-fit p-2.5 rounded-full transition-all duration-500"
          href="https://space.bilibili.com/668322664?spm_id_from=333.1007.0.0"
          target="_blank"
        >
          <button class="button border-none bg-transparent transition-all duration-500">
            <svg-icon name="bilibili" size="5xl" />
          </button>
        </a>
      </el-container>
    </el-container>
    <el-container
      class="p-5 gap-2 use-box-large bg-gradient-to-r from-teal-100 to-orange-100"
      direction="vertical"
    >
      <span class="text-black">联系作者 - yshujia</span>
      <el-container class="gap-5">
        <image-box
          :src="wechat"
          class="radius-sm !size-24"
          @click="systemStore.preview.open(wechat)"
        />
        <image-box :src="QQ" class="radius-sm !size-24" @click="systemStore.preview.open(QQ)" />
      </el-container>
    </el-container>
  </el-container>
</template>

<style scoped>
.child {
  transform-style: preserve-3d;
}

.child:hover {
  background-color: white;
  background-position:
    -100px 100px,
    -100px 100px;

  transform: perspective(180px) rotateX(60deg) translateY(2px);
  box-shadow: 0 10px 10px rgb(1, 49, 182);

  .button {
    transform: translate3d(0px, 0px, 16px) perspective(100px) rotateX(-60deg) translateY(0);
  }
}
</style>
