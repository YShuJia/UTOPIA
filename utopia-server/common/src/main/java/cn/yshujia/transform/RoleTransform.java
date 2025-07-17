package cn.yshujia.transform;


import cn.yshujia.domain.dto.RoleDTO;
import cn.yshujia.domain.entity.Role;

/**
 * @author: yshujia
 * @create: 2025/6/14 20:36
 * @description: RoleTransform
 */
public class RoleTransform {
	
	public static Role dto2entity(RoleDTO dto) {
		if (dto == null) {
			return null;
		}
		Role role = new Role();
		role.setId(dto.getId());
		role.setName(dto.getName());
		role.setPermission(dto.getPermission());
		role.setExperience(dto.getExperience());
		role.setIntroduction(dto.getIntroduction());
		role.setCreateTime(dto.getCreateTime());
		role.setAdmin(dto.getAdmin());
		role.setEnabled(dto.getEnabled());
		return role;
	}
	
}
