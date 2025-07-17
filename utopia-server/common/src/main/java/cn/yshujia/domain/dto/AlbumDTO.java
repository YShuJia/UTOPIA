package cn.yshujia.domain.dto;

import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * @author: yshujia
 * @create: 2025/5/27 21:12
 * @description: AlbumDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[相册ID] 不能为空！")
	private Long id;
	
	@Positive(groups = InsertGroup.class, message = "[标签ID] 不能为空！")
	private Long labelId;
	
	@NotBlank(groups = InsertGroup.class, message = "[相册标题] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 32, message = "[相册标题] 长度不能超过 32！")
	private String title;
	
	private List<String> urls;
	
	@NotBlank(groups = InsertGroup.class, message = "[相册简介] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 128, message = "[相册简介] 长度不能超过 128！")
	private String introduction;
	
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