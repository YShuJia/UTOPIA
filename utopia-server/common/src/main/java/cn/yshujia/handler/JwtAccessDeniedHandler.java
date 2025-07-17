package cn.yshujia.handler;

import cn.yshujia.constant.HttpCode;
import cn.yshujia.domain.vo.ApiVO;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 拒绝访问处理器
 */

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		ApiVO<Boolean> apiVO = ApiVO.failure(HttpCode.FORBIDDEN, "暂无权限访问！");
		response.setStatus(403);
		try (PrintWriter writer = response.getWriter()) {
			writer.write(JSON.toJSONString(apiVO));
			writer.flush();
		}
	}
	
}