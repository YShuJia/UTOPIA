package cn.yshujia.domain.entity;

import cn.yshujia.domain.Base;
import cn.yshujia.handler.domain.DomainHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
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
@TableName(value = "t_file", autoResultMap = true)
@Schema(name = "File", description = "")
@NoArgsConstructor
@AllArgsConstructor
public class File extends Base {

	@Schema(description = "标签ID")
	@TableField("label_id")
	private Long labelId;

	@Schema(description = "资源文件名")
	@TableField("`name`")
	private String name;

	@Schema(description = "链接")
	@TableField(value = "url", typeHandler = DomainHandler.class)
	private String url;

	@Schema(description = "大小kb")
	@TableField("size")
	private Double size;

	@Schema(description = "标签")
	@TableField(value = "tags", typeHandler = Fastjson2TypeHandler.class)
	private List<String> tags;

	@Schema(description = "是否通过审核")
	@TableField("is_reviewed")
	private Integer reviewed;

	public File(Long labelId, Boolean enabled, Integer reviewed) {
		this.labelId = labelId;
		this.enabled = enabled;
		this.reviewed = reviewed;
	}

}
