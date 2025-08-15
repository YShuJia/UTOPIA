<script lang="ts" setup></script>

<template>
  <el-container class="gap-5" direction="vertical">
    <start-home />
    <el-container class="size-full gap-16 relative" direction="vertical">
      <el-container class="inner-box" direction="vertical">
        <el-container class="gap-5 items-end px-4">
          <svg-icon class="w-fit pb-1.5 border-b-4" message="关于网站" name="info" size="2xl" />
          <span>网站部署文档...</span>
        </el-container>
        <start-about />
      </el-container>
      <el-container class="inner-box" direction="vertical">
        <el-container class="gap-5 items-end px-4">
          <svg-icon class="pb-1.5 border-b-4" message="博客文章" name="note" size="2xl" />
          <span>吾将上下而求索...</span>
        </el-container>
        <start-article />
      </el-container>
      <el-container class="inner-box" direction="vertical">
        <el-container class="gap-5 items-end px-4">
          <svg-icon class="pb-1.5 border-b-4" message="微语界" name="message" size="2xl" />
          <span>非礼勿言、勿视、勿听</span>
        </el-container>
        <start-leave-word />
      </el-container>
      <el-container class="inner-box" direction="vertical">
        <el-container class="gap-5 items-end px-4">
          <svg-icon class="pb-1.5 border-b-4" message="如梦令" name="picture" size="2xl" />
          <span>短暂而美好的回忆...</span>
        </el-container>
        <start-album />
      </el-container>
      <el-container class="inner-box" direction="vertical">
        <el-container class="gap-5 items-end px-4">
          <svg-icon class="pb-1.5 border-b-4" message="青玉案" name="diary" size="2xl" />
          <span>日常琐事记录...</span>
        </el-container>
        <start-diary />
      </el-container>
      <el-container class="inner-box" direction="vertical">
        <el-container class="gap-5 items-end px-4">
          <svg-icon class="pb-1.5 border-b-4" message="友人帐" name="friend-link" size="2xl" />
          <span>虽千万人吾往矣...</span>
        </el-container>
        <start-friend />
      </el-container>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped></style>
