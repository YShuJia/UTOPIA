package cn.yshujia.domain.dto;

import cn.yshujia.validation.SelectGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 分页入参
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO {
	
	@NotNull(groups = SelectGroup.class, message = "[分页索引] 不能为空！")
	private Integer pageNum;
	
	@NotNull(groups = SelectGroup.class, message = "[分页数量] 不能为空！")
	private Integer pageSize;
	
	//参数
	private List<String> orderByColumns;
	
}
