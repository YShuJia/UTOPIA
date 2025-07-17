package cn.yshujia.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author yshujia
 * @create 2025/4/7
 * @description GenderEnum
 */

@Getter
public enum GenderEnum implements IEnum<Integer> {
	
	MAN(1, "男"),
	
	SECRET(0, "保密"),
	
	WOMAN(-1, "女");
	
	@EnumValue
	private final Integer type;
	
	@JsonValue
	private final String message;
	
	@JsonCreator
	GenderEnum(Integer type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public static GenderEnum getByType(Integer type) {
		for (GenderEnum genderEnum : values()) {
			if (genderEnum.getType().equals(type)) {
				return genderEnum;
			}
		}
		return null;
	}
	
	@Override
	public Integer getValue() {
		return type;
	}
}
