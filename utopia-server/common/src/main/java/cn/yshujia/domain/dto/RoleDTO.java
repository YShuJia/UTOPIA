package cn.yshujia.domain.dto;


import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author: yshujia
 * @create: 2025/5/27 21:15
 * @description: RoleDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[角色ID] 不能为空！")
	private Long id;
	
	@NotBlank(groups = InsertGroup.class, message = "[角色名] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 16, message = "[角色名] 长度不能超过 16！")
	private String name;
	
	@NotNull(groups = InsertGroup.class, message = "[角色权限] 不能为空！")
	private List<String> permission;
	
	private Integer experience;
	
	@NotBlank(groups = InsertGroup.class, message = "[角色简介] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 255, message = "[角色简介] 长度不能超过 255！")
	private String introduction;
	
	private Date createTime;
	
	private Boolean admin;
	
	private Boolean enabled;
	
}
