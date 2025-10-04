package cn.yshujia.controller;

import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.DiaryDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.DiaryServiceImpl;
import cn.yshujia.ui.vo.DiaryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@Tag(name = "Diary", description = "日记Api")
@RequestMapping("/ui/diary")
public class DiaryController extends BaseController {

	@Resource
	private DiaryServiceImpl service;

	@RateLimiter
	@Experience
	@GetMapping("/{id}")
	@Operation(summary = "通过ID获取日记")
	public ApiVO<DiaryVO> info(@PathVariable Long id) {
		return success(service.selectById(id));
	}

	@RateLimiter(count = 2)
	@GetMapping("/page")
	@Operation(summary = "分页获取日记信息")
	public ApiVO<PageVO<DiaryVO>> page(DiaryDTO dto) {
		startPage();
		return success(service.page(dto));
	}

}
