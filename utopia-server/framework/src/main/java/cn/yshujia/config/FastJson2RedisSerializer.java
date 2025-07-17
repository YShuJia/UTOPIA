package cn.yshujia.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author YShuJia
 */
@Slf4j
public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {
	
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	
	// 自动识别 json 对象白名单配置（仅允许解析的包名，范围越小越安全）
	static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter("org.springframework", "cn.yshujia");
	
	private final Class<T> clazz;
	
	public FastJson2RedisSerializer(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}
	
	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
	}
	
	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		String str = new String(bytes, DEFAULT_CHARSET);
		
		return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
	}
	
}
