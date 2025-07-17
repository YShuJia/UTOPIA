package cn.yshujia.constant;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 枚举响应码
 */

public class HttpCode {
	
	// 2000 成功
	public final static int SUCCESS = 2000;
	
	// 2001 需要解密
	public final static int NEED_DECRYPTION = 2001;
	
	// 4000 失败
	public final static int FAILURE = 4000;
	
	// 2002 验证码错误！
	public final static int CODE_ERROR = 2002;
	
	// 2003 触发限流
	public final static int TRIGGER_LIMIT = 2003;
	
	// 3001 资源被移除
	public final static int MOVED_PERM = 3001;
	
	// 4001 参数校验失败
	public final static int BAD_PARAMS = 4001;
	
	// 4002 未授权或授权过期，请重新登录
	public final static int UNAUTHORIZED = 4002;
	
	// 4003 暂无权限访问
	public final static int FORBIDDEN = 4003;
	
	// 4004 资源未找到
	public final static int NOT_FOUND = 4004;
	
	// 4005 该账号已被注册
	public final static int HAD_USER = 4005;
	
	// 4006 账号或密码错误
	public final static int NON_USER = 4006;
	
	// 5000 服务器未知异常
	public final static int SERVER_ERROR = 5000;
	
}