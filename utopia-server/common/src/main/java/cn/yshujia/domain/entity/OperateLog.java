package cn.yshujia.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_operate_log")
@Schema(name = "OperateLog", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class OperateLog implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	
	@Schema(description = "操作的表")
	@TableField("table")
	private String table;
	
	@Schema(description = "操作类型")
	@TableField("operate")
	private String operate;
	
	@Schema(description = "请求参数")
	@TableField("params")
	private String params;
	
	@Schema(description = "执行的方法")
	@TableField("method")
	private String method;
	
	@Schema(description = "操作SQL")
	@TableField("sql")
	private String sql;
	
	@Schema(description = "操作结果 1 成功")
	@TableField("result")
	private Boolean result;
	
	@Schema(description = "错误信息")
	@TableField("error_msg")
	private String errorMsg;
	
	@Schema(description = "创建人")
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	private Long createBy;
	
	@Schema(description = "创建时间")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	
}
