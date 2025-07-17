package cn.yshujia.ui.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yshujia
 * @create 2025/3/6
 * @description AdminWebsiteVO
 */

@Data
public class WebsiteVO implements Serializable {
	
	private String title;
	
	private String avatar;
	
	private String introduction;
	
	private String url;
	
	private String cover;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
}
