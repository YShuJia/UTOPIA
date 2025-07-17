package cn.yshujia.ui.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description domain
 */

@Data
public class RouterVO implements Serializable {
	
	@JSONField(serialize = false)
	private Long id;
	
	@JSONField(serialize = false)
	private Long parentId;
	
	private String name;
	
	private String title;
	
	private String path;
	
	private String type;
	
	private String icon;
	
	private String component;
	
	private Boolean admin;
	
	private Boolean frame;
	
	private List<RouterVO> children;
	
}