package cn.yshujia.controller;

import cn.yshujia.annotation.DecryptFields;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.service.impl.RoleServiceImpl;
import cn.yshujia.ui.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */
@RestController
@Tag(name = "Role", description = "角色Api")
@RequestMapping("/ui/role")
public class RoleController extends BaseController {
	
	@Resource
	private RoleServiceImpl service;
	
	@RateLimiter(count = 2)
	@GetMapping("/gtId")
	@Operation(summary = "获取等级大一级的角色经验值")
	public ApiVO<RoleVO> getGtIdExperience() {
		Long id = getRoleId();
		return success(service.getGtIdRole(id));
	}
	
	@RateLimiter(count = 2)
	@DecryptFields
	@GetMapping("/list")
	@Operation(summary = "获取所有角色经验值及简介")
	public ApiVO<List<RoleVO>> list() {
		return success(service.listUI());
	}
	
}
