package cn.yshujia.service.impl;

import cn.yshujia.admin.vo.AdminRouterVO;
import cn.yshujia.admin.vo.AdminSelectRouterVO;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.RouterDTO;
import cn.yshujia.domain.entity.Router;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.RouterMapper;
import cn.yshujia.transform.RouterTransform;
import cn.yshujia.ui.vo.RouterVO;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class AdminRouterServiceImpl extends ServiceImpl<RouterMapper, Router> {
	
	@Resource
	public RouterMapper mapper;
	
	@Resource
	RedisServiceImpl<RouterVO> redis;
	
	private static List<AdminRouterVO> buildAdminRouterTree(List<AdminRouterVO> routers) {
		if (CollectionUtils.isEmpty(routers)) {
			return new ArrayList<>();
		}
		Map<Long, AdminRouterVO> routerMap = routers.stream()
				.collect(Collectors.toMap(AdminRouterVO::getId, Function.identity()));
		
		List<AdminRouterVO> rootNodes = new ArrayList<>();
		
		for (AdminRouterVO router : routers) {
			Long parentId = router.getParentId();
			if (parentId == null || parentId == 0) {
				rootNodes.add(router);
			} else {
				AdminRouterVO parent = routerMap.get(parentId);
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
	
	public PageVO<AdminRouterVO> pageAdmin(RouterDTO dto) {
		Router router = RouterTransform.dto2Entity(dto);
		List<AdminRouterVO> list = mapper.listByAdmin(router);
		list = buildAdminRouterTree(list);
		return PageUtils.page(list);
	}
	public List<AdminSelectRouterVO> listSelectData() {
		return mapper.listSelectByAdmin();
	}
	@Transactional(rollbackFor = {Exception.class})
	public void insert(RouterDTO dto) {
		Router router = RouterTransform.dto2Entity(dto);
		router.setId(null);
		try {
			int n = mapper.insert(router);
			if (n <= 0) {
				throw new ServiceException("新增路由失败，请稍后重试！");
			}
			redis.delKeysByPrefix(RedisKeys.ROUTER_ROLE_ID);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	@Transactional(rollbackFor = {Exception.class})
	public void update(RouterDTO dto) {
		Router router = RouterTransform.dto2Entity(dto);
		try {
			int n = mapper.updateById(router);
			if (n <= 0) {
				throw new ServiceException("路由不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.ROUTER_ROLE_ID);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	@Transactional(rollbackFor = {Exception.class})
	public void remove(List<Long> ids) {
		try {
			int n = mapper.deleteByIds(ids);
			if (n < ids.size()) {
				throw new ServiceException("路由不存在，无法删除！");
			}
			redis.delKeysByPrefix(RedisKeys.ROUTER_ROLE_ID);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
}