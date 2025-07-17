<script lang="ts" setup>
const emoji = [
  '😄',
  '😁',
  '😆',
  '😅',
  '🥹',
  '🙃',
  '😉',
  '😌',
  '😚',
  '🤨',
  '😎',
  '🤩',
  '🥳',
  '🙂',
  '‍😏',
  '😖',
  '🥺',
  '😢',
  '😭',
  '😠',
  '😱',
  '😥',
  '😬',
  '🫨',
  '🫠',
  '😵‍💫',
  '🤮',
  '🤧',
  '🤕',
  '🤡',
  '💩',
  '👻',
  '💀',
  '👽',
  '🤖'
]

const emit = defineEmits(['getEmoji'])
const sendEmoji = (emoji: string) => {
  emit('getEmoji', emoji)
}
</script>

<template>
  <a href="javascript:">
    <el-popover trigger="hover" width="300px">
      <template #reference>
        <svg-icon name="emoji" size="2xl" />
      </template>
      <span class="flex gap-1 flex-wrap">
        <a v-for="item in emoji" :key="item" href="javascript:" @click="sendEmoji(item)">
          <el-tooltip :effect="'light'" placement="top">
            <span class="text-xl">{{ item }}</span>
            <template #content>
              <span class="text-4xl">{{ item }}</span>
            </template>
          </el-tooltip>
        </a>
      </span>
    </el-popover>
  </a>
</template>

<style lang="scss">
.el-popover.el-popper {
  padding: 10px !important;
}
</style>
