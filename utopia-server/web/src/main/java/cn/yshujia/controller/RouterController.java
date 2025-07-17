package cn.yshujia.controller;

import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.service.impl.RouterServiceImpl;
import cn.yshujia.ui.vo.RouterVO;
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
@Tag(name = "Common", description = "路由Api")
@RequestMapping("/ui/router")
public class RouterController extends BaseController {
	
	@Resource
	RouterServiceImpl service;
	
	@RateLimiter(count = 2)
	@GetMapping("/list")
	@Operation(summary = "根据用户携带Token获取路由")
	public ApiVO<List<RouterVO>> defaultList() {
		try {
			Long roleId = getRoleId();
			return success(service.getList(roleId));
		} catch (Exception e) {
			return success(service.getList(null));
		}
	}
	
}


