package cn.yshujia.admin.vo;

import cn.yshujia.domain.entity.File;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yshujia
 * @create 2024/11/30
 * @description AdminFileVO
 */


@Data
@EqualsAndHashCode(callSuper = true)
public class AdminFileVO extends File {
	
	@Schema(description = "标签名")
	private String labelName;
	
	@Schema(description = "创建人")
	private String createUsername;
	
	@Schema(description = "更新人")
	private String updateUsername;
	
}
