package cn.yshujia.service;

import cn.yshujia.domain.LoginUser;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.SecurityUtils;
import cn.yshujia.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Set;

/**
 * @author yshujia
 * @create 2025/3/9
 * @description PermissionService 实现权限验证
 */

@Service("sys")
public class PermissionService {
	
	/**
	 * @param args 权限字符串数组
	 * @return 只要有其中一个权限，返回 true
	 */
	public boolean hasOnePermission(String... args) throws ServiceException {
		if (ObjectUtils.isEmpty(args)) {
			return false;
		}
		LoginUser loginUser = SecurityUtils.getLoginUser();
		if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermission())) {
			return false;
		}
		Set<String> permissions = loginUser.getPermission();
		for (String arg : args) {
			if (permissions.contains(StringUtils.trim(arg))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param args 权限字符串数组
	 * @return 必须含有所有权限，返回 true
	 */
	public boolean hasAllPermission(String... args) throws ServiceException {
		if (ObjectUtils.isEmpty(args)) {
			return false;
		}
		LoginUser loginUser = SecurityUtils.getLoginUser();
		if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermission())) {
			return false;
		}
		Set<String> permissions = loginUser.getPermission();
		for (String arg : args) {
			if (!permissions.contains(StringUtils.trim(arg))) {
				return false;
			}
		}
		return true;
	}
	
}
