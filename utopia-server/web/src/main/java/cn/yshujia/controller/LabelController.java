package cn.yshujia.controller;

import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.ClassifyDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.service.impl.LabelServiceImpl;
import cn.yshujia.ui.vo.LabelVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */

@RestController
@Tag(name = "Label", description = "标签Api")
@RequestMapping("/ui/label")
public class LabelController extends BaseController {
	
	@Resource
	private LabelServiceImpl service;
	
	@RateLimiter(count = 2)
	@GetMapping("/classify")
	@Operation(summary = "获取分类下的所有标签")
	public ApiVO<List<LabelVO>> listByClassify(ClassifyDTO dto) {
		clearPage();
		return success(service.listByClassify(dto));
	}
	
}


