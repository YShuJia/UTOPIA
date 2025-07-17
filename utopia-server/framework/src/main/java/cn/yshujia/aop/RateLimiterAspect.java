package cn.yshujia.aop;

import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.config.SystemConfig;
import cn.yshujia.constant.HttpCode;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.service.impl.RedisServiceImpl;
import cn.yshujia.utils.RequestUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 限流
 */

@Slf4j
@Aspect
@Component
public class RateLimiterAspect {
	
	@Resource
	private SystemConfig systemConfig;
	
	@Resource
	private RedisServiceImpl<Integer> redis;
	
	@Resource
	private RedisScript<Long> limitScript;
	
	@Before("@annotation(rateLimiter)")
	public void doBefore(RateLimiter rateLimiter) throws ServiceException {
		int time = rateLimiter.time();
		int count = rateLimiter.count();
		String combineKey = getCombineKey();
		List<Object> keys = Collections.singletonList(combineKey);
		Long number = redis.execute(limitScript, keys, count, time);
		if (number > count) {
			log.info("限制请求：{}\n当前请求：{}\n缓存 key：{}", count, number.intValue(), combineKey);
			refreshLimit();
			throw new ServiceException(HttpCode.TRIGGER_LIMIT, "操作过于频繁，请稍候再试！");
		}
	}
	
	private void refreshLimit() {
		// 获取 ip 地址
		String ip = RequestUtils.getIp();
		String limitKey = systemConfig.getLimitKey() + ip;
		Long limitTimes = redis.increment(limitKey, 1L, Duration.ofHours(systemConfig.getLimitTime()));
		// 判断是否达到限流次数
		if (limitTimes > systemConfig.getLimitTimes()) {
			String banKey = systemConfig.getLimitBanKey() + ip;
			redis.set(banKey, 1, Duration.ofHours(systemConfig.getBanTime()));
			throw new ServiceException(HttpCode.TRIGGER_LIMIT, "ip 已被封禁，请解封后再访问！");
		}
	}
	
	private String getCombineKey() {
		HttpServletRequest req = RequestUtils.getReq();
		// 获取请求地址 去掉第一个 /
		String url = req.getRequestURI().replaceAll("^/", "");
		// 获取请求ip
		return systemConfig.getLimitKey() + RequestUtils.getIp() + ":" + url;
	}
	
}
