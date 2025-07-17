package cn.yshujia.controller;

import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.domain.vo.ServerVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yshujia
 * @create 2025/3/19
 * @description ServerController
 */

@RestController
@Tag(name = "Server", description = "服务器监控 Api")
public class ServerController {
	
	@GetMapping("/admin/server/info")
	public ApiVO<ServerVO> getServerInfo() throws Exception {
		ServerVO vo = new ServerVO();
		vo.copyTo();
		return ApiVO.success(vo);
	}
	
}
