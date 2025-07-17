package cn.yshujia.annotation;

import java.lang.annotation.*;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 节流
 */


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
	
	/**
	 * 限流时间,单位秒 默认 3 秒内允许请求 1 次
	 */
	int time() default 3;
	
	/**
	 * 限流次数
	 */
	int count() default 1;
	
}

