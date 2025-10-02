package cn.yshujia.mapper;

import cn.yshujia.domain.entity.WebConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author YShuJia
 * @since 2024/4/23
 */
@Mapper
public interface WebConfigMapper extends BaseMapper<WebConfig> {

	List<WebConfig> listByAdmin(WebConfig sysConfig);
}
