package cn.yshujia.ex;


import cn.yshujia.constant.HttpCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: yshujia
 * @create: 2025/7/5 11:19
 * @description: CustomException
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomException extends RuntimeException {
	
	private Integer code;
	
	private String message;
	
	public CustomException(String message) {
		super(message);
		this.code = HttpCode.SERVER_ERROR;
		this.message = message;
	}
	
}
