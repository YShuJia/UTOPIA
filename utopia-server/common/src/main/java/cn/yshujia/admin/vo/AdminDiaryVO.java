package cn.yshujia.admin.vo;


import cn.yshujia.domain.entity.Diary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author：yshujia
 * @create: 2025/5/2 19:47
 * @description: AdminDiaryVO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminDiaryVO extends Diary {
	
	@Schema(description = "创建人")
	private String createUsername;
	
	@Schema(description = "更新人")
	private String updateUsername;
	
}
