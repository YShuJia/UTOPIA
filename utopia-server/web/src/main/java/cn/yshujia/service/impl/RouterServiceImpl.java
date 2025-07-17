package cn.yshujia.service.impl;

import cn.yshujia.constant.DefaultConst;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.entity.Router;
import cn.yshujia.mapper.RouterMapper;
import cn.yshujia.ui.vo.RouterVO;
import cn.yshujia.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Slf4j
@Service
public class RouterServiceImpl extends ServiceImpl<RouterMapper, Router> {
	
	@Resource
	RouterMapper mapper;
	
	@Resource
	RedisServiceImpl<RouterVO> redis;
	private static List<RouterVO> buildRouterTree(List<RouterVO> routers) {
		if (CollectionUtils.isEmpty(routers)) {
			return new ArrayList<>();
		}
		Map<Long, RouterVO> routerMap = routers.stream()
				.collect(Collectors.toMap(RouterVO::getId, Function.identity()));
		
		List<RouterVO> rootNodes = new ArrayList<>();
		
		for (RouterVO router : routers) {
			Long parentId = router.getParentId();
			if (parentId == null || parentId == 0) {
				rootNodes.add(router);
			} else {
				RouterVO parent = routerMap.get(parentId);
				if (parent != null) {
					if (parent.getChildren() == null) {
						parent.setChildren(new ArrayList<>());
					}
					parent.getChildren().add(router);
				}
			}
		}
		return rootNodes;
	}
	public List<RouterVO> getList(Long roleId) {
		if (null == roleId) {
			roleId = DefaultConst.MIN_ROLE_ID;
		}
		List<RouterVO> list = redis.range(RedisKeys.ROUTER_ROLE_ID + roleId, 0L, -1L);
		if (!CollectionUtils.isEmpty(list)) {
			return list;
		}
		// 查询数据库
		list = buildRouterTree(mapper.list(new Router(roleId)));
		redis.rightPushAll(RedisKeys.ROUTER_ROLE_ID + roleId, list, RedisKeys.THREE_DAYS);
		return list;
	}
	
}