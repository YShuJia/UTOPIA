package cn.yshujia.transform;

import cn.yshujia.domain.dto.CommentDTO;
import cn.yshujia.domain.entity.Comment;
import cn.yshujia.domain.vo.CommentVO;

/**
 * @author yshujia
 * @create 2024/12/16
 * @description CommentTransform
 */

public class CommentTransform {
	
	public static CommentVO entity2VO(Comment comment) {
		if (comment == null) {
			return null;
		}
		CommentVO commentVO = new CommentVO();
		commentVO.setId(comment.getId());
		commentVO.setUserId(comment.getUserId());
		commentVO.setSourceId(comment.getSourceId());
		commentVO.setFloorId(comment.getFloorId());
		commentVO.setParentUserId(comment.getParentUserId());
		commentVO.setLikeCount(comment.getLikeCount());
		commentVO.setContent(comment.getContent());
		commentVO.setReplayContent(comment.getReplayContent());
		commentVO.setCreateTime(comment.getCreateTime());
		return commentVO;
	}
	
	public static Comment dto2Entity(CommentDTO dto) {
		if (dto == null) {
			return null;
		}
		Comment comment = new Comment();
		comment.setId(dto.getId());
		comment.setUserId(dto.getUserId());
		comment.setSourceId(dto.getSourceId());
		comment.setFloorId(dto.getFloorId());
		comment.setParentUserId(dto.getParentUserId());
		comment.setLikeCount(dto.getLikeCount());
		comment.setContent(dto.getContent());
		comment.setReplayContent(dto.getReplayContent());
		return comment;
	}
	
}
