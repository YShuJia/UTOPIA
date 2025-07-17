package cn.yshujia.ui.vo;

import cn.yshujia.handler.domain.DomainHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yshujia
 * @create 2024/11/30
 * @description AdminFileVO
 */


@Data
public class FileVO implements Serializable {
	
	@TableField(value = "url", typeHandler = DomainHandler.class)
	private String url;
	
}
