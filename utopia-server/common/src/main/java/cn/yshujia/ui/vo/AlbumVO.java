package cn.yshujia.ui.vo;

import cn.yshujia.handler.domain.DomainHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author yshujia
 * @create 2025/3/4
 * @description AdminAlbumVO
 */


@Data
public class AlbumVO implements Serializable {
	
	private Long id;
	
	private String labelName;
	
	private String title;
	
	@TableField(value = "urls", typeHandler = DomainHandler.class)
	private List<String> urls;
	
	private Integer likeCount;
	
	private Integer viewCount;
	
	private Integer commentCount;
	
	private String introduction;
	
	private Boolean likeable;
	
	private Boolean commentable;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	
}
