package cn.yshujia.domain.entity;

import cn.yshujia.handler.domain.DomainContentHandler;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author YShuJia
 * @since 2024/4/23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_web_config", autoResultMap = true)
@Schema(name = "WebConfig", description = "")
public class WebConfig implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	@Schema(description = "配置名")
	@TableField("`name`")
	private String name;

	@Schema(description = "作者名称")
	@TableField("author_name")
	private String authorName;

	@Schema(description = "作者头像地址")
	@TableField("author_avatar")
	private String authorAvatar;

	@Schema(description = "作者标签配置")
	@TableField(value = "author_tag", typeHandler = Fastjson2TypeHandler.class)
	private List<String> authorTag;

	@Schema(description = "作者联系方式配置")
	@TableField(value = "author_contact", typeHandler = Fastjson2TypeHandler.class)
	private List<Object> authorContact;

	@Schema(description = "作者支付方式配置")
	@TableField(value = "author_payment", typeHandler = Fastjson2TypeHandler.class)
	private List<Object> authorPayment;

	@Schema(description = "作者家乡经纬度配置")
	@TableField(value = "author_home", typeHandler = Fastjson2TypeHandler.class)
	private List<String> authorHome;

	@Schema(description = "作者mbti人格配置")
	@TableField(value = "author_mbti", typeHandler = Fastjson2TypeHandler.class)
	private Object authorMbti;

	@Schema(description = "作者擅长的技术配置")
	@TableField(value = "author_skill", typeHandler = Fastjson2TypeHandler.class)
	private List<String> authorSkill;

	@Schema(description = "作者爱玩的游戏配置")
	@TableField(value = "author_game", typeHandler = Fastjson2TypeHandler.class)
	private List<String> authorGame;

	@Schema(description = "作者喜爱的书籍配置")
	@TableField(value = "author_book", typeHandler = Fastjson2TypeHandler.class)
	private List<String> authorBook;

	@Schema(description = "作者走过的地点配置")
	@TableField(value = "author_footprint", typeHandler = Fastjson2TypeHandler.class)
	private List<Object> authorFootprint;

	@Schema(description = "关于作者")
	@TableField(value = "author_about", typeHandler = DomainContentHandler.class)
	private String authorAbout;

	@Schema(description = "网站标题")
	@TableField("site_title")
	private String siteTitle;

	@Schema(description = "网站座右铭")
	@TableField("site_motto")
	private String siteMotto;

	@Schema(description = "网站备案号")
	@TableField("site_record")
	private String siteRecord;

	@Schema(description = "网站favicon地址")
	@TableField("site_favicon")
	private String siteFavicon;

	@Schema(description = "关于网站")
	@TableField(value = "site_about", typeHandler = DomainContentHandler.class)
	private String siteAbout;

	@Schema(description = "网站运行起始时间")
	@TableField("site_create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date siteCreateTime;

	@Schema(description = "是否启用该配置")
	@TableField("is_enabled")
	private Boolean enabled;

	@Schema(description = "配置创建时间")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
}
