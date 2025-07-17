package cn.yshujia.utils;

import cn.hutool.core.lang.UUID;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;

import java.util.Date;


/**
 * @author yshujia
 * @description id生成器
 * @create 2024/4/23
 */

public class IDUtils {
	
	private static final IdGeneratorOptions ID_GENERATOR = new IdGeneratorOptions();
	
	public static Long getId() {
		YitIdHelper.setIdGenerator(ID_GENERATOR);
		return YitIdHelper.nextId();
	}
	
	public static Long getTimeId() {
		//时间戳
		return new Date().getTime();
	}
	
	public static String simpleUUID() {
		return UUID.fastUUID().toString(true);
	}
	
}
