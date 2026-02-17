<script lang="ts" setup>
import type { RouterType } from '@/type'
import { routerTo } from '@/router'

const route = useRoute()
const router = useRouter()

const { routerData } = defineProps({
  routerData: {
    type: Array<RouterType>,
    required: true
  }
})
</script>

<template>
  <el-container v-for="(item, index) in routerData" :key="index" class="h-full max-w-fit">
    <template v-if="item.meta.type === 'M'">
      <el-dropdown :teleported="false">
        <a
          :data-title="item.meta.title"
          class="flex items-center gap-1 one-level-link use-link-default"
          href="javascript:"
          target="_blank"
        >
          <svg-icon :message="item.meta.title" :name="item.meta.icon" />
          <svg-icon name="downward" />
        </a>
        <template #dropdown>
          <el-container class="py-1.5 overflow-hidden" direction="vertical">
            <a
              v-for="(child, i) in item.children"
              :key="i"
              :class="
                'min-w-48 use-link-default hover:translate-x-5 duration-300 ' +
                (route.name === child.name ? ' text-amber-500!' : '')
              "
              :href="child.meta.frame ? child.path : 'javascript:'"
              target="_blank"
              @click="child.components ? routerTo(child.name) : ''"
            >
              <svg-icon
                :message="child.meta.title"
                :name="child.meta.icon"
                class="py-1.5 px-6 min-w-36 justify-center"
              />
            </a>
          </el-container>
        </template>
      </el-dropdown>
    </template>
    <template v-else-if="item.meta.type === 'B'">
      <a
        :class="
          'one-level-link flex items-center use-link-default relative ' +
          (route.name === item.name ? ' text-amber-500' : '')
        "
        :href="item.meta.frame ? item.path : 'javascript:'"
        target="_blank"
        @click="item.components ? routerTo(item.name) : ''"
      >
        <svg-icon :message="item.meta.title" :name="item.meta.icon" />
      </a>
    </template>
  </el-container>
</template>

<style lang="scss" scoped>
.one-level-link {
  &::before {
    content: '';
    position: absolute;
    bottom: 0;
    width: 0;
    height: 0.45rem;
    border-radius: 1px;
    background-color: rgba(255, 165, 0, 0.9) !important;
    transition: width 0.5s;
  }

  &:hover {
    &::before {
      width: 100%;
    }
  }
}
</style>
