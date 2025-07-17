package cn.yshujia.service.impl;

import cn.yshujia.constant.ClassifyKeys;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.ArticleDTO;
import cn.yshujia.domain.entity.Article;
import cn.yshujia.domain.entity.Classify;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.mapper.ArticleMapper;
import cn.yshujia.mapper.LabelMapper;
import cn.yshujia.ui.vo.ArticleVO;
import cn.yshujia.ui.vo.LabelVO;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> {
	
	@Resource
	RedisServiceImpl<ArticleVO> redis;
	
	@Resource
	private ArticleMapper mapper;
	
	@Resource
	private CommentServiceImpl commentService;
	
	@Resource
	private LabelMapper labelMapper;
	
	public ArticleVO oneById(Long id) {
		ArticleVO vo = redis.get(RedisKeys.ARTICLE + id);
		if (vo == null) {
			vo = mapper.one(new Article(id, null, true, 1));
		}
		vo.setViewCount(vo.getViewCount() + 1);
		redis.set(RedisKeys.ARTICLE + id, vo, RedisKeys.ONE_DAYS);
		// 更新浏览次数
		updateViewCount(id, vo.getViewCount());
		// 获取评论数量
		updateCommentCount(id);
		return vo;
	}
	
	public PageVO<ArticleVO> page(ArticleDTO dto) {
		String key = RedisKeys.ARTICLE;
		if (dto.getLabelId() != null) {
			key = RedisKeys.ARTICLE_LABEL + dto.getLabelId();
		} else if (dto.getRecommend() != null) {
			key = RedisKeys.ARTICLE + dto.getRecommend();
		}
		// 判断缓存中是否存在
		List<ArticleVO> list = redis.range(key, dto.getPageNum(), dto.getPageSize());
		if (!CollectionUtils.isEmpty(list) || dto.getPageNum() != 1) {
			return PageUtils.page(dto, list, redis.listSize(key));
		}
		// 获取数据库数据
		list = mapper.list(new Article(null, dto.getLabelId(), true, 1));
		redis.rightPushAll(key, list, RedisKeys.ONE_DAYS);
		return PageUtils.page(list);
	}
	
	public List<ArticleVO> selectDeployList() {
		List<ArticleVO> voList = redis.range(RedisKeys.ARTICLE_DEPLOY, 0L, -1L);
		if (!CollectionUtils.isEmpty(voList)) {
			return voList;
		}
		List<LabelVO> labelList = labelMapper.listByClassify(new Classify(null, ClassifyKeys.DEPLOY, true, 1));
		List<Long> labelIds = labelList.stream().map(LabelVO::getId).toList();
		if (CollectionUtils.isEmpty(labelIds)) {
			return new ArrayList<>();
		}
		// 获取所有文章
		voList = mapper.listByLabelIds(labelIds);
		redis.rightPushAll(RedisKeys.ARTICLE_DEPLOY, voList, RedisKeys.ONE_DAYS);
		return voList;
	}
	
	@Async("Task")
	protected void updateViewCount(Long id, Integer viewCount) {
		mapper.update(new LambdaUpdateWrapper<Article>()
				              .eq(Article::getId, id)
				              .set(Article::getViewCount, viewCount));
	}
	
	@Async("Task")
	protected void updateCommentCount(Long id) {
		Long count = Optional.ofNullable(commentService.countBySourceId(id)).orElse(0L);
		mapper.update(new LambdaUpdateWrapper<Article>()
				              .eq(Article::getId, id)
				              .set(Article::getCommentCount, count));
	}
	
}
