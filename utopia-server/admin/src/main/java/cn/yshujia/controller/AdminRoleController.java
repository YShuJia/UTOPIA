package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminSelectRoleVO;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.RoleDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminRoleServiceImpl;
import cn.yshujia.ui.vo.RoleVO;
import cn.yshujia.validation.UpdateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */

@Validated
@RestController
@Tag(name = "Role", description = "管理端角色Api")
@RequestMapping("/admin/role")
public class AdminRoleController extends BaseController {
	
	@Resource
	private AdminRoleServiceImpl service;
	
	
	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "admin分页获取角色")
	@PreAuthorize("@sys.hasOnePermission('role:admin')")
	public ApiVO<PageVO<RoleVO>> page(RoleDTO dto) {
		startPage();
		return success(service.pageByAdmin(dto));
	}
	
	@RateLimiter(count = 5)
	@GetMapping("/select")
	@Operation(summary = "admin获取角色做下拉框数据")
	public ApiVO<List<AdminSelectRoleVO>> select() {
		return success(service.listSelectData());
	}
	
	
	@RateLimiter(count = 5)
	@GetMapping("/list/table")
	@Operation(summary = "admin获取表名生成权限字符 ")
	public ApiVO<List<String>> table() {
		return success(service.table());
	}
	
	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "admin更新角色")
	@PreAuthorize("@sys.hasOnePermission('role:admin')")
	public ApiVO<Boolean> update(@Validated(UpdateGroup.class) @RequestBody RoleDTO dto) {
		service.update(dto);
		return message("角色更新成功！");
	}
	
}
