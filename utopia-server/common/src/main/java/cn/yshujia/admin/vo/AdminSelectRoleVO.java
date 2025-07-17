package cn.yshujia.admin.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import lombok.Data;

import java.util.List;

/**
 * @author: yshujia
 * @create: 2025/6/10 17:12
 * @description: AdminSelectRoleVO
 */

@Data
public class AdminSelectRoleVO {
	
	private Long id;
	
	private String name;
	
	@TableField(value = "permission", typeHandler = Fastjson2TypeHandler.class)
	private List<String> permission;
	
}
