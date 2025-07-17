package cn.yshujia.controller;

import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.service.impl.ClassifyServiceImpl;
import cn.yshujia.ui.vo.ClassifyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */

@RestController
@Tag(name = "Classify", description = "分类Api")
@RequestMapping("/ui/classify")
public class ClassifyController extends BaseController {
	
	@Resource
	ClassifyServiceImpl service;
	
	@RateLimiter(count = 2)
	@GetMapping("/{id}")
	@Operation(summary = "根据分类ID获取分类")
	public ApiVO<ClassifyVO> list(@PathVariable Long id) {
		return success(service.selectById(id));
	}
	
}

