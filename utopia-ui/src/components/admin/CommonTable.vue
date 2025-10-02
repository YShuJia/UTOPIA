<script lang="ts" setup>
import {useGlobalDialog} from '@/hooks'
import {initPageVO, type PageVO, type ResultType} from '@/request/config'
import {ClassifyEnum, RouteNameEnum, TableEnum} from '@/enum'
import {useStyleStore} from '@/stores/style'
import {useSystemStore} from '@/stores/system'

const router = useRouter()
const styleStore = useStyleStore()
const systemStore = useSystemStore()

const reviewedMsg = ['已拒绝', '已同意', '待审核']
const reviewedTag: any = ['danger', 'success', 'warning']

type TagType = {
  component: 'tag'
  labels?: Map<any, string>
  types?: Map<any, 'success' | 'warning' | 'info' | 'primary' | 'danger'>
}
type ImgType = {
  component: 'img'
  type: 'string' | 'array'
}
type SwitchType = {
  component: 'switch'
  inactiveText?: string
  activeText?: string
  activeValue?: boolean | string | number
  inactiveValue?: boolean | string | number
}

type OperationType = {
  component: 'operation'
  type: 'dialog' | RouteNameEnum
}
type SvgType = {
  component: 'svg'
}

export type ColumnsType = {
  label?: string
  prop: string
  align?: 'center' | 'left' | 'right'
  // 根据数据类型使用不同的组件渲染
  dataType?: TagType | ImgType | SwitchType | OperationType | SvgType
  fixed?: 'left' | 'right' | boolean
  minWidth?: string | number
  sortable?: boolean
  type?: 'default' | 'selection' | 'index' | 'expand'
  overflowTooltip?: boolean
}

const {
  selectApi,
  updateApi,
  deleteApi,
  updateReviewedApi,
  voToDTO,
  columns,
  table,
  classifyKey,
  pageSize,
  showAdd,
  showDel
} = defineProps({
  selectApi: {
    type: Function,
    required: true
  },
  columns: {
    type: Array as PropType<ColumnsType[]>,
    required: true
  },
  // 表名
  table: {
    type: String as PropType<TableEnum>,
    required: true
  },
  voToDTO: {
    type: Function
  },
  // 根据 key 构建分类、标签下拉框
  classifyKey: {
    type: String as PropType<ClassifyEnum>
  },
  updateApi: {
    type: Function,
    default: () => {}
  },
  deleteApi: {
    type: Function,
    default: () => {}
  },
  updateReviewedApi: {
    type: Function,
    default: () => {}
  },
  pageSize: {
    type: Number,
    default: 10
  },
  showAdd: {
    type: Boolean,
    default: true
  },
  showDel: {
    type: Boolean,
    default: true
  }
})

const loading = ref(true)

// 接收数据
const vo = ref<PageVO<any>>(initPageVO())

// 用于查询的参数
const dto = ref({
  pageNum: 1,
  pageSize: pageSize
})

// 传递给弹窗用于新增和修改的参数
const form = ref({})

// 批量删除选中项
const ids = ref<number[]>([])
const handleSelectionChange = (selection: any[]) => {
  ids.value = selection.map((item) => item.id)
}

// 查询参数改变、分页改变等刷新数据
const select = async () => {
  selectApi(dto.value)
    .then((res: ResultType<PageVO<any>>) => {
      vo.value = res.data
    })
    .finally(() => {
      loading.value = false
    })
}

const update = (dto: any) => {
  updateApi(dto).then(() => {
    select()
  })
}

/** 删除按钮操作 */
const del = async (idArr: Array<number>) => {
  await useGlobalDialog('确定删除ID 为 "' + idArr + '" 的数据?').then((res: boolean) => {
    if (res) {
      deleteApi(idArr).then((res: ResultType<boolean>) => {
        if (res.data) {
          select()
          ids.value = []
        }
      })
    }
  })
}

// 修改审核
const options = [
  {
    label: '同意',
    value: 1,
    name: 'success'
  },
  {
    label: '待审核',
    value: 2
  },
  {
    label: '拒绝',
    value: 0,
    name: 'error'
  }
]
const reviewedChange = async (row: any) => {
  useGlobalDialog(
    '确定' + (row.reviewed ? '同意 “' : '拒绝 “') + (row.name ?? row.title) + '” 的提交?'
  ).then((res) => {
    if (res) {
      updateReviewedApi({ id: row.id, reviewed: row.reviewed, updateStatus: true })
        .then(() => select())
        .catch(() => {
          row.reviewed = 2
        })
    } else {
      row.reviewed = 2
    }
  })
}

// 打开修改弹窗
const add = ref(true)
const openDialog = (isAdd: boolean, row?: any) => {
  form.value = {}
  add.value = isAdd
  if (!add.value && row) {
    form.value = voToDTO?.(row)
  }
  systemStore.dialog[table] = true
}

onMounted(() => {
  select()
})
</script>

