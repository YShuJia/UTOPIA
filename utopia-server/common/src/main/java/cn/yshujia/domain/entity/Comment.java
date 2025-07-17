package cn.yshujia.domain.entity;

import cn.yshujia.handler.CommentHandler;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_comment", autoResultMap = true)
@Schema(name = "Comment", description = "评论")
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	
	@Schema(description = "用户ID")
	@TableField("user_id")
	private Long userId;
	
	@Schema(description = "源id（该id和评论id相同时为树洞评论）")
	@TableField("source_id")
	private Long sourceId;
	
	@Schema(description = "楼层评论（该id和评论id相同时无楼层，自己为楼层）")
	@TableField("floor_id")
	private Long floorId;
	
	@Schema(description = "父评论用户id（该id和user_id相同时未回复评论，回复楼层评论不算回复）")
	@TableField("parent_user_id")
	private Long parentUserId;
	
	@Schema(description = "点赞量")
	@TableField("like_count")
	private Integer likeCount;
	
	@Schema(description = "内容 [文字, 图片]")
	@TableField(value = "content", typeHandler = CommentHandler.class)
	private List<String> content;
	
	@Schema(description = "被回复内容 [文字, 图片]")
	@TableField(value = "replay_content", typeHandler = CommentHandler.class)
	private List<String> replayContent;
	
	@Schema(description = "创建时间")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	public Comment(Long sourceId) {
		this.sourceId = sourceId;
	}
	
}
