<script lang="ts" setup>
import { useUserStore } from '@/stores/user'
import { getDateDiff } from '@/utils/timeUtils'
import { getGtRoleApi, type RoleVO } from '@/request/api/role'
import type { ResultType } from '@/request/config'
import { getTodayExperienceApi } from '@/request/api/user'

const userStore = useUserStore()

let date = ref<any>(getDateDiff(userStore.user.createTime, new Date()))
let interval = ref<any>()

const gtRole = ref<RoleVO>({
  name: '',
  experience: 0,
  introduction: ''
})
const todayExperience = ref<number>(0)
const init = () => {
  getGtRoleApi().then((res: ResultType<RoleVO>) => {
    gtRole.value = res.data
  })
  getTodayExperienceApi().then((res: ResultType<number>) => {
    todayExperience.value = res.data
  })
}

const updateDate = () => {
  date.value.second = (date.value.second + 1) % 60
  interval.value = setInterval(() => {
    if (date.value.second === 0) {
      date.value.minute = (date.value.minute + 1) % 60
      if (date.value.minute === 0) {
        date.value = getDateDiff('2024-04-23 8:00:00', new Date())
      }
    }
    date.value.second = (date.value.second + 1) % 60
  }, 1000)
}

const percentage = computed(() => {
  if (!gtRole.value) {
    return 100
  }
  const currentExp = userStore.user?.experience
  const targetExp = gtRole.value?.experience
  if (targetExp <= 0 || currentExp < 0) {
    return 0
  }
  let percent = (currentExp * 100) / targetExp
  if (!isFinite(percent)) {
    return 0
  }
  percent = parseFloat(percent.toFixed(2))
  return Math.min(100, Math.max(0, percent))
})

onMounted(() => {
  updateDate()
  init()
})

onBeforeMount(() => {
  clearInterval(interval.value)
})
</script>

