<script lang="ts" setup>
import { type ClassifyEnum, TableEnum } from '@/enum'

const { classifyKey, change, table } = defineProps({
  classifyKey: {
    type: String as PropType<ClassifyEnum>,
    default: undefined
  },
  change: {
    type: Function,
    required: true
  },
  table: {
    type: String as PropType<TableEnum>,
    required: true
  }
})

const query = defineModel('query', {
  type: Object
})

const searchClick = () => {
  const result =
    query.value.title === '' &&
    query.value.name === '' &&
    query.value.reviewed === -1 &&
    query.value.enabled === -1 &&
    query.value.reviewed === -1
  if (!result) {
    change()
  } else {
    ElMessage.warning('请输入查询条件！')
  }
}

const reset = () => {
  query.value.classifyId = undefined
  query.value.labelId = undefined
  query.value.title = undefined
  query.value.name = undefined
  query.value.admin = undefined
  query.value.recommend = undefined
  query.value.likeable = undefined
  query.value.commentable = undefined
  query.value.enabled = undefined
  query.value.reviewed = undefined
  query.value.type = undefined
  query.value.frame = undefined
  query.value.content = undefined
  change()
}
</script>

<template>
  <el-container class="gap-x-5 gap-y-3 flex-wrap query-form">
    <el-form-item v-if="table !== 'leave_word' && table !== 'comment'" class="w-52" label="名称">
      <el-input
        v-model="query.name"
        maxlength="16"
        placeholder="请输入名称/标题"
        @change="
          () => {
            query.title = query.name
            query.username = query.name
          }
        "
        @keyup.enter.native="change"
      />
    </el-form-item>
    <el-form-item v-if="table === 'leave_word'" class="w-52" label="内容">
      <el-input
        v-model="query.content"
        maxlength="16"
        placeholder="请输入内容"
        @keyup.enter.native="change"
      />
    </el-form-item>
    <el-form-item v-if="table === 'user'" class="w-52" label="性别">
      <el-select v-model="query.gender">
        <el-option :value="-1" label="女" />
        <el-option :value="1" label="男" />
        <el-option :value="0" label="保密" />
      </el-select>
    </el-form-item>

    <classify-label-form
      v-if="
        table === 'label' ||
        table === 'file' ||
        table === 'article' ||
        table === 'album' ||
        table === 'diary' ||
        table === 'website'
      "
      v-model:classify-id="query.classifyId"
      v-model:label-id="query.labelId"
      :classify-key="classifyKey"
    />

    <el-form-item v-if="table === 'role'" class="w-52" label="管理员">
      <el-select v-model="query.admin">
        <el-option :value="true" label="是" />
        <el-option :value="false" label="否" />
      </el-select>
    </el-form-item>

    <el-form-item
      v-if="table !== 'leave_word' && table !== 'comment'"
      class="w-52"
      label="启停状态"
    >
      <el-select v-model="query.enabled">
        <el-option :value="true" label="启用" />
        <el-option :value="false" label="停用" />
      </el-select>
    </el-form-item>

    <el-form-item v-if="table === 'website'" class="w-52" label="推荐状态">
      <el-select v-model="query.recommend">
        <el-option :value="true" label="开启" />
        <el-option :value="false" label="关闭" />
      </el-select>
    </el-form-item>

    <el-container v-if="table === 'router'" class="gap-x-5 max-w-fit">
      <el-form-item class="w-52" label="路由类型">
        <el-select v-model="query.type">
          <el-option label="菜单" value="M" />
          <el-option label="按钮" value="B" />
          <el-option label="其他" value="H" />
        </el-select>
      </el-form-item>
      <el-form-item class="w-52" label="管理员">
        <el-select v-model="query.admin">
          <el-option :value="true" label="是" />
          <el-option :value="false" label="否" />
        </el-select>
      </el-form-item>
      <el-form-item class="w-52" label="是否外链">
        <el-select v-model="query.frame">
          <el-option :value="true" label="是" />
          <el-option :value="false" label="否" />
        </el-select>
      </el-form-item>
    </el-container>

    <el-container
      v-if="table === 'article' || table === 'album' || table === 'diary'"
      class="gap-x-5 max-w-fit"
    >
      <el-form-item class="w-52" label="评论状态">
        <el-select v-model="query.commentable">
          <el-option :value="true" label="开启" />
          <el-option :value="false" label="关闭" />
        </el-select>
      </el-form-item>
      <el-form-item class="w-52" label="点赞状态">
        <el-select v-model="query.likeable">
          <el-option :value="true" label="开启" />
          <el-option :value="false" label="关闭" />
        </el-select>
      </el-form-item>
      <el-form-item class="w-52" label="推荐状态">
        <el-select v-model="query.recommend">
          <el-option :value="true" label="开启" />
          <el-option :value="false" label="关闭" />
        </el-select>
      </el-form-item>
    </el-container>

    <el-form-item
      v-if="
        table === 'label' ||
        table === 'file' ||
        table === 'article' ||
        table === 'album' ||
        table === 'diary' ||
        table === 'website'
      "
      class="w-52"
      label="审核状态"
    >
      <el-select v-model="query.reviewed">
        <el-option :value="1" label="已通过" />
        <el-option :value="0" label="未通过" />
        <el-option :value="2" label="待审核" />
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button class="!px-2" plain type="primary" @click="searchClick">
        <svg-icon message="搜索" name="search" />
      </el-button>
      <el-button class="!px-2" plain type="info" @click="reset">
        <svg-icon message="重置" name="refresh" />
      </el-button>
    </el-form-item>
  </el-container>
</template>

<style lang="scss">
.query-form {
  max-width: calc(100vw - 15rem);
}
</style>
