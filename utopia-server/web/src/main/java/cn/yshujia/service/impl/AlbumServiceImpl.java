package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.AlbumDTO;
import cn.yshujia.domain.entity.Album;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.mapper.AlbumMapper;
import cn.yshujia.ui.vo.AlbumVO;
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
 * @author yshujia
 * @create 2025/3/4
 * @description AlbumServiceImpl
 */


@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> {
	
	@Resource
	RedisServiceImpl<AlbumVO> redis;
	
	@Resource
	private AlbumMapper mapper;
	
	@Resource
	private CommentServiceImpl commentService;
	
	public AlbumVO selectById(Long id) {
		AlbumVO vo = redis.get(RedisKeys.ALBUM + id);
		if (null == vo) {
			vo = mapper.one(new Album(id, null, true, 1));
		}
		vo.setViewCount(vo.getViewCount() + 1);
		redis.set(RedisKeys.ALBUM + id, vo, RedisKeys.ONE_DAYS);
		// 更新浏览量
		updateViewCount(id, vo.getViewCount());
		// 更新评论数量
		updateCommentCount(id);
		return vo;
	}
	
	public PageVO<AlbumVO> page(AlbumDTO dto) {
		String key = RedisKeys.ALBUM_LABEL + dto.getLabelId();
		// 判断缓存中是否存在
		List<AlbumVO> list = redis.range(key, dto.getPageNum(), dto.getPageSize());
		if (!CollectionUtils.isEmpty(list) || dto.getPageNum() != 1) {
			return PageUtils.page(list);
		}
		// 获取数据库数据
		list = mapper.list(new Album(null, dto.getLabelId(), true, 1));
		redis.rightPushAll(key, list, RedisKeys.ONE_DAYS);
		return PageUtils.page(list);
	}
	
	@Async("Task")
	protected void updateViewCount(Long id, Integer viewCount) {
		mapper.update(new LambdaUpdateWrapper<Album>()
				              .eq(Album::getId, id)
				              .set(Album::getViewCount, viewCount));
	}
	
	@Async("Task")
	protected void updateCommentCount(Long id) {
		Long count = Optional.ofNullable(commentService.countBySourceId(id)).orElse(0L);
		mapper.update(new LambdaUpdateWrapper<Album>()
				              .eq(Album::getId, id)
				              .set(Album::getCommentCount, count));
	}
	
}
