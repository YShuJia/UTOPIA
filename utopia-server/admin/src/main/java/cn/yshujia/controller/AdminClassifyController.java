package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminClassifyVO;
import cn.yshujia.admin.vo.AdminTreeVO;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.ClassifyDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminClassifyServiceImpl;
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
@Tag(name = "Classify", description = "管理端分类Api")
@RequestMapping("/admin/classify")
public class AdminClassifyController extends BaseController {

	@Resource
	AdminClassifyServiceImpl service;

	@RateLimiter(count = 5)
	@GetMapping("/tree/{key}")
	@Operation(summary = "通过分类 type 获取分类、标签树信息")
	public ApiVO<List<AdminTreeVO>> treeList(@PathVariable String key) {
		return success(service.treeList(key));
	}

	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "admin获取类别分页")
	@PreAuthorize("@sys.hasOnePermission('classify:admin', 'classify:select')")
	public ApiVO<PageVO<AdminClassifyVO>> page(ClassifyDTO dto) {
		startPage();
		if (!SecurityUtils.isAdmin()) {
			dto.setCreateBy(SecurityUtils.getUserId());
		}
		return success(service.pageAdmin(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "admin添加分类")
	@PreAuthorize("@sys.hasOnePermission('classify:admin', 'classify:insert')")
	public ApiVO<Boolean> insert(@Validated(InsertGroup.class) @RequestBody ClassifyDTO dto) {
		if (!isAdmin()) {
			dto.setReviewed(1);
		}
		service.insert(dto);
		return message("分类添加成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "admin更新分类")
	@PreAuthorize("@sys.hasOnePermission('classify:admin', 'classify:update')")
	public ApiVO<Boolean> update(@Validated(UpdateGroup.class) @RequestBody ClassifyDTO dto) {
		dto.setReviewed(null);
		service.update(dto);
		return message("分类更新成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update/reviewed")
	@Operation(summary = "admin更新类别审核状态（分离方便鉴权）")
	@PreAuthorize("@sys.hasOnePermission('classify:admin')")
	public ApiVO<Boolean> updateReviewed(@Validated(UpdateGroup.class) @RequestBody ClassifyDTO dto) {
		service.update(dto);
		return message(dto.getReviewed() == 0 ? "已拒绝类别提交！" : "已同意类别提交！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "admin删除分类")
	@PreAuthorize("@sys.hasOnePermission('classify:admin', 'classify:delete')")
	public ApiVO<Boolean> remove(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("分类删除成功！");
	}

}

