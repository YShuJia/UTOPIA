package cn.yshujia.controller;

import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.AlbumDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.AlbumServiceImpl;
import cn.yshujia.ui.vo.AlbumVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yshujia
 * @create 2025/3/4
 * @description AlbumController
 */

@RestController
@Tag(name = "Album", description = "相册Api")
@RequestMapping("/ui/album")
public class AlbumController extends BaseController {

	@Resource
	private AlbumServiceImpl service;

	@Experience
	@RateLimiter
	@GetMapping("/{id}")
	@Operation(summary = "通过ID获取相册")
	public ApiVO<AlbumVO> info(@PathVariable Long id) {
		return success(service.selectById(id));
	}

	@RateLimiter(count = 2)
	@GetMapping("/page")
	@Operation(summary = "获取相册分页")
	public ApiVO<PageVO<AlbumVO>> page(AlbumDTO dto) {
		startPage();
		dto.setReviewed(1);
		dto.setEnabled(true);
		return success(service.page(dto));
	}

}
