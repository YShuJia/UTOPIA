package cn.yshujia.domain.dto;


import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: yshujia
 * @create: 2025/5/27 21:12
 * @description: ClassifyDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ClassifyDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[类别ID] 不能为空！")
	private Long id;
	
	@NotBlank(groups = InsertGroup.class, message = "[类别名] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 16, message = "[类别名] 长度不能超过 32！")
	private String name;
	
	@NotBlank(groups = InsertGroup.class, message = "[类别 KEY] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 16, message = "[类别 kEY] 长度不能超过 64！")
	private String key;
	
	@NotBlank(groups = InsertGroup.class, message = "[类别简介] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 128, message = "[类别简介] 长度不能超过 128！")
	private String introduction;
	
	private Boolean enabled;
	
	@PositiveOrZero(message = "[审核状态] 必须为正整数！")
	@Max(value = 2, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	@Min(value = 0, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	private Integer reviewed;
	
	private Long createBy;
	
}
