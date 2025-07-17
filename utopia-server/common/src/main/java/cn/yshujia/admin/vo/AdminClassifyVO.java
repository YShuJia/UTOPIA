package cn.yshujia.admin.vo;


import cn.yshujia.domain.entity.Classify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author：yshujia
 * @create: 2025/5/2 19:55
 * @description: AdminClassifyVO
 */


@Data
@EqualsAndHashCode(callSuper = true)
public class AdminClassifyVO extends Classify {
	
	@Schema(description = "创建人")
	private String createUsername;
	
	@Schema(description = "更新人")
	private String updateUsername;
	
}
