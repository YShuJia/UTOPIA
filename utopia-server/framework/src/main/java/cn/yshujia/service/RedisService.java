package cn.yshujia.service;


import cn.yshujia.utils.CollectionUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: yshujia
 * @create: 2025/6/16 13:58
 * @description: RedisService
 */
public interface RedisService<T> {

	// 子类提供 RedisTemplate
	RedisTemplate<Object, T> redisTemplate();

	default void set(Object key, T value, Duration duration) {
		redisTemplate().opsForValue().set(key.toString(), value);
		if (duration != null) {
			redisTemplate().expire(key.toString(), duration);
		}
	}

	default T get(Object key) {
		ValueOperations<Object, T> ops = redisTemplate().opsForValue();
		return ops.get(key);
	}


	default void setHash(Object key, Object hashKey, T value, Duration duration) {
		if (ObjectUtils.isEmpty(value)) {
			return;
		}
		redisTemplate().opsForHash().put(key.toString(), hashKey.toString(), value);
		if (duration != null) {
			redisTemplate().expire(key.toString(), duration);
		}
	}


	default T getHash(Object key, Object hashKey) {
		HashOperations<Object, Object, T> ops = redisTemplate().opsForHash();
		return ops.get(key.toString(), hashKey.toString());
	}

	default Map<Object, T> hGetAll(Object hashKey) {
		HashOperations<Object, Object, T> ops = redisTemplate().opsForHash();
		return ops.entries(hashKey);
	}


	default void leftPush(Object key, T value, Duration duration) {
		if (ObjectUtils.isEmpty(value)) {
			return;
		}
		redisTemplate().opsForList().leftPush(key.toString(), value);
		if (duration != null) {
			redisTemplate().expire(key.toString(), duration);
		}
	}


	default void leftPushAll(Object key, List<T> value, Duration duration) {
		if (CollectionUtils.isEmpty(value)) {
			return;
		}
		redisTemplate().opsForList().leftPushAll(key, value);
		if (duration != null) {
			redisTemplate().expire(key.toString(), duration);
		}
	}


	default void rightPush(Object key, T value, Duration duration) {
		if (ObjectUtils.isEmpty(value)) {
			return;
		}
		redisTemplate().opsForList().rightPush(key, value);
		if (duration != null) {
			redisTemplate().expire(key.toString(), duration);
		}
	}


	default void rightPushAll(Object key, List<T> value, Duration duration) {
		if (CollectionUtils.isEmpty(value)) {
			return;
		}
		redisTemplate().opsForList().rightPushAll(key.toString(), value);
		if (duration != null) {
			redisTemplate().expire(key.toString(), duration);
		}
	}


	default List<T> range(Object key, Long start, Long end) {
		return redisTemplate().opsForList().range(key.toString(), start, end);
	}


	default List<T> range(Object key, Integer pageNum, Integer pageSize) {
		long start = (long) (pageNum - 1) * pageSize;
		long end = start + pageSize - 1;
		return redisTemplate().opsForList().range(key.toString(), start, end);
	}


	default Long increment(Object key, Long delta, Duration duration) {
		Boolean exists = redisTemplate().hasKey(key.toString());
		Long result = redisTemplate().opsForValue().increment(key, delta);
		if (!exists) {
			redisTemplate().expire(key.toString(), duration);
		}
		return result;
	}


	default Boolean expire(Object key) {
		return redisTemplate().expire(key.toString(), Duration.ofDays(1));
	}

	default Long getExpireSeconds(Object key) {
		return Optional.ofNullable(key)
				.map(Object::toString)
				.map(redisTemplate()::getExpire)
				.orElse(-1L);
	}

	default Long getExpireMinutes(Object key) {
		return Math.max(-1L, TimeUnit.SECONDS.toMinutes(getExpireSeconds(key)));
	}

	default Double getExpireDecimalMinutes(Object key, Integer decimal) {
		Long secs = getExpireSeconds(key);
		if (secs == null || secs == -1L) return -1.0;
		if (secs == 0L) return 0.0;

		return BigDecimal.valueOf(secs / 60.0)
				.setScale(decimal, RoundingMode.UP)
				.doubleValue();
	}

	default Long getExpireHours(Object key) {
		return Math.max(-1L, TimeUnit.SECONDS.toHours(getExpireSeconds(key)));
	}

	default Double getExpireDecimalHours(Object key, Integer decimal) {
		Long secs = getExpireSeconds(key);
		if (secs == null || secs == -1L) return -1.0;
		if (secs == 0L) return 0.0;

		return BigDecimal.valueOf(secs / 3600.0)
				.setScale(decimal, RoundingMode.UP)
				.doubleValue();
	}

	default Long execute(RedisScript<Long> redisScript, List<Object> keys, Integer count, Integer time) {
		return redisTemplate().execute(redisScript, keys, count, time);
	}


	default Set<Object> keys(Object keyPrefix) {
		return redisTemplate().keys(keyPrefix + "*");
	}


	default Long hashSize(Object key) {
		return redisTemplate().opsForHash().size(key.toString());
	}


	default Long listSize(Object key) {
		return redisTemplate().opsForList().size(key.toString());
	}


	default Boolean hasKey(Object key) {
		return redisTemplate().hasKey(key.toString());
	}


	default Boolean hasKey(Object key, Object hashKey) {
		return redisTemplate().opsForHash().hasKey(key.toString(), hashKey.toString());
	}


	default void delKey(Object key) {
		redisTemplate().delete(key.toString());
	}


	default void delKeysByPrefix(Object keyPrefix) {
		Set<Object> keys = redisTemplate().keys(keyPrefix + "*");
		if (!CollectionUtils.isEmpty(keys)) {
			redisTemplate().delete(keys);
		}
	}

}
