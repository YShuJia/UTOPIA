package cn.yshujia.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

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
@TableName("t_router")
@Schema(name = "Router", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class Router implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "主键（自增）")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	
	@Schema(description = "权限ID 向下兼容")
	@TableField("role_id")
	private Long roleId;
	
	@Schema(description = "父路由ID")
	@TableField("parent_id")
	private Long parentId;
	
	@Schema(description = "前端通过name获取路由")
	@TableField("`name`")
	private String name;
	
	@Schema(description = "路由标题")
	@TableField("title")
	private String title;
	
	@Schema(description = "路由排序")
	@TableField("sort")
	private Byte sort;
	
	@Schema(description = "路由类型（B 按钮，M 菜单，H 导航栏不显示的路由）")
	@TableField("`type`")
	private String type;
	
	@Schema(description = "路由地址")
	@TableField("`path`")
	private String path;
	
	@Schema(description = "路由前缀图标")
	@TableField("icon")
	private String icon;
	
	@Schema(description = "路由组件路径")
	@TableField("component")
	private String component;
	
	@Schema(description = "是否admin端路由")
	@TableField("is_admin")
	private Boolean admin;
	
	@Schema(description = "是否外链")
	@TableField("is_frame")
	private Boolean frame;
	
	@Schema(description = "是否启用")
	@TableField("is_enabled")
	private Boolean enabled;
	
	public Router(Long roleId) {
		this.roleId = roleId;
	}
	
}
