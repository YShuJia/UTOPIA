package cn.yshujia.ui.vo;


import cn.yshujia.handler.domain.DomainHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author：yshujia
 * @create: 2025/5/2 19:47
 * @description: AdminDiaryVO
 */

@Data
public class DiaryVO {
	
	private Long id;
	
	private String title;
	
	private String content;
	
	@TableField(value = "urls", typeHandler = DomainHandler.class)
	private List<String> urls;
	
	private Integer year;
	
	private Integer month;
	
	private Integer likeCount;
	
	private Integer viewCount;
	
	private Integer commentCount;
	
	private Boolean likeable;
	
	private Boolean commentable;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	
	private String createUsername;
	
}
