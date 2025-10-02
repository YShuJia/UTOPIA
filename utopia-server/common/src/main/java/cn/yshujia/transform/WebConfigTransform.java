package cn.yshujia.transform;


import cn.yshujia.domain.dto.WebConfigDTO;
import cn.yshujia.domain.entity.WebConfig;

/**
 * @author: yshujia
 * @create: 2025/9/21 16:33
 * @description: WebConfigTransform TODO
 */
public class WebConfigTransform {

	public static WebConfig dto2Entity(WebConfigDTO dto) {
		if (dto == null) {
			return null;
		}
		WebConfig webConfig = new WebConfig();
		webConfig.setId(dto.getId());
		webConfig.setName(dto.getName());
		webConfig.setAuthorName(dto.getAuthorName());
		webConfig.setAuthorAvatar(dto.getAuthorAvatar());
		webConfig.setAuthorTag(dto.getAuthorTag());
		webConfig.setAuthorContact(dto.getAuthorContact());
		webConfig.setAuthorPayment(dto.getAuthorPayment());
		webConfig.setAuthorHome(dto.getAuthorHome());
		webConfig.setAuthorMbti(dto.getAuthorMbti());
		webConfig.setAuthorSkill(dto.getAuthorSkill());
		webConfig.setAuthorGame(dto.getAuthorGame());
		webConfig.setAuthorBook(dto.getAuthorBook());
		webConfig.setAuthorFootprint(dto.getAuthorFootprint());
		webConfig.setAuthorAbout(dto.getAuthorAbout());
		webConfig.setSiteTitle(dto.getSiteTitle());
		webConfig.setSiteMotto(dto.getSiteMotto());
		webConfig.setSiteRecord(dto.getSiteRecord());
		webConfig.setSiteFavicon(dto.getSiteFavicon());
		webConfig.setSiteAbout(dto.getSiteAbout());
		webConfig.setSiteCreateTime(dto.getSiteCreateTime());
		webConfig.setEnabled(dto.getEnabled());
		return webConfig;
	}
}
