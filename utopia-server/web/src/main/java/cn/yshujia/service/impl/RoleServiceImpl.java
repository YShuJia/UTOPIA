package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.entity.Role;
import cn.yshujia.mapper.RoleMapper;
import cn.yshujia.ui.vo.RoleVO;
import cn.yshujia.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> {
	
	@Resource
	RoleMapper mapper;
	
	@Resource
	RedisServiceImpl<RoleVO> redis;
	
	public List<RoleVO> listUI() {
		List<RoleVO> list = redis.range(RedisKeys.ROLE_LIST, 0L, -1L);
		if (!CollectionUtils.isEmpty(list)) {
			return list;
		}
		list = mapper.list(new Role(false, true));
		if (!CollectionUtils.isEmpty(list)) {
			redis.rightPushAll(RedisKeys.ROLE_LIST, list, RedisKeys.THREE_DAYS);
		}
		return list;
	}
	
	public RoleVO getGtIdRole(Long id) {
		return mapper.oneGtId(id);
	}
	
	public RoleVO selectById(Long id) {
		return redis.get(RedisKeys.ROLE + id);
	}
	
}