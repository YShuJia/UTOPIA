package cn.yshujia.ex;

import cn.yshujia.constant.HttpCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yshujia
 * @description 自定义异常
 * @create 2024/4/23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {
	
	private Integer code;
	
	private String message;
	
	public ServiceException(String message) {
		super(message);
		this.code = HttpCode.FAILURE;
		this.message = message;
	}
	
	public ServiceException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	
}