<template>
  <el-container class="gap-5 size-full overflow-visible" direction="vertical">
    <el-container v-if="showAdd || showDel">
      <slot name="button" />
      <el-button v-if="showAdd" class="!px-2" plain type="primary" @click="openDialog(true)">
        <svg-icon message="新增" name="add" />
      </el-button>
      <el-button
        v-if="showDel"
        :disabled="ids.length === 0"
        class="!px-2"
        plain
        type="danger"
        @click="del(ids)"
      >
        <svg-icon message="删除" name="delete" />
      </el-button>
    </el-container>
    <query-form v-model:query="dto" :change="select" :classify-key="classifyKey" :table="table" />
    <el-table
      v-loading="loading"
      :data="vo.list"
      :tooltip-effect="styleStore.background.bgType"
      border
      class="use-theme"
      height="100%"
      row-key="id"
      table-layout="auto"
      @selection-change="handleSelectionChange"
    >
      <template v-for="item in columns">
        <el-table-column
          :fixed="item.fixed"
          :label="item.label"
          :min-width="item.minWidth"
          :prop="item.prop"
          :show-overflow-tooltip="item.overflowTooltip"
          :sortable="item.sortable"
          :type="item.type"
        >
          <template #default="scope">
            <!--  渲染 tag  -->
            <template v-if="item.dataType?.component === 'tag'">
              <el-tag
                v-if="item.dataType.types && item.dataType.labels"
                :type="item.dataType.types.get(scope.row[item.prop])"
                >{{ item.dataType.labels.get(scope.row[item.prop]) }}
              </el-tag>
              <el-tag v-else :type="reviewedTag[scope.row.reviewed]">
                {{ reviewedMsg[scope.row.reviewed] }}
              </el-tag>
            </template>
            <!--  渲染图片  -->
            <template v-if="item.dataType?.component === 'img' && scope.row[item.prop]">
              <image-box
                v-if="item.dataType.type === 'array'"
                :src="scope.row[item.prop][0]"
                class="radius-sm min-w-28"
                @click="systemStore.preview.open(scope.row[item.prop])"
              />
              <image-box
                v-else-if="item.prop !== 'avatar'"
                :src="scope.row[item.prop]"
                class="radius-sm min-w-28"
                @click="systemStore.preview.open(scope.row[item.prop])"
              />
              <image-box
                v-else
                :src="scope.row[item.prop]"
                class="rounded-full !size-12"
                @click="systemStore.preview.open(scope.row[item.prop])"
              />
            </template>
            <!--  渲染 svg   -->
            <template v-if="item.dataType?.component === 'svg'">
              <svg-icon :name="scope.row[item.prop]" class="size-4" />
            </template>
            <!--  渲染 switch 组件  -->
            <template v-if="item.dataType?.component === 'switch'">
              <el-switch
                v-model="scope.row[item.prop]"
                :active-text="item.dataType.activeText ? item.dataType.activeText : '开启'"
                :active-value="item.dataType.activeValue ? item.dataType.activeValue : true"
                :before-change="
                  () =>
                    useGlobalDialog(
                      '确定修改 “' +
                        (scope.row.name ?? scope.row.title ?? scope.row.username) +
                        '” 的' +
                        item.label +
                        ' ?'
                    )
                "
                :inactive-text="item.dataType.inactiveText ? item.dataType.inactiveText : '关闭'"
                :inactive-value="item.dataType.inactiveValue ? item.dataType.inactiveValue : false"
                inline-prompt
                size="large"
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                @change="
                  update({
                    id: scope.row.id,
                    [item.prop]: scope.row[item.prop],
                    updateStatus: true
                  })
                "
              />
            </template>
            <!--  渲染操作列  -->
            <template v-if="item.dataType?.component === 'operation'">
              <el-container class="items-center flex-wrap justify-center gap-1">
                <el-button
                  class="!px-1 !py-1.5"
                  size="small"
                  type="primary"
                  @click="
                    item.dataType.type === 'dialog'
                      ? openDialog(false, scope.row)
                      : router.push({
                          name: item.dataType.type,
                          params: {
                            id: scope.row.id
                          }
                        })
                  "
                >
                  <svg-icon message="编辑" name="edit" size="sm" />
                </el-button>
                <template v-if="scope.row.reviewed === 2">
                  <el-segmented
                    v-model="scope.row.reviewed"
                    :options="options"
                    size="small"
                    @change="reviewedChange(scope.row)"
                  >
                    <template #default="scope: any">
                      <svg-icon :message="scope.item.label" :name="scope.item.name" />
                    </template>
                  </el-segmented>
                </template>
              </el-container>
            </template>
          </template>
        </el-table-column>
      </template>
    </el-table>
    <page-box v-model:dto="dto" :change="select" :total="vo.total" />
  </el-container>

  <album-dialog v-model:form="form" :refresh="select" />
  <file-dialog v-model:form="form" :refresh="select" />
  <router-dialog v-model:form="form" :refresh="select" />
  <classify-dialog v-model:form="form" :refresh="select" />
  <label-dialog v-model:form="form" :refresh="select" />
  <website-dialog v-model:form="form" :refresh="select" />
  <user-dialog v-model:form="form" :refresh="select" />
  <role-dialog v-model:form="form" :refresh="select" />
  <diary-dialog v-model:form="form" :refresh="select" />
  <leave-word-dialog v-model:form="form" :refresh="select" />
  <sys-config-dialog v-model:form="form" :refresh="select" />
</template>

<style lang="scss" scoped></style>
