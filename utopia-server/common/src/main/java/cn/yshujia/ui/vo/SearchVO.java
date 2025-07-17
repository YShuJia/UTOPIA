package cn.yshujia.ui.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yshujia
 * @create 2025/3/25
 * @description SearchVO
 */

@Data
public class SearchVO {
	
	private List<ArticleVO> article;
	
	private List<DiaryVO> diary;
	
	private List<AlbumVO> album;
	
}
