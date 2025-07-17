package cn.yshujia.domain.entity;

import cn.yshujia.handler.domain.DomainHandler;
import cn.yshujia.handler.sensitive.EncryptionHandler;
import cn.yshujia.handler.sensitive.SensitiveHandler;
import com.baomidou.mybatisplus.annotation.*;
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

/**
 * <p>
 * 树洞（留言）
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_leave_word", autoResultMap = true)
@Schema(name = "LeaveWord", description = "树洞（留言）")
@AllArgsConstructor
@NoArgsConstructor
public class LeaveWord implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "自增主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	
	@Schema(description = "发送人ip地址")
	@TableField(value = "ip", typeHandler = EncryptionHandler.class)
	private String ip;
	
	@Schema(description = "随机头像")
	@TableField(value = "avatar", typeHandler = DomainHandler.class)
	private String avatar;
	
	@Schema(description = "留言内容")
	@TableField(value = "content", typeHandler = SensitiveHandler.class)
	private String content;
	
	@Schema(description = "创建时间")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
}
