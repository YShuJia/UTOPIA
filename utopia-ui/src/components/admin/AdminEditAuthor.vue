<script lang="ts" setup>
import { initPageVO, type PageVO, type ResultType } from '@/request/config'
import { type FileVO, pageFileSelectAdminApi } from '@/request/api/file'
import { useTemporaryStore } from '@/stores/temporary'
import { uploadWebConfigFileApi } from '@/request/api/web_config'

const store = useTemporaryStore()

const rules = {
  authorName: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
  authorAvatar: [{ required: true, message: '头像不能为空', trigger: 'blur' }],
  authorTag: [{ required: true, message: '标签不能为空', trigger: 'blur' }],
  authorContact: [{ required: true, message: '联系方式不能为空', trigger: 'blur' }],
  authorGame: [{ required: true, message: '游戏不能为空', trigger: 'blur' }],
  authorBook: [{ required: true, message: '书籍不能为空', trigger: 'blur' }],
  authorFootprint: [{ required: true, message: '足迹不能为空', trigger: 'blur' }],
  authorMbti: [{ required: true, message: 'MBTI 不能为空', trigger: 'blur' }],
  authorSkill: [{ required: true, message: '技能特长不能为空', trigger: 'blur' }],
  authorHome: [{ required: true, message: '主页不能为空', trigger: 'blur' }]
}

const tag = ref('')
const tagVisible = ref(false)
const tagInput = () => {
  if (tag.value) {
    store.webConfig.authorTag.push(tag.value)
    tag.value = ''
    tagVisible.value = false
    console.log(store.webConfig.authorTag)
  }
}

const book = ref('')
const bookVisible = ref(false)
const inputBook = () => {
  if (book.value) {
    store.webConfig.authorBook.push(book.value)
    book.value = ''
    bookVisible.value = false
  }
}

