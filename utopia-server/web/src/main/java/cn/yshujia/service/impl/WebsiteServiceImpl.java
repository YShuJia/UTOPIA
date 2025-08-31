package cn.yshujia.service.impl;

import cn.yshujia.constant.DefaultConst;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.WebsiteDTO;
import cn.yshujia.domain.entity.Website;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.WebsiteMapper;
import cn.yshujia.transform.WebsiteTransform;
import cn.yshujia.ui.vo.WebsiteVO;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.IDUtils;
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
public class WebsiteServiceImpl extends ServiceImpl<WebsiteMapper, Website> {

	@Resource
	public WebsiteMapper mapper;

	@Resource
	RedisServiceImpl<WebsiteVO> redis;

	public PageVO<WebsiteVO> page(WebsiteDTO dto) {
		// 判断缓存中是否存在
		List<WebsiteVO> list = redis.range(RedisKeys.WEBSITE_LIST_RECOMMEND + dto.getRecommend(), dto.getPageNum(), dto.getPageSize());

		if (!CollectionUtils.isEmpty(list) || dto.getPageNum() != 1) {
			return PageUtils.page(dto, list, redis.listSize(RedisKeys.WEBSITE_LIST_RECOMMEND + dto.getRecommend()));
		}
		// 获取数据库数据
		list = mapper.list(new Website(dto.getRecommend(), true, 1));
		redis.rightPushAll(RedisKeys.WEBSITE_LIST_RECOMMEND + dto.getRecommend(), list, RedisKeys.THREE_DAYS);
		return PageUtils.page(dto, list);
	}

	@Transactional(rollbackFor = {Exception.class})
	public void insert(WebsiteDTO dto) {
		Website website = WebsiteTransform.dto2Entity(dto);
		website.setId(IDUtils.getTimeId());
		website.setLabelId(DefaultConst.WEBSITE_LABEL_ID);
		try {
			int r = mapper.insert(website);
			if (r <= 0) {
				throw new ServiceException("网站添加失败，请稍后重试！");
			}
			redis.rightPush(dto.getRecommend() ? RedisKeys.WEBSITE_LIST_RECOMMEND : RedisKeys.WEBSITE_LIST, WebsiteTransform.entity2VO(website), RedisKeys.THREE_DAYS);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

}