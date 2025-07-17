package cn.yshujia.controller;

import cn.yshujia.annotation.Experience;
import cn.yshujia.annotation.Logger;
import cn.yshujia.annotation.RateLimiter;
import cn.yshujia.domain.dto.LeaveWordDTO;
import cn.yshujia.domain.entity.LeaveWord;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.service.impl.LeaveWordServiceImpl;
import cn.yshujia.utils.RequestUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 控制器
 */

@RestController
@Tag(name = "LeaveWord", description = "树洞Api")
@RequestMapping("/ui/leave_word")
public class LeaveWordController extends BaseController {
	
	@Resource
	private LeaveWordServiceImpl service;
	
	@RateLimiter(count = 5)
	@GetMapping("/page")
	@Operation(summary = "获取留言分页")
	public ApiVO<PageVO<LeaveWord>> page(LeaveWordDTO dto) {
		return success(service.page(dto));
	}
	
	
	@Logger
	@RateLimiter
	@Experience(value = 10)
	@PostMapping("/insert")
	@Operation(summary = "新增留言")
	public ApiVO<Boolean> insert(@RequestBody LeaveWordDTO dto, HttpServletRequest req) {
		dto.setIp(RequestUtils.getIp(req));
		service.insert(dto);
		return message("发布留言成功！");
	}
	
}

