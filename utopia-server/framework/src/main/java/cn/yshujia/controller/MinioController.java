package cn.yshujia.controller;

import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.MinioUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yshujia
 * @create 2025/3/21
 * @description MinioController
 */

@RestController
@Tag(name = "Minio", description = "Minio Api")
public class MinioController extends BaseController {
	
	@DeleteMapping("/minio/delete")
	@Operation(summary = "删除 Minio 文件")
	public ApiVO<Boolean> delete(@RequestBody List<String> urls) {
		if (!CollectionUtils.isEmpty(urls)) {
			MinioUtils.delete(urls);
		}
		return success(true);
	}
	
}
