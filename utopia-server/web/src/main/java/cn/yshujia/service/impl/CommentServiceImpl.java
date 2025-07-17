package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.CommentDTO;
import cn.yshujia.domain.entity.Comment;
import cn.yshujia.domain.vo.CommentVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.enums.MinioFolder;
import cn.yshujia.mapper.CommentMapper;
import cn.yshujia.transform.CommentTransform;
import cn.yshujia.ui.vo.UserVO;
import cn.yshujia.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> {
	
	@Resource
	RedisServiceImpl<CommentVO> redis;
	
	@Resource
	private CommentMapper mapper;
	
	@Resource
	private CommentServiceImpl commentService;
	
	@Resource
	private UserServiceImpl userService;
	
	@Resource
	private SocketUtils socketUtils;
	
	public Long countBySourceId(Long sourceId) {
		return mapper.selectCount(new LambdaQueryWrapper<Comment>()
				                          .eq(Comment::getSourceId, sourceId));
	}
	
	public CommentVO selectById(Long id) {
		if (id != null) {
			Comment comment = mapper.selectById(id);
			if (comment != null) {
				CommentVO commentVO = CommentTransform.entity2VO(comment);
				enrichCommentWithUserInfo(commentVO, new HashMap<>());
				return commentVO;
			}
		}
		return null;
	}
	
	public PageVO<CommentVO> page(CommentDTO dto) {
		// 默认获取留言中的评论
		String KEY = RedisKeys.COMMENT_TREE_HOLE;
		// sourceId == null 获取 sourceId 评论 否则获取留言中的评论
		if (dto.getSourceId() != null) {
			KEY = RedisKeys.COMMENT_SOURCE + dto.getSourceId();
		}
		List<CommentVO> list = redis.range(KEY, dto.getPageNum(), dto.getPageSize());
		if (!CollectionUtils.isEmpty(list) || dto.getPageNum() != 1) {
			return PageUtils.page(dto, list);
		}
		// 从数据库获取数据
		list = mapper.list(new Comment(dto.getSourceId()));
		// 获取所有用户ID集合
		Map<Long, UserVO> userMap = new HashMap<>();
		for (CommentVO commentVO : list) {
			enrichCommentWithUserInfo(commentVO, userMap);
			List<CommentVO> children = mapper.listChildren(commentVO.getFloorId());
			for (CommentVO child : children) {
				enrichCommentWithUserInfo(child, userMap);
			}
			commentVO.setChildren(children);
		}
		redis.rightPushAll(KEY, list, RedisKeys.FOREVER);
		return PageUtils.page(dto, list);
	}
	
	private void enrichCommentWithUserInfo(CommentVO commentVO, Map<Long, UserVO> userMap) {
		UserVO userVO = userMap.get(commentVO.getUserId());
		UserVO parentUserVO;
		if (userVO == null) {
			userVO = userService.oneById(commentVO.getUserId());
		}
		userMap.put(commentVO.getUserId(), userVO);
		// 获取父评论用户信息
		if (!commentVO.getParentUserId().equals(commentVO.getUserId())) {
			parentUserVO = userMap.get(commentVO.getParentUserId());
			userMap.put(commentVO.getParentUserId(), parentUserVO);
		} else {
			parentUserVO = userVO;
		}
		if (userVO != null) {
			commentVO.setAvatar(userVO.getAvatar());
			commentVO.setUsername(userVO.getUsername());
			commentVO.setRoleName(userVO.getRoleName());
		}
		if (parentUserVO != null) {
			commentVO.setParentUsername(parentUserVO.getUsername());
			commentVO.setParentAvatar(parentUserVO.getAvatar());
			commentVO.setParentRoleName(parentUserVO.getRoleName());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public Long insert(Comment comment) {
		// 评论ID
		comment.setId(IDUtils.getTimeId());
		// 评论来源ID 为空时为 树洞评论 sourceId = id
		if (comment.getSourceId() == null) {
			comment.setSourceId(comment.getId());
			redis.delKey(RedisKeys.COMMENT_TREE_HOLE);
		} else {
			redis.delKey(RedisKeys.COMMENT_SOURCE + comment.getSourceId());
		}
		// 楼层评论ID 为空时为 自己为楼层评论 floorId = id
		if (comment.getFloorId() == null) {
			comment.setFloorId(comment.getId());
		}
		// 父评论用户ID为空时无回复或回复楼层 parentUserId = userId
		if (comment.getParentUserId() == null) {
			comment.setParentUserId(comment.getUserId());
		}
		if (mapper.insert(comment) > 0) {
			redis.delKeysByPrefix(RedisKeys.COMMENT_SOURCE);
			return comment.getId();
		}
		return null;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void insert(MultipartFile file, CommentDTO dto) {
		Comment comment = CommentTransform.dto2Entity(dto);
		List<String> content = comment.getContent();
		String url = null;
		if (null != file) {
			url = MinioUtils.upload(file, MinioFolder.COMMENT);
			if (content.size() == 2) {
				content.set(1, url);
			} else {
				content.add(url);
			}
			comment.setContent(content);
		}
		Long id = insert(comment);
		if (null == id) {
			MinioUtils.delete(url);
		}
		CommentVO vo = commentService.selectById(id);
		if (null != vo) {
			socketUtils.sendComment(vo);
		}
	}
	
}