<template>
  <el-container class="cards p-5" direction="vertical">
    <el-container class="outlinePage p-5 shadow-lg radius-lg" direction="vertical">
      <el-container>
        <el-container class="gap-3" direction="vertical">
          <span class="text-2xl">
            {{ userStore.user.roleName }}
          </span>
          <div class="splitLine"></div>
          <el-container class="items-center gap-2">
            <image-box
              :src="userStore.user.avatar"
              class="size-14 min-w-14 max-w-14 min-h-14 max-h-14 radius-sm"
              shape="square"
            />
            <el-container direction="vertical">
              <p class="text-lg">{{ userStore.user.username }}</p>
              <el-container class="gap-x-1 items-baseline flex-wrap">
                <div class="flex flex-wrap gap-1">
                  <span class="text-sm">亲爱的 {{ userStore.user.username }} 你好！</span>
                  <span class="text-sm">你已加入 UTOPIA</span>
                </div>
                <span class="flex line-clamp-1 items-baseline">
                  <span class="text-xl">{{ date.year }}</span>
                  <span class="text-sm mx-0.5">年</span>
                  <span class="text-xl">{{ date.month }}</span>
                  <span class="text-sm mx-0.5">月</span>
                  <span class="text-xl">{{ date.day }}</span>
                  <span class="text-sm mx-0.5">日</span>
                  <span class="text-xl">{{ date.hour }}</span>
                  <span class="text-sm mx-0.5">时</span>
                  <span class="text-xl">{{ date.minute }}</span>
                  <span class="text-sm mx-0.5">分</span>
                  <span class="text-xl">{{ date.second }}</span>
                  <span class="text-sm mx-0.5">秒</span>
                </span>
              </el-container>
            </el-container>
          </el-container>
        </el-container>
        <svg
          class="icon trophy"
          height="160"
          version="1.1"
          viewBox="0 0 1024 1024"
          width="160"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            class=""
            d="M469.333333 682.666667h85.333334v128h-85.333334zM435.2 810.666667h153.6c4.693333 0 8.533333 3.84 8.533333 8.533333v34.133333h-170.666666v-34.133333c0-4.693333 3.84-8.533333 8.533333-8.533333z"
            data-spm-anchor-id="a313x.search_index.0.i10.40193a81WcxQiT"
            fill="#ea9518"
          ></path>
          <path
            class=""
            d="M384 853.333333h256a42.666667 42.666667 0 0 1 42.666667 42.666667v42.666667H341.333333v-42.666667a42.666667 42.666667 0 0 1 42.666667-42.666667z"
            data-spm-anchor-id="a313x.search_index.0.i1.40193a81WcxQiT"
            fill="#6e4a32"
          ></path>
          <path
            class=""
            d="M213.333333 256v85.333333a42.666667 42.666667 0 0 0 85.333334 0V256H213.333333zM170.666667 213.333333h170.666666v128a85.333333 85.333333 0 1 1-170.666666 0V213.333333zM725.333333 256v85.333333a42.666667 42.666667 0 0 0 85.333334 0V256h-85.333334z m-42.666666-42.666667h170.666666v128a85.333333 85.333333 0 1 1-170.666666 0V213.333333z"
            data-spm-anchor-id="a313x.search_index.0.i9.40193a81WcxQiT"
            fill="#f4ea2a"
          ></path>
          <path
            class=""
            d="M298.666667 85.333333h426.666666a42.666667 42.666667 0 0 1 42.666667 42.666667v341.333333a256 256 0 1 1-512 0V128a42.666667 42.666667 0 0 1 42.666667-42.666667z"
            data-spm-anchor-id="a313x.search_index.0.i5.40193a81WcxQiT"
            fill="#f2be45"
          ></path>
          <path
            d="M512 469.333333l-100.309333 52.736 19.157333-111.701333-81.152-79.104 112.128-16.298667L512 213.333333l50.176 101.632 112.128 16.298667-81.152 79.104 19.157333 111.701333z"
            fill="#FFF2A0"
          ></path>
        </svg>
      </el-container>
      <el-container class="mt-5" direction="vertical">
        <svg-icon message="我的特权" name="authority" />
        <pre class="pt-2 px-5">{{ userStore.user.roleIntroduction }}</pre>
      </el-container>
    </el-container>
    <el-container
      class="detailPage max-h-0 duration-500 -mt-10 pt-10 radius-lg bg-color-three relative overflow-hidden"
      direction="vertical"
    >
      <svg
        class="icon medals"
        height="80"
        version="1.1"
        viewBox="0 0 1024 1024"
        width="80"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path d="M896 42.666667h-128l-170.666667 213.333333h128z" fill="#FF4C4C"></path>
        <path d="M768 42.666667h-128l-170.666667 213.333333h128z" fill="#3B8CFF"></path>
        <path d="M640 42.666667h-128L341.333333 256h128z" fill="#F1F1F1"></path>
        <path d="M128 42.666667h128l170.666667 213.333333H298.666667z" fill="#FF4C4C"></path>
        <path d="M256 42.666667h128l170.666667 213.333333h-128z" fill="#3B8CFF"></path>
        <path d="M384 42.666667h128l170.666667 213.333333h-128z" fill="#FBFBFB"></path>
        <path d="M298.666667 256h426.666666v213.333333H298.666667z" fill="#E3A815"></path>
        <path
          d="M512 661.333333m-320 0a320 320 0 1 0 640 0 320 320 0 1 0-640 0Z"
          fill="#FDDC3A"
        ></path>
        <path
          d="M512 661.333333m-256 0a256 256 0 1 0 512 0 256 256 0 1 0-512 0Z"
          fill="#E3A815"
        ></path>
        <path
          d="M512 661.333333m-213.333333 0a213.333333 213.333333 0 1 0 426.666666 0 213.333333 213.333333 0 1 0-426.666666 0Z"
          fill="#F5CF41"
        ></path>
        <path
          d="M277.333333 256h469.333334a21.333333 21.333333 0 0 1 0 42.666667h-469.333334a21.333333 21.333333 0 0 1 0-42.666667z"
          fill="#D19A0E"
        ></path>
        <path
          d="M277.333333 264.533333a12.8 12.8 0 1 0 0 25.6h469.333334a12.8 12.8 0 1 0 0-25.6h-469.333334z m0-17.066666h469.333334a29.866667 29.866667 0 1 1 0 59.733333h-469.333334a29.866667 29.866667 0 1 1 0-59.733333z"
          fill="#F9D525"
        ></path>
        <path
          d="M512 746.666667l-100.309333 52.736 19.157333-111.701334-81.152-79.104 112.128-16.298666L512 490.666667l50.176 101.632 112.128 16.298666-81.152 79.104 19.157333 111.701334z"
          fill="#FFF2A0"
        ></path>
      </svg>
      <el-container class="py-5" direction="vertical">
        <el-container class="items-center">
          <svg
            class="icon"
            height="90"
            version="1.1"
            viewBox="0 0 1024 1024"
            width="90"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              class=""
              d="M382.6 805H242.2c-6.7 0-12.2-5.5-12.2-12.2V434.3c0-6.7 5.5-12.2 12.2-12.2h140.4c6.7 0 12.2 5.5 12.2 12.2v358.6c0 6.6-5.4 12.1-12.2 12.1z"
              data-spm-anchor-id="a313x.search_index.0.i36.40193a81WcxQiT"
              fill="#ea9518"
            ></path>
            <path
              class=""
              d="M591.1 805H450.7c-6.7 0-12.2-5.5-12.2-12.2V254.9c0-6.7 5.5-12.2 12.2-12.2h140.4c6.7 0 12.2 5.5 12.2 12.2v537.9c0 6.7-5.5 12.2-12.2 12.2z"
              data-spm-anchor-id="a313x.search_index.0.i35.40193a81WcxQiT"
              fill="#f2be45"
            ></path>
            <path
              class=""
              d="M804.4 805H663.9c-6.7 0-12.2-5.5-12.2-12.2v-281c0-6.7 5.5-12.2 12.2-12.2h140.4c6.7 0 12.2 5.5 12.2 12.2v281c0.1 6.7-5.4 12.2-12.1 12.2z"
              data-spm-anchor-id="a313x.search_index.0.i37.40193a81WcxQiT"
              fill="#ea9518"
            ></path>
          </svg>
          <el-container direction="vertical">
            <p class="">EXP</p>
            <el-container class="items-baseline">
              <span class="text-3xl text-amber-500">{{ userStore.user.experience }}</span>
              <span>/</span>
              <span>{{ gtRole ? gtRole.experience : userStore.user.experience }}</span>
            </el-container>
          </el-container>
        </el-container>

        <el-container class="px-5" direction="vertical">
          <span>
            今日已获取：
            <span class="text-xl text-amber-500">{{ todayExperience }}</span>
            点经验
          </span>
          <span v-if="gtRole">
            距离下一个段位 [{{ gtRole ? gtRole.name : '未知' }}] 还差
            <span class="text-xl text-amber-500">{{
              gtRole.experience - userStore.user.experience
            }}</span>
            经验值
          </span>
          <span v-else>恭喜你，已达到最高段位！</span>
          <el-progress
            :duration="30"
            :percentage="percentage"
            :stroke-width="10"
            :width="520"
            color="#F59E0B"
            striped
            striped-flow
          />
        </el-container>
        <el-container class="px-5 mt-5" direction="vertical">
          <span>下一段位特权：</span>
          <pre class="pt-2 px-5">{{ gtRole.introduction }}</pre>
        </el-container>
      </el-container>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
