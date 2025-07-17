package cn.yshujia.ui.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yshujia
 * @create 2024/11/29
 * @description AdminLabelVO
 */

@Data
public class LabelVO implements Serializable {
	
	private Long id;
	
	private String name;
	
	private String introduction;
	
}
