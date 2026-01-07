package cn.yshujia.controller;

import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.LeaveWordDTO;
import cn.yshujia.domain.entity.LeaveWord;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminLeaveWordServiceImpl;
import cn.yshujia.utils.RequestUtils;
import cn.yshujia.validation.InsertGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "LeaveWord", description = "管理端树洞Api")
@RequestMapping("/admin/leave_word")
public class AdminLeaveWordController extends BaseController {

	@Resource
	private AdminLeaveWordServiceImpl service;

	@Logger
	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "Admin 获取留言分页")
	@PreAuthorize("@sys.hasOnePermission('leave_word:admin', 'leave_word:select')")
	public ApiVO<PageVO<LeaveWord>> page(LeaveWordDTO dto) {
		startPage();
		return success(service.pageAdmin(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "Admin 新增留言")
	@Experience(value = 10)
	@PreAuthorize("@sys.hasOnePermission('leave_word:admin', 'leave_word:insert')")
	public ApiVO<Boolean> insert(@Validated(InsertGroup.class) @RequestBody LeaveWordDTO dto,
	                             HttpServletRequest req) {
		dto.setIp(RequestUtils.getIp(req));
		service.insert(dto);
		return message("留言新增成功");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "Admin 删除留言")
	@PreAuthorize("@sys.hasOnePermission('leave_word:admin')")
	public ApiVO<Boolean> delete(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("留言删除成功！");
	}

}

