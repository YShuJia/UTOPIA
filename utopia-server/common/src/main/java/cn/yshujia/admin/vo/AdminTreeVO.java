package cn.yshujia.admin.vo;


import lombok.Data;

import java.util.List;

/**
 * @author: yshujia
 * @create: 2025/6/4 21:53
 * @description: AdminTreeVO 构建类别、标签的树形结构（value 为 id, label 为 name）
 */

@Data
public class AdminTreeVO {
	
	private Long value;
	
	private String label;
	
	private List<AdminTreeVO> children;
	
}
