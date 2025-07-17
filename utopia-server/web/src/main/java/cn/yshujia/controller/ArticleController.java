package cn.yshujia.controller;

import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.ArticleDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.ArchiveServiceImpl;
import cn.yshujia.service.impl.ArticleServiceImpl;
import cn.yshujia.ui.vo.ArchiveVO;
import cn.yshujia.ui.vo.ArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 文章控制器
 */

@RestController
@Tag(name = "Article", description = "文章Api")
@RequestMapping("/ui/article")
public class ArticleController extends BaseController {
	
	@Resource
	private ArticleServiceImpl service;
	
	@Resource
	private ArchiveServiceImpl archiveServiceImpl;
	
	@Experience
	@RateLimiter
	@GetMapping("/{id}")
	@Operation(summary = "根据ID获取文章")
	public ApiVO<ArticleVO> article(@PathVariable Long id) {
		return success(service.oneById(id));
	}
	
	@RateLimiter(count = 2)
	@GetMapping("/page")
	@Operation(summary = "通过标签ID获取文章分页集合")
	public ApiVO<PageVO<ArticleVO>> list(ArticleDTO dto) {
		startPage();
		return success(service.page(dto));
	}
	
	@RateLimiter(count = 2)
	@GetMapping("/deploy/list")
	@Operation(summary = "获取部署文章列表")
	public ApiVO<List<ArticleVO>> deployList() {
		return success(service.selectDeployList());
	}
	
	@RateLimiter(count = 2)
	@GetMapping("/archive/list")
	@Operation(summary = "获取所有文章归档信息")
	public ApiVO<List<ArchiveVO>> archiveList() {
		return success(archiveServiceImpl.selectArchiveList());
	}
	
}