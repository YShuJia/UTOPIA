package cn.yshujia.mapper;


import cn.yshujia.domain.entity.Comment;
import cn.yshujia.domain.vo.CommentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
	
	CommentVO oneById(Long id);
	
	List<CommentVO> list(Comment comment);
	
	List<CommentVO> listChildren(Long floorId);
	
}
