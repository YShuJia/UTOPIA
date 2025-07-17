package cn.yshujia.transform;

import cn.yshujia.domain.dto.UserDTO;
import cn.yshujia.domain.entity.User;
import cn.yshujia.ui.vo.UserVO;

/**
 * @author yshujia
 * @create 2025/1/6
 * @description UserTransform
 */

public class UserTransform {
	
	public static UserVO entity2VO(User user) {
		if (user == null) {
			return null;
		}
		UserVO userVO = new UserVO();
		userVO.setRoleId(user.getRoleId());
		userVO.setAvatar(user.getAvatar());
		userVO.setUsername(user.getUsername());
		userVO.setPassword(user.getPassword());
		userVO.setGender(user.getGender());
		userVO.setEmail(user.getEmail());
		userVO.setExperience(user.getExperience());
		userVO.setId(user.getId());
		userVO.setCreateTime(user.getCreateTime());
		return userVO;
	}
	
	public static User dtoToEntity(UserDTO dto) {
		if (dto == null) {
			return null;
		}
		User user = new User();
		user.setId(dto.getId());
		user.setRoleId(dto.getRoleId());
		user.setAvatar(dto.getAvatar());
		user.setUsername(dto.getUsername());
		user.setPassword(dto.getPassword());
		user.setGender(dto.getGender());
		user.setEmail(dto.getEmail());
		user.setEnabled(dto.getEnabled());
		return user;
	}
	
}
