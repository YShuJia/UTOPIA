package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.DiaryDTO;
import cn.yshujia.domain.entity.Diary;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.mapper.DiaryMapper;
import cn.yshujia.transform.DiaryTransform;
import cn.yshujia.ui.vo.DiaryVO;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */

@Service
public class DiaryServiceImpl extends ServiceImpl<DiaryMapper, Diary> {

	@Resource
	public DiaryMapper mapper;

	@Resource
	CommentServiceImpl commentService;

	@Resource
	RedisServiceImpl<DiaryVO> redis;

	public DiaryVO selectById(Long id) {
		DiaryVO vo = redis.get(RedisKeys.DIARY + id);
		if (null == vo) {
			vo = mapper.one(new Diary(id, true, 1));
		}
		vo.setViewCount(vo.getViewCount() + 1);
		redis.set(RedisKeys.DIARY + id, vo, RedisKeys.ONE_DAYS);
		// 更新浏览量
		updateViewCount(id, vo.getViewCount());
		// 更新评论数量
		updateCommentCount(id);
		return vo;
	}

	public PageVO<DiaryVO> page(DiaryDTO dto) {
		List<DiaryVO> list;
		if (dto.getCreateTime() != null) {
			list = mapper.list(DiaryTransform.dto2Entity(dto));
		} else {
			list = redis.range(RedisKeys.DIARY_LIST, dto.getPageNum(), dto.getPageSize());
			if (CollectionUtils.isEmpty(list)) {
				list = mapper.list(DiaryTransform.dto2Entity(dto));
				redis.leftPushAll(RedisKeys.DIARY_LIST, list, RedisKeys.THREE_DAYS);
			}
		}
		return PageUtils.page(dto, list);
	}

	@Async("Task")
	protected void updateViewCount(Long id, Integer viewCount) {
		mapper.update(new LambdaUpdateWrapper<Diary>()
				.eq(Diary::getId, id)
				.set(Diary::getViewCount, viewCount));
	}

	@Async("Task")
	protected void updateCommentCount(Long id) {
		Long count = Optional.ofNullable(commentService.countBySourceId(id)).orElse(0L);
		mapper.update(new LambdaUpdateWrapper<Diary>()
				.eq(Diary::getId, id)
				.set(Diary::getCommentCount, count));
	}

}
