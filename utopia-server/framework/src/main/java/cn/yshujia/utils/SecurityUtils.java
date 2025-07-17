package cn.yshujia.utils;

import cn.yshujia.constant.HttpCode;
import cn.yshujia.constant.SecurityConst;
import cn.yshujia.domain.LoginUser;
import cn.yshujia.ex.ServiceException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description
 */

@Data
@Component
public class SecurityUtils {
	
	public static Long getUserId() {
		try {
			return getLoginUser().getUserId();
		} catch (Exception e) {
			throw response();
		}
	}
	
	public static Long getRoleId() {
		try {
			return getLoginUser().getRoleId();
		} catch (Exception e) {
			throw response();
		}
	}
	
	public static Boolean isAdmin() {
		return getLoginUser().getAdmin();
	}
	
	public static LoginUser getLoginUser() {
		try {
			return (LoginUser) getAuthentication().getPrincipal();
		} catch (Exception e) {
			throw response();
		}
	}
	
	private static ServiceException response() {
		String token = RequestUtils.getReq().getHeader(SecurityConst.TOKEN_KEY);
		if (token != null) {
			throw new ServiceException(HttpCode.UNAUTHORIZED, "授权过期，请重新登录！");
		}
		throw new ServiceException(HttpCode.UNAUTHORIZED, "请登录后重试！");
	}
	
	public static String encrypt(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}
	
	public static boolean matches(String rawPassword, String encodedPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
	
	public static <T> Wrapper<T> createDeleteWrapper(List<Long> ids) {
		if (isAdmin()) {
			return new QueryWrapper<T>().in("id", ids);
		}
		Long userId = SecurityUtils.getUserId();
		return new QueryWrapper<T>().in("id", ids).eq("create_by", userId);
	}
	
	public static <T> Wrapper<T> createUpdateWrapper(Long id) {
		if (isAdmin()) {
			return new UpdateWrapper<T>().eq("id", id);
		}
		Long userId = SecurityUtils.getUserId();
		return new UpdateWrapper<T>().eq("id", id).eq("create_by", userId);
	}
	
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public static void clearContext() {
		SecurityContextHolder.clearContext();
	}
	
}