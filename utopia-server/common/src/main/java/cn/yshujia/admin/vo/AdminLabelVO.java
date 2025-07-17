package cn.yshujia.admin.vo;

import cn.yshujia.domain.entity.Label;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yshujia
 * @create 2024/11/29
 * @description AdminLabelVO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminLabelVO extends Label implements Serializable {
	
	@Schema(description = "类别")
	private String classifyName;
	
	@Schema(description = "创建人")
	private String createUsername;
	
	@Schema(description = "更新人")
	private String updateUsername;
	
}
