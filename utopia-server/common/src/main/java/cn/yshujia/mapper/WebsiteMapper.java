package cn.yshujia.mapper;

import cn.yshujia.admin.vo.AdminWebsiteVO;
import cn.yshujia.domain.entity.Website;
import cn.yshujia.ui.vo.WebsiteVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 友情链接信息 Mapper 接口
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */
@Mapper
public interface WebsiteMapper extends BaseMapper<Website> {
	
	List<WebsiteVO> list(Website website);
	
	List<AdminWebsiteVO> listByAdmin(Website website);
	
}
