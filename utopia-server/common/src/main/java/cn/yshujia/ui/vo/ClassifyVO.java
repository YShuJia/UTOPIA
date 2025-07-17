package cn.yshujia.ui.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author：yshujia
 * @create: 2025/5/2 19:55
 * @description: AdminClassifyVO
 */


@Data
public class ClassifyVO implements Serializable {
	
	private Long id;
	
	private String name;
	
	private String introduction;
	
}
