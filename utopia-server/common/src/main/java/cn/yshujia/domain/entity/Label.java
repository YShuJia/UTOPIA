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

/**
 * <p>
 * 标签
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_label", autoResultMap = true)
@Schema(name = "Label", description = "标签")
@AllArgsConstructor
@NoArgsConstructor
public class Label extends Base {
	
	@Schema(description = "类别ID")
	@TableField("classify_id")
	private Long classifyId;
	
	@Schema(description = "标签名")
	@TableField("`name`")
	private String name;
	
	@Schema(description = "封面")
	@TableField(value = "cover", typeHandler = DomainHandler.class)
	private String cover;
	
	@Schema(description = "简介")
	@TableField("introduction")
	private String introduction;
	
	@Schema(description = "是否通过审核")
	@TableField("is_reviewed")
	private Integer reviewed;
	
	public Label(Long id, Long classifyId, Boolean enabled, Integer reviewed) {
		this.id = id;
		this.classifyId = classifyId;
		this.enabled = enabled;
		this.reviewed = reviewed;
	}
	
}
