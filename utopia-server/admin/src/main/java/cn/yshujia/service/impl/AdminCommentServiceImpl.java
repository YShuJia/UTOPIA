package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.CommentDTO;
import cn.yshujia.domain.entity.Comment;
import cn.yshujia.domain.vo.CommentVO;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.enums.MinioFolder;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
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

import java.util.ArrayList;
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
public class AdminCommentServiceImpl extends ServiceImpl<CommentMapper, Comment> {
	
	@Resource
	public CommentMapper mapper;
	
	@Resource
	RedisServiceImpl<CommentVO> redis;
	
	@Resource
	private AdminUserServiceImpl userService;
	
	@Resource
	private SocketUtils socketUtils;
	
	public PageVO<CommentVO> pageAdmin(CommentDTO dto) {
		Comment comment = CommentTransform.dto2Entity(dto);
		List<CommentVO> list = mapper.list(comment);
		Map<Long, UserVO> userMap = new HashMap<>();
		for (CommentVO commentVO : list) {
			enrichCommentWithUserInfo(commentVO, userMap);
			List<CommentVO> children = mapper.listChildren(commentVO.getFloorId());
			for (CommentVO child : children) {
				enrichCommentWithUserInfo(child, userMap);
			}
			commentVO.setChildren(children);
		}
		return PageUtils.page(list);
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
	
	public CommentVO selectById(Long id) {
		Comment comment = mapper.selectById(id);
		if (comment != null) {
			CommentVO commentVO = CommentTransform.entity2VO(comment);
			UserVO userVO = userService.oneById(comment.getUserId());
			commentVO.setUsername(userVO.getUsername());
			commentVO.setAvatar(userVO.getAvatar());
			commentVO.setRoleName(userVO.getRoleName());
			if (commentVO.getParentUserId() != null && !commentVO.getParentUserId().equals(commentVO.getUserId())) {
				userVO = userService.oneById(comment.getParentUserId());
			}
			commentVO.setParentUsername(userVO.getUsername());
			commentVO.setParentAvatar(userVO.getAvatar());
			commentVO.setParentRoleName(userVO.getRoleName());
			return commentVO;
		}
		return null;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void insert(MultipartFile file, CommentDTO dto) {
		Comment comment = CommentTransform.dto2Entity(dto);
		List<String> content = comment.getContent();
		// 评论ID
		comment.setId(IDUtils.getTimeId());
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
		try {
			if (insert(comment)) {
				CommentVO vo = selectById(comment.getId());
				socketUtils.sendComment(vo);
			}
		} catch (Exception e) {
			MinioUtils.delete(url);
			throw new CustomException(e.getMessage());
		}
	}
	
	
	@Transactional(rollbackFor = {Exception.class})
	public void remove(List<Long> ids) {
		List<String> urls = new ArrayList<>();
		try {
			List<Comment> comments = mapper.selectList(
					new LambdaQueryWrapper<Comment>()
							.in(Comment::getId, ids)
							.or()
							.in(Comment::getFloorId, ids));
			// 获取评论图片地址
			for (Comment comment : comments) {
				List<String> con = comment.getContent();
				if (!CollectionUtils.isEmpty(con) && con.size() == 2) {
					urls.add(con.get(1));
				}
			}
			if (!removeBatchByIds(comments)) {
				throw new ServiceException("评论不存在，无法删除！");
			}
			// 删除成功，删除缓存
			redis.delKeysByPrefix(RedisKeys.COMMENT_SOURCE);
			// 删除图片
			MinioUtils.delete(urls);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	
	private Boolean insert(Comment comment) {
		// 评论来源ID 为空时为 树洞评论 sourceId = id
		if (comment.getSourceId() == null) {
			comment.setSourceId(comment.getId());
			redis.delKey(RedisKeys.COMMENT_TREE_HOLE);
		} else {
			redis.delKey(comment.getSourceId());
		}
		// 楼层评论ID 为空时为 自己为楼层评论 floorId = id
		if (comment.getFloorId() == null) {
			comment.setFloorId(comment.getId());
		}
		// 父评论用户ID 为空时为 无回复或回复楼层 parentUserId = userId
		if (comment.getParentUserId() == null) {
			comment.setParentUserId(comment.getUserId());
		}
		try {
			int n = mapper.insert(comment);
			if (n <= 0) {
				return false;
			}
			redis.delKeysByPrefix(RedisKeys.COMMENT_SOURCE);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return true;
	}
	
}
