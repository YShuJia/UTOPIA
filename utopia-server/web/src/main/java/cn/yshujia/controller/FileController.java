package cn.yshujia.controller;

import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.FileDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.FileServiceImpl;
import cn.yshujia.ui.vo.FileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */
@RestController
@Tag(name = "Common", description = "资源Api")
@RequestMapping("/ui/file")
public class FileController extends BaseController {
	
	@Resource
	private FileServiceImpl service;
	
	@RateLimiter(count = 2)
	@GetMapping("/page/wall")
	@Operation(summary = "获取随机背景集合")
	public ApiVO<PageVO<FileVO>> pageWall(FileDTO dto) {
		startPage();
		return success(service.pageWall(dto));
	}
	
	@RateLimiter(count = 2)
	@GetMapping("/page/avatar")
	@Operation(summary = "获取随机背景集合")
	public ApiVO<PageVO<FileVO>> pageAvatar(FileDTO dto) {
		startPage();
		return success(service.pageAvatar(dto));
	}
	
}
