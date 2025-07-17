<script lang="ts" setup>
import { RouteNameEnum } from '@/enum'

const router = useRouter()

const { name, message } = defineProps({
  name: {
    type: String as () => RouteNameEnum,
    required: true
  },
  message: {
    type: String,
    default: '查看更多'
  }
})
</script>

<template>
  <a class="w-fit mx-auto" @click="router.push({ name: name })">
    <cool-button :message="message" />
  </a>
</template>

<style lang="scss" scoped></style>
