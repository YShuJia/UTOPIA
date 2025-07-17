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
 * @description: DiaryDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DiaryDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[日记ID] 不能为空！")
	private Long id;
	
	@NotBlank(groups = InsertGroup.class, message = "[日记标题] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 32, message = "[日记标题] 长度不能超过 32！")
	private String title;
	
	@NotBlank(groups = InsertGroup.class, message = "[日记内容] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 512, message = "[日记内容] 长度不能超过 512！")
	private String content;
	
	private List<String> urls;
	
	private Integer year;
	
	private Integer month;
	
	private Boolean recommend;
	
	private Boolean likeable;
	
	private Boolean commentable;
	
	private Boolean enabled;
	
	@PositiveOrZero(message = "[审核状态] 必须为正整数！")
	@Max(value = 2, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	@Min(value = 0, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	private Integer reviewed;
	
	// 是修改状态还是修改所有数据
	@NotNull(groups = {UpdateGroup.class}, message = "[是否只修改状态] 不能为空！")
	private Boolean updateStatus;
	
	private Long createBy;
	
}
