package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.entity.Classify;
import cn.yshujia.mapper.ClassifyMapper;
import cn.yshujia.transform.ClassifyTransform;
import cn.yshujia.ui.vo.ClassifyVO;
import cn.yshujia.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Service
public class ClassifyServiceImpl extends ServiceImpl<ClassifyMapper, Classify> {
	
	@Resource
	RedisServiceImpl<ClassifyVO> redis;
	
	@Resource
	private ClassifyMapper mapper;
	public ClassifyVO selectById(Long id) {
		ClassifyVO classify = redis.get(RedisKeys.CLASSIFY + id);
		if (null != classify) {
			return classify;
		}
		classify = ClassifyTransform.entity2VO(mapper.selectById(id));
		if (null != classify) {
			redis.set(RedisKeys.CLASSIFY + id, classify, RedisKeys.ONE_DAYS);
		}
		return classify;
	}
	
	public List<ClassifyVO> listByLikeKey(String key) {
		String cacheKey = RedisKeys.CLASSIFY_TYPE + key;
		// 从缓存中获取
		List<ClassifyVO> list = redis.range(cacheKey, 0L, -1L);
		if (!CollectionUtils.isEmpty(list)) {
			return list;
		}
		list = mapper.list(new Classify(null, key, true, 1));
		redis.rightPushAll(cacheKey, list, RedisKeys.ONE_DAYS);
		return list;
	}
	
}
