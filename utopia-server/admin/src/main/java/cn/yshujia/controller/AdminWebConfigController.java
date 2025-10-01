package cn.yshujia.controller;

import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.WebConfigDTO;
import cn.yshujia.domain.entity.WebConfig;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminWebConfigServiceImpl;
import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author YShuJia
 * @since 2024/4/23
 */

@Validated
@RestController
@RequestMapping("/admin/web-config")
public class AdminWebConfigController extends BaseController {

	@Resource
	private AdminWebConfigServiceImpl service;

	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "admin获取配置分页")
	@PreAuthorize("@sys.hasOnePermission('sys_config:admin')")
	public ApiVO<PageVO<WebConfig>> page(WebConfigDTO dto) {
		startPage();
		return success(service.pageAdmin(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "admin新增配置")
	@PreAuthorize("@sys.hasOnePermission('sys_config:admin')")
	public ApiVO<Boolean> insert(@Validated(InsertGroup.class) @RequestBody WebConfigDTO dto) {
		dto.setEnabled(false);
		service.insert(dto);
		return message("配置添加成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "admin更新配置")
	@PreAuthorize("@sys.hasOnePermission('sys_config:admin')")
	public ApiVO<Boolean> update(@Validated(UpdateGroup.class) @RequestBody WebConfigDTO dto) {
		service.update(dto);
		return message("配置更新成功！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "admin删除配置")
	@PreAuthorize("@sys.hasOnePermission('sys_config:admin')")
	public ApiVO<Boolean> delete(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("配置删除成功！");
	}
}
