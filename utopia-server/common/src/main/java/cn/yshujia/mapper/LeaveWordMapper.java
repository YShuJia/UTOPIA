package cn.yshujia.mapper;


import cn.yshujia.domain.entity.LeaveWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 树洞（留言） Mapper 接口
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */
@Mapper
public interface LeaveWordMapper extends BaseMapper<LeaveWord> {
	
	List<LeaveWord> list();
	
	List<LeaveWord> listByAdmin(LeaveWord leaveWord);
	
}
