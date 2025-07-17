<script lang="ts" setup>
import type { WebsiteVO } from '@/request/api/website'
import { useSystemStore } from '@/stores/system'

const systemStore = useSystemStore()

const { item } = defineProps({
  item: {
    type: Object as PropType<WebsiteVO>,
    default: () => {}
  }
})
</script>

<template>
  <a v-slide-in="systemStore.system.isOpenAnimation" class="flex p-3" target="_blank">
    <el-container class="card_box use-hover-box-large relative" direction="vertical">
      <image-box :src="item.cover" class="h-36 img-box" />
      <el-container class="p-3 gap-1 justify-start" direction="vertical">
        <el-container class="gap-2 pl-3 justify-between items-center">
          <image-box
            :src="item.avatar"
            class="min-w-10 max-w-10 border-1 min-h-10 max-h-10 rounded-full"
          />
          <a :href="item.url" class="flex items-center">
            <svg-button message="去康康" name="paper-plane" size="sm" />
          </a>
        </el-container>
        <svg-icon :message="item.createTime" class="ml-auto" name="edit" size="sm" />
        <span class="line-clamp-2 h-full break-all">{{ item.introduction }}</span>
      </el-container>
      <span :data-before="item.title" class="title absolute overflow-hidden -top-2.5 -left-2.5" />
    </el-container>
  </a>
</template>

<style scoped>
.card_box {
  .img-box {
    border-radius: var(--radius-lg) var(--radius-lg) 0 0;
  }

  .title {
    width: 180px;
    height: 180px;
    display: flex;
    align-items: center;
    justify-content: center;

    &::before {
      content: attr(data-before);
      position: absolute;
      width: 150%;
      height: 40px;
      background-image: linear-gradient(45deg, #ff6547 0%, #ffb144 51%, #ff7053 100%);
      transform: rotate(-45deg) translateY(-20px);
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 5px 10px var(--shadow-color);
    }
  }
}
</style>
