package cn.yshujia.service.impl;

import cn.yshujia.admin.vo.AdminClassifyVO;
import cn.yshujia.admin.vo.AdminTreeVO;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.ClassifyDTO;
import cn.yshujia.domain.entity.Classify;
import cn.yshujia.domain.entity.Label;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.ClassifyMapper;
import cn.yshujia.mapper.LabelMapper;
import cn.yshujia.transform.ClassifyTransform;
import cn.yshujia.ui.vo.ClassifyVO;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.PageUtils;
import cn.yshujia.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class AdminClassifyServiceImpl extends ServiceImpl<ClassifyMapper, Classify> {
	
	@Resource
	RedisServiceImpl<ClassifyVO> redis;
	
	@Resource
	private ClassifyMapper mapper;
	
	@Resource
	private LabelMapper labelMapper;
	
	public List<AdminTreeVO> treeList(String key) {
		List<AdminTreeVO> list = mapper.tree(key);
		if (CollectionUtils.isEmpty(list)) {
			return list;
		}
		for (AdminTreeVO vo : list) {
			vo.setChildren(labelMapper.tree(new Label(null, vo.getValue(), true, 1)));
		}
		return list;
	}
	
	public PageVO<AdminClassifyVO> pageAdmin(ClassifyDTO dto) {
		List<AdminClassifyVO> list = mapper.listByAdmin(ClassifyTransform.dto2Entity(dto));
		return PageUtils.page(list);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void insert(ClassifyDTO dto) {
		Classify classify = ClassifyTransform.dto2Entity(dto);
		// 获取最大 ID
		List<ClassifyVO> list = mapper.list(new Classify(null, classify.getKey(), null, null));
		if (!CollectionUtils.isEmpty(list)) {
			classify.setId(list.get(list.size() - 1).getId() + 1);
		} else {
			classify.setId(100001L);
			list = mapper.list(null);
			if (!CollectionUtils.isEmpty(list)) {
				Long maxId = list.get(list.size() - 1).getId();
				char[] maxIdStr = maxId.toString().toCharArray();
				maxIdStr[0] += 1;
				classify.setId(Long.parseLong(new String(maxIdStr)));
			}
		}
		if (!classify.getKey().contains("sys:classify:")) {
			classify.setKey("sys:classify:" + classify.getKey());
		}
		try {
			int n = mapper.insert(classify);
			if (n <= 0) {
				throw new ServiceException("类别添加失败，请稍后重试！");
			}
			redis.delKeysByPrefix(RedisKeys.CLASSIFY);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		
		
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void update(ClassifyDTO dto) {
		Classify classify = ClassifyTransform.dto2Entity(dto);
		try {
			int n = mapper.update(classify, SecurityUtils.createUpdateWrapper(classify.getId()));
			if (n <= 0) {
				throw new ServiceException("类别不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.CLASSIFY);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void remove(List<Long> ids) {
		List<Label> list = labelMapper.selectList(new LambdaQueryWrapper<Label>()
				                                          .in(Label::getClassifyId, ids));
		if (!CollectionUtils.isEmpty(list)) {
			throw new ServiceException("类别下包含标签，不能删除！");
		}
		try {
			int n = mapper.delete(SecurityUtils.createDeleteWrapper(ids));
			if (n < ids.size()) {
				throw new ServiceException("类别不存在，无法删除！");
			}
			redis.delKeysByPrefix(RedisKeys.CLASSIFY);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
}
