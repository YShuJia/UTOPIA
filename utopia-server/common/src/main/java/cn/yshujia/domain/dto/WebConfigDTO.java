package cn.yshujia.domain.dto;

import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: yshujia
 * @create: 2025/9/21 16:33
 * @description: WebConfigDTO DTO
 */

@Data
public class WebConfigDTO {

	@Positive(groups = UpdateGroup.class, message = "[路由ID] 不能为空！")
	private Long id;

	@NotBlank(groups = InsertGroup.class, message = "[配置名] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 16, message = "[配置名] 长度不能超过 16！")
	private String name;

	@NotBlank(groups = InsertGroup.class, message = "[作者名称] 不能为空！")
	private String authorName;

	@NotBlank(groups = InsertGroup.class, message = "[作者头像] 不能为空！")
	private String authorAvatar;

	@NotEmpty(groups = InsertGroup.class, message = "[作者标签] 不能为空！")
	private List<String> authorTag;

	@NotEmpty(groups = InsertGroup.class, message = "[作者联系方式] 不能为空！")
	private List<Object> authorContact;

	@NotEmpty(groups = InsertGroup.class, message = "[作者打赏链接] 不能为空！")
	private List<Object> authorPayment;

	@NotEmpty(groups = InsertGroup.class, message = "[作者家乡地址] 不能为空！")
	private List<String> authorHome;

	private Object authorMbti;

	@NotEmpty(groups = InsertGroup.class, message = "[作者技能] 不能为空！")
	private List<String> authorSkill;

	@NotEmpty(groups = InsertGroup.class, message = "[作者游戏] 不能为空！")
	private List<String> authorGame;

	@NotEmpty(groups = InsertGroup.class, message = "[作者书籍] 不能为空！")
	private List<String> authorBook;

	@NotEmpty(groups = InsertGroup.class, message = "[作者足迹] 不能为空！")
	private List<Object> authorFootprint;

	@NotBlank(groups = InsertGroup.class, message = "[关于作者] 不能为空！")
	private String authorAbout;

	@NotBlank(groups = InsertGroup.class, message = "[网站标题] 不能为空！")
	private String siteTitle;

	@NotBlank(groups = InsertGroup.class, message = "[网站座右铭] 不能为空！")
	private String siteMotto;

	@NotBlank(groups = InsertGroup.class, message = "[网站备案号] 不能为空！")
	private String siteRecord;

	@NotBlank(groups = InsertGroup.class, message = "[网站favicon] 不能为空！")
	private String siteFavicon;

	@NotBlank(groups = InsertGroup.class, message = "[关于网站] 不能为空！")
	private String siteAbout;

	@NotNull(groups = InsertGroup.class, message = "[网站创建时间] 不能为空！")
	private Date siteCreateTime;

	private Boolean enabled;
}
