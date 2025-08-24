export enum ClickEnum {
  CHAR = 'CHAR',
  SQUARE = 'SQUARE',
  LINE = 'LINE',
  LOVE = 'LOVE',
  MARIO = 'MARIO',
  CIRCLE = 'CIRCLE'
}

export enum RouteNameEnum {
  HOME = 'home',
  LOGIN = 'login',
  NOT_FOUND = 'not_found',
  ARCHIVES = 'archives',
  LEAVE_WORD = 'leave_word',
  ARTICLE = 'article',
  ARTICLE_EDIT = 'article_edit',
  ARTICLE_DETAIL = 'article_detail',
  TIME = 'time',
  ALBUM = 'album',
  ALBUM_DETAIL = 'album_detail',
  DIARY = 'diary',
  DIARY_DETAIL = 'diary_detail',
  MORE = 'more',
  ABOUT = 'about',
  USER_INFO = 'user_info',
  ADMIN_HOME = 'admin_home',
  FRIEND = 'friends'
}

export enum RouterEnum {
  UI = '/',
  ADMIN = '/admin',
  LOGIN = '/login'
}

export enum ClassifyEnum {
  NULL = 'sys:classify',
  FILE = 'sys:classify:file',
  ARTICLE = 'sys:classify:article',
  DEPLOY = 'sys:classify:article:deploy',
  DIARY = 'sys:classify:diary',
  WEBSITE = 'sys:classify:website',
  ALBUM = 'sys:classify:album'
}

export enum TableEnum {
  // 路由修改、新增弹窗
  ROUTER = 'router',
  // 资源修改、新增弹窗
  FILE = 'file',
  // 类别修改、新增弹窗
  CLASSIFY = 'classify',
  // 标签修改、新增弹窗
  LABEL = 'label',
  // 用户修改、新增弹窗
  USER = 'user',
  // 相册修改、新增弹窗
  ALBUM = 'album',
  // 日记修改、新增弹窗
  DIARY = 'diary',
  // 角色修改、新增弹窗
  ROLE = 'role',
  // 评论修改、新增弹窗
  COMMENT = 'comment',
  // 文章详细 弹窗
  ARTICLE = 'article',
  // 网站修改、新增弹窗
  WEBSITE = 'website',
  // 树洞弹窗
  LEAVE_WORD = 'leave_word'
}

export enum DialogEnum {
  // 登录弹窗状态
  LOGIN = 'login',
  //导航栏搜索框弹出状态
  SEARCH = 'search',
  // 设置弹窗状态
  SETTING = 'setting',
  // 资源表单修改、新增弹窗
  MEDIA_FORM = 'media_form'
}