.cards:hover {
  .outlinePage {
    box-shadow: 0 10px 15px #b1985e;
  }

  .detailPage {
    height: fit-content;
    max-height: 100vh;

    .medals {
      animation: slide-in-top 1s cubic-bezier(0.65, 0.05, 0.36, 1) both;
    }
  }
}

.outlinePage {
  position: relative;
  background: linear-gradient(45deg, #fffbf0, #ffdd87);
  transition-duration: 0.5s;
  z-index: 2;

  .splitLine {
    width: 240px;
    height: 10px;
    background-image: linear-gradient(
      to right,
      transparent 5%,
      #ffe8a0 20%,
      #f7b733 50%,
      #ffe8a0 70%,
      transparent 90%
    );
  }

  .trophy {
    position: absolute;
    right: 0;
    top: 5px;
    z-index: -1;
  }
}

.medals {
  position: absolute;
  right: 20px;
}

@keyframes slide-in-top {
  0% {
    transform: translateY(-100px);
    opacity: 0;
  }

  100% {
    transform: translateY(0);
    opacity: 1;
  }
}

html.dark {
  .cards:hover {
    .outlinePage {
      box-shadow: 0 10px 15px #000000;
    }
  }

  .outlinePage {
    background: linear-gradient(45deg, #3c3c3c, #000000);
  }
}
</style>
