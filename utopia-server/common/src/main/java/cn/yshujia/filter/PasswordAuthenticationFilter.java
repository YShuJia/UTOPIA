package cn.yshujia.filter;

import cn.yshujia.config.SystemConfig;
import cn.yshujia.constant.HttpCode;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.constant.SecurityConst;
import cn.yshujia.domain.LoginUser;
import cn.yshujia.domain.entity.User;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.encryption.SMEncrypt;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.UserMapper;
import cn.yshujia.repository.SMRepository;
import cn.yshujia.service.TokenService;
import cn.yshujia.service.impl.RedisServiceImpl;
import cn.yshujia.ui.vo.UserVO;
import cn.yshujia.utils.*;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 实现账号，密码解密过滤器
 */

@Slf4j
public class PasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	
	@Resource
	TokenService service;
	
	@Resource
	SMRepository smRepository;
	
	@Resource
	UserMapper userMapper;
	
	@Resource
	RedisServiceImpl<Object> redis;
	
	@Resource
	SystemConfig systemConfig;
	
	@Autowired
	public PasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String key = systemConfig.getPasswordBanKey() + RequestUtils.getIp(request);
		if (redis.hasKey(key)) {
			Long expiration = redis.getExpireHours(key);
			ResponseUtils.writeFailure(response, "已被封禁，" + expiration + "小时后解封！");
			return null;
		}
		// 验证码校验
		String code = request.getParameter("code");
		String ip = RequestUtils.getIp(request);
		String captcha = Optional.ofNullable(redis.get(RedisKeys.CAPTCHA + ip)).orElse("").toString();
		if (StringUtils.isEmpty(captcha)) {
			ResponseUtils.writeFailure(response, "验证码已过期！");
		} else if (!code.equals(captcha)) {
			ResponseUtils.writeFailure(response, "验证码错误！");
		} else {
			String encryptedUsername = request.getParameter("username");
			String encryptedPassword = request.getParameter("password");
			String privateKey = this.smRepository.getPrivateKey(request);
			String username;
			String password;
			try {
				username = SMEncrypt.deSm2(privateKey, encryptedUsername);
				password = SMEncrypt.deSm2(privateKey, encryptedPassword);
			} catch (CustomException e) {
				// 移除 密钥 缓存
				smRepository.remove(request);
				throw new ServiceException("数据解密失败，请刷新页面后重试！");
			}
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
			return this.authenticationManager.authenticate(authRequest);
		}
		return null;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
		LoginUser loginUser = (LoginUser) authResult.getPrincipal();
		String publicKey = request.getHeader(SecurityConst.PUBLIC_KEY);
		// 获取用户信息
		String uuid = IDUtils.simpleUUID();
		String token = JwtTokenUtils.generatorToken(uuid);
		// 设置 token 和登录时间
		loginUser.setToken(token);
		loginUser.setLoginTime(System.currentTimeMillis());
		// 存储用户信息
		service.setLoginUser(loginUser, uuid);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		// 删除验证码
		redis.delKey(RedisKeys.CAPTCHA + RequestUtils.getIp(request));
		ApiVO<?> apiVO = null;
		try {
			UserVO userVO = userMapper.one(new User(loginUser.getUserId(), true));
			userVO.setToken(token);
			apiVO = ApiVO.success(HttpCode.NEED_DECRYPTION, "登录成功，即将刷新页面！", SMEncrypt.enSm2(publicKey, userVO));
		} catch (CustomException e) {
			log.error("\n数据加密失败：{}", e.getLocalizedMessage());
			throw new ServiceException("数据加密失败，请刷新页面后重试！");
		}
		ResponseUtils.writeSuccess(response, apiVO);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
		Long times = ban(request);
		if (times == 0) {
			ResponseUtils.writeFailure(response, "邮箱或密码错误，已封禁 ip！");
		} else if (times < 0) {
			ResponseUtils.writeFailure(response, "已被封禁，" + systemConfig.getBanTime() + "小时后解封！");
		} else {
			ResponseUtils.writeFailure(response, "邮箱或密码错误，还可验证 " + times + " 次！");
		}
	}
	
	
	private Long ban(HttpServletRequest request) {
		String key = systemConfig.getPasswordErrorKey() + RequestUtils.getIp(request);
		Long times = redis.increment(key, 1L, Duration.ofHours(systemConfig.getErrorTime()));
		if (times > systemConfig.getErrorTimes()) {
			redis.set(systemConfig.getPasswordBanKey() + RequestUtils.getIp(request), "true", Duration.ofHours(systemConfig.getBanTime()));
			return -1L;
		}
		return systemConfig.getErrorTimes() - times;
	}
	
}