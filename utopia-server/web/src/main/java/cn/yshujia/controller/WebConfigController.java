package cn.yshujia.controller;

import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.entity.WebConfig;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.service.impl.WebConfigServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author YShuJia
 * @since 2024/4/23
 */
@RestController
@RequestMapping("/ui/web-config")
public class WebConfigController extends BaseController {

	@Resource
	private WebConfigServiceImpl service;

	@RateLimiter(count = 3)
	@GetMapping("/default")
	@Operation(summary = "admin获取配置分页")
	public ApiVO<WebConfig> defaultConfig() {
		return success(service.defaultConfig());
	}
}
