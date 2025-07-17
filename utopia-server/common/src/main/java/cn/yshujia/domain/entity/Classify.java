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
 * 类别
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_classify")
@Schema(name = "Classify", description = "类别")
@NoArgsConstructor
public class Classify extends Base {
	
	@Schema(description = "类别名")
	@TableField("`name`")
	private String name;
	
	@Schema(description = "类型")
	@TableField("`key`")
	private String key;
	
	@Schema(description = "简介")
	@TableField("introduction")
	private String introduction;
	
	@Schema(description = "是否通过审核")
	@TableField("is_reviewed")
	private Integer reviewed;
	
	public Classify(Long id, String key, Boolean enabled, Integer reviewed) {
		this.id = id;
		this.key = key;
		this.enabled = enabled;
		this.reviewed = reviewed;
	}
	
}
