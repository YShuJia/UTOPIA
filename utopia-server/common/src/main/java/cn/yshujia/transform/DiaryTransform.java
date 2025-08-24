package cn.yshujia.transform;


import cn.yshujia.domain.dto.DiaryDTO;
import cn.yshujia.domain.entity.Diary;

/**
 * @author: yshujia
 * @create: 2025/6/14 15:33
 * @description: DiaryTransform
 */
public class DiaryTransform {
	
	public static Diary dto2Entity(DiaryDTO diaryDTO) {
		if (diaryDTO == null) {
			return null;
		}
		Diary diary = new Diary();
		diary.setId(diaryDTO.getId());
		diary.setContent(diaryDTO.getContent());
		diary.setTitle(diaryDTO.getTitle());
		diary.setUrls(diaryDTO.getUrls());
		diary.setYear(diaryDTO.getYear());
		diary.setMonth(diaryDTO.getMonth());
		diary.setRecommend(diaryDTO.getRecommend());
		diary.setLikeable(diaryDTO.getLikeable());
		diary.setCommentable(diaryDTO.getCommentable());
		diary.setEnabled(diaryDTO.getEnabled());
		diary.setReviewed(diaryDTO.getReviewed());
		diary.setCreateBy(diaryDTO.getCreateBy());
		diary.setCreateTime(diaryDTO.getCreateTime());
		return diary;
	}
	
}
