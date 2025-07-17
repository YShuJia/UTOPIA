package cn.yshujia.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_role")
@Schema(name = "Role", description = "角色")
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "自增主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	
	@Schema(description = "角色名称")
	@TableField("`name`")
	private String name;
	
	@Schema(description = "权限字符")
	@TableField(value = "permission", typeHandler = Fastjson2TypeHandler.class)
	private List<String> permission;
	
	@Schema(description = "需要的经验值")
	@TableField("experience")
	private Integer experience;
	
	@Schema(description = "创建时间")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	@Schema(description = "简介")
	@TableField("introduction")
	private String introduction;
	
	@Schema(description = "是否管理员角色")
	@TableField("is_admin")
	private Boolean admin;
	
	@Schema(description = "是否启用")
	@TableField("is_enabled")
	private Boolean enabled;
	
	public Role(Boolean admin, Boolean enabled) {
		this.admin = admin;
		this.enabled = enabled;
	}
	
}
