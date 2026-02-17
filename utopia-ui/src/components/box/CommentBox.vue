<script lang="ts" setup>
import type { CommentDTO } from '@/request/api/comment'
import { type CommentVO } from '@/request/api/comment'
import { type ResultType } from '@/request/config'
import { useSystemStore } from '@/stores/system'
import { fromNow } from '@/utils/timeUtils'
import { getLikeApi, updateLikeApi } from '@/request/api/like'

const systemStore = useSystemStore()

const comment = defineModel<
  CommentDTO & {
    isReply?: boolean
    parentName?: string
    replayContent?: string[]
  }
>('comment', {
  default: () => ({})
})

const item = defineModel<CommentVO>('item', {
  default() {
    return {
      id: 0,
      floorId: 0,
      userId: 0,
      parentUserId: 0,
      username: '',
      parentUsername: '',
      avatar: '',
      parentAvatar: '',
      roleName: '',
      parentRoleName: '',
      likeCount: 0,
      content: [],
      replayContent: [],
      createTime: new Date()
    }
  }
})

const reply = (item: CommentVO) => {
  // 预设置评论的楼层， 评论的父级用户ID， 评论的回复内容
  comment.value.floorId = item.floorId
  comment.value.parentUserId = item.userId
  comment.value.replayContent = item.content
  comment.value.isReply = true
  comment.value.parentName = item.username
}

const liked = ref(false)
const like = () => {
  getLikeApi(item.value.id).then((res: ResultType<boolean>) => {
    liked.value = res.data
  })
}

const updateLike = () => {
  updateLikeApi(item.value.id, liked.value).catch(() => {
    liked.value = !liked.value
  })
}

onMounted(() => {
  like()
})
</script>

<template>
  <el-container class="gap-1 max-h-fit" direction="vertical">
    <el-container class="gap-2 items-center">
      <image-box
        :src="item.avatar"
        class="size-12! radius-sm"
        @click="systemStore.preview.open(item.avatar)"
      />
      <el-container class="gap-1.5" direction="vertical">
        <el-container class="items-center text-amber-500 line-clamp-1">
          <span>{{ item.username }}</span>
          <span class="text-sm text-teal-300">【{{ item.roleName }}】</span>
        </el-container>
        <el-container class="items-center justify-between">
          <span class="text-sm line-clamp-1 text-gray-500">
            {{ fromNow(item.createTime) }}
          </span>
          <span class="flex items-center gap-1">
            <a class="" href="javascript:">
              <label
                :class="`py-0.5 px-1 min-w-fit flex cursor-pointer text-sm use-btn-default ${liked ? 'bg-amber-500' : ''}`"
              >
                <input
                  id="heart"
                  :key="item.id"
                  v-model="liked"
                  class="hidden"
                  type="checkbox"
                  @change="updateLike"
                />
                点赞{{ item.likeCount }}
              </label>
            </a>
            <a
              class="h-fit py-0.5 px-1 min-w-fit text-sm use-btn-default"
              href="javascript:"
              @click="reply(item)"
            >
              回复
            </a>
          </span>
        </el-container>
      </el-container>
    </el-container>
    <el-container class="pl-14 gap-5 max-sm:pl-5" direction="vertical">
      <el-container class="w-full p-3 max-md:flex-wrap bg-color-three radius-sm gap-2">
        <!--     渲染评论回复内容     -->
        <el-container
          v-if="item.replayContent"
          class="p-1.5 gap-1 min-w-fit bg-color radius-sm"
          direction="vertical"
        >
          <el-container class="gap-1 max-h-fit break-words">
            <template v-if="item.replayContent[0]">
              <span class="leading-6 line-clamp-2 max-w-64">
                <span class="text-amber-500">@{{ item.parentUsername }}</span
                >{{ item.replayContent[0] }}</span
              >
            </template>
          </el-container>
          <template v-if="item.replayContent.length > 1">
            <image-box
              :lazy="false"
              :src="item.replayContent[1]"
              class="h-16 w-fit radius-sm"
              @click="systemStore.preview.open(item!.replayContent![1])"
            />
          </template>
        </el-container>
        <!--     渲染评论主要内容     -->
        <el-container class="gap-1 w-full justify-center overflow-hidden" direction="vertical">
          <template v-if="item.content[0]">
            <span class="leading-6">{{ item.content[0] }}</span>
          </template>
          <template v-if="item.content.length > 1">
            <image-box
              :lazy="false"
              :src="item.content[1]"
              class="h-24 w-fit radius-sm"
              @click="systemStore.preview.open(item.content[1])"
            />
          </template>
        </el-container>
      </el-container>
      <!--     渲染子评论     -->
      <el-container v-for="child in item.children" v-if="item.children" class="h-fit">
        <comment-box v-model:comment="comment" :item="child" />
      </el-container>
    </el-container>
  </el-container>
</template>
