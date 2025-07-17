package cn.yshujia.domain.dto;


import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: yshujia
 * @create: 2025/5/27 21:16
 * @description: WebsiteDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class WebsiteDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[网站ID] 不能为空！")
	private Long id;
	
	@Positive(groups = InsertGroup.class, message = "[标签ID] 不能为空！")
	private Long labelId;
	
	private String title;
	
	private String avatar;
	
	private String introduction;
	
	private String url;
	
	private String cover;
	
	private Boolean recommend;
	
	private Boolean enabled;
	
	@PositiveOrZero(message = "[审核状态] 必须为正整数！")
	@Max(value = 2, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	@Min(value = 0, groups = {InsertGroup.class, UpdateGroup.class}, message = "[审核状态] 值范围 [0, 2]！")
	private Integer reviewed;
	
	private Long createBy;
	
}
