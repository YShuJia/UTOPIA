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
 * @description: LeaveWordDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LeaveWordDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[留言ID] 不能为空！")
	private Long id;
	
	private String ip;
	
	private String avatar;
	
	@NotBlank(groups = InsertGroup.class, message = "[留言内容] 不能为空！")
	@Size(groups = InsertGroup.class, max = 32, message = "[留言内容] 长度不能超过 32！")
	private String content;
	
}
