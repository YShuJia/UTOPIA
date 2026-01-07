package cn.yshujia.controller;

import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.CommentDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.CommentVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminCommentServiceImpl;
import cn.yshujia.validation.InsertGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */

@Validated
@RestController
@Tag(name = "Comment", description = "管理端评论Api")
@RequestMapping("/admin/comment")
public class AdminCommentController extends BaseController {

	@Resource
	private AdminCommentServiceImpl service;

	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "Admin 根据查询条件获取评论分页")
	@PreAuthorize("@sys.hasOnePermission('comment:admin')")
	public ApiVO<PageVO<CommentVO>> page(CommentDTO dto) {
		startPage();
		return success(service.pageAdmin(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "Admin 添加评论")
	@Experience(value = 10)
	@PreAuthorize("@sys.hasOnePermission('comment:admin')")
	public ApiVO<Boolean> insert(@RequestParam(value = "files", required = false) MultipartFile files,
	                             @Validated(InsertGroup.class) CommentDTO dto) throws IOException {
		service.insert(files, dto);
		return message("评论添加成功！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "Admin 删除评论，将评论内容更新为 该评论已删除")
	@PreAuthorize("@sys.hasOnePermission('comment:admin')")
	public ApiVO<Boolean> delete(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("评论删除成功！");
	}

}
