package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminLabelVO;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.LabelDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminLabelServiceImpl;
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
@Tag(name = "Label", description = "管理端标签Api")
@RequestMapping("/admin/label")
public class AdminLabelController extends BaseController {

	@Resource
	private AdminLabelServiceImpl service;

	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "admin获取标签分页")
	@PreAuthorize("@sys.hasOnePermission('label:admin', 'label:select')")
	public ApiVO<PageVO<AdminLabelVO>> page(LabelDTO dto) {
		startPage();
		if (!SecurityUtils.isAdmin()) {
			dto.setCreateBy(SecurityUtils.getUserId());
		}
		return success(service.pageAdmin(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "admin添加标签")
	@PreAuthorize("@sys.hasOnePermission('label:admin', 'label:insert')")
	public ApiVO<Boolean> insert(@Validated(InsertGroup.class) @RequestBody LabelDTO dto) {
		if (isAdmin()) {
			dto.setReviewed(1);
		}
		service.insert(dto);
		return message("标签添加成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "admin更新标签")
	@PreAuthorize("@sys.hasOnePermission('label:admin', 'label:update')")
	public ApiVO<Boolean> update(@Validated(UpdateGroup.class) @RequestBody LabelDTO dto) {
		dto.setReviewed(null);
		service.update(dto);
		return message("标签更新成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update/reviewed")
	@Operation(summary = "admin更新标签审核状态（分离方便鉴权）")
	@PreAuthorize("@sys.hasOnePermission('label:admin')")
	public ApiVO<Boolean> updateReviewed(@Validated(UpdateGroup.class) @RequestBody LabelDTO dto) {
		service.update(dto);
		return message(dto.getReviewed() == 0 ? "已拒绝标签提交！" : "已同意标签提交！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "admin删除标签")
	@PreAuthorize("@sys.hasOnePermission('label:admin', 'label:delete')")
	public ApiVO<Boolean> remove(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("标签删除成功！");
	}

}


