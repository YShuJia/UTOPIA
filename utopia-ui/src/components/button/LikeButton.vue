<script lang="ts" setup>
import { getLikeApi, updateLikeApi } from '@/request/api/like'
import type { ResultType } from '@/request/config'

const { count, sourceId } = defineProps({
  count: {
    type: Number,
    required: true
  },
  sourceId: {
    type: Number,
    required: true
  }
})

const liked = ref(false)

const like = () => {
  getLikeApi(sourceId).then((res: ResultType<boolean>) => {
    liked.value = res.data
  })
}

const updateLike = () => {
  updateLikeApi(sourceId, liked.value).catch(() => {
    liked.value = !liked.value
  })
}

onMounted(() => {
  like()
})
</script>

<template>
  <label
    class="flex w-fit cursor-pointer gap-1 p-2 h-10 use-btn-large bg-color-three items-center overflow-hidden shadow"
  >
    <input id="heart" v-model="liked" class="on hidden" type="checkbox" @change="updateLike" />
    <el-container class="like items-center pr-0.5 justify-around w-fit">
      <svg
        class="like-icon"
        fill-rule="nonzero"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z"
        ></path>
      </svg>
      <span class="like-text">点赞</span>
    </el-container>
    <el-container
      class="relative like-count pl-1.5 border-l-2 border-gray-400 w-fit"
      direction="vertical"
    >
      <span class="">{{ count - 1 }}</span>
      <span class="">{{ count }}</span>
      <span class="">{{ count + 1 }}</span>
    </el-container>
  </label>
</template>

<style scoped>
.like-icon {
  fill: #ababab;
  height: 20px;
  width: 20px;
}

.like-count {
  transition: transform 0.3s ease-out;

  span {
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 40px;
  }
}

.on:checked ~ .like .like-icon {
  fill: red;
  animation: enlarge 0.2s ease-out 1;
  transition: all 0.2s ease-out;
}

.on:checked ~ .like-count {
  transform: translateY(-40px);
}

@keyframes enlarge {
  0% {
    transform: scale(0.5);
  }
  100% {
    transform: scale(1.2);
  }
}
</style>
