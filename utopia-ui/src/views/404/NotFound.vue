<script lang="ts" setup>
import { RouteNameEnum } from '@/enum'

const router = useRouter()
</script>

<template>
  <el-container class="w-screen h-screen bg-gray-950 gap-5" direction="vertical">
    <el-container class="head block relative w-52 max-h-40 mt-52 mx-auto">
      <div class="eye"></div>
      <div class="eye"></div>
    </el-container>
    <span class="text-8xl text-white text-center">404</span>
    <span class="flex items-center justify-center text-white text-center">
      <span class="text-xl">找不到该页面！</span>
      <a
        class="text-xl use-link-default"
        href="javascript:"
        @click="
          router.push({
            name: RouteNameEnum.HOME
          })
        "
      >
        返回主页?
      </a>
    </span>
  </el-container>
</template>

<style lang="scss" scoped>
.head {
  -webkit-animation: shvr 0.2s infinite;
  animation: shvr 0.2s infinite;

  &::after {
    content: '';
    width: 20px;
    height: 20px;
    background: #000;
    position: absolute;
    top: 30px;
    left: 25px;
    border-radius: 50%;
    box-shadow: 125px 0 0 #000;
    -webkit-animation: eye 2.5s infinite;
    animation: eye 2.5s infinite;
  }
}

.eye {
  position: relative;
  display: inline-block;
  background: #fff;
  width: 75px;
  height: 80px;
  border-radius: 50% 50% 50% 50% / 45px 45px 45% 45%;
  -webkit-transform: rotate(45deg);
  -ms-transform: rotate(45deg);
  transform: rotate(45deg);

  &::after {
    content: '';
    position: absolute;
    border-bottom: 2px solid #fff;
    width: 70px;
    height: 50px;
    left: 0px;
    bottom: -10px;
    border-radius: 50%;
  }

  &::before {
    bottom: auto;
    top: -100px;
    -webkit-transform: rotate(45deg);
    -ms-transform: rotate(45deg);
    transform: rotate(45deg);
    left: 0;
  }

  &:nth-of-type(2) {
    float: right;
    -webkit-transform: rotate(-45deg);
    -ms-transform: rotate(-45deg);
    transform: rotate(-45deg);

    &::after {
      left: 5px;
    }
  }

  &:nth-of-type(3) {
    display: none;
  }
}

@keyframes eye {
  0%,
  30%,
  55%,
  90%,
  100% {
    -webkit-transform: translate(0, 0);
    -ms-transform: translate(0, 0);
    transform: translate(0, 0);
  }
  10%,
  25% {
    -webkit-transform: translate(0, 20px);
    -ms-transform: translate(0, 20px);
    transform: translate(0, 20px);
  }
  65% {
    -webkit-transform: translate(-20px, 0);
    -ms-transform: translate(-20px, 0);
    transform: translate(-20px, 0);
  }
  80% {
    -webkit-transform: translate(20px, 0);
    -ms-transform: translate(20px, 0);
    transform: translate(20px, 0);
  }
}

@keyframes shvr {
  0% {
    -webkit-transform: translate(1px);
    -ms-transform: translate(1px);
    transform: translate(1px);
  }
  50% {
    -webkit-transform: translate(0);
    -ms-transform: translate(0);
    transform: translate(0);
  }
  100% {
    -webkit-transform: translate(-1px);
    -ms-transform: translate(-1px);
    transform: translate(-1px);
  }
}
</style>
