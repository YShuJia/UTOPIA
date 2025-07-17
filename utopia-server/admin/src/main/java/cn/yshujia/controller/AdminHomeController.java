package cn.yshujia.controller;

import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.service.impl.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yshujia
 * @create 2025/3/26
 * @description AdminHomeController
 */
@RestController
@RequestMapping("/admin/home")
@Tag(name = "System", description = "系统模块Api")
public class AdminHomeController extends BaseController {
	
	@Resource
	private AdminUserServiceImpl userService;
	
	@Resource
	private AdminRoleServiceImpl roleService;
	
	@Resource
	private AdminClassifyServiceImpl classifyService;
	
	@Resource
	private AdminLabelServiceImpl labelService;
	
	@Resource
	private AdminFileServiceImpl fileService;
	
	@Resource
	private AdminRouterServiceImpl routerService;
	
	@Resource
	private AdminAlbumServiceImpl albumService;
	
	@Resource
	private AdminArticleServiceImpl articleService;
	
	@Resource
	private AdminCommentServiceImpl commentService;
	
	@Resource
	private AdminLeaveWordServiceImpl leaveWordService;
	
	@Resource
	private AdminWebsiteServiceImpl websiteService;
	
	@Resource
	private AdminLikeServiceImpl likeService;
	
	@GetMapping("/count")
	@Operation(summary = "获取系统模块数据统计")
	public ApiVO<Map<String, Long>> count() {
		Map<String, Long> map = new HashMap<>();
		map.put("user", userService.count());
		map.put("role", roleService.count());
		map.put("classify", classifyService.count());
		map.put("label", labelService.count());
		map.put("file", fileService.count());
		map.put("router", routerService.count());
		map.put("album", albumService.count());
		map.put("article", articleService.count());
		map.put("comment", commentService.count());
		map.put("leaveWord", leaveWordService.count());
		map.put("website", websiteService.count());
		map.put("like", likeService.count());
		return success(map);
	}
	
}
