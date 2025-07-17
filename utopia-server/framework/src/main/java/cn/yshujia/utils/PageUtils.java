package cn.yshujia.utils;

import cn.hutool.core.convert.Convert;
import cn.yshujia.domain.dto.PageDTO;
import cn.yshujia.domain.vo.PageVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yshujia
 * @create 2025/1/10
 * @description PageUtils
 */

public class PageUtils extends PageHelper {
	
	/**
	 * 开启分页
	 */
	public static void startPage() {
		Integer current = Convert.toInt(RequestUtils.getParameter("pageNum"), 1);
		Integer size = Convert.toInt(RequestUtils.getParameter("pageSize"), 10);
		String orderBy = RequestUtils.getParameter("orderByColumn");
		Boolean reasonable = true;
		PageHelper.startPage(current, size, orderBy).setReasonable(reasonable);
	}
	
	public static <T> PageVO<T> page(List<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			list = new ArrayList<>();
		}
		PageInfo<T> pageInfo = new PageInfo<>(list);
		PageVO<T> page = new PageVO<>();
		page.setPageSize(pageInfo.getPageSize());
		page.setPageNum(pageInfo.getPageNum());
		page.setIsEmpty(!pageInfo.isHasNextPage());
		page.setTotal(pageInfo.getTotal());
		page.setList(list);
		clearPage();
		return page;
	}
	
	/**
	 * 清理分页的线程变量
	 */
	public static void clearPage() {
		PageHelper.clearPage();
	}
	
	
	public static <T> PageVO<T> page(PageDTO dto, List<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			list = new ArrayList<>();
		}
		PageVO<T> page = new PageVO<>();
		page.setPageNum(dto.getPageNum());
		page.setPageSize(dto.getPageSize());
		page.setIsEmpty(list.size() < dto.getPageSize());
		page.setList(list);
		clearPage();
		return page;
	}
	
	public static <T> PageVO<T> page(PageDTO dto, List<T> list, Long total) {
		PageVO<T> page = page(dto, list);
		page.setTotal(total);
		return page;
	}
	
}
