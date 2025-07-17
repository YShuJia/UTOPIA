package cn.yshujia.domain.vo;

import cn.yshujia.constant.HttpCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 响应对象
 */

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ApiVO<T> implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1905122041950251207L;
	
	private int code;
	
	private String msg;
	
	private T data;
	
	public static <T> ApiVO<T> success(int code, String msg, T obj) {
		return new ApiVO<>(code, msg, obj);
	}
	
	public static <T> ApiVO<T> success(T data) {
		return new ApiVO<>(HttpCode.SUCCESS, "成功", data);
	}
	
	public static ApiVO<Boolean> message(String msg) {
		return new ApiVO<>(HttpCode.SUCCESS, msg, true);
	}
	
	public static <T> ApiVO<T> failure(String msg) {
		return new ApiVO<>(HttpCode.FAILURE, msg, null);
	}
	
	public static ApiVO<Boolean> failure(int code, String msg) {
		return new ApiVO<>(code, msg, false);
	}
	
	@Override
	public String toString() {
		return "{ code = " + this.code + " <---> msg =  " + this.msg + " <---> data = " + this.data.toString() + " }";
	}
	
}