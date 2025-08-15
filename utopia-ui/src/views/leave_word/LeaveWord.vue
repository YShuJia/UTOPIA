<script lang="ts" setup>
import type { LeaveWordDTO, LeaveWordVO } from '@/request/api/leave_word'
import { addLeaveWordApi, pageLeaveWordApi } from '@/request/api/leave_word'
import { useSystemStore } from '@/stores/system'
import type { PageVO, ResultType } from '@/request/config'

const systemStore = useSystemStore()
const url = systemStore.getRandomImg()
const treeHoleBoxRef = ref<HTMLElement>()

const size = Math.floor(window.innerHeight / 56)
/* 查询参数 */
const page = reactive<LeaveWordDTO>({
  pageNum: 1,
  pageSize: size
})

const list = ref<LeaveWordVO[]>([])
const getList = () => {
  pageLeaveWordApi(page).then((res: ResultType<PageVO<LeaveWordVO>>) => {
    if (res.data.isEmpty) {
      page.pageNum = 1
    }
    list.value.push(...res.data.list)
  })
  page.pageNum = (page.pageNum ?? 1) + 1
}

let interval = setInterval(() => {
  if (!systemStore.system.isLeave) {
    getList()
  }
}, 5000)

onMounted(() => {
  getList()
})

const text = ref<string>('')
const sendText = () => {
  if (text.value === '') {
    ElMessage.warning('请输入留言内容')
    return
  }
  addLeaveWordApi({ content: text.value })
  text.value = ''
}

onDeactivated(() => {
  clearInterval(interval)
})
</script>

<template>
  <div>
    <div class="sticky top-0">
      <div
        id="treeHoleBox"
        ref="treeHoleBoxRef"
        :style="{ backgroundImage: `url(${url})` }"
        class="w-screen h-screen overflow-hidden bg-no-repeat bg-center bg-cover flex items-center relative"
      >
        <div
          class="bg-gray-600/60 py-1 px-3 mx-auto border-2 flex items-center rounded-full relative z-30 text-base text-gray-50"
        >
          <input
            v-model="text"
            class="bg-transparent px-1 duration-300 radius-sm"
            maxlength="32"
            placeholder="非礼无言..."
          />
          <a class="p-1 flex animate-pulse" href="javascript:" @click="sendText">
            <svg-icon name="paper-plane" size="2xl" />
          </a>
        </div>
        <barrage-box
          v-for="(item, index) in list"
          :key="item.id"
          :index="index % size"
          :item="item"
          @del="() => list.splice(index, 1)"
        />
      </div>
    </div>
    <el-container class="w-screen relative use-theme">
      <comment class="inner-box" />
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
#treeHoleBox {
  min-height: calc(100vh);
  max-height: 100vh;
}
</style>
