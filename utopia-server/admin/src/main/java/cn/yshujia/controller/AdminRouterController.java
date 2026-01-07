package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminRouterVO;
import cn.yshujia.admin.vo.AdminSelectRouterVO;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.RouterDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminRouterServiceImpl;
import cn.yshujia.validation.InsertGroup;
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
@Tag(name = "Router", description = "管理端路由Api")
@RequestMapping("/admin/router")
public class AdminRouterController extends BaseController {

	@Resource
	AdminRouterServiceImpl service;

	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "Admin 获取权限内路由")
	@PreAuthorize("@sys.hasOnePermission('router:admin')")
	public ApiVO<PageVO<AdminRouterVO>> page(RouterDTO dto) {
		startPage();
		return success(service.pageAdmin(dto));
	}

	@RateLimiter(count = 5)
	@GetMapping("/select")
	@Operation(summary = "Admin 获取菜单路由做下拉框数据")
	public ApiVO<List<AdminSelectRouterVO>> select() {
		return success(service.listSelectData());
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "Admin 添加路由")
	@PreAuthorize("@sys.hasOnePermission('router:admin')")
	public ApiVO<Boolean> add(@Validated(InsertGroup.class) @RequestBody RouterDTO dto) {
		service.insert(dto);
		return message("路由添加成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "Admin 更新路由")
	@PreAuthorize("@sys.hasOnePermission('router:admin')")
	public ApiVO<Boolean> edit(@Validated(UpdateGroup.class) @RequestBody RouterDTO dto) {
		service.update(dto);
		return message("路由更新成功！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "Admin 删除路由")
	@PreAuthorize("@sys.hasOnePermission('router:admin')")
	public ApiVO<Boolean> remove(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("路由删除成功！");
	}

}


