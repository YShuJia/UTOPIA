package cn.yshujia.constant;


/**
 * @author: yshujia
 * @create: 2025/6/7 13:14
 * @description: SecurityConst
 */
public class SecurityConst {

	// Security 登录接口
	public static final String LOGIN = "/login";

	// Security 退出登录接口
	public static final String LOGIN_OUT = "/logout";

	// sm密钥存储、前端请求头携带 public key
	public static final String PUBLIC_KEY = "Public-Key";

	// sm密钥存储 private key
	public static final String PRIVATE_KEY = "Private-Key";

	// 前端请求头携带 Token
	public static final String TOKEN_KEY = "Authorization";

	// 请求头携带 Token 前缀
	public static final String TOKEN_PREFIX = "Bearer ";

}
