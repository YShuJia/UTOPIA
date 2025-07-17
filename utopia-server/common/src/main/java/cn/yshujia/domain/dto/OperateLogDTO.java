package cn.yshujia.domain.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: yshujia
 * @create: 2025/5/27 21:15
 * @description: OperateLogDTO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class OperateLogDTO extends PageDTO {
	
	private Long id;
	
	private String table;
	
	private String operate;
	
	private String params;
	
	private String method;
	
	private String sql;
	
	private Boolean result;
	
	private String errorMsg;
	
}
