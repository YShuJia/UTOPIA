package cn.yshujia.handler;

import cn.yshujia.repository.SMRepository;
import cn.yshujia.service.TokenService;
import cn.yshujia.utils.JwtTokenUtils;
import cn.yshujia.utils.ResponseUtils;
import cn.yshujia.utils.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 退出成功处理器
 */

@Slf4j
@Component
public class LogoutSuccessHandle implements LogoutSuccessHandler {

	@Resource
	private TokenService tokenService;

	@Resource
	private SMRepository smRepository;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		String uuid = JwtTokenUtils.getId(request);
		if (uuid != null) {
			SecurityUtils.clearContext();
			tokenService.delLoginUser(uuid);
			smRepository.remove(request);
		}
		ResponseUtils.writeSuccess(response, "退出成功，即将重新加载页面！", true);
	}

}