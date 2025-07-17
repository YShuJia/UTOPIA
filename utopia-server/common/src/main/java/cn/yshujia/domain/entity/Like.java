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
@TableName("t_like")
@Schema(name = "Like", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class Like implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	
	@Schema(description = "点赞用户")
	@TableField(value = "user_id")
	private Long userId;
	
	@Schema(description = "源ID")
	@TableField(value = "source_id")
	private Long sourceId;
	
	@Schema(description = "是否点赞")
	@TableField("is_liked")
	private Boolean liked;
	
	@Schema(description = "创建时间")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	
	public Like(Long userId, Long sourceId) {
		this.userId = userId;
		this.sourceId = sourceId;
	}
	
}
