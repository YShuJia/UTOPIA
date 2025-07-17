package cn.yshujia.controller;

import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.service.impl.LikeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */

@Slf4j
@RestController
@Tag(name = "Like", description = "点赞Api")
@RequestMapping("/ui/like")
public class LikeController extends BaseController {
	
	@Resource
	private LikeServiceImpl service;
	
	@GetMapping("/liked")
	@Operation(summary = "获取当前点赞状态")
	public ApiVO<Boolean> select(@RequestParam Long sourceId) {
		try {
			Long userId = getUserId();
			return success(service.select(sourceId, userId));
		} catch (Exception e) {
			return success(false);
		}
	}
	
	@Logger
	@RateLimiter(count = 2)
	@PutMapping("/update")
	@Operation(summary = "点赞")
	@PreAuthorize("@sys.hasOnePermission('like:admin', 'like:insert')")
	public ApiVO<Boolean> updateCount(Long sourceId, Boolean liked) {
		Long userId = getUserId();
		service.updateLikeCount(userId, sourceId, liked);
		return success(liked, liked ? "点赞成功！" : "已取消该点赞！");
	}
	
}

