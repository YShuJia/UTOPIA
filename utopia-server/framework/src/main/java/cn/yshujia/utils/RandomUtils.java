package cn.yshujia.utils;

import java.util.Random;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 生成随机数
 */

public class RandomUtils {
	
	private static final Random RANDOM;
	
	static {
		RANDOM = new Random();
	}
	
	// 生成[1, max]的随机数
	public static Integer getRandom(Integer max) {
		return RANDOM.nextInt(max);
	}
	
	// 生成[min, max]的随机数
	public static Integer getRandom(Integer min, Integer max) {
		return (RANDOM.nextInt((max - min)) + min);
	}
	
}
