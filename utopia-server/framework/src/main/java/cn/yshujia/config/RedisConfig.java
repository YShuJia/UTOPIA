package cn.yshujia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author yshujia
 * @description redis配置
 * @create 2024/4/23
 */

@Configuration
public class RedisConfig {
	
	@Bean(name = "Redis")
	public <T> RedisTemplate<Object, T> getRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Object, T> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		// 配置序列化
		FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<>(Object.class);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(fastJson2RedisSerializer);
		
		// 使用StringRedisSerializer来序列化和反序列化redis的Hash Key值
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(fastJson2RedisSerializer);
		// 初始化
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
	
	@Bean
	public DefaultRedisScript<Long> limitScript() {
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptText(limitScriptText());
		redisScript.setResultType(Long.class);
		return redisScript;
	}
	
	/**
	 * 限流脚本
	 */
	private String limitScriptText() {
		return """
				local key = KEYS[1];
				local count = tonumber(ARGV[1]);
				local time = tonumber(ARGV[2]);
				
				local current = redis.call('get', key);
				if current and tonumber(current) > count then
				    return tonumber(current);
				end;
				
				current = redis.call('incr', key);
				if current and tonumber(current) == 1 then
				    redis.call('expire', key, time);
				end;
				
				return tonumber(current);
				""";
	}
	
}
