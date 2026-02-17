<script lang="ts" setup>
const { src, lazy, fit } = defineProps({
  src: {
    type: String as unknown as PropType<string | undefined>,
    required: true
  },
  lazy: {
    type: Boolean,
    default: true
  },
  fit: {
    type: String as PropType<
      'object-cover' | 'object-contain' | 'object-fill' | 'object-none' | 'object-scale-down'
    >,
    default: 'object-cover'
  }
})
</script>

<template>
  <a class="flex translate-x-0 size-full overflow-hidden" href="javascript:">
    <img
      v-if="src"
      v-img-lazy="lazy ? src : ''"
      :class="`size-full ${fit}`"
      :src="lazy ? '' : src"
      alt=""
    />
  </a>
</template>
