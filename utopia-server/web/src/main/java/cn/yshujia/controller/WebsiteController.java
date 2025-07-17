package cn.yshujia.controller;

import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.WebsiteDTO;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.WebsiteServiceImpl;
import cn.yshujia.ui.vo.WebsiteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */

@RestController
@Tag(name = "Website", description = "网站Api")
@RequestMapping("/ui/website")
public class WebsiteController extends BaseController {
	
	@Resource
	private WebsiteServiceImpl service;
	
	@RateLimiter(count = 2)
	@GetMapping("/page")
	@Operation(summary = "获取网站分页")
	public ApiVO<PageVO<WebsiteVO>> page(WebsiteDTO dto) {
		startPage();
		return success(service.page(dto));
	}
	
	@Logger
	@RateLimiter
	@Experience(value = 20)
	@PostMapping("/insert")
	@Operation(summary = "添加网站")
	@PreAuthorize("@sys.hasOnePermission('website:admin', 'website:insert')")
	public ApiVO<Boolean> insert(@RequestBody WebsiteDTO dto) {
		service.insert(dto);
		return message("网站添加成功！");
	}
	
}
