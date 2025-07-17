package cn.yshujia.utils;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: yshujia
 * @create: 2025/6/25 16:32
 * @description: CollectionUtils
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {
	
	
	/**
	 * @Param: [set, allSet]
	 * @Return: java.util.Set<T>
	 * @Author: yshujia
	 * @CreateTime: 2025/6/25 16:34
	 * @Description: remove 从 allSet 中移除 has 中有的元素
	 **/
	public static <T> Set<T> removeHas(Collection<T> has, Collection<T> allSet) {
		if (isEmpty(allSet)) {
			return new HashSet<>();
		}
		if (isEmpty(has)) {
			return new HashSet<>(allSet);
		}
		Set<T> result = new HashSet<>(allSet);
		result.removeAll(has);
		return result;
	}
	
}
