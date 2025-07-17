package cn.yshujia.ui.vo;

import cn.yshujia.domain.enums.GenderEnum;
import cn.yshujia.handler.domain.DomainHandler;
import cn.yshujia.handler.sensitive.EncryptionHandler;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description domain
 */

@Data
public class UserVO implements Serializable {
	
	@JSONField(serialize = false)
	private Long id;
	
	@JSONField(serialize = false)
	private Long roleId;
	
	@JSONField(serialize = false)
	private String password;
	
	@JSONField(serialize = false)
	private Boolean roleAdmin;
	
	@JSONField(serialize = false)
	@TableField(value = "permission", typeHandler = Fastjson2TypeHandler.class)
	private List<String> permission;
	
	@TableField(value = "avatar", typeHandler = DomainHandler.class)
	private String avatar;
	
	private String username;
	
	private GenderEnum gender;
	
	@TableField(value = "email", typeHandler = EncryptionHandler.class)
	private String email;
	
	private Integer experience;
	
	private String roleName;
	
	@JSONField(serialize = false)
	private Long userCount;
	
	private String roleIntroduction;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	private String token;
	
}
