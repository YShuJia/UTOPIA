package cn.yshujia.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yshujia
 * @create 2025/1/4
 * @description DecryptFields
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecryptFields {
	
	String[] value() default {};
	
}