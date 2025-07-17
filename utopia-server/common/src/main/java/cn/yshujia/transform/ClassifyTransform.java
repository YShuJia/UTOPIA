package cn.yshujia.transform;

import cn.yshujia.domain.dto.ClassifyDTO;
import cn.yshujia.domain.entity.Classify;
import cn.yshujia.ui.vo.ClassifyVO;

/**
 * @author yshujia
 * @create 2025/2/23
 * @description ClassifyTransform
 */

public class ClassifyTransform {
	
	public static Classify dto2Entity(ClassifyDTO dto) {
		if (dto == null) {
			return null;
		}
		Classify classify = new Classify();
		classify.setId(dto.getId());
		classify.setName(dto.getName());
		classify.setKey(dto.getKey());
		classify.setIntroduction(dto.getIntroduction());
		classify.setEnabled(dto.getEnabled());
		classify.setReviewed(dto.getReviewed());
		classify.setCreateBy(dto.getCreateBy());
		return classify;
	}
	
	public static ClassifyVO entity2VO(Classify classify) {
		if (classify == null) {
			return null;
		}
		ClassifyVO classifyVO = new ClassifyVO();
		classifyVO.setName(classify.getName());
		classifyVO.setIntroduction(classify.getIntroduction());
		classifyVO.setId(classify.getId());
		return classifyVO;
	}
	
}
