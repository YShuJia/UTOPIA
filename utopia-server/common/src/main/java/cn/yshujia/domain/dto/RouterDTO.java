package cn.yshujia.domain.dto;


import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: yshujia
 * @create: 2025/5/27 21:15
 * @description: RouterDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RouterDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[路由ID] 不能为空！")
	private Long id;
	
	@Positive(groups = InsertGroup.class, message = "[角色ID] 不能为空！")
	private Long roleId;
	
	private Long parentId;
	
	private String name;
	
	private String title;
	
	private Byte sort;
	
	private String path;
	
	private String icon;
	
	private String component;
	
	@NotBlank(groups = InsertGroup.class, message = "[角色简介] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 1, message = "[路由类型] 长度不能超过 1！")
	private String type;
	
	private Boolean admin;
	
	private Boolean frame;
	
	private Boolean enabled;
	
}
