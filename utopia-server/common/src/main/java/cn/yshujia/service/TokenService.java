package cn.yshujia.service;


import cn.yshujia.config.JwtConfig;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.constant.SecurityConst;
import cn.yshujia.domain.LoginUser;
import cn.yshujia.service.impl.RedisServiceImpl;
import cn.yshujia.utils.IDUtils;
import cn.yshujia.utils.JwtTokenUtils;
import cn.yshujia.utils.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author: yshujia
 * @create: 2025/5/26 11:06
 * @description: TokenService
 */

@Slf4j
@Service
public class TokenService {
	
	@Resource
	JwtConfig jwtConfig;
	
	@Resource
	RedisServiceImpl<LoginUser> redis;
	
	/* 用户登录凭证 */
	public LoginUser getLoginUser(String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		String uuid = JwtTokenUtils.getId(token);
		return redis.get(RedisKeys.LOGIN_UUID + uuid);
	}
	
	public LoginUser getLoginUser(HttpServletRequest request) {
		String token = request.getHeader(SecurityConst.TOKEN_KEY);
		return getLoginUser(token);
	}
	
	public void refreshToken(LoginUser loginUser, HttpServletResponse response) {
		if (null == loginUser) {
			return;
		}
		// 获取 token 信息
		String token = loginUser.getToken();
		// 判断 token 有效时长 更新 token 和用户信息
		if (JwtTokenUtils.isRefresh(token)) {
			// 删除旧用户信息
			delLoginUser(JwtTokenUtils.getId(token));
			String uuid = IDUtils.simpleUUID();
			// 更新 token
			token = JwtTokenUtils.generatorToken(uuid);
			response.setHeader(SecurityConst.TOKEN_KEY, SecurityConst.TOKEN_PREFIX + token);
			loginUser.setToken(token);
			setLoginUser(loginUser, uuid);
		}
	}
	
	public void setLoginUser(LoginUser loginUser, String uuid) {
		redis.set(RedisKeys.LOGIN_UUID + uuid, loginUser, Duration.ofMinutes(jwtConfig.getExpire()));
	}
	
	public void delLoginUser(String uuid) {
		redis.delKey(RedisKeys.LOGIN_UUID + uuid);
	}
	
}
