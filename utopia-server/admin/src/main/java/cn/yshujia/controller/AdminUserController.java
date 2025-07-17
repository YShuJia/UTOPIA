package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminUserVO;
import cn.yshujia.annotation.DecryptFields;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.UserDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.service.impl.AdminUserServiceImpl;
import cn.yshujia.validation.InsertGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */

@Validated
@RestController
@Tag(name = "User", description = "管理端用户Api")
@RequestMapping("/admin/user")
public class AdminUserController extends BaseController {
	
	@Resource
	private AdminUserServiceImpl service;
	
	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "admin分页查询用户")
	@PreAuthorize("@sys.hasOnePermission('user:admin')")
	public ApiVO<PageVO<AdminUserVO>> page(UserDTO dto) {
		startPage();
		return success(service.pageAdmin(dto));
	}
	
	@RateLimiter
	@GetMapping("/distribution")
	@Operation(summary = "admin获取用户分布柱状图数据")
	public ApiVO<Map<String, List<String>>> selectCountGroupByRoleId() {
		return success(service.selectCountGroupByRoleId());
	}
	
	@RateLimiter
	@DecryptFields(value = {"email", "password"})
	@PostMapping("/insert")
	@Operation(summary = "admin添加用户")
	@PreAuthorize("@sys.hasOnePermission('user:admin')")
	public ApiVO<Boolean> insert(@Validated(InsertGroup.class) @RequestBody UserDTO dto) throws ServiceException {
		service.insert(dto);
		return message("用户添加成功！");
	}
	
}