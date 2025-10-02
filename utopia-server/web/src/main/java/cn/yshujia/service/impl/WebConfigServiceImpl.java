package cn.yshujia.service.impl;

import cn.yshujia.domain.entity.WebConfig;
import cn.yshujia.mapper.WebConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YShuJia
 * @since 2024/4/23
 */
@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements IService<WebConfig> {

	@Resource
	WebConfigMapper mapper;
	
	public WebConfig defaultConfig() {
		return mapper.selectOne(new LambdaQueryWrapper<WebConfig>()
				.eq(WebConfig::getEnabled, true));
	}
}
