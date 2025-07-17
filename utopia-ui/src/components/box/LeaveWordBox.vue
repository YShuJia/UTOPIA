<script lang="ts" setup>
import type { LeaveWordVO } from '@/request/api/leave_word'
import router from '@/router'
import { RouteNameEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()

const { item } = defineProps({
  item: {
    type: Object as PropType<LeaveWordVO>,
    default: []
  }
})
</script>

<template>
  <a
    :key="item.id"
    v-slide-in="systemStore.system.isOpenAnimation"
    class="card overflow-hidden flex flex-col gap-2 p-5 use-hover-box-large"
    href="javascript:"
    @click="router.push({ name: RouteNameEnum.LEAVE_WORD })"
  >
    <image-box :src="item.avatar" class="!size-12 radius-sm" />
    <span class="line-clamp-1">{{ item.content }}</span>
    <svg-icon :message="item.createTime" class="text-color-gray" name="edit" size="sm" />
    <div class="card__arrow absolute bottom-0 right-0 py-1.5 px-2 duration-300">
      <svg
        fill="none"
        height="15"
        viewBox="0 0 24 24"
        width="15"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M13.4697 17.9697C13.1768 18.2626 13.1768 18.7374 13.4697 19.0303C13.7626 19.3232 14.2374 19.3232 14.5303 19.0303L20.3232 13.2374C21.0066 12.554 21.0066 11.446 20.3232 10.7626L14.5303 4.96967C14.2374 4.67678 13.7626 4.67678 13.4697 4.96967C13.1768 5.26256 13.1768 5.73744 13.4697 6.03033L18.6893 11.25H4C3.58579 11.25 3.25 11.5858 3.25 12C3.25 12.4142 3.58579 12.75 4 12.75H18.6893L13.4697 17.9697Z"
          fill="#fff"
        ></path>
      </svg>
    </div>
  </a>
</template>

<style lang="scss" scoped>
.card {
  position: relative;

  .card__arrow {
    background: var(--hover-color);
    border-top-left-radius: var(--radius-lg);
    border-bottom-right-radius: var(--radius-lg);
  }

  svg {
    transition: 0.3s;
  }

  &:hover .card__arrow {
    background: #e10000;

    & svg {
      transform: translateX(3px);
    }
  }
}
</style>
