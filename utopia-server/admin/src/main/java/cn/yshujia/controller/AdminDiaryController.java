package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminDiaryVO;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.DiaryDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminDiaryServiceImpl;
import cn.yshujia.utils.SecurityUtils;
import cn.yshujia.validation.DeleteGroup;
import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
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
@Tag(name = "Diary", description = "管理端日记Api")
@RequestMapping("/admin/diary")
public class AdminDiaryController extends BaseController {

	@Resource
	private AdminDiaryServiceImpl service;

	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "admin分页查询日记")
	@PreAuthorize("@sys.hasOnePermission('diary:admin', 'diary:select')")
	public ApiVO<PageVO<AdminDiaryVO>> page(DiaryDTO dto) {
		startPage();
		if (!SecurityUtils.isAdmin()) {
			dto.setCreateBy(SecurityUtils.getUserId());
		}
		return success(service.pageAdmin(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "admin新增日记")
	@PreAuthorize("@sys.hasOnePermission('diary:admin', 'diary:insert')")
	public ApiVO<Boolean> insert(@RequestPart(value = "files") MultipartFile[] files,
	                             @Validated(InsertGroup.class) DiaryDTO dto) {
		if (isAdmin()) {
			dto.setReviewed(1);
		}
		service.insert(files, dto);
		return message("日记添加成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "admin更新日记")
	@PreAuthorize("@sys.hasOnePermission('diary:admin', 'diary:update')")
	public ApiVO<Boolean> update(@RequestPart(value = "files", required = false) MultipartFile[] files,
	                             @Validated(UpdateGroup.class) DiaryDTO dto) {
		dto.setReviewed(null);
		service.update(files, dto);
		return message("日记更新成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update/reviewed")
	@Operation(summary = "admin更新日记审核状态（分离方便鉴权）")
	@PreAuthorize("@sys.hasOnePermission('diary:admin')")
	public ApiVO<Boolean> updateReviewed(@Validated(UpdateGroup.class) @RequestBody DiaryDTO dto) {
		service.update(dto);
		return message(dto.getReviewed() == 0 ? "已拒绝日记提交！" : "已同意日记提交！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "admin删除日记")
	@PreAuthorize("@sys.hasOnePermission('diary:admin', 'diary:delete')")
	public ApiVO<Boolean> delete(@NotNull(groups = DeleteGroup.class, message = "[日记ID] 不能为空！")
	                             @RequestBody List<Long> ids) {
		service.remove(ids);
		return message("日记删除成功！");
	}

}
