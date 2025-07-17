package cn.yshujia.service.impl;


import cn.yshujia.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: yshujia
 * @create: 2025/6/16 14:01
 * @description: RedisServiceImpl
 */

@Service
public class RedisServiceImpl<T> implements RedisService<T> {
	
	@Resource(name = "Redis")
	private RedisTemplate<Object, T> redisTemplate;
	
	
	@Override
	public RedisTemplate<Object, T> redisTemplate() {
		return redisTemplate;
	}
	
}
