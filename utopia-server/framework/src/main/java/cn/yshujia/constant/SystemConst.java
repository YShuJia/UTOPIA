package cn.yshujia.constant;


import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 系统常量
 */

@Component
public class SystemConst implements Serializable {
	
	// 限流 key
	public static final String RATE_LIMIT_KEY = "rate_limit:";
	
	// 敏感数据替换符
	public static final String SENSITIVE_REPLACE = "[非礼勿言]";
	
	// 每天最大经验值
	public static final Integer MAX_EXPERIENCE = 300;
	
}
