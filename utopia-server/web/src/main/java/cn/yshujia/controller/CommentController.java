package cn.yshujia.controller;

import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.CommentDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.CommentVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.CommentServiceImpl;
import cn.yshujia.validation.InsertGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */

@Validated
@RestController
@Tag(name = "Comment", description = "评论Api")
@RequestMapping("/ui/comment")
public class CommentController extends BaseController {
	
	@Resource
	private CommentServiceImpl service;
	
	
	@Logger
	@RateLimiter
	@Experience(value = 10)
	@PostMapping("/insert")
	@Operation(summary = "添加评论")
	@PreAuthorize("@sys.hasOnePermission('comment:admin', 'comment:insert')")
	public ApiVO<Boolean> insert(@RequestParam(value = "files", required = false) MultipartFile files,
	                             @Validated(InsertGroup.class) @RequestBody CommentDTO dto) {
		dto.setUserId(getUserId());
		service.insert(files, dto);
		return message("发布评论成功！");
	}
	
	@RateLimiter(count = 2)
	@GetMapping("/page")
	@Operation(summary = "根据查询条件获取评论分页")
	public ApiVO<PageVO<CommentVO>> page(CommentDTO dto) {
		startPage();
		return success(service.page(dto));
	}
	
}
