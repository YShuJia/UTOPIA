package cn.yshujia.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 生成随机数
 */

public class RandomUtils {
	// 生成 [0, max) 的随机数
	public static int random(Integer max) {
		if (max <= 0) {
			return 0;
		}
		return ThreadLocalRandom.current().nextInt(0, max);
	}

	// 生成 [min, max) 的随机数
	public static int random(Integer min, Integer max) {
		if (min > max) {
			return ThreadLocalRandom.current().nextInt(max, min);
		}
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	// 生成 [0, max] 的随机数
	public static int randomIncludeMax(Integer max) {
		if (max <= 0) {
			return 0;
		}
		return ThreadLocalRandom.current().nextInt(0, max + 1);
	}

	// 生成 [min, max] 的随机数
	public static int randomIncludeMax(Integer min, Integer max) {
		if (min > max) {
			return ThreadLocalRandom.current().nextInt(max, min + 1);
		}
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
}
