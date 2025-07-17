package cn.yshujia.transform;

import cn.yshujia.domain.dto.LabelDTO;
import cn.yshujia.domain.entity.Label;

/**
 * @author yshujia
 * @create 2025/2/23
 * @description LabelTransform
 */

public class LabelTransform {
	
	public static Label dto2Entity(LabelDTO dto) {
		if (dto == null) {
			return null;
		}
		Label label = new Label();
		label.setId(dto.getId());
		label.setClassifyId(dto.getClassifyId());
		label.setCover(dto.getCover());
		label.setName(dto.getName());
		label.setIntroduction(dto.getIntroduction());
		label.setEnabled(dto.getEnabled());
		label.setReviewed(dto.getReviewed());
		label.setCreateBy(dto.getCreateBy());
		return label;
	}
	
}
