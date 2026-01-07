package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminFileVO;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.FileDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminFileServiceImpl;
import cn.yshujia.ui.vo.FileVO;
import cn.yshujia.utils.SecurityUtils;
import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */

@Validated
@RestController
@Tag(name = "File", description = "管理端资源Api")
@RequestMapping("/admin/file")
public class AdminFileController extends BaseController {

	@Resource
	private AdminFileServiceImpl service;

	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "Admin 根据查询条件获取资源分页")
	@PreAuthorize("@sys.hasOnePermission('file:admin', 'file:select')")
	public ApiVO<PageVO<AdminFileVO>> page(FileDTO dto) {
		startPage();
		if (!SecurityUtils.isAdmin()) {
			dto.setCreateBy(SecurityUtils.getUserId());
		}
		return success(service.pageAdmin(dto));
	}

	@RateLimiter(count = 5)
	@GetMapping("/page/select")
	@Operation(summary = "Admin 获取资源做选择数据")
	public ApiVO<PageVO<FileVO>> pageSelect(FileDTO dto) {
		startPage();
		if (!SecurityUtils.isAdmin()) {
			dto.setCreateBy(SecurityUtils.getUserId());
		}
		return success(service.pageSelect(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "Admin 添加资源，批量时以 file 传递的信息为基础")
	@PreAuthorize("@sys.hasOnePermission('file:admin', 'file:insert')")
	public ApiVO<Boolean> insert(@RequestParam("files") MultipartFile[] files,
	                             @Validated(InsertGroup.class) FileDTO dto) {
		if (isAdmin()) {
			dto.setReviewed(1);
		}
		service.insert(files, dto);
		return message("资源新增成功");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "Admin 更新资源")
	@PreAuthorize("@sys.hasOnePermission('file:admin', 'file:update')")
	public ApiVO<Boolean> update(@RequestParam(value = "files", required = false) MultipartFile files,
	                             @Validated(UpdateGroup.class) FileDTO dto) {
		dto.setReviewed(null);
		service.update(files, dto);
		return message("资源更新成功");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update/reviewed")
	@Operation(summary = "Admin 更新资源审核状态（分离方便鉴权）")
	@PreAuthorize("@sys.hasOnePermission('file:admin')")
	public ApiVO<Boolean> updateReviewed(@Validated(UpdateGroup.class) @RequestBody FileDTO dto) {
		service.update(null, dto);
		return message(dto.getReviewed() == 0 ? "已拒绝文件提交！" : "已同意文件提交！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "Admin 删除资源")
	@PreAuthorize("@sys.hasOnePermission('file:admin', 'file:delete')")
	public ApiVO<Boolean> remove(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("资源删除成功");
	}

}
