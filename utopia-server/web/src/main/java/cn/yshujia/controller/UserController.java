package cn.yshujia.controller;

import cn.yshujia.annotation.DecryptFields;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.LoginUser;
import cn.yshujia.domain.dto.UserDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.service.impl.UserServiceImpl;
import cn.yshujia.ui.vo.SearchVO;
import cn.yshujia.ui.vo.UserVO;
import cn.yshujia.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */
@RestController
@Tag(name = "User", description = "用户Api")
@RequestMapping("/ui/user")
public class UserController extends BaseController {
	
	@Resource
	private UserServiceImpl service;
	
	@RateLimiter
	@GetMapping("/code")
	@Operation(summary = "获取登录注册验证码")
	public ApiVO<String> getCode(HttpServletRequest request) {
		return success(service.getCaptcha(request));
	}
	
	@RateLimiter(count = 2)
	@GetMapping("/exchange")
	@Operation(summary = "交换sm2密钥")
	public ApiVO<String> exchange(HttpServletRequest request) {
		return success(service.exchangeSm2Key(request));
	}
	
	@RateLimiter(count = 2)
	@GetMapping("/search/{key}")
	@Operation(summary = "全局搜索功能")
	public ApiVO<SearchVO> searchByTask(@PathVariable String key) {
		return success(service.searchByTask(key));
	}
	
	@RateLimiter(count = 2)
	@GetMapping("/today/experience")
	@Operation(summary = "获取用户今日经验值")
	public ApiVO<Integer> experience() {
		Long userId = SecurityUtils.getUserId();
		return success(service.getExperience(userId));
	}
	
	@RateLimiter(count = 2)
	@DecryptFields
	@GetMapping("/info")
	@Operation(summary = "根据 token 获取用户信息")
	public ApiVO<UserVO> info() {
		LoginUser loginUser = SecurityUtils.getLoginUser();
		UserVO user = service.oneById(loginUser.getUserId());
		user.setToken(loginUser.getToken());
		return success(user);
	}
	
	@RateLimiter
	@DecryptFields(value = {"email", "password"})
	@PostMapping("/insert")
	@Operation(summary = "注册用户")
	public ApiVO<Boolean> insert(@RequestParam String email, @RequestParam String password) {
		service.insert(email, password);
		return message("用户注册成功！");
	}
	
	@Logger
	@RateLimiter
	@DecryptFields(value = {"email", "password"})
	@PutMapping("/update")
	@Operation(summary = "更新用户信息")
	@PreAuthorize("@sys.hasOnePermission({'user:admin', 'user:update'})")
	public ApiVO<Boolean> update(@RequestBody UserDTO user) {
		user.setId(SecurityUtils.getUserId());
		service.update(user);
		return message("用户信息更新成功！");
	}
	
	@RateLimiter
	@DecryptFields(value = {"password", "newPassword"})
	@PutMapping("/update/password")
	@Operation(summary = "更新用户密码")
	@PreAuthorize("@sys.hasOnePermission({'user:admin', 'user:update'})")
	public ApiVO<Boolean> updatePassword(@RequestParam String password, @RequestParam String newPassword) {
		service.updatePassword(SecurityUtils.getUserId(), password, newPassword);
		return message("密码更新成功，请重新登录！");
	}
	
}