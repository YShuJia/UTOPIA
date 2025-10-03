package cn.yshujia.domain.dto;


import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author: yshujia
 * @create: 2025/5/27 21:13
 * @description: FileDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class FileDTO extends PageDTO {

	@Positive(groups = UpdateGroup.class, message = "[资源ID] 不能为空！")
	private Long id;

	@Positive(groups = InsertGroup.class, message = "[标签ID] 不能为空！")
	private Long labelId;

	@NotBlank(groups = InsertGroup.class, message = "[资源名] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 32, message = "[资源名] 长度不能超过 32！")
	private String name;

	private String url;

	private Double size;

	@NotNull(groups = InsertGroup.class, message = "[资源标签] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 10, message = "[资源标签] 不能超过 10 个！")
	private List<String> tags;

	private Boolean enabled;

	@PositiveOrZero(message = "[审核状态] 必须为正整数！")
	@Max(value = 2, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	@Min(value = 0, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	private Integer reviewed;

	private Long createBy;

}
