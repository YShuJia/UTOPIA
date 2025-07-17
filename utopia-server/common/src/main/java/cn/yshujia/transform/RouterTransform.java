package cn.yshujia.transform;

import cn.yshujia.domain.dto.RouterDTO;
import cn.yshujia.domain.entity.Router;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 实体转换
 */

public class RouterTransform {
	
	public static Router dto2Entity(RouterDTO dto) {
		if (dto == null) {
			return null;
		}
		Router router = new Router();
		router.setId(dto.getId());
		router.setRoleId(dto.getRoleId());
		router.setParentId(dto.getParentId());
		router.setName(dto.getName());
		router.setTitle(dto.getTitle());
		router.setSort(dto.getSort());
		router.setPath(dto.getPath());
		router.setIcon(dto.getIcon());
		router.setComponent(dto.getComponent());
		router.setType(dto.getType());
		router.setAdmin(dto.getAdmin());
		router.setFrame(dto.getFrame());
		router.setEnabled(dto.getEnabled());
		return router;
	}
	
}