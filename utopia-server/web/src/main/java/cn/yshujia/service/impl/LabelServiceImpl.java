package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.ClassifyDTO;
import cn.yshujia.domain.entity.Classify;
import cn.yshujia.domain.entity.Label;
import cn.yshujia.mapper.LabelMapper;
import cn.yshujia.ui.vo.LabelVO;
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
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> {
	
	@Resource
	public LabelMapper mapper;
	
	@Resource
	RedisServiceImpl<LabelVO> redis;
	
	public List<LabelVO> listByClassify(ClassifyDTO dto) {
		// 通过 key 或者 id 获取类别下的标签
		String key = RedisKeys.LABEL_CLASSIFY + (dto.getKey() == null ? dto.getId() : dto.getKey());
		List<LabelVO> voList = redis.range(key, 0L, -1L);
		if (!CollectionUtils.isEmpty(voList)) {
			return voList;
		}
		voList = mapper.listByClassify(new Classify(dto.getId(), dto.getKey(), true, 1));
		redis.rightPushAll(key, voList, RedisKeys.ONE_DAYS);
		return voList;
	}
	
}