/* 技能特长 */
const skillVO = ref<PageVO<FileVO>>(initPageVO())
const getList = () => {
  pageFileSelectAdminApi({
    labelId: 10000100003,
    pageNum: 1,
    pageSize: 999
  }).then((res: ResultType<PageVO<FileVO>>) => {
    skillVO.value = res.data
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <el-container direction="vertical">
    <el-form :model="store.webConfig" :rules="rules" class="flex flex-col gap-5">
      <el-form-item label="作者名称" prop="authorName">
        <el-input
          v-model="store.webConfig.authorName"
          :maxlength="32"
          placeholder="请输入 （max 32）"
        />
      </el-form-item>
      <el-form-item label="作者头像" prop="authorAvatar">
        <el-input
          v-model="store.webConfig.authorAvatar"
          :maxlength="128"
          placeholder="请输入 （max 128）"
        />
      </el-form-item>
      <el-container class="gap-3" direction="vertical">
        <el-form-item label="个性化标签" prop="authorTag">
          <el-button type="primary" @click="tagVisible = true">新建标签</el-button>
        </el-form-item>
        <el-container class="gap-2 px-5 flex-wrap">
          <el-tag
            v-for="(tag, i) in store.webConfig.authorTag"
            :key="i"
            closable
            size="large"
            @close="store.webConfig.authorTag.splice(i, 1)"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-if="tagVisible"
            ref="InputRef"
            v-model="tag"
            class="max-w-32"
            @blur="tagInput"
            @keyup.enter="tagInput"
          />
        </el-container>
      </el-container>
      <el-container class="gap-3" direction="vertical">
        <el-form-item label="联系方式配置" prop="authorContact">
          <el-button
            class="button-new-tag"
            type="primary"
            @click="
              store.webConfig.authorContact.push({
                name: '',
                url: '',
                type: ''
              })
            "
          >
            新建联系方式
          </el-button>
        </el-form-item>
        <el-container class="gap-2 px-5" direction="vertical">
          <el-container
            v-for="(p, i) in store.webConfig.authorContact"
            :key="i"
            class="gap-2 flex-wrap"
          >
            <el-form-item :label="'联系方式' + i + 1">
              <el-input
                v-model="p.name"
                :maxlength="32"
                label="联系方式名称"
                placeholder="请输入 （max 32）"
              />
            </el-form-item>
            <el-form-item :label="'联系方式链接' + i + 1">
              <el-input v-model="p.url" :maxlength="32" placeholder="请输入 （max 32）" />
            </el-form-item>
            <el-form-item :label="'类型' + i + 1">
              <el-select v-model="p.type" class="min-w-32">
                <el-option label="账号" value="account" />
                <el-option label="链接" value="link" />
                <el-option label="图片地址" value="img" />
              </el-select>
            </el-form-item>
            <el-button
              class="button-new-tag"
              type="danger"
              @click="store.webConfig.authorContact.splice(i, 1)"
            >
              删除
            </el-button>
          </el-container>
        </el-container>
      </el-container>
      <el-container class="gap-3" direction="vertical">
        <el-form-item label="支付方式配置" prop="authorContact">
          <el-button
            class="button-new-tag"
            type="primary"
            @click="
              store.webConfig.authorPayment.push({
                name: '',
                url: ''
              })
            "
          >
            新建支付方式
          </el-button>
        </el-form-item>
        <el-container class="gap-2 px-5" direction="vertical">
          <el-container
            v-for="(p, i) in store.webConfig.authorPayment"
            :key="i"
            class="gap-2 flex-wrap"
          >
            <el-form-item :label="'支付方式' + i + 1">
              <el-input v-model="p.name" :maxlength="32" placeholder="请输入 （max 32）" />
            </el-form-item>
            <el-form-item :label="'收款码链接' + i + 1">
              <el-input v-model="p.url" :maxlength="32" placeholder="请输入 （max 32）" />
            </el-form-item>
            <el-button
              class="button-new-tag"
              type="danger"
              @click="store.webConfig.authorPayment.splice(i, 1)"
            >
              删除
            </el-button>
          </el-container>
        </el-container>
      </el-container>
      <el-form-item label="家乡坐标配置" label-position="top" prop="authorHome">
        <el-container class="gap-2 px-5 py-2 flex-wrap">
          <el-form-item label="经度">
            <el-input
              v-model="store.webConfig.authorHome[0]"
              class="w-40 mr-2"
              placeholder="请输入"
            />
          </el-form-item>
          <el-form-item label="纬度">
            <el-input v-model="store.webConfig.authorHome[1]" class="w-40" placeholder="请输入" />
          </el-form-item>
        </el-container>
      </el-form-item>
      <el-form-item label="MBTI人格配置" label-position="top" prop="authorContact">
        <el-container class="gap-5 px-5 py-2 flex-wrap">
          <el-form-item label="名称">
            <el-input
              v-model="store.webConfig.authorMbti.name"
              maxlength="16"
              placeholder="请输入 （max 16）"
            />
          </el-form-item>
          <el-form-item label="E">
            <el-input-number
              v-model="store.webConfig.authorMbti.E"
              :max="100"
              :min="0"
              placeholder="请输入 （max 100）"
            />
          </el-form-item>
          <el-form-item label="S">
            <el-input-number
              v-model="store.webConfig.authorMbti.S"
              :max="100"
              :min="0"
              placeholder="请输入 （max 100）"
            />
          </el-form-item>
          <el-form-item label="T">
            <el-input-number
              v-model="store.webConfig.authorMbti.T"
              :max="100"
              :min="0"
              placeholder="请输入 （max 100）"
            />
          </el-form-item>
          <el-form-item label="J">
            <el-input-number
              v-model="store.webConfig.authorMbti.J"
              :max="100"
              :min="0"
              placeholder="请输入 （max 100）"
            />
          </el-form-item>
        </el-container>
      </el-form-item>
      <el-form-item label="技能配置" label-position="top" prop="authorContact">
        <el-checkbox-group v-model="store.webConfig.authorSkill">
          <el-checkbox-button v-for="p in skillVO.list" :key="p.url" :value="p.url">
            <img :src="p.url" alt="" class="min-w-8 radius-lg cursor-pointer" />
          </el-checkbox-button>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="游戏配置" label-position="top" prop="authorContact">
        <el-checkbox-group v-model="store.webConfig.authorGame">
          <el-checkbox-button v-for="p in skillVO.list" :key="p.url" :value="p.url">
            <img :src="p.url" alt="" class="min-w-8 radius-lg cursor-pointer" />
          </el-checkbox-button>
        </el-checkbox-group>
      </el-form-item>
      <el-container class="gap-3" direction="vertical">
        <el-form-item label="书籍配置" prop="authorTag">
          <el-button type="primary" @click="bookVisible = true">新建书籍</el-button>
        </el-form-item>
        <el-container class="gap-2 px-5 flex-wrap">
          <el-tag
            v-for="(p, i) in store.webConfig.authorBook"
            :key="p"
            closable
            size="large"
            @close="store.webConfig.authorBook.splice(i, 1)"
          >
            {{ p }}
          </el-tag>
          <el-input
            v-if="bookVisible"
            ref="InputRef"
            v-model="book"
            class="max-w-32"
            placeholder="请输入书籍名称"
            @blur="inputBook"
            @keyup.enter="inputBook"
          />
        </el-container>
      </el-container>
      <el-container class="gap-3" direction="vertical">
        <el-form-item label="足迹配置" prop="authorContact">
          <el-button
            class="button-new-tag"
            type="primary"
            @click="
              store.webConfig.authorFootprint.push({
                name: '',
                position: '',
                time: new Date()
              })
            "
          >
            新建足迹
          </el-button>
        </el-form-item>
        <el-container class="gap-2 px-5" direction="vertical">
          <el-container
            v-for="(p, i) in store.webConfig.authorFootprint"
            :key="i"
            class="gap-2 flex-wrap"
          >
            <el-form-item :label="'地名' + i + 1">
              <el-input
                v-model="p.name"
                :maxlength="32"
                label="联系方式名称"
                placeholder="请输入 （max 32）"
              />
            </el-form-item>
            <el-form-item :label="'经纬度' + i + 1">
              <el-input v-model="p.position" :maxlength="32" placeholder="请输入 （max 32）" />
            </el-form-item>
            <el-form-item :label="'时间' + i + 1">
              <el-date-picker
                v-model="p.time"
                placeholder="选择日期时间"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
            <el-button
              class="button-new-tag"
              type="danger"
              @click="store.webConfig.authorFootprint.splice(i, 1)"
            >
              删除
            </el-button>
          </el-container>
        </el-container>
      </el-container>
      <el-form-item label="关于作者" label-position="top" prop="authorContact">
        <custom-editor
          v-model:text="store.webConfig.authorAbout"
          v-model:urls="store.webConfig.authorUrls"
          :upload="uploadWebConfigFileApi"
        />
      </el-form-item>
    </el-form>
  </el-container>
</template>

<style lang="scss">
.el-checkbox-button__inner {
  border: none !important;
  border-radius: 10px !important;
}

.el-checkbox-group {
  gap: 10px !important;
}
</style>
