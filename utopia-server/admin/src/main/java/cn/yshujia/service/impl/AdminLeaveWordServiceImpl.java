package cn.yshujia.service.impl;

import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.LeaveWordDTO;
import cn.yshujia.domain.entity.LeaveWord;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.LeaveWordMapper;
import cn.yshujia.transform.LeaveWordTransform;
import cn.yshujia.utils.PageUtils;
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
public class AdminLeaveWordServiceImpl extends ServiceImpl<LeaveWordMapper, LeaveWord> {
	
	@Resource
	public LeaveWordMapper mapper;
	
	@Resource
	RedisServiceImpl<LeaveWord> redis;
	
	
	@Resource
	private FileServiceImpl fileService;
	
	public PageVO<LeaveWord> pageAdmin(LeaveWordDTO dto) {
		LeaveWord leaveWord = LeaveWordTransform.dto2entity(dto);
		List<LeaveWord> list = mapper.listByAdmin(leaveWord);
		return PageUtils.page(list);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void insert(LeaveWordDTO dto) {
		LeaveWord leaveWord = LeaveWordTransform.dto2entity(dto);
		leaveWord.setAvatar(fileService.selectRandomIcon());
		try {
			int n = mapper.insert(leaveWord);
			if (n <= 0) {
				throw new ServiceException("留言添加失败，请稍后重试！");
			}
			LeaveWord lw = mapper.selectById(leaveWord.getId());
			redis.rightPush(RedisKeys.LEAVE_WORD_LIST, lw, RedisKeys.FOREVER);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void remove(List<Long> ids) {
		try {
			int r = mapper.deleteByIds(ids);
			if (r < ids.size()) {
				throw new ServiceException("留言添加失败，请稍后重试！");
			}
			redis.delKeysByPrefix(RedisKeys.LEAVE_WORD_LIST);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
}