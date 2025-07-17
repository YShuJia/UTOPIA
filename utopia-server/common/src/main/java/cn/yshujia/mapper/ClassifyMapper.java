package cn.yshujia.mapper;

import cn.yshujia.admin.vo.AdminClassifyVO;
import cn.yshujia.admin.vo.AdminTreeVO;
import cn.yshujia.domain.entity.Classify;
import cn.yshujia.ui.vo.ClassifyVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description dao持久层
 */

@Mapper
public interface ClassifyMapper extends BaseMapper<Classify> {
	
	ClassifyVO oneById(@Param("id") Long id);
	
	List<ClassifyVO> list(Classify classify);
	
	List<AdminClassifyVO> listByAdmin(Classify classify);
	
	List<AdminTreeVO> tree(@Param("key") String key);
	
}
