<script lang="ts" setup>
import type { LeaveWordVO } from '@/request/api/leave_word'

const { item, index } = defineProps({
  item: {
    type: Object as PropType<LeaveWordVO>,
    required: true
  },
  index: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['del'])

const barrageRef = ref<HTMLAnchorElement | null>(null)
onMounted(() => {
  if (!barrageRef.value) {
    return
  }
  const duration = Math.random() * 8000 + 15000
  // 设置样式
  barrageRef.value.style.top = Math.random() * 2 + 3.5 * index + 'rem'
  barrageRef.value.style.left = '100vw'
  // 创建动画
  const animation = barrageRef.value.animate([{ left: '100vw' }, { left: '-50px' }], {
    easing: 'linear',
    duration
  })
  // 鼠标移入暂停动画
  barrageRef.value.addEventListener('mouseenter', () => {
    animation.pause()
  })
  // 鼠标移出继续播放
  barrageRef.value.addEventListener('mouseleave', () => {
    animation.play()
  })
  // 动画结束时移除组件
  animation.onfinish = () => {
    if (barrageRef.value) {
      barrageRef.value.remove()
      emit('del')
    }
  }
})
</script>

<template>
  <a
    ref="barrageRef"
    class="leave_word min-w-[152px] p-1.5 -right-full w-fit absolute text-gray-50 bg-gray-950/80 flex flex-nowrap items-center rounded-full"
    href="javascript:"
  >
    <image-box :src="item.avatar" class="!size-9 rounded-full mr-1.5" />
    <span class="message duration-500 whitespace-nowrap">{{ item.content }}</span>
    <span class="time absolute duration-500 left-12 opacity-0 text-sm leading-4 flex flex-col">
      <span>{{ item.createTime }}</span>
    </span>
  </a>
</template>

<style lang="scss" scoped>
.leave_word {
  &:hover {
    .message {
      opacity: 0;
    }

    .time {
      opacity: 1;
      color: #f5f5f5;
    }
  }
}
</style>
