package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminArticleVO;
import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.ArticleDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.enums.MinioFolder;
import cn.yshujia.service.impl.AdminArticleServiceImpl;
import cn.yshujia.utils.MinioUtils;
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
 * @author yshujia
 * @create 2024/4/23
 * @description 文章控制器
 */


@Validated
@RestController
@Tag(name = "Article", description = "管理端文章Api")
@RequestMapping("/admin/article")
public class AdminArticleController extends BaseController {

	@Resource
	private AdminArticleServiceImpl service;

	@Logger
	@RateLimiter(count = 10)
	@PostMapping("/upload/files")
	@Operation(summary = "上传文章文件，返回上传地址")
	@PreAuthorize("@sys.hasOnePermission('article:admin', 'article:select')")
	public ApiVO<List<String>> upload(@RequestParam("files") MultipartFile[] files) {
		List<String> urls = MinioUtils.upload(files, MinioFolder.ARTICLE);
		urls.replaceAll(s -> MinioUtils.STATIC_DOMAIN + s);
		return success(urls);
	}


	@RateLimiter
	@Experience
	@GetMapping("/{id}")
	@Operation(summary = "Admin根据文章ID获取文章")
	@PreAuthorize("@sys.hasOnePermission('article:admin', 'article:select')")
	public ApiVO<AdminArticleVO> article(@PathVariable Long id) {
		return success(service.oneById(id, getUserId()));
	}

	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "admin通过分页数据获取文章集合")
	@PreAuthorize("@sys.hasOnePermission('article:admin', 'article:select')")
	public ApiVO<PageVO<AdminArticleVO>> page(ArticleDTO dto) {
		startPage();
		if (!isAdmin()) {
			dto.setCreateBy(getUserId());
		}
		return success(service.pageAdmin(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "admin插入文章")
	@PreAuthorize("@sys.hasOnePermission('article:admin', 'article:insert')")
	public ApiVO<Boolean> insert(@Validated(InsertGroup.class) @RequestBody ArticleDTO dto) {
		service.insert(dto);
		return message("文章添加成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "admin更新文章，urls 中存放新上传的所有文件的地址")
	@PreAuthorize("@sys.hasOnePermission('article:admin', 'article:update')")
	public ApiVO<Boolean> update(@Validated(UpdateGroup.class) @RequestBody ArticleDTO dto) {
		dto.setReviewed(null);
		service.update(dto);
		return message("文章更新成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update/reviewed")
	@Operation(summary = "admin更新文章审核状态（分离方便鉴权）")
	@PreAuthorize("@sys.hasOnePermission('article:admin')")
	public ApiVO<Boolean> updateReviewed(@Validated(UpdateGroup.class) @RequestBody ArticleDTO dto) {
		service.update(dto);
		return message(dto.getReviewed() == 0 ? "已拒绝文章提交！" : "已同意文章提交！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "admin删除文章")
	@PreAuthorize("@sys.hasOnePermission('article:admin', 'article:delete')")
	public ApiVO<Boolean> delete(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("文章删除成功！");
	}

}