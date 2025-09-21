package cn.yshujia.repository;


import cn.yshujia.domain.entity.SysConfig;
import cn.yshujia.mapper.SysConfigMapper;
import cn.yshujia.service.PermissionService;
import cn.yshujia.service.impl.RedisServiceImpl;
import cn.yshujia.utils.RequestUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.time.Duration;

/**
 * @author: yshujia
 * @create: 2025/9/21 10:34
 * @description: SysRepository 系统配置仓库
 */

@Data
@Repository
public class SysRepository {

	// 限流缓存 key
	private final String limitKey = "rate_limit:ip:";

	// 限流封禁缓存 key
	private final String limitBanKey = "rate_limit:ban:";

	// 密码试错缓存 key
	private final String passwordErrorKey = "password:error:ip:";

	// 密码封禁缓存 key
	private final String passwordBanKey = "password:ban:";


	private final String SYS_CONFIG_KEY = "sys_config:";

	@Resource
	RedisServiceImpl<Object> redis;

	@Resource
	private SysConfigMapper mapper;
	@Autowired
	private PermissionService sys;

	public SysConfig getSysConfig() {
		SysConfig sysConfig = (SysConfig) redis.get(SYS_CONFIG_KEY);
		if (sysConfig != null) {
			return sysConfig;
		}
		sysConfig = mapper.selectOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getEnabled, true));
		redis.set(SYS_CONFIG_KEY, sysConfig, null);
		return sysConfig;
	}


	@Async("Task")
	public void removeSysConfig() {
		redis.delKey(SYS_CONFIG_KEY);
	}

	public Double expirationLimit(HttpServletRequest request) {
		String key = limitBanKey + RequestUtils.getIp(request);
		if (redis.hasKey(key)) {
			return redis.getExpireDecimalHours(key, 2);
		}
		return -1.0;
	}

	public Double expirationPassword(HttpServletRequest request) {
		String key = passwordBanKey + RequestUtils.getIp(request);
		if (redis.hasKey(key)) {
			return redis.getExpireDecimalHours(key, 2);
		}
		return -1.0;
	}

	public Double expirationPassword(String username) {
		String key = passwordBanKey + username;
		if (redis.hasKey(key)) {
			return redis.getExpireDecimalHours(key, 2);
		}
		return -1.0;
	}

	public void refreshLimitBanCount() {
		SysConfig sysConfig = getSysConfig();
		// 获取 ip 地址
		String ip = RequestUtils.getIp();
		Long count = redis.increment(limitKey + ip, 1L, Duration.ofHours(sysConfig.getSysLimitTime()));
		// 判断是否达到限流次数
		if (count > sysConfig.getSysLimitCount()) {
			redis.set(limitBanKey + ip, 1, Duration.ofHours(sysConfig.getSysLimitBan()));
		}
	}

	public Long refreshPasswordBanCount(String username, HttpServletRequest request) {
		SysConfig sysConfig = getSysConfig();
		String key = passwordErrorKey + RequestUtils.getIp(request);
		Long count = redis.increment(key, 1L, Duration.ofHours(sysConfig.getSysPasswordTime()));
		// 判断是否达到最大错误次数
		if (count > sysConfig.getSysPasswordCount()) {
			redis.set(passwordBanKey + username, true, Duration.ofHours(sysConfig.getSysPasswordBan()));
			redis.set(passwordBanKey + RequestUtils.getIp(request), true, Duration.ofHours(sysConfig.getSysPasswordBan()));
		}
		// 返回剩余错误次数
		return sysConfig.getSysPasswordCount() - count;
	}

}
