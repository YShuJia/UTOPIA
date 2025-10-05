package cn.yshujia.controller;

import cn.yshujia.admin.vo.AdminAlbumVO;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.AlbumDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AdminAlbumServiceImpl;
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
 * @create 2025/3/4
 * @description AdminAlbumController
 */


@Validated
@RestController
@Tag(name = "Album", description = "管理端相册Api")
@RequestMapping("/admin/album")
public class AdminAlbumController extends BaseController {

	@Resource
	private AdminAlbumServiceImpl service;

	@RateLimiter(count = 3)
	@GetMapping("/page")
	@Operation(summary = "admin获取相册分页")
	@PreAuthorize("@sys.hasOnePermission('album:admin', 'album:select')")
	public ApiVO<PageVO<AdminAlbumVO>> page(AlbumDTO dto) {
		startPage();
		if (!isAdmin()) {
			dto.setCreateBy(getUserId());
		}
		return success(service.pageAdmin(dto));
	}

	@Logger
	@RateLimiter
	@PostMapping("/insert")
	@Operation(summary = "admin新增相册")
	@PreAuthorize("@sys.hasOnePermission('album:admin', 'album:insert')")
	public ApiVO<Boolean> insert(@RequestPart("files") MultipartFile[] files,
	                             @Validated(InsertGroup.class) AlbumDTO dto) {
		if (isAdmin()) {
			dto.setReviewed(1);
		}
		service.insert(files, dto);
		return message("相册添加成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update")
	@Operation(summary = "admin更新相册，可传入图片")
	@PreAuthorize("@sys.hasOnePermission('album:admin', 'album:update')")
	public ApiVO<Boolean> update(@RequestPart(value = "files", required = false) MultipartFile[] files,
	                             @Validated(UpdateGroup.class) AlbumDTO dto) {
		dto.setReviewed(null);
		service.update(files, dto);
		return message("相册更新成功！");
	}

	@Logger
	@RateLimiter
	@PutMapping("/update/reviewed")
	@Operation(summary = "admin更新相册审核状态（分离方便鉴权）")
	@PreAuthorize("@sys.hasOnePermission('album:admin')")
	public ApiVO<Boolean> updateReviewed(@Validated(UpdateGroup.class) @RequestBody AlbumDTO dto) {
		service.update(dto);
		return message(dto.getReviewed() == 0 ? "已拒绝相册提交！" : "已同意相册提交！");
	}

	@Logger
	@RateLimiter
	@DeleteMapping("/delete")
	@Operation(summary = "admin删除相册")
	@PreAuthorize("@sys.hasOnePermission('album:admin', 'album:delete')")
	public ApiVO<Boolean> delete(@RequestBody List<Long> ids) {
		service.remove(ids);
		return message("相册删除成功！");
	}

}
