package cn.yshujia.mapper;

import cn.yshujia.admin.vo.AdminLabelVO;
import cn.yshujia.admin.vo.AdminTreeVO;
import cn.yshujia.domain.entity.Classify;
import cn.yshujia.domain.entity.Label;
import cn.yshujia.ui.vo.LabelVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description dao持久层
 */

@Mapper
public interface LabelMapper extends BaseMapper<Label> {
	
	List<LabelVO> list(Label label);
	
	List<LabelVO> listByClassify(Classify classify);
	
	List<AdminLabelVO> listByAdmin(Label label);
	
	List<AdminTreeVO> tree(Label label);
	
}
