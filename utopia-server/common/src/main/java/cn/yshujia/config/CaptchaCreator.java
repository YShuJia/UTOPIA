package cn.yshujia.config;

import cn.yshujia.utils.RandomUtils;
import com.google.code.kaptcha.text.impl.DefaultTextCreator;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 验证码文本生成器
 */
public class CaptchaCreator extends DefaultTextCreator {
	
	private static final String[] C_NUMBERS = "0,1,2,3,4,5,6,7,8,9,10".split(",");
	
	@Override
	public String getText() {
		Integer result;
		int x = RandomUtils.getRandom(10);
		int y = RandomUtils.getRandom(10);
		StringBuilder suChinese = new StringBuilder();
		int randomOperands = RandomUtils.getRandom(3);
		if (randomOperands == 0) {
			result = x * y;
			suChinese.append(C_NUMBERS[x]);
			suChinese.append("*");
			suChinese.append(C_NUMBERS[y]);
		} else if (randomOperands == 1) {
			if ((x != 0) && y % x == 0) {
				result = y / x;
				suChinese.append(C_NUMBERS[y]);
				suChinese.append("/");
				suChinese.append(C_NUMBERS[x]);
			} else {
				result = x + y;
				suChinese.append(C_NUMBERS[x]);
				suChinese.append("+");
				suChinese.append(C_NUMBERS[y]);
			}
		} else {
			if (x >= y) {
				result = x - y;
				suChinese.append(C_NUMBERS[x]);
				suChinese.append("-");
				suChinese.append(C_NUMBERS[y]);
			} else {
				result = y - x;
				suChinese.append(C_NUMBERS[y]);
				suChinese.append("-");
				suChinese.append(C_NUMBERS[x]);
			}
		}
		suChinese.append("=?@").append(result);
		return suChinese.toString();
	}
	
}