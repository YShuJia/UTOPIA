package cn.yshujia.repository;


import cn.yshujia.domain.entity.WebConfig;
import cn.yshujia.mapper.WebConfigMapper;
import cn.yshujia.service.impl.RedisServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

/**
 * @author: yshujia
 * @create: 2025/9/21 16:44
 * @description: WebRepository TODO
 */

@Repository
public class WebRepository {

	private final String CONFIG_KEY = "web_config:";

	@Resource
	RedisServiceImpl<Object> redis;

	@Resource
	private WebConfigMapper mapper;

	public WebConfig getWebConfig() {
		WebConfig webConfig = (WebConfig) redis.get(CONFIG_KEY);
		if (webConfig != null) {
			return webConfig;
		}
		webConfig = mapper.selectOne(new LambdaQueryWrapper<WebConfig>().eq(WebConfig::getEnabled, true));
		redis.set(CONFIG_KEY, webConfig, null);
		return webConfig;
	}

	@Async("Task")
	public void removeWebConfig() {
		redis.delKey(CONFIG_KEY);
	}
}
