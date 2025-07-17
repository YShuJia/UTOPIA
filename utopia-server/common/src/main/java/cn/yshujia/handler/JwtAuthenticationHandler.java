package cn.yshujia.handler;

import cn.yshujia.constant.HttpCode;
import cn.yshujia.domain.vo.ApiVO;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 认证失败处理器
 */

@Component
public class JwtAuthenticationHandler implements AuthenticationEntryPoint {
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		ApiVO<Boolean> apiVO = ApiVO.failure(HttpCode.UNAUTHORIZED, "授权过期，请登录后重试！");
		response.setStatus(HttpCode.FORBIDDEN);
		try (PrintWriter writer = response.getWriter()) {
			writer.write(JSON.toJSONString(apiVO));
			writer.flush();
		}
	}
	
}
