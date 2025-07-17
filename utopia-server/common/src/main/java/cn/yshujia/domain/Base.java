package cn.yshujia.domain;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author：yshujia
 * @create: 2025/5/2 13:03
 * @description: Base
 */

@Data
public class Base implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	public Long id;
	
	@Schema(description = "创建时间")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date createTime;
	
	@Schema(description = "更新时间")
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date updateTime;
	
	@Schema(description = "创建人")
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	public Long createBy;
	
	@Schema(description = "更新人")
	@TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
	public Long updateBy;
	
	@Schema(description = "是否启用")
	@TableField("is_enabled")
	public Boolean enabled;
	
}
