package cn.yshujia.ui.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yshujia
 * @create 2024/11/30
 * @description ArchiveVO
 */

@Data
public class ArchiveVO implements Serializable {
	
	private Long classifyId;
	
	private String classifyName;
	
	// 标签列表
	private List<ArticleLabelVO> labelList;
	
}
