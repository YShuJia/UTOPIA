package cn.yshujia.domain.entity;

import cn.yshujia.domain.Base;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 友情链接信息
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_website", autoResultMap = true)
@Schema(name = "Website", description = "友情链接信息")
@NoArgsConstructor
public class Website extends Base {
	
	@Schema(description = "标签ID")
	@TableField("label_id")
	private Long labelId;
	
	@Schema(description = "网站标题")
	@TableField("title")
	private String title;
	
	@Schema(description = "头像")
	@TableField("avatar")
	private String avatar;
	
	@Schema(description = "网站简介")
	@TableField("introduction")
	private String introduction;
	
	@Schema(description = "链接")
	@TableField("url")
	private String url;
	
	@Schema(description = "封面")
	@TableField("cover")
	private String cover;
	
	@Schema(description = "是否开启推荐")
	@TableField("is_recommend")
	private Boolean recommend;
	
	@Schema(description = "是否通过审核")
	@TableField("is_reviewed")
	private Integer reviewed;
	
	public Website(Boolean recommend, Boolean enabled, Integer reviewed) {
		this.recommend = recommend;
		this.enabled = enabled;
		this.reviewed = reviewed;
	}
	
}
