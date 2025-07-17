package cn.yshujia.utils;


import cn.yshujia.constant.HttpCode;
import cn.yshujia.domain.vo.ApiVO;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: yshujia
 * @create: 2025/6/6 21:08
 * @description: ResponseUtils
 */

@Slf4j
public class ResponseUtils {
	
	public static void writeSuccess(HttpServletResponse response, ApiVO<?> apiVO) {
		try {
			response.setContentType("application/json;charset=UTF-8");
			try (PrintWriter writer = response.getWriter()) {
				writer.write(JSON.toJSONString(apiVO));
				writer.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException("写入 JSON 错误响应失败", e);
		}
	}
	
	public static void writeSuccess(HttpServletResponse response, String message, Object data) {
		try {
			response.setContentType("application/json;charset=UTF-8");
			ApiVO<Object> apiVO = ApiVO.success(HttpCode.SUCCESS, message, data);
			try (PrintWriter writer = response.getWriter()) {
				writer.write(JSON.toJSONString(apiVO));
				writer.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException("写入 JSON 错误响应失败", e);
		}
	}
	
	public static void writeFailure(HttpServletResponse response, String message) {
		try {
			response.setContentType("application/json;charset=UTF-8");
			ApiVO<Boolean> apiVO = ApiVO.failure(message);
			try (PrintWriter writer = response.getWriter()) {
				writer.write(JSON.toJSONString(apiVO));
				writer.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException("写入 JSON 错误响应失败", e);
		}
	}
	
}
