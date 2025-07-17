package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.LeaveWordDTO;
import cn.yshujia.domain.entity.LeaveWord;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.LeaveWordMapper;
import cn.yshujia.transform.LeaveWordTransform;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
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
public class LeaveWordServiceImpl extends ServiceImpl<LeaveWordMapper, LeaveWord> {
	
	@Resource
	public LeaveWordMapper mapper;
	
	@Resource
	public FileServiceImpl fileService;
	
	@Resource
	RedisServiceImpl<LeaveWord> redis;
	
	@Transactional(rollbackFor = {Exception.class})
	public void insert(LeaveWordDTO dto) {
		LeaveWord leaveWord = LeaveWordTransform.dto2entity(dto);
		leaveWord.setAvatar(fileService.selectRandomIcon());
		try {
			int n = mapper.insert(leaveWord);
			if (n <= 0) {
				throw new ServiceException("留言插入失败！");
			}
			LeaveWord lw = mapper.selectById(leaveWord.getId());
			lw.setIp(null);
			redis.rightPush(RedisKeys.LEAVE_WORD_LIST, lw, RedisKeys.FOREVER);
		} catch (Exception e) {
			throw new ServiceException("留言插入失败！");
		}
	}
	
	public PageVO<LeaveWord> page(LeaveWordDTO dto) {
		List<LeaveWord> list = redis.range(RedisKeys.LEAVE_WORD_LIST, dto.getPageNum(), dto.getPageSize());
		if (!CollectionUtils.isEmpty(list) || dto.getPageNum() != 1) {
			return PageUtils.page(dto, list);
		}
		PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
		list = mapper.list();
		redis.rightPushAll(RedisKeys.LEAVE_WORD_LIST, list, RedisKeys.FOREVER);
		return PageUtils.page(dto, list);
	}
	
}