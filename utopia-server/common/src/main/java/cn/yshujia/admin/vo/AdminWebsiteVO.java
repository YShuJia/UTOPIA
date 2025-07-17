package cn.yshujia.admin.vo;

import cn.yshujia.domain.entity.Website;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yshujia
 * @create 2025/3/6
 * @description AdminWebsiteVO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminWebsiteVO extends Website {
	
	private String labelName;
	
	@Schema(description = "创建人")
	private String createUsername;
	
	@Schema(description = "更新人")
	private String updateUsername;
	
}
