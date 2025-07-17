export enum Code {
  // 成功
  SUCCESS = 2000,
  // 需要解密
  NEED_DECRYPTION = 2001,
  // 验证码错误
  CODE_ERROR = 2002,
  // 触发限流
  TRIGGER_LIMIT = 2003,
  // 资源已转移
  MOVED_PERM = 3001,
  // 失败
  FAILURE = 4000,
  // 参数错误
  BAD_REQUEST = 4001,
  // 授权过期
  UNAUTHORIZED = 4002,
  // 暂无权限访问
  FORBIDDEN = 4003,
  NOT_FOUND = 4004,
  HAD_USER = 4005,
  NON_USER = 4006,
  SERVER_ERROR = 5000
}
