package cn.yshujia.service.impl;

import cn.yshujia.admin.vo.AdminWebsiteVO;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.WebsiteDTO;
import cn.yshujia.domain.entity.Website;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.WebsiteMapper;
import cn.yshujia.transform.WebsiteTransform;
import cn.yshujia.ui.vo.WebsiteVO;
import cn.yshujia.utils.IDUtils;
import cn.yshujia.utils.PageUtils;
import cn.yshujia.utils.SecurityUtils;
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
public class AdminWebsiteServiceImpl extends ServiceImpl<WebsiteMapper, Website> {
	
	@Resource
	public WebsiteMapper mapper;
	
	@Resource
	RedisServiceImpl<WebsiteVO> redis;
	public PageVO<AdminWebsiteVO> pageAdmin(WebsiteDTO dto) {
		Website website = WebsiteTransform.dto2Entity(dto);
		List<AdminWebsiteVO> list = mapper.listByAdmin(website);
		return PageUtils.page(list);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void insert(WebsiteDTO dto) {
		Website website = WebsiteTransform.dto2Entity(dto);
		website.setId(IDUtils.getTimeId());
		try {
			int r = mapper.insert(website);
			if (r <= 0) {
				throw new ServiceException("网站添加失败，请稍后重试！");
			}
			redis.delKeysByPrefix(RedisKeys.WEBSITE_LIST);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void update(WebsiteDTO dto) {
		Website website = WebsiteTransform.dto2Entity(dto);
		try {
			int r = mapper.update(website, SecurityUtils.createUpdateWrapper(website.getId()));
			if (r <= 0) {
				throw new ServiceException("网站不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.WEBSITE_LIST);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void remove(List<Long> ids) {
		try {
			int n = mapper.delete(SecurityUtils.createDeleteWrapper(ids));
			if (n < ids.size()) {
				throw new ServiceException("网站不存在，无法删除！");
			}
			redis.delKeysByPrefix(RedisKeys.WEBSITE_LIST);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
}