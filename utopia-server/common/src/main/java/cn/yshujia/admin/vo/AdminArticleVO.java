package cn.yshujia.admin.vo;

import cn.yshujia.domain.entity.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/11/28
 * @description AdminArticleVO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminArticleVO extends Article {
	
	@Schema(description = "标签")
	private String labelName;
	
	@Schema(description = "旧图片地址，用于比对更新和添加时删除的图片")
	private List<String> imgUrls;
	
	@Schema(description = "创建人")
	private String createUsername;
	
	@Schema(description = "更新人")
	private String updateUsername;
	
}
