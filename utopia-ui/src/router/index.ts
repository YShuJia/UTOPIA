import { createRouter, createWebHistory } from 'vue-router'
import { listRouterApi, type RouterVO } from '@/request/api/router'
import type { ResultType } from '@/request/config'
import { useScrollStore } from '@/stores/scroll'
import { useStyleStore } from '@/stores/style'
import { RouteNameEnum, RouterEnum } from '@/enum'
import { useSystemStore } from '@/stores/system'
import { useGlobalLoading } from '@/hooks'
import type { RouterType } from '@/type'

/*加载所有组件*/
const modules: any = import.meta.glob('../views/**/*.vue', { eager: true })
const map = new Map<string, any>()
for (const path in modules) {
  map.set(path.replace('../views', ''), modules[path].default)
}

// 收集导航栏显示的路由
const routeBar: { [key in 'ui' | 'admin']: RouterType[] } = {
  ui: [],
  admin: []
}

export function getUiRouter(): RouterType[] {
  return routeBar['ui']
}

export function getAdminRouter(): RouterType[] {
  return routeBar['admin']
}

// 收集除外链外的所有路由
const routeAll: { [key in 'ui' | 'admin']: RouterType[] } = {
  ui: [],
  admin: []
}

const createRoute = (item: RouterVO) => {
  const index = item.admin ? 'admin' : 'ui'
  const route: RouterType = {
    path: item.path,
    name: item.name,
    components: item.component ? { [index]: map.get(item.component) } : undefined,
    meta: {
      title: item.title,
      icon: item.icon,
      name: item.name,
      type: item.type,
      admin: item.admin,
      frame: item.frame
    },
    children: []
  }
  return route
}

// 生成动态路由
const createRouteList = (list: RouterVO[]) => {
  list.forEach((item) => {
    // 构建路由
    const routeAllItem = createRoute(item)
    const routeBarItem = createRoute(item)
    // 如果存在子路由，递归调用 setRoute 处理
    if (item.children && item.children.length > 0) {
      for (let i = 0; i < item.children.length; i++) {
        const child = item.children[i]
        const children = createRoute(child)
        // 不是外链（外链不入路由）
        if (!child.frame) {
          routeAllItem.children.push(children)
        }
        // 不是隐藏路由
        if (child.type !== 'H') {
          routeBarItem.children.push(children)
        }
      }
    }
    const index = item.admin ? 'admin' : 'ui'
    // 不是外链（外链不入路由）
    if (!item.frame) {
      routeAll[index].push(routeAllItem)
    }
    // 不是隐藏路由
    if (item.type !== 'H') {
      routeBar[index].push(routeBarItem)
    }
  })
}

// 获取动态路由
await listRouterApi().then((res: ResultType<RouterVO[]>) => {
  createRouteList(res.data)
})

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: RouterEnum.UI,
      name: 'root_ui',
      components: { root: () => import('@/components/UI.vue') },
      children: [...routeAll.ui]
    },
    {
      path: RouterEnum.ADMIN,
      name: 'root_admin',
      components: { root: () => import('@/components/admin/Admin.vue') },
      redirect: {
        name: RouteNameEnum.ADMIN_HOME
      },
      children: [...routeAll.admin]
    },
    {
      path: '/:pathMatch(.*)*',
      name: RouteNameEnum.NOT_FOUND,
      components: { root: () => import('@/views/404/NotFound.vue') }
    }
  ]
})

let loading: ReturnType<typeof useGlobalLoading> | null = null

router.beforeEach((to, from, next) => {
  // 相同路径且不是首页，阻止跳转
  if (to.fullPath === from.fullPath && to.fullPath !== '/') {
    return
  }
  // 刷新时间 自动调整主题色
  useStyleStore().refreshBgColor()
  // 回到顶部
  useScrollStore().scrollTo(0, 0)
  if (!to.meta.admin) {
    loading = useGlobalLoading({
      text: '数据加载中...',
      background:
        useStyleStore().background.bgType === 'dark' ? 'rgb(0, 0, 0)' : 'rgb(255, 255, 255)'
    })
    const timer = setTimeout(() => {
      next()
      clearTimeout(timer)
    }, 500)
  } else {
    next()
  }
})

router.afterEach(async (to) => {
  // 隐藏手机端导航栏
  useSystemStore().system.isShowMobile = false
  if (!useSystemStore().system.isMobile) {
    useSystemStore().system.isShowHeader = true
  }
  // 添加管理端面包屑
  if (to.meta.admin && to.meta.title) {
    useSystemStore().admin.routeMap.set(to.meta.title.toString(), to.fullPath)
  } else {
    //等待 Vue 完成 DOM 更新
    await nextTick()
    const timer = setTimeout(() => {
      loading?.close()
      clearTimeout(timer)
    }, 500)
    // 滚动到指定位置（如果有）
    if (useScrollStore().scroll.isToTop && to.query.id != undefined) {
      const doc = document.getElementById(to.query.id.toString())
      if (doc != null) {
        useScrollStore().scrollTo(doc.offsetTop + doc.clientHeight - 100)
      }
    }
  }
})

export const routerTo = (name: RouteNameEnum | string, params?: any) => {
  router.push({ name: name, params: params })
}

export const routerPath = (path: string, query?: any) => {
  router.push({ path: path, query: query })
}

export default router
