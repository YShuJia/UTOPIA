<script lang="ts" setup>
import { RouterEnum } from '@/enum'
import type { RouterType } from '@/type'
import { routerTo } from '@/router'

const { data } = defineProps({
  data: {
    type: Array<RouterType>,
    default: () => []
  }
})

const openUrl = (item: RouterType) => {
  if (item.meta.frame) {
    window.location.href = item.path
  } else {
    routerTo(item.name)
  }
}
</script>

<template>
  <template v-for="item in data">
    <el-menu-item
      v-if="item.meta.type === 'B'"
      @click="openUrl(item)"
      :index="RouterEnum.ADMIN + '/' + item.path.toString()"
    >
      <svg-icon :name="item.meta.icon" class="pl-0.5" />
      <span class="pl-5 pr-20">{{ item.meta.title }}</span>
    </el-menu-item>
    <el-sub-menu
      v-if="item.meta.type === 'M'"
      :index="RouterEnum.ADMIN + '/' + item.path?.toString()"
    >
      <template #title>
        <svg-icon :name="item.meta.icon" class="pl-0.5" />
        <span class="pl-5 pr-20">{{ item.meta.title }}</span>
      </template>
      <template v-for="child in item.children">
        <a
          v-if="child.meta.type === 'B'"
          :href="child.meta.frame ? child.path : 'javascript:'"
          @click="child.components ? routerTo(child.name) : ''"
        >
          <el-menu-item :index="RouterEnum.ADMIN + '/' + item.path + '/' + child.path">
            <svg-icon
              :message="child.meta.title"
              :name="child.meta.icon"
              class="size-full pl-1 gap-5"
            />
          </el-menu-item>
        </a>
        <router-menu v-else-if="child.meta.type === 'M'" :data="[child]" />
      </template>
    </el-sub-menu>
  </template>
</template>

<style lang="scss"></style>
