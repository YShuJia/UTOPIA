package cn.yshujia.domain.entity;

import cn.yshujia.domain.Base;
import cn.yshujia.handler.domain.DomainContentHandler;
import cn.yshujia.handler.domain.DomainHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_article", autoResultMap = true)
@Schema(name = "Article", description = "文章")
@AllArgsConstructor
@NoArgsConstructor
public class Article extends Base {
	
	@Schema(description = "标签ID")
	@TableField("label_id")
	private Long labelId;
	
	@Schema(description = "标题")
	@TableField("title")
	private String title;
	
	@Schema(description = "封面")
	@TableField(value = "cover", typeHandler = DomainHandler.class)
	private String cover;
	
	@Schema(description = "内容")
	@TableField(value = "content", typeHandler = DomainContentHandler.class)
	private String content;
	
	@Schema(description = "版权方")
	@TableField("copyright")
	private String copyright;
	
	@Schema(description = "点赞量")
	@TableField("like_count")
	private Integer likeCount;
	
	@Schema(description = "浏览量")
	@TableField("view_count")
	private Integer viewCount;
	
	@Schema(description = "评论数量")
	@TableField("comment_count")
	private Integer commentCount;
	
	@Schema(description = "是否含有视频")
	@TableField("has_video")
	private Boolean hasVideo;
	
	@Schema(description = "文章密码")
	@TableField("`password`")
	private String password;
	
	@Schema(description = "密码提示")
	@TableField("password_tip")
	private String passwordTip;
	
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
	
	public Article(Boolean recommend, Boolean enabled, Integer reviewed) {
		this.recommend = recommend;
		this.enabled = enabled;
		this.reviewed = reviewed;
	}
	
	public Article(String title, Boolean enabled, Integer reviewed) {
		this.title = title;
		this.enabled = enabled;
		this.reviewed = reviewed;
	}
	
	public Article(Long id, Long labelId, Boolean enabled, Integer reviewed) {
		this.id = id;
		this.labelId = labelId;
		this.enabled = enabled;
		this.reviewed = reviewed;
	}
	
}
