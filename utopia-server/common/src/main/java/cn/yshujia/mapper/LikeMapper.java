package cn.yshujia.mapper;

import cn.yshujia.domain.entity.Like;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author yshujia
 * @create 2024/12/20
 * @description LikeMapper
 */

@Mapper
public interface LikeMapper extends BaseMapper<Like> {
	
	@MapKey("source_id")
	Map<Long, Integer> selectLikeCount();
	
}
