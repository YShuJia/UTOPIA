package cn.yshujia.mapper;

import cn.yshujia.admin.vo.AdminRouterVO;
import cn.yshujia.admin.vo.AdminSelectRouterVO;
import cn.yshujia.domain.entity.Router;
import cn.yshujia.ui.vo.RouterVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description dao持久层
 */

@Mapper
public interface RouterMapper extends BaseMapper<Router> {
	
	List<RouterVO> list(Router router);
	
	List<AdminRouterVO> listByAdmin(Router router);
	
	List<AdminSelectRouterVO> listSelectByAdmin();
	
}
