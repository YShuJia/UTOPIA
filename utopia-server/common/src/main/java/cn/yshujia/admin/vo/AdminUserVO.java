package cn.yshujia.admin.vo;

import cn.yshujia.domain.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description domain
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserVO extends User {
	
	private String roleName;
	
	private String createUsername;
	
	private String updateUsername;
	
}
