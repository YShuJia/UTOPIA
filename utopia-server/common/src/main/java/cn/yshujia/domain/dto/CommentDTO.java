package cn.yshujia.domain.dto;


import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author: yshujia
 * @create: 2025/5/27 21:12
 * @description: CommentDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentDTO extends PageDTO {
	
	@Positive(groups = UpdateGroup.class, message = "[评论ID] 不能为空！")
	private Long id;
	
	@Positive(groups = InsertGroup.class, message = "[用户ID] 不能为空！")
	private Long userId;
	
	private Long sourceId;
	
	private Long floorId;
	
	private Long parentUserId;
	
	private Integer likeCount;
	
	@NotNull(groups = InsertGroup.class, message = "[评论内容] 不能为空！")
	private List<String> content;
	
	private List<String> replayContent;
	
}
