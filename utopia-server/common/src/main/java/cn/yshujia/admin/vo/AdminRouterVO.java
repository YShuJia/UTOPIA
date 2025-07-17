package cn.yshujia.admin.vo;

import cn.yshujia.domain.entity.Router;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description domain
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminRouterVO extends Router {
	
	@Schema(description = "角色名")
	private String roleName;
	
	@Schema(description = "子路由")
	private List<AdminRouterVO> children;
	
}