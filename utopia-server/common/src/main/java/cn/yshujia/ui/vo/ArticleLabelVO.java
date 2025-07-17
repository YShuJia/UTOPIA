package cn.yshujia.ui.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yshujia
 * @create 2024/11/30
 * @description ArticleLabelVO
 */

@Data
public class ArticleLabelVO implements Serializable {
	
	private Long labelId;
	
	private String labelName;
	
	private String labelCover;
	
	private String articleCount;
	
}
