package cn.yshujia.service.impl;

import cn.yshujia.domain.entity.Like;
import cn.yshujia.mapper.LikeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author yshujia
 * @create 2024/12/20
 * @description AdminLikeServiceImpl
 */

@Service
public class AdminLikeServiceImpl extends ServiceImpl<LikeMapper, Like> {
	
	@Resource
	public LikeMapper mapper;
	
	@Resource
	RedisServiceImpl<Boolean> redis;
	
	
}

