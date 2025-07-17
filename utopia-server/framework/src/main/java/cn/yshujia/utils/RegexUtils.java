package cn.yshujia.utils;

import java.util.regex.Pattern;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 验证号码和邮箱
 */

public class RegexUtils {
	
	private static final String EMAIL_REGEX = "[^@]+@[^@]+\\.[^@]+";
	
	private static final String TEL_REGEX = "^1\\d{10}$";
	
	private static final String IMG_REGEX = "^!\\[]\\([0-9a-zA-Z/]+\\)$";
	
	public static boolean isValidPhone(String text) {
		// 可以使用正则表达式来验证电话号码
		return text.matches(TEL_REGEX);
	}
	
	public static boolean isValidEmail(String text) {
		// 验证电子邮件
		return Pattern.matches(EMAIL_REGEX, text);
	}
	
}
