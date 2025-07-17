package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.entity.Article;
import cn.yshujia.domain.entity.Comment;
import cn.yshujia.domain.entity.Like;
import cn.yshujia.mapper.ArticleMapper;
import cn.yshujia.mapper.CommentMapper;
import cn.yshujia.mapper.LikeMapper;
import cn.yshujia.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author yshujia
 * @create 2024/12/20
 * @description LikeServiceImpl
 */

@Slf4j
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> {
	
	@Resource
	public LikeMapper mapper;
	
	@Resource
	public CommentMapper commentMapper;
	
	@Resource
	RedisServiceImpl<Boolean> redis;
	
	@Resource
	private ArticleMapper articleMapper;
	
	public Boolean select(Long sourceId, Long userId) {
		if (redis.hasKey(userId, sourceId)) {
			return redis.getHash(RedisKeys.LIKE_USER_ID + userId, sourceId);
		}
		Like like = mapper.selectOne(new LambdaQueryWrapper<Like>()
				                             .eq(Like::getSourceId, sourceId)
				                             .eq(Like::getUserId, userId));
		return Optional.ofNullable(like).map(Like::getLiked).orElse(false);
	}
	
	
	public void updateLikeCount(Long userId, Long sourceId, Boolean liked) {
		String key = RedisKeys.LIKE_USER_ID + userId;
		if (redis.hasKey(userId, sourceId)) {
			redis.setHash(key, sourceId, liked, RedisKeys.THREE_DAYS);
			return;
		}
		Like like = mapper.selectOne(new LambdaQueryWrapper<Like>()
				                             .eq(Like::getSourceId, sourceId)
				                             .eq(Like::getUserId, userId));
		if (null == like) {
			like = new Like(userId, sourceId);
		}
		like.setLiked(liked);
		redis.setHash(RedisKeys.LIKE_USER_ID + userId, sourceId, liked, RedisKeys.THREE_DAYS);
		saveOrUpdate(like);
	}
	
	/**
	 * [] * @return void
	 *
	 * @author yshujia
	 * @description 每 3 个小时同步一次数据库
	 * @author yshujia
	 * @create 2025/3/14 20:08
	 */
	@Async("Task")
	@Scheduled(cron = "0 0 */3 * * ?")
	public void synchronousLiked() {
		// 获取当前小时
		int currentHour = LocalTime.now().getHour();
		if (currentHour > 0 && currentHour < 6) {
			return;
		}
		Set<Object> keys = redis.keys(RedisKeys.LIKE_USER_ID + "*");
		if (CollectionUtils.isEmpty(keys)) {
			return;
		}
		for (Object key : keys) {
			Set<Object> sourceIds = redis.keys(key);
			for (Object source : sourceIds) {
				Boolean status = redis.getHash(key, source);
				mapper.update(new LambdaUpdateWrapper<Like>()
						              .set(Like::getLiked, status)
						              .eq(Like::getSourceId, Long.parseLong(source.toString()))
						              .eq(Like::getUserId, Long.parseLong(key.toString().split(":")[2])));
			}
		}
	}
	
	/**
	 * [] * @return void
	 *
	 * @author yshujia
	 * @description 每天的凌晨 1点 同步一次文章的点赞数量
	 * @create 2024/12/20 14:55
	 */
	@Async("Task")
	@Scheduled(cron = "0 0 1 * * ?")
	protected void updateArticleCount() {
		redis.delKeysByPrefix(RedisKeys.ARTICLE);
		List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<Article>()
				                                                  .select(
						                                                  Article::getId,
						                                                  Article::getUpdateBy, Article::getUpdateTime
				                                                  ));
		if (CollectionUtils.isEmpty(articles)) {
			return;
		}
		for (Article article : articles) {
			Long count = mapper.selectCount(new LambdaQueryWrapper<Like>().eq(Like::getSourceId, article.getId()).eq(Like::getLiked, true));
			if (null == count) {
				count = 0L;
			}
			article.setLikeCount(count.intValue());
		}
		articleMapper.updateById(articles);
		log.info("更新文章点赞数量完成！");
	}
	
	/**
	 * [] * @return void
	 *
	 * @author yshujia
	 * @description 每天的凌晨 2点 同步一次评论的点赞数量
	 * @create 2024/12/20 14:55
	 */
	@Async("Task")
	@Scheduled(cron = "0 10 1 * * ?")
	protected void updateCommentCount() {
		redis.delKeysByPrefix(RedisKeys.COMMENT_SOURCE);
		List<Comment> comments = commentMapper.selectList(new LambdaQueryWrapper<Comment>()
				                                                  .select(Comment::getId));
		if (CollectionUtils.isEmpty(comments)) {
			return;
		}
		for (Comment comment : comments) {
			Long count = mapper.selectCount(new LambdaQueryWrapper<Like>()
					                                .eq(Like::getSourceId, comment.getId())
					                                .eq(Like::getLiked, true));
			if (null == count) {
				count = 0L;
			}
			comment.setLikeCount(count.intValue());
		}
		commentMapper.updateById(comments);
		log.info("更新评论点赞数量完成！");
	}
	
}

