package cn.yshujia.domain.vo;

import cn.yshujia.domain.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author yshujia
 * @create 2024/12/16
 * @description CommentVO
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentVO extends Comment implements Serializable {
	
	private String username;
	
	private String avatar;
	
	private String roleName;
	
	private String parentUsername;
	
	private String parentAvatar;
	
	private String parentRoleName;
	
	private List<CommentVO> children;
	
}
