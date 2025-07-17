package cn.yshujia.transform;

import cn.yshujia.admin.vo.AdminWebsiteVO;
import cn.yshujia.domain.dto.WebsiteDTO;
import cn.yshujia.domain.entity.Website;
import cn.yshujia.ui.vo.WebsiteVO;

/**
 * @author yshujia
 * @create 2025/1/10
 * @description WebsiteTransform
 */

public class WebsiteTransform {
	
	public static WebsiteVO entity2VO(Website website) {
		if (website == null) {
			return null;
		}
		WebsiteVO websiteVO = new WebsiteVO();
		websiteVO.setTitle(website.getTitle());
		websiteVO.setAvatar(website.getAvatar());
		websiteVO.setIntroduction(website.getIntroduction());
		websiteVO.setUrl(website.getUrl());
		websiteVO.setCover(website.getCover());
		websiteVO.setCreateTime(website.getCreateTime());
		return websiteVO;
	}
	
	public static AdminWebsiteVO entity2AdminVO(Website website) {
		if (website == null) {
			return null;
		}
		AdminWebsiteVO adminWebsiteVO = new AdminWebsiteVO();
		adminWebsiteVO.setLabelId(website.getLabelId());
		adminWebsiteVO.setTitle(website.getTitle());
		adminWebsiteVO.setAvatar(website.getAvatar());
		adminWebsiteVO.setIntroduction(website.getIntroduction());
		adminWebsiteVO.setUrl(website.getUrl());
		adminWebsiteVO.setCover(website.getCover());
		adminWebsiteVO.setRecommend(website.getRecommend());
		adminWebsiteVO.setReviewed(website.getReviewed());
		adminWebsiteVO.setId(website.getId());
		adminWebsiteVO.setCreateTime(website.getCreateTime());
		adminWebsiteVO.setUpdateTime(website.getUpdateTime());
		adminWebsiteVO.setCreateBy(website.getCreateBy());
		adminWebsiteVO.setUpdateBy(website.getUpdateBy());
		adminWebsiteVO.setEnabled(website.getEnabled());
		return adminWebsiteVO;
	}
	
	public static Website dto2Entity(WebsiteDTO dto) {
		if (dto == null) {
			return null;
		}
		Website website = new Website();
		website.setId(dto.getId());
		website.setLabelId(dto.getLabelId());
		website.setTitle(dto.getTitle());
		website.setAvatar(dto.getAvatar());
		website.setIntroduction(dto.getIntroduction());
		website.setUrl(dto.getUrl());
		website.setCover(dto.getCover());
		website.setRecommend(dto.getRecommend());
		website.setEnabled(dto.getEnabled());
		website.setReviewed(dto.getReviewed());
		website.setCreateBy(dto.getCreateBy());
		return website;
	}
	
}
