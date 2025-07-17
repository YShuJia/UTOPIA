package cn.yshujia.domain.entity;

import cn.yshujia.domain.Base;
import cn.yshujia.domain.enums.GenderEnum;
import cn.yshujia.handler.domain.DomainHandler;
import cn.yshujia.handler.sensitive.EncryptionHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author yshujia
 * @since 2025/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_user", autoResultMap = true)
@Schema(name = "User", description = "用户")
@AllArgsConstructor
@NoArgsConstructor
public class User extends Base {
	
	@Schema(description = "角色ID")
	@TableField("role_id")
	private Long roleId;
	
	@Schema(description = "头像")
	@TableField(value = "avatar", typeHandler = DomainHandler.class)
	private String avatar;
	
	@Schema(description = "用户名")
	@TableField("username")
	private String username;
	
	@Schema(description = "密码")
	@TableField("`password`")
	private String password;
	
	@Schema(description = "性别 -1女 0保密 1男")
	@TableField("gender")
	private GenderEnum gender;
	
	@Schema(description = "邮箱")
	@TableField(value = "email", typeHandler = EncryptionHandler.class)
	private String email;
	
	@Schema(description = "经验值")
	@TableField("experience")
	private Integer experience;
	
	public User(Long id, Boolean enabled) {
		this.id = id;
	}
	
	public User(String email, Boolean enabled) {
		this.email = email;
	}
	
	public User(Long id, String username, String encode, String email, String icon) {
		this.id = id;
		this.username = username;
		this.password = encode;
		this.email = email;
		this.avatar = icon;
	}
	
}
