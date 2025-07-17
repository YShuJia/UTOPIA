package cn.yshujia.admin.vo;

import cn.yshujia.domain.entity.Classify;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yshujia
 * @create 2025/2/26
 * @description ClassifyLabelVO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ClassifyLabelVO extends Classify {
	
	private List<AdminLabelVO> labels;
	
}
