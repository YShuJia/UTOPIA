package cn.yshujia.domain.entity;

import cn.yshujia.domain.Base;
import cn.yshujia.handler.domain.DomainHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_album", autoResultMap = true)
@Schema(name = "Album", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class Album extends Base {
	
	@Schema(description = "标签ID")
	@TableField("label_id")
	private Long labelId;
	
	@Schema(description = "标题")
	@TableField("title")
	private String title;
	
	@Schema(description = "路径数组最多9张")
	@TableField(value = "urls", typeHandler = DomainHandler.class)
	private List<String> urls;
	
	@Schema(description = "点赞数量")
	@TableField("like_count")
	private Integer likeCount;
	
	@Schema(description = "浏览量")
	@TableField("view_count")
	private Integer viewCount;
	
	@Schema(description = "评论数量")
	@TableField("comment_count")
	private Integer commentCount;
	
	@Schema(description = "简介")
	@TableField("introduction")
	private String introduction;
	
	@Schema(description = "是否开启推荐")
	@TableField("is_recommend")
	private Boolean recommend;
	
	@Schema(description = "是否开启点赞")
	@TableField("is_likeable")
	private Boolean likeable;
	
	@Schema(description = "是否开启评论")
	@TableField("is_commentable")
	private Boolean commentable;
	
	@Schema(description = "是否通过审核")
	@TableField("is_reviewed")
	private Integer reviewed;
	
	public Album(String title, Boolean enabled, Integer reviewed) {
		this.title = title;
		this.enabled = enabled;
		this.reviewed = reviewed;
	}
	
	public Album(Long id, Long labelId, Boolean enabled, Integer reviewed) {
		this.id = id;
		this.labelId = labelId;
		this.enabled = enabled;
		this.reviewed = reviewed;
	}
	
}
