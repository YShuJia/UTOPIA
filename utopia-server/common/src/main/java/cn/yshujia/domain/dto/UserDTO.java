package cn.yshujia.domain.dto;


import cn.yshujia.domain.enums.GenderEnum;
import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: yshujia
 * @create: 2025/5/27 21:15
 * @description: UserDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[用户ID] 不能为空！")
	private Long id;
	
	@Positive(groups = InsertGroup.class, message = "[角色ID] 不能为空！")
	private Long roleId;
	
	private String avatar;
	
	private String username;
	
	private String password;
	
	private GenderEnum gender;
	
	private String email;
	
	private Boolean enabled;
	
}
