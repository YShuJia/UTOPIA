package cn.yshujia.domain.enums;


import lombok.Getter;

/**
 * @author: yshujia
 * @create: 2025/8/29 20:22
 * @description: OperateEnum 发送验证码操作类型枚举
 */

@Getter
public enum OperateEnum {

	REGISTER("注册用户"),

	LOGIN("本次登录"),

	UPDATE("本次修改信息");

	private String str;

	OperateEnum(String str) {
	}
}
