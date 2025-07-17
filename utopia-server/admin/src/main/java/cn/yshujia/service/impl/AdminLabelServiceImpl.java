package cn.yshujia.service.impl;

import cn.yshujia.admin.vo.AdminLabelVO;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.LabelDTO;
import cn.yshujia.domain.entity.Classify;
import cn.yshujia.domain.entity.Label;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.LabelMapper;
import cn.yshujia.transform.LabelTransform;
import cn.yshujia.ui.vo.LabelVO;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.PageUtils;
import cn.yshujia.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Service
public class AdminLabelServiceImpl extends ServiceImpl<LabelMapper, Label> {
	
	@Resource
	public LabelMapper mapper;
	
	@Resource
	RedisServiceImpl<LabelVO> redis;
	
	public PageVO<AdminLabelVO> pageAdmin(LabelDTO dto) {
		Label label = LabelTransform.dto2Entity(dto);
		List<AdminLabelVO> list = mapper.listByAdmin(label);
		return PageUtils.page(list);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void insert(LabelDTO dto) {
		Label label = LabelTransform.dto2Entity(dto);
		List<LabelVO> list = mapper.listByClassify(new Classify(label.getClassifyId(), null, null, null));
		if (!CollectionUtils.isEmpty(list)) {
			label.setId(list.get(list.size() - 1).getId() + 1);
		} else {
			Long id = label.getClassifyId() * 100000 + 1;
			label.setId(id);
		}
		try {
			int n = mapper.insert(label);
			if (n <= 0) {
				throw new ServiceException("标签添加失败，请稍后重试！");
			}
			redis.delKeysByPrefix(RedisKeys.LABEL);
			redis.delKeysByPrefix(RedisKeys.ARTICLE);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void update(LabelDTO dto) {
		Label label = LabelTransform.dto2Entity(dto);
		try {
			int n = mapper.update(label, SecurityUtils.createUpdateWrapper(label.getId()));
			if (n <= 0) {
				throw new ServiceException("标签不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.LABEL);
			redis.delKeysByPrefix(RedisKeys.ARTICLE);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	@Transactional(rollbackFor = {Exception.class})
	public void remove(List<Long> ids) {
		Wrapper<Label> qw = SecurityUtils.createDeleteWrapper(ids);
		try {
			int n = mapper.delete(qw);
			if (n < ids.size()) {
				throw new ServiceException("标签不存在，无法删除！");
			}
			redis.delKeysByPrefix(RedisKeys.LABEL);
			redis.delKeysByPrefix(RedisKeys.ARTICLE);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
}
