package cn.yshujia.ui.vo;

import cn.yshujia.handler.domain.DomainContentHandler;
import cn.yshujia.handler.domain.DomainHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yshujia
 * @create 2024/11/28
 * @description AdminArticleVO
 */

@Data
public class ArticleVO implements Serializable {
	
	private Long id;
	
	private Long labelId;
	
	private String labelName;
	
	private Long classifyId;
	
	private String classifyName;
	
	private String title;
	
	@TableField(value = "cover", typeHandler = DomainHandler.class)
	private String cover;
	
	@TableField(value = "content", typeHandler = DomainContentHandler.class)
	private String content;
	
	private String copyright;
	
	private Integer likeCount;
	
	private Integer viewCount;
	
	private Integer commentCount;
	
	private Boolean hasVideo;

	private String passwordTip;
	
	private Boolean likeable;
	
	private Boolean commentable;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	
}
