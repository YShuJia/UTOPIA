package cn.yshujia.domain.dto;

import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yshujia
 * @create 2025/3/21
 * @description ArticleDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[文章ID] 不能为空！")
	private Long id;
	
	@Positive(groups = InsertGroup.class, message = "[标签ID] 不能为空！")
	private Long labelId;
	
	@NotBlank(groups = InsertGroup.class, message = "[文章标题] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 32, message = "[标题] 长度不能超过 32！")
	private String title;
	
	@Size(min = 1, max = 128)
	@NotBlank(groups = InsertGroup.class, message = "[文章封面] 不能为空！")
	private String cover;
	
	@NotBlank(groups = InsertGroup.class, message = "[文章内容] 不能为空！")
	private String content;
	
	@NotBlank(groups = InsertGroup.class, message = "[文章版权] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 128, message = "[文章版权] 长度不能超过 128！")
	private String copyright;
	
	private Boolean hasVideo;
	
	private String password;
	
	private String passwordTip;
	
	private Boolean recommend;
	
	private Boolean likeable;
	
	private Boolean commentable;
	
	private Boolean enabled;
	
	@PositiveOrZero(message = "[审核状态] 必须为正整数！")
	@Max(value = 2, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	@Min(value = 0, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	private Integer reviewed;
	
	// 修改文章时新上传的图片地址集合
	private List<String> urls = new ArrayList<>();
	
	// 是修改状态还是修改所有数据
	@NotNull(groups = {UpdateGroup.class}, message = "[是否只修改状态] 不能为空！")
	private Boolean updateStatus;
	
	private Long createBy;
	
}
