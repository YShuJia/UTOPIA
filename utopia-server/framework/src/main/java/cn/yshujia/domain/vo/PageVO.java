package cn.yshujia.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 分页响应对象
 */

@Data
@AllArgsConstructor
public class PageVO<T> implements Serializable {
	
	private Integer pageNum;
	
	private Boolean isEmpty;
	
	private Integer pageSize;
	
	private Long total;
	
	private List<T> list;
	
	public PageVO() {
		this.pageSize = 10;
		this.pageNum = 1;
		this.isEmpty = true;
		this.total = 0L;
		this.list = new ArrayList<>();
	}
	
}
