package cn.yshujia.filter;

import cn.yshujia.domain.LoginUser;
import cn.yshujia.repository.SysRepository;
import cn.yshujia.service.TokenService;
import cn.yshujia.service.impl.RedisServiceImpl;
import cn.yshujia.utils.ResponseUtils;
import cn.yshujia.utils.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description jwt过滤器
 */

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Resource
	private TokenService service;

	@Resource
	private RedisServiceImpl<Long> redis;

	@Resource
	private SysRepository sysRepository;

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws IOException, ServletException {
		// 判断 IP 是否被封禁
		Double expiration = sysRepository.expirationLimit(request);
		if (expiration > 0) {
			ResponseUtils.writeFailure(response, "ip 已被封禁，请 " + expiration + " 小时后重试");
			return;
		}
		// 获取用户信息
		LoginUser loginUser = service.getLoginUser(request);
		// 更新用户 token
		service.refreshToken(loginUser, response);
		if (!ObjectUtils.isEmpty(loginUser) && ObjectUtils.isEmpty(SecurityUtils.getAuthentication())) {
			// 存储 用户信息到上下文
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		} else if (ObjectUtils.isEmpty(loginUser) && !ObjectUtils.isEmpty(SecurityUtils.getAuthentication())) {
			SecurityUtils.clearContext();
		}
		chain.doFilter(request, response);
	}

}