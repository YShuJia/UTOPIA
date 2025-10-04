package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminWebsiteVO;
import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.WebsiteDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminWebsiteServiceImpl;
import cn.yshujia.utils.SecurityUtils;
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
@Tag(name = "Website", description = "管理端网站Api")
@RequestMapping("/admin/website")
public class AdminWebsiteController extends BaseController {

	@Resource
	private AdminWebsiteServiceImpl service;


	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "admin获取网站分页")
	@PreAuthorize("@sys.hasOnePermission('website:admin', 'website:select')")
	public ApiVO<PageVO<AdminWebsiteVO>> page(WebsiteDTO dto) {
		startPage();
		if (!SecurityUtils.isAdmin()) {
			dto.setCreateBy(SecurityUtils.getUserId());
		}
		return success(service.pageAdmin(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "添加网站")
	@Experience(value = 20)
	@PreAuthorize("@sys.hasOnePermission('website:admin', 'website:insert')")
	public ApiVO<Boolean> insert(@Validated(InsertGroup.class) @RequestBody WebsiteDTO dto) {
		if (!isAdmin()) {
			dto.setReviewed(1);
		}
		service.insert(dto);
		return message("网站添加成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "更新网站")
	@PreAuthorize("@sys.hasOnePermission('website:admin', 'website:update')")
	public ApiVO<Boolean> update(@Validated(UpdateGroup.class) @RequestBody WebsiteDTO dto) {
		dto.setReviewed(null);
		service.update(dto);
		return message("更新网站成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update/reviewed")
	@Operation(summary = "admin更新网站审核状态（分离方便鉴权）")
	@PreAuthorize("@sys.hasOnePermission('website:admin')")
	public ApiVO<Boolean> updateReviewed(@Validated(UpdateGroup.class) @RequestBody WebsiteDTO dto) {
		service.update(dto);
		return message(dto.getReviewed() == 0 ? "已拒绝网站提交！" : "已同意网站提交！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "admin删除网站")
	@PreAuthorize("@sys.hasOnePermission('website:admin', 'website:delete')")
	public ApiVO<Boolean> delete(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("删除网站成功！");
	}

}
