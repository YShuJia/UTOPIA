package cn.yshujia.admin.vo;

import cn.yshujia.domain.entity.Album;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yshujia
 * @create 2025/3/4
 * @description AdminAlbumVO
 */


@Data
@EqualsAndHashCode(callSuper = true)
public class AdminAlbumVO extends Album {
	
	@Schema(description = "标签")
	private String labelName;
	
	@Schema(description = "创建人")
	private String createUsername;
	
	@Schema(description = "更新人")
	private String updateUsername;
	
}
