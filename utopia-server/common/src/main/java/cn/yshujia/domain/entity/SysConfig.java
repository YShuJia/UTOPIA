package cn.yshujia.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author YShuJia
 * @since 2024/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_sys_config", autoResultMap = true)
@Schema(name = "SysConfig", description = "")
public class SysConfig implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	@Schema(description = "配置名")
	@TableField("`name`")
	private String name;

	@Schema(description = "密码试错次数")
	@TableField("sys_password_count")
	private Byte sysPasswordCount;

	@Schema(description = "密码试错间隔时间 h")
	@TableField("sys_password_time")
	private Byte sysPasswordTime;

	@Schema(description = "密码试错封禁账号、IP时间 h")
	@TableField("sys_password_ban")
	private Byte sysPasswordBan;

	@Schema(description = "触发接口限流次数")
	@TableField("sys_limit_count")
	private Byte sysLimitCount;

	@Schema(description = "触发接口限流间隔时间 h")
	@TableField("sys_limit_time")
	private Byte sysLimitTime;

	@Schema(description = "触发限流封禁IP时间 h")
	@TableField("sys_limit_ban")
	private Byte sysLimitBan;

	@Schema(description = "敏感词替换符")
	@TableField("sys_replace_char")
	private String sysReplaceChar;

	@Schema(description = "每天可获取的最大经验值")
	@TableField("sys_max_exp")
	private Short sysMaxExp;

	@Schema(description = "生成角色权限的表配置")
	@TableField(value = "sys_role_table", typeHandler = Fastjson2TypeHandler.class)
	private List<String> sysRoleTable;

	@Schema(description = "是否启用该配置")
	@TableField("is_enabled")
	private Boolean enabled;

	@Schema(description = "配置创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
}
